package com.build.commitanalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.dump.DumpArchiveEntry.TYPE;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.gitective.core.BlobUtils;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TypeSet;

public class CommitAnalyzingUtils {

	public CommitAnalyzingUtils() {

	}

	public Repository setRepository(String repo_path) throws Exception {
		File gitDir = new File(repo_path);

		RepositoryBuilder builder = new RepositoryBuilder();
		Repository repository;
		repository = builder.setGitDir(gitDir).readEnvironment().findGitDir().build();

		return repository;
	}

	public Iterable<RevCommit> getAllCommits(Git git) throws Exception {
		return git.log().all().call();
	}

	public List<Ref> getAllBranches(Git git) throws Exception {
		return git.branchList().call();
	}

	public File writeContentInFile(String name, String content) throws IOException {
		if(content==null)
			return null;
		
		File f = new File(name);
		
		if (f.exists()) {
			f.delete();
		}
		
		f = new File(name);
		
		content=content.trim();
		if(content.length()<0)
			content="abcdef";
		
		FileUtils.writeStringToFile(f, content);

		return f;
	}

	public String[] getContent(Repository repository, DiffEntry diff, RevCommit commit) {
		return new String[] { BlobUtils.getContent(repository, commit.getId(), diff.getNewPath()),
				BlobUtils.getContent(repository, commit.getParent(0).getId(), diff.getOldPath()) };
	}
	
	public String getContentOnCommit(Repository repository, DiffEntry diff, RevCommit commit) {
		return new String (BlobUtils.getContent(repository, commit.getId(), diff.getNewPath()));
		
	}

	public DiffFormatter setDiffFormatter(Repository repository, boolean detectRenames) {
		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);

		df.setRepository(repository);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(detectRenames);

		return df;
	}
	
	public Map<String,Boolean> getCommandPresenceMap(List<String> allFieldsInMap,List<String> nodesToLookFor){
		Map<String,Boolean> temp = nodesToLookFor.stream ()
                .collect (Collectors.toMap (Function.identity (), 
                                            k -> false));
		
		for(String field:allFieldsInMap) {
			if(nodesToLookFor.contains(field)) {
				temp.replace(field, true);
			}
		}
		return temp;
	}
	public List<String> extractFieldsFromTrees(List<ITree> prevTreeNodes,List<ITree> currTreeNodes){
		HashSet<ITree> combinedNodes;
		Set<String> temp = new HashSet<String>();
		if(prevTreeNodes==null && currTreeNodes==null) return new ArrayList<String>();
		
		if(prevTreeNodes==null) {
			combinedNodes=new HashSet<ITree>(currTreeNodes);
		}
		else if(currTreeNodes==null) {
			combinedNodes = new HashSet<ITree>(prevTreeNodes);
		}else {
			combinedNodes = new HashSet<ITree>();
			combinedNodes.addAll(prevTreeNodes);
			combinedNodes.addAll(currTreeNodes);
		}
		for(ITree node: combinedNodes) {
			if(node.hasLabel()&&node.getType().toString().equals("String")){
				temp.add(node.getLabel().replaceAll("^\"|\"$", ""));
			}
		}
		List<String> returnList = new ArrayList<String>();
		returnList.addAll(temp);
		return returnList;
	}
	public Map<String,Boolean> getCommandPresenceMapInChange(EditScript actions,List<String> nodesToLookFor){
		Map<String,Boolean> temp = nodesToLookFor.stream ()
                .collect (Collectors.toMap (Function.identity (), 
                                            k -> false));
		for(Action action:actions.asList()) {
			String parent=action.getNode().getMetadata("json_parent").toString();
			if(!parent.isEmpty()&& nodesToLookFor.contains( parent.replaceAll("^\"|\"$", ""))){
				temp.replace(parent.replaceAll("^\"|\"$", ""), true);
			}
		}
		return temp;
	}
}
