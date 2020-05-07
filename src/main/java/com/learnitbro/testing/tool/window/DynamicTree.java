package com.learnitbro.testing.tool.window;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;

@SuppressWarnings("serial")
public class DynamicTree extends JPanel {

	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public DynamicTree() {
		super(new GridLayout(1, 0));

		rootNode = new DefaultMutableTreeNode("Test Suite");
		treeModel = new DefaultTreeModel(rootNode);

		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.setCellRenderer(new MyTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);

//		tree.setDragEnabled(true);
//		tree.setDropMode(DropMode.ON_OR_INSERT);
//		tree.setTransferHandler(new TreeTransferHandler());
//		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}

	public JTree getJTree() {
		return tree;
	}

	public DefaultMutableTreeNode getDefaultMutableTreeNode() {
		return rootNode;
	}

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/** Remove the currently selected node. */
	public void removeCurrentNode() {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());

			int level = -1;
			int index = -1;
			int parentIndex = -1;
			int grandparentIndex = -1;
			int superparentIndex = -1;

			MyTreeNode n = new MyTreeNode(currentNode);
			for (int x = 0; x < MyTreeNode.all.length(); x++) {
				JSONObject value = (JSONObject) MyTreeNode.all.get(x);

				boolean isIndexMatch = n.getIndex() == value.getInt("index");
				boolean isLevelMatch = n.getLevel() == value.getInt("level");

				boolean isParentIndexMatch = true;
				boolean isGrandParentIndexMatch = true;
				boolean isSuperParentIndexMatch = true;
				if (n.getParentTreeNode() != null) {
					isParentIndexMatch = n.getParentIndex() == value.getInt("parentIndex");
					if (n.getGrandParentTreeNode() != null) {
						isGrandParentIndexMatch = n.getGrandParentIndex() == value.getInt("grandparentIndex");
						if (n.getSuperParentTreeNode() != null) {
							isSuperParentIndexMatch = n.getSuperParentIndex() == value.getInt("superparentIndex");
						}
					}
				}

				if (isLevelMatch && isIndexMatch && isParentIndexMatch && isGrandParentIndexMatch
						&& isSuperParentIndexMatch) {
					level = value.getInt("level");
					index = value.getInt("index");
					parentIndex = value.getInt("parentIndex");
					grandparentIndex = value.getInt("grandparentIndex");
					superparentIndex = value.getInt("superparentIndex");
					MyTreeNode.all.remove(x);
					break;
				}
			}

			for (int y = 0; y < MyTreeNode.all.length(); y++) {
				JSONObject v = MyTreeNode.all.getJSONObject(y);
				if (parentIndex == v.getInt("parentIndex") && grandparentIndex == v.getInt("grandparentIndex")
						&& superparentIndex == v.getInt("superparentIndex") && level == v.getInt("level")) {

					if (index < v.getInt("index"))
						MyTreeNode.all.getJSONObject(y).put("index", v.getInt("index") - 1);
				}
			}

			System.out.println("Removing this node : " + currentNode);
			if (parent != null) {
				treeModel.removeNodeFromParent(currentNode);
				return;
			}
		}

		// Either there was no selection, or the root was selected.
		toolkit.beep();
	}

	public void moveCurrentNode(String direction) {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			DefaultMutableTreeNode previousNode = currentNode.getPreviousNode();
			DefaultMutableTreeNode nextNode = currentNode.getNextNode();

			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());

			int level = -1;
			int index = -1;
			int parentIndex = -1;
			int grandparentIndex = -1;
			int superparentIndex = -1;

			MyTreeNode n = new MyTreeNode(currentNode);
			for (int x = 0; x < MyTreeNode.all.length(); x++) {
				JSONObject value = (JSONObject) MyTreeNode.all.get(x);

				boolean isIndexMatch = n.getIndex() == value.getInt("index");
				boolean isLevelMatch = n.getLevel() == value.getInt("level");

				boolean isParentIndexMatch = true;
				boolean isGrandParentIndexMatch = true;
				boolean isSuperParentIndexMatch = true;
				if (n.getParentTreeNode() != null) {
					isParentIndexMatch = n.getParentIndex() == value.getInt("parentIndex");
					if (n.getGrandParentTreeNode() != null) {
						isGrandParentIndexMatch = n.getGrandParentIndex() == value.getInt("grandparentIndex");
						if (n.getSuperParentTreeNode() != null) {
							isSuperParentIndexMatch = n.getSuperParentIndex() == value.getInt("superparentIndex");
						}
					}
				}

				if (isLevelMatch && isIndexMatch && isParentIndexMatch && isGrandParentIndexMatch
						&& isSuperParentIndexMatch) {
					level = value.getInt("level");
					index = value.getInt("index");
					parentIndex = value.getInt("parentIndex");
					grandparentIndex = value.getInt("grandparentIndex");
					superparentIndex = value.getInt("superparentIndex");
					break;
				}
			}

			JSONArray org = MyTreeNode.all;
			for (int y = 0; y < MyTreeNode.all.length(); y++) {
				JSONObject v = MyTreeNode.all.getJSONObject(y);
				JSONObject o = org.getJSONObject(y);
				boolean isTrue = parentIndex == v.getInt("parentIndex")
						&& grandparentIndex == v.getInt("grandparentIndex")
						&& superparentIndex == v.getInt("superparentIndex") && level == v.getInt("level");

				if (index == o.getInt("index") && isTrue) {
					if (direction.equalsIgnoreCase("down")) {
						v.put("index", o.getInt("index") + 1);
						treeModel.removeNodeFromParent(currentNode);
						treeModel.insertNodeInto(currentNode, parent, o.getInt("index"));

					} else if (direction.equalsIgnoreCase("up")) {
						v.put("index", o.getInt("index") - 1);
						treeModel.removeNodeFromParent(currentNode);
						treeModel.insertNodeInto(currentNode, parent, o.getInt("index"));
					}
				} else if (index + 1 == o.getInt("index") && isTrue) {
					if (direction.equalsIgnoreCase("down")) {
						v.put("index", o.getInt("index") - 1);
					}
				} else if (index - 1 == o.getInt("index") && isTrue) {
					if (direction.equalsIgnoreCase("up")) {
						v.put("index", o.getInt("index") + 1);
					}
				}
			}

			System.out.println("Moving this node : " + currentNode);
		}
	}

	public void removeAll() {
		for (int x = treeModel.getChildCount(rootNode) - 1; x >= 0; x--) {
			treeModel.removeNodeFromParent((DefaultMutableTreeNode) treeModel.getChild(rootNode, x));
		}

//		System.out.println("Length: " +  MyTreeNode.all.length());
		if (MyTreeNode.all.length() != 0) {
			MyTreeNode.all = new JSONArray();
		}
	}

	/** Add child to the currently selected node. */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}

		return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		return addObject(parent, child, false);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

		if (parent == null) {
			parent = rootNode;
		}

		// It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	public DefaultMutableTreeNode getSelectedNode() {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}

		return parentNode;
	}

	public int getSelectedNodeLevel() {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}

		return parentNode.getLevel();
	}

	class MyTreeModelListener implements TreeModelListener {
		public void treeNodesChanged(TreeModelEvent e) {
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

			/*
			 * If the event lists children, then the changed node is the child of the node
			 * we've already gotten. Otherwise, the changed node and the specified node are
			 * the same.
			 */

			int index = e.getChildIndices()[0];
			node = (DefaultMutableTreeNode) (node.getChildAt(index));

			System.out.println("The user has finished editing the node.");
			System.out.println("New value: " + node.getUserObject());
		}

		public void treeNodesInserted(TreeModelEvent e) {
		}

		public void treeNodesRemoved(TreeModelEvent e) {
		}

		public void treeStructureChanged(TreeModelEvent e) {
		}
	}
}