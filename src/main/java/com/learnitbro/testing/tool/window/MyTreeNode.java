package com.learnitbro.testing.tool.window;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

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
	private String name;
	private String parentName;
	private String grandparentName;

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
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/object.json"));
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

	public boolean isMatch(String jTextFieldName) {
		String content = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/object.json"));
		} catch (Exception e) {
			throw new ReadFileException("Can't read file", e);
		}
		JSONArray array = new JSONArray(content);

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);
			String uuid = item.getString("uuid");
			String name = item.getString("name");
//			String parentName = item.getString("parentName");
//			String grandparentName = item.getString("grandparentName");

			int index = item.getInt("index");
			int parentIndex = item.getInt("parentIndex");
			int grandparentIndex = item.getInt("grandparentIndex");

			boolean isUuidMatch = uuid.equalsIgnoreCase(jTextFieldName);

			boolean isNameMatch = name.equalsIgnoreCase(node.getUserObject().toString());
//			boolean isParentNameMatch = parentName.equalsIgnoreCase(node.getParent().toString());
//			boolean isGrandParentNameMatch = grandparentName.equalsIgnoreCase(node.getParent().getParent().toString());

			boolean isIndexMatch = index == node.getParent().getIndex(node);
			boolean isParentIndexMatch = parentIndex == node.getParent().getParent().getIndex(node.getParent());
			boolean isGrandParentIndexMatch = grandparentIndex == node.getParent().getParent().getParent()
					.getIndex(node.getParent().getParent());

			// && isParentNameMatch && isGrandParentNameMatch
			if (isUuidMatch && isNameMatch && isIndexMatch && isParentIndexMatch && isGrandParentIndexMatch)
				return true;
		}

		return false;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Object getParentName() {
		if (parentName == null)
			return JSONObject.NULL;
		return parentName;
	}

	public void setGrandParentName(String grandparentName) {
		this.grandparentName = grandparentName;
	}

	public Object getGrandParentName() {
		if (grandparentName == null)
			return JSONObject.NULL;
		return grandparentName;
	}

	private int getIndex() {
		if (node.getParent() == null)
			return -1;
		return node.getParent().getIndex(node);
	}

	private TreeNode getParentTreeNode() {
		return node.getParent().getParent();
	}

	private TreeNode getGrandParentTreeNode() {
		return getParentTreeNode().getParent();
	}

	private int getParentIndex() {
		if (getParentTreeNode() == null)
			return -1;
		return getParentTreeNode().getIndex(node.getParent());
	}

	private int getGrandParentIndex() {
		if (getParentTreeNode() != null) {
			if (getGrandParentTreeNode() == null)
				return -1;
		} else
			return -1;
		return getGrandParentTreeNode().getIndex(getParentTreeNode());
	}

	public void build() {

		JSONObject jo = new JSONObject();
		jo.put("uuid", getUUID());
		jo.put("name", getName());
//		jo.put("parentName", getParentName());
//		jo.put("grandparentName", getGrandParentName());
		jo.put("index", getIndex());
		jo.put("parentIndex", getParentIndex());
		jo.put("grandparentIndex", getGrandParentIndex());
		jo.put("hasChildren", getParentIndex() == -1);
		
		all.put(jo);
//		System.out.println(all.toString(1));
		JSONHandler.write(new File(FileHandler.getUserDir() + "/object.json"), all.toString(1));
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
			jsonObject.addProperty("level", "category");
		} else if (src.getLevel() == 2) {
			jsonObject.addProperty("level", "testCase");
		} else if (src.getLevel() == 3) {
			jsonObject.addProperty("level", "input");
			src.setAllowsChildren(false);
			for (Component item : UI.generalPanel.getComponents()) {
				if (item.toString().contains("JTextField")) {
					String uuid = ((JTextField) item).getName();
					if (myNode.isMatch(uuid)) {

						String type = ((JTextField) item).getDocument().getProperty("type").toString();
						String text = ((JTextField) item).getText();

						if (uuidList.contains(uuid + "-" + type))
							continue;

						uuidList.add(uuid + "-" + type);

						jsonObject.addProperty("uuid", uuid);
						if ("t1".equalsIgnoreCase(type)) {
							jsonObject.addProperty("t1", text);
						} else if ("t2".equalsIgnoreCase(type)) {
							jsonObject.addProperty("t2", text);
						}
					}
				}
			}
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
}