package com.travisdiff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utility.NodeLabelWrapper;

public class TravisCIChangeBlocks {
	Map<String, List<NodeLabelWrapper>> changeList;
	String commmitId;
	String projectName;
	public TravisCIChangeBlocks() {
		changeList = new HashMap<String, List<NodeLabelWrapper>>();

	}


	public Map<String, List<NodeLabelWrapper>> getChangeList() {
		return changeList;
	}

	public void addItemToMap(String key, NodeLabelWrapper change) {
		if(key.contains("placeholder"))
			return;
		
		if (changeList.containsKey(key)) {
			changeList.get(key).add(change);
		} else {
			List<NodeLabelWrapper> list = new ArrayList<>();
			list.add(change);
			changeList.put(key, list);
		}

	}

	public List<NodeLabelWrapper> getChangeList(String key) {
		if (changeList.containsKey(key)) {
			return changeList.get(key);
		} else {
			return null;
		}
	}

}
