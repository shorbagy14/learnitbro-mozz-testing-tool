package com.learnitbro.testing.tool.window;

import java.awt.GridLayout;
import java.awt.Toolkit;

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

			MyTreeNode n = new MyTreeNode(currentNode);

			JSONObject levels = MyTreeNode.list.getJSONObject("levels");
			JSONObject superparentIndex = levels.getJSONObject(String.valueOf(n.getLevel()))
					.getJSONObject("superparentIndex");
			JSONObject grandparentIndex = superparentIndex.getJSONObject(String.valueOf(n.getSuperParentIndex()))
					.getJSONObject("grandparentIndex");
			JSONObject parentIndex = grandparentIndex.getJSONObject(String.valueOf(n.getGrandParentIndex()))
					.getJSONObject("parentIndex");
			JSONArray arr = parentIndex.getJSONArray(String.valueOf(n.getParentIndex()));
			for (int i = 0; i < arr.length(); i++) {
				if (n.getIndex() == i) {
					arr.remove(i);
					break;
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
			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());

			MyTreeNode n = new MyTreeNode(currentNode);

			JSONObject levels = MyTreeNode.list.getJSONObject("levels");
			JSONObject superparentIndex = levels.getJSONObject(String.valueOf(n.getLevel()))
					.getJSONObject("superparentIndex");
			JSONObject grandparentIndex = superparentIndex.getJSONObject(String.valueOf(n.getSuperParentIndex()))
					.getJSONObject("grandparentIndex");
			JSONObject parentIndex = grandparentIndex.getJSONObject(String.valueOf(n.getGrandParentIndex()))
					.getJSONObject("parentIndex");
			JSONArray arr = parentIndex.getJSONArray(String.valueOf(n.getParentIndex()));
			for (int i = 0; i < arr.length(); i++) {
				if (n.getIndex() == i) {
					if (direction.equalsIgnoreCase("down")) {
						treeModel.removeNodeFromParent(currentNode);
						treeModel.insertNodeInto(currentNode, parent, i + 1);
						JSONObject x = arr.getJSONObject(i);
						JSONObject x1 = arr.getJSONObject(i + 1);
						arr.put(i, x1);
						arr.put(i+1, x);
						break;
					} else if (direction.equalsIgnoreCase("up")) {
						treeModel.removeNodeFromParent(currentNode);
						treeModel.insertNodeInto(currentNode, parent, i - 1);
						JSONObject x = arr.getJSONObject(i);
						JSONObject x1 = arr.getJSONObject(i - 1);
						arr.put(i, x1);
						arr.put(i-1, x);
						break;
					}
				}
			}

			System.out.println( MyTreeNode.list);
			System.out.println("Moving this node : " + currentNode);
		}
	}

	public void removeAll() {
		for (int x = treeModel.getChildCount(rootNode) - 1; x >= 0; x--) {
			treeModel.removeNodeFromParent((DefaultMutableTreeNode) treeModel.getChild(rootNode, x));
		}

//		System.out.println("Length: " +  MyTreeNode.all.length());
		if (MyTreeNode.list.length() != 0) {
			MyTreeNode.list = new JSONObject();
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