package com.utility;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.travisdiff.NodeLabel;

public class NodeLabelWrapper extends NodeLabel {
	public NodeLabelWrapper(ITree node, String label, String straction, Action action, List<String> changecmds) {
		super(node, label, straction, action, changecmds);
		// TODO Auto-generated constructor stub
	}
	String commitId;
	String projectName;
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public NodeLabelWrapper(ITree node, String label, String straction, Action action,
							List<String> changecmds,String commitId,String projectName) {
		super(node, label, straction, action, changecmds);
		this.commitId=commitId;
		this.projectName=projectName;
	}
}
