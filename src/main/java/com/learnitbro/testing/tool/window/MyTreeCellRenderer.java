package com.learnitbro.testing.tool.window;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.learnitbro.testing.tool.stream.StreamHandler;

@SuppressWarnings("serial")
public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	private String config;

	public MyTreeCellRenderer() {
		config = StreamHandler.inputStreamTextBuilder(getClass().getResourceAsStream("/config.json"));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

		// Assuming you have a tree of Strings
		String node = ((DefaultMutableTreeNode) value).getUserObject().toString();
		int level = (int) ((DefaultMutableTreeNode) value).getLevel();

		if (level == 1)
			setIcon(new ImageIcon(getClass().getResource("/icons/config.png")));
		else if (level == 2)
			setIcon(new ImageIcon(getClass().getResource("/icons/group.png")));
		else if (level == 3)
			setIcon(new ImageIcon(getClass().getResource("/icons/test.png")));
		else if (level == 4) {
			if (getCategory(node).equalsIgnoreCase("action"))
				setIcon(new ImageIcon(getClass().getResource("/icons/action.png")));
			else if (getCategory(node).equalsIgnoreCase("wait"))
				setIcon(new ImageIcon(getClass().getResource("/icons/wait.png")));
			else if (getCategory(node).equalsIgnoreCase("assert"))
				setIcon(new ImageIcon(getClass().getResource("/icons/assert.png")));
			else if (getCategory(node).equalsIgnoreCase("video"))
				setIcon(new ImageIcon(getClass().getResource("/icons/video.png")));
			else if (getCategory(node).equalsIgnoreCase("script"))
				setIcon(new ImageIcon(getClass().getResource("/icons/script.png")));
			else if (getCategory(node).equalsIgnoreCase("picture"))
				setIcon(new ImageIcon(getClass().getResource("/icons/picture.png")));
		} else {
			setIcon(new ImageIcon(getClass().getResource("/icons/default.png")));
		}

		return this;
	}

	private String getCategory(String node) {
		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {
			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCat = obj.getString("category");
			if (objName.equals(node))
				return objCat;
		}
		return null;
	}
}