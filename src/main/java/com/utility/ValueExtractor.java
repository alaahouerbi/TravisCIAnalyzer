package com.utility;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

public class ValueExtractor {
    public static List<String> extractValues(String input) {
        List<String> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("(replace |othervalue)\\s+\"(.*?)\"");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            values.add(matcher.group(2));
        }

        return values;
    }
    public static String getFirstChildLabel(ITree node) {
    	if(node.hasLabel()){
    		return node.getLabel();
    	}
    	if(!node.getChildren().isEmpty()) {
    		for(ITree child:node.getChildren()) {
    			String label=getFirstChildLabel(child);
    			if(label!=null) return label;
    		}
    	}
    	//node label in this subtree return null
    	return null;
    }
    
}
