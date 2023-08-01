package com.python.parser;

import java.io.IOException;
import java.util.regex.Pattern;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

public class PythonFileParser {

	/**Used to be able to return both the editscript and the mappings from a function*/
	public class EditResults{
		public final EditScript edits;
		public final MappingStore mappings;
		public final String fileName;
		
		public EditResults(String fileName, EditScript edits, MappingStore mappings) {
			this.fileName = fileName;
			this.edits = edits;
			this.mappings = mappings;
		}
	}
	
	public PythonFileParser() {
		Run.initGenerators();
	}
	
	public EditResults getPythonDiff(String beforePath, String afterPath, String originalFileName) {
		Run.initGenerators();
		EditScript edits = null;
		MappingStore mappings = null;
		PatchedPythonTreeGenerator gen1, gen2;
		try {
			gen1 = new PatchedPythonTreeGenerator();
			gen2 = new PatchedPythonTreeGenerator();
			ITree tree1 = gen1.generateFrom().file(beforePath).getRoot();
			ITree tree2 = gen2.generateFrom().file(afterPath).getRoot();
			Matcher matcher = Matchers.getInstance().getMatcher();
			mappings = matcher.match(tree1, tree2);
			EditScriptGenerator scriptGen = new SimplifiedChawatheScriptGenerator();
			edits = scriptGen.computeActions(mappings);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new EditResults(originalFileName, edits, mappings);
	}
	
	/**Formats the string in a way similar to TravisYamlFileParser.getYamlDiffStr(), but with an extra file_name: in front of the tag<br>
	 * Example:<br>
	 * file_name:['update-node:{editedType1 [edit1Start,edit1End], editedType2 [edit2Start,edit2End]}->{editedType1 [edit1Start, edit1NewEnd], editedType2 [edit2Start,edit2End]}','insert-tree:{}->{editType3 [edit3Start,edit3End]}']*/
	public String getPythonDiffString(EditResults results) {
		int i = 0;
		Pattern newlineDetect = Pattern.compile("\r?\n\t* *"); //detect yaml newlines, which can be followed by whitespace for indentation
		StringBuilder sb = new StringBuilder(results.fileName);
		sb.append(":['");
		if(results.edits != null) {
			for(Action a : results.edits) {
				if(i++ != 0) {
					sb.append("','");
				}
				sb.append(a.getName().trim());
				sb.append(":{");
				String treeStr = a.getNode().toTreeString().trim();
				treeStr = newlineDetect.matcher(treeStr).replaceAll(", ");
				if(results.mappings.isSrcMapped(a.getNode()) || a.getName().contains("delet")) //deletions or updates only
					sb.append(treeStr);
				sb.append("}->{");
				ITree mapped = results.mappings.getDstForSrc(a.getNode()); //only non-null for updates and moves
				String treeStr2 = ""; //should be left as this for deletions
				if(a.getName().contains("insert")) //addition
					treeStr2 = treeStr; //addition should be {},{newStuff}
				if(mapped != null) { //update or move of some sort
					treeStr2 = mapped.toTreeString().trim();
					treeStr2 = newlineDetect.matcher(treeStr2).replaceAll(", ");
				}
				sb.append(treeStr2);
				sb.append("}");
				
				//System.out.println("Edit number: " + i);
				//System.out.println("Name: " + a.getName());
				//System.out.println("Tree:\n" + a.getNode().toTreeString());
				//System.out.println("\nMapped tree:\n" + (mapped == null ? "Tree empty!" : mapped.toTreeString()));
				//System.out.println("\n\n");
				
			}
		}
		sb.append("']");
		System.out.println(sb.toString());
		return sb.toString();
	}
}
