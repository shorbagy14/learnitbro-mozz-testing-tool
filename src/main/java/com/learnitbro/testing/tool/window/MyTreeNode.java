package com.learnitbro.testing.tool.window;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;

@SuppressWarnings("serial")
public class MyTreeNode extends DefaultMutableTreeNode {

	private String uuid;
	private DefaultMutableTreeNode node;

	static JSONArray all = new JSONArray();

	public MyTreeNode(DefaultMutableTreeNode node) {
		super(node);
		this.node = node;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	private String getUUID() {
		return uuid;
	}

	public boolean isUuidExist(String uuid) {
		String content = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/node.json"));
		} catch (Exception e) {
			throw new ReadFileException("Can't read file", e);
		}
		JSONArray array = new JSONArray(content);

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = (JSONObject) array.get(i);
			String id = (String) item.get("uuid");
			if (id.equalsIgnoreCase(uuid))
				return true;
		}

		return false;
	}

	boolean isMatch(String value) {
		String content = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/node.json"));
		} catch (Exception e) {
			throw new ReadFileException("Can't read file", e);
		}
		JSONArray array = new JSONArray(content);

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);
			String uuid = item.getString("uuid");

			int index = item.getInt("index");
			int parentIndex = item.getInt("parentIndex");
			int grandparentIndex = item.getInt("grandparentIndex");
			int superparentIndex = item.getInt("superparentIndex");

			boolean isUuidMatch = uuid.equalsIgnoreCase(value);

			boolean isIndexMatch = index == getIndex();
			boolean isParentIndexMatch = parentIndex == getParentIndex();
			boolean isGrandParentIndexMatch = grandparentIndex == getGrandParentIndex();
			boolean isSuperParentIndexMatch = superparentIndex == getSuperParentIndex();

			if (isUuidMatch && isIndexMatch && isParentIndexMatch && isGrandParentIndexMatch && isSuperParentIndexMatch)
				return true;
		}

		return false;
	}

	public int getIndex() {
		if (getTreeNode() == null)
			return -1;
		return getTreeNode().getIndex(node);
	}

	public TreeNode getTreeNode() {
		return node.getParent();
	}

	public TreeNode getParentTreeNode() {
		return getTreeNode().getParent();
	}

	public TreeNode getGrandParentTreeNode() {
		return getParentTreeNode().getParent();
	}

	public TreeNode getSuperParentTreeNode() {
		return getGrandParentTreeNode().getParent();
	}

	public int getParentIndex() {
		if (getTreeNode() == null)
			return -1;

		if (getParentTreeNode() == null)
			return -1;
		return getParentTreeNode().getIndex(getTreeNode());
	}

	public int getGrandParentIndex() {
		if (getTreeNode() == null)
			return -1;

		if (getParentTreeNode() != null) {
			if (getGrandParentTreeNode() == null)
				return -1;
		} else
			return -1;
		return getGrandParentTreeNode().getIndex(getParentTreeNode());
	}

	public int getSuperParentIndex() {
		if (getTreeNode() == null)
			return -1;

		if (getParentTreeNode() == null)
			return -1;

		if (getGrandParentTreeNode() != null) {
			if (getSuperParentTreeNode() == null)
				return -1;
		} else
			return -1;
		return getSuperParentTreeNode().getIndex(getGrandParentTreeNode());
	}

	public void build() {

		JSONObject jo = new JSONObject();
		jo.put("uuid", getUUID());
		jo.put("index", getIndex());
		jo.put("parentIndex", getParentIndex());
		jo.put("grandparentIndex", getGrandParentIndex());
		jo.put("superparentIndex", getSuperParentIndex());

		all.put(jo);
		JSONHandler.write(new File(FileHandler.getUserDir() + "/temp/node.json"), all.toString(1));
	}
}

class DefaultMutableTreeNodeSerializer implements JsonSerializer<DefaultMutableTreeNode> {

	private List<String> uuidList = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public JsonElement serialize(DefaultMutableTreeNode src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		MyTreeNode myNode = new MyTreeNode(src);

		if (src.getLevel() == 0) {
			jsonObject.addProperty("level", "suite");
		} else if (src.getLevel() == 1) {
			jsonObject.addProperty("level", "configuration");
			setLevel(myNode, jsonObject);
		} else if (src.getLevel() == 2) {
			jsonObject.addProperty("level", "group");
		} else if (src.getLevel() == 3) {
			jsonObject.addProperty("level", "testCase");
		} else if (src.getLevel() == 4) {
			jsonObject.addProperty("level", "input");
			src.setAllowsChildren(false);
			setLevel(myNode, jsonObject);

		} else {
			src.setAllowsChildren(true);
		}

		jsonObject.addProperty("allowsChildren", src.getAllowsChildren());
		jsonObject.add("userObject", context.serialize(src.getUserObject()));
		if (src.getChildCount() > 0) {
			jsonObject.add("children", context.serialize(Collections.list(src.children())));
		}

		return jsonObject;
	}

	@SuppressWarnings("rawtypes")
	private void setLevel(MyTreeNode myNode, JsonObject jsonObject) {
		JsonArray arr = null;
		String t = null;

		for (Component item : UI.generalPanel.getComponents()) {
			if (item.toString().contains("JTextField")) {
				String uuid = ((JTextField) item).getName();
				if (myNode.isMatch(uuid)) {

					String type = ((JTextField) item).getClientProperty("type").toString();
					String category = ((JTextField) item).getClientProperty("category").toString();
					String index = ((JTextField) item).getClientProperty("index").toString();
					String text = ((JTextField) item).getText();

					if (uuidList.contains(uuid + "-" + type + "-" + category + "-" + index))
						continue;

					uuidList.add(uuid + "-" + type + "-" + category + "-" + index);

					if (!type.equalsIgnoreCase(t)) {
						arr = new JsonArray();
					}

					t = type;
					arr.add(text);

					jsonObject.addProperty("uuid", uuid);
					jsonObject.addProperty("category", category);
					jsonObject.add(type, arr);

				}
			} else if (item.toString().contains("JComboBox")) {
				String uuid = ((JComboBox) item).getName();
				if (myNode.isMatch(uuid)) {

					String text = ((JComboBox) item).getSelectedItem().toString();
					String type = ((JComboBox) item).getClientProperty("type").toString();
					String category = ((JComboBox) item).getClientProperty("category").toString();
					String index = ((JComboBox) item).getClientProperty("index").toString();

					if (uuidList.contains(uuid + "-" + type + "-" + category + "-" + index))
						continue;

					uuidList.add(uuid + "-" + type + "-" + category + "-" + index);

					if (!type.equalsIgnoreCase(t)) {
						arr = new JsonArray();
					}

					t = type;
					arr.add(text);

					jsonObject.addProperty("uuid", uuid);
					jsonObject.addProperty("category", category);
					jsonObject.add(type, arr);
				}
			} else if (item.toString().contains("JLabel")) {
				String uuid = ((JLabel) item).getName();
				if (myNode.isMatch(uuid)) {

					if (((JLabel) item).getClientProperty("category") == null)
						continue;

					String category = ((JLabel) item).getClientProperty("category").toString();

					if (uuidList.contains(uuid + "-" + category))
						continue;

					uuidList.add(uuid + "-" + category);

					jsonObject.addProperty("category", category);
					jsonObject.addProperty("uuid", uuid);

				}
			}
		}
	}
}