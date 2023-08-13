package com.python.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.config.Config;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.SyntaxException;
import com.github.gumtreediff.io.ActionsIoUtils;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class PythonFileParser {

	/**Used to be able to return both the editscript and the mappings from a function*/
	public class EditResults{
		public final EditScript edits;
		public final MappingStore mappings;
		public final String fileName;
		public final TreeContext ctx;
		
		public EditResults(String fileName, EditScript edits, MappingStore mappings, TreeContext beforeContext) {
			this.fileName = fileName;
			this.edits = edits;
			this.mappings = mappings;
			this.ctx = beforeContext;
		}
	}
	
	public PythonFileParser() {
		Run.initGenerators();
	}
	
	static final Path logPath = Path.of(Config.rootDir + "Project_Data" + File.separator + "python_errors.log");
	
	public EditResults getPythonDiff(String beforeContent, String afterContent, String originalFileName) {
		Run.initGenerators();
		EditScript edits = null;
		MappingStore mappings = null;
		TreeContext ctx1 = null;
		try {
			PatchedPythonTreeGenerator gen1, gen2;
			gen1 = new PatchedPythonTreeGenerator();
			gen2 = new PatchedPythonTreeGenerator();
			
			ctx1 = gen1.generate(new StringReader(beforeContent));
			ITree tree1 = gen1.generateFrom().string(beforeContent).getRoot();
			ITree tree2 = gen2.generateFrom().string(afterContent).getRoot();
			Matcher matcher = Matchers.getInstance().getMatcher();
			mappings = matcher.match(tree1, tree2);
			EditScriptGenerator scriptGen = new SimplifiedChawatheScriptGenerator();
			edits = scriptGen.computeActions(mappings);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (SyntaxException e) {
			System.err.println("Syntax Error");
			e.printStackTrace();
		}catch (RuntimeException e) {
			e.printStackTrace();
			try {
				Files.writeString(logPath, originalFileName + System.lineSeparator(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				System.out.println("Failed to write about error");
				e1.printStackTrace();
			}
		}
		return new EditResults(originalFileName, edits, mappings, ctx1);
	}
	
	/**Formats the string into Gumtree's JSON format*/
	public static String getPythonDiffString(EditResults results) {
		try {
			StringWriter sw = new StringWriter();
			ActionsIoUtils.toJson(results.ctx, results.edits, results.mappings).writeTo(sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		/*
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
		*/
	}
}
