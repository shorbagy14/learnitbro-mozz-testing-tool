package com.learnitbro.testing.tool.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;
import com.learnitbro.testing.tool.run.Control;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UI extends JPanel implements ActionListener {

	private static String ADD_COMMAND = "add";
	private static String REMOVE_COMMAND = "remove";
	private static String LAUNCH_COMMAND = "launch";

	public JFrame frame;
	private DynamicTree treePanel;

	private JTextField tf_01, tf_02;

	static JPanel generalPanel;
	
	String[] add = {"Category", "Test Case"};
	String[] action = {"Link", "Click", "Clear", "Send Keys", "Upload"};
	String[] wait = {"Page to Load", "Clickable Element", "Visible Element", "Available Element"};

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (ADD_COMMAND.equals(command)) {
			// Add button clicked
			String name = ((JMenuItem) e.getSource()).getText();
			DefaultMutableTreeNode node = treePanel.addObject(name);
			addNode(node.getLevel(), name, node);

		} else if (REMOVE_COMMAND.equals(command)) {
			// Remove button clicked
			treePanel.removeCurrentNode();
		} else if (LAUNCH_COMMAND.equals(command)) {
			saveTree();
			Control control = new Control();
			control.start();
		}
	}

	private void addNode(int level, String name, DefaultMutableTreeNode node) {

		UUID uuid = UUID.randomUUID();

		MyTreeNode myNode = new MyTreeNode(node);
		myNode.setUUID(uuid.toString());
		myNode.setName(name);

		if (1 == level || 2 == level) {
			myNode.build();
		}
		
		// NEED TO MAKE CHANGES
		
		tf_01 = new JTextField();
		tf_01.setBounds(89, 213, 513, 35);
		tf_01.setColumns(10);
		tf_01.setName(uuid.toString());
		tf_01.getDocument().putProperty("type", "t1");

		tf_02 = new JTextField();
		tf_02.setBounds(89, 313, 513, 35);
		tf_02.setColumns(10);
		tf_02.setName(uuid.toString());
		tf_02.getDocument().putProperty("type", "t2");

		JLabel lbl_01 = new JLabel();
		lbl_01.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_01.setBounds(275, 185, 161, 16);
		lbl_01.setName(uuid.toString());

		JLabel lbl_02 = new JLabel();
		lbl_02.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_02.setBounds(275, 285, 161, 16);
		lbl_02.setName(uuid.toString());

		if ("click".equalsIgnoreCase(name)) {
			// Xpath
			lbl_01.setText("Xpath");
			generalPanel.add(tf_01);
			generalPanel.add(lbl_01);
			myNode.build();
		} else if ("link".equalsIgnoreCase(name)) {
			// Link
			lbl_01.setText("Link");
			generalPanel.add(tf_01);
			generalPanel.add(lbl_01);
			myNode.build();
		} else if ("clear".equalsIgnoreCase(name)) {
			// Xpath
			lbl_01.setText("Xpath");
			generalPanel.add(tf_01);
			generalPanel.add(lbl_01);
			myNode.build();
		} else if ("upload".equalsIgnoreCase(name)) {
			// Xpath
			lbl_01.setText("Xpath");
			generalPanel.add(tf_01);
			generalPanel.add(lbl_01);
			// File location
			lbl_02.setText("Text");
			generalPanel.add(tf_02);
			generalPanel.add(lbl_02);
			myNode.build();
		} else if ("send keys".equalsIgnoreCase(name)) {
			// Xpath
			lbl_01.setText("Xpath");
			generalPanel.add(tf_01);
			generalPanel.add(lbl_01);
			// Text to send
			lbl_02.setText("Text To Type");
			generalPanel.add(tf_02);
			generalPanel.add(lbl_02);
			myNode.build();
		}
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1040, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		treePanel = new DynamicTree();
		treePanel.setBackground(Color.WHITE);
		treePanel.setBounds(0, 0, 276, 607);
		frame.getContentPane().add(treePanel);

		// populateTree(treePanel);

		// <---- Button Panel

		JPanel btnPanel = new JPanel();
		btnPanel.setBounds(275, 537, 765, 69);
		frame.getContentPane().add(btnPanel);
		btnPanel.setLayout(null);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setEnabled(false);
		btnRefresh.setBounds(86, 18, 136, 29);
		btnPanel.add(btnRefresh);
		btnRefresh.setActionCommand(ADD_COMMAND);
		btnRefresh.addActionListener(this);

		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(316, 18, 136, 29);
		btnPanel.add(btnRemove);
		btnRemove.setActionCommand(REMOVE_COMMAND);
		btnRemove.addActionListener(this);

		JButton btnLaunch = new JButton("Launch");
		btnLaunch.setBounds(543, 18, 136, 29);
		btnPanel.add(btnLaunch);
		btnLaunch.setActionCommand(LAUNCH_COMMAND);
		btnLaunch.addActionListener(this);

		// End of Button Panel ---->

		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(275, 0, 765, 538);
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		// <---- General Panel

		generalPanel = new JPanel();
		generalPanel.setBounds(39, 49, 685, 447);
		mainPanel.add(generalPanel);
		generalPanel.setLayout(null);
		generalPanel.setVisible(false);

		// End of General Panel ---->

		// <---- Menu Bar

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu lblGeneralText = new JMenu("File");
		menuBar.add(lblGeneralText);

		JMenuItem mntmSave = new JMenuItem("Save");
		lblGeneralText.add(mntmSave);

		JMenuItem mntmLoad = new JMenuItem("Load");
		lblGeneralText.add(mntmLoad);

		// Add Menu
		final JMenu mnAdd = new JMenu("Add");
		menuBar.add(mnAdd);
		for(int x=0; x< add.length; x++) {
			JMenuItem mntm = new JMenuItem(add[x]);
			mnAdd.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAdd);
		enableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
		
//		// Condition Menu
//		JMenu mnCondition = new JMenu("Condition");
//		menuBar.add(mnCondition);
		
		/**
		 *  Condition Menu - To be added
		 */

		// Action Menu
		final JMenu mnAction = new JMenu("Action");
		menuBar.add(mnAction);
		for(int x=0; x< action.length; x++) {
			JMenuItem mntm = new JMenuItem(action[x]);
			mnAction.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAction);

		// Wait Menu
		JMenu mnWait = new JMenu("Wait");
		menuBar.add(mnWait);
		for(int x=0; x< wait.length; x++) {
			JMenuItem mntm = new JMenuItem(wait[x]);
			mnWait.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnWait);

//		// Assert Menu
//		JMenu mnAssert = new JMenu("Assert");
//		menuBar.add(mnAssert);
		
		/**
		 *  Assert Menu - To be added
		 */

		// End of Menu Bar ---->

		MouseListener mouseListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				checkTreePanel();
			}

			public void mousePressed(MouseEvent e) {
				checkTreePanel();
			}

			public void mouseReleased(MouseEvent e) {
				checkTreePanel();
			}

			public void mouseEntered(MouseEvent e) {
				checkTreePanel();
			}

			public void mouseExited(MouseEvent e) {
				checkTreePanel();
			}

			public void checkTreePanel() {
				int level = treePanel.getSelectedNodeLevel();
				if (level == 0 || level == 1) {
					disableMenuItems(mnAction);
					if (level == 0) {
						enableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(1));
					} else {
						enableMenuItem((JMenuItem) mnAdd.getMenuComponent(1));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
					}

				} else if (level == 2) {
					enableMenuItems(mnAction);
					disableMenuItems(mnAdd);
				} else {
					disableMenuItems(mnAction);
					disableMenuItems(mnAdd);
				}
			}
		};

		// Save Tree
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTree();
			}
		});

		// Load Tree
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String content = null;
				try {
					content = JSONHandler.read(new File(FileHandler.getUserDir() + "/tree.json"));
				} catch (Exception ex) {
					throw new ReadFileException("Can't read file", ex);
				}

				Object root = treePanel.getTreeModel().getRoot();
				int count = treePanel.getTreeModel().getChildCount(root);
				System.out.println(count);
				if (count != 0) {
					// ADD REMOVE FUNCTION
				}

				JSONObject obj = new JSONObject(content);
				JSONArray category = obj.getJSONArray("children");
				for (int x = 0; x < category.length(); x++) {
					DefaultMutableTreeNode p1;
					JSONObject cat = category.getJSONObject(x);
					p1 = treePanel.addObject(null, cat.getString("userObject"));
					addNode(1, cat.getString("userObject"), p1);

					JSONArray testCase = cat.getJSONArray("children");
					for (int y = 0; y < testCase.length(); y++) {
						DefaultMutableTreeNode p2;
						JSONObject test = testCase.getJSONObject(y);
						p2 = treePanel.addObject(p1, test.getString("userObject"));
						addNode(2, test.getString("userObject"), p2);
						JSONArray input = test.getJSONArray("children");
						try {
							for (int i = 0; i < input.length(); i++) {
								DefaultMutableTreeNode p3;
								JSONObject run = input.getJSONObject(i);
								p3 = treePanel.addObject(p2, run.getString("userObject"));
								addNode(3, run.getString("userObject"), p3);
								if (run.has("t1"))
									tf_01.setText(run.getString("t1"));
								if (run.has("t2"))
									tf_02.setText(run.getString("t2"));
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});

		treePanel.getJTree().addMouseListener(mouseListener);

		treePanel.getJTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePanel.getJTree()
						.getLastSelectedPathComponent();

				/* if nothing is selected */
				if (node == null) {
					generalPanel.setVisible(false);
					return;
				}
				
				if(node.getChildCount() == 0)
					btnRemove.setEnabled(true);
				else
					btnRemove.setEnabled(false);
				
				/* retrieve the node that was selected */
				// Object nodeInfo = node.getUserObject();
				int level = node.getLevel();

				MyTreeNode myNode = new MyTreeNode(node);
				System.out.println(myNode);

				if (level == 3) {
					generalPanel.setVisible(true);
					treePanel.getJTree().setEditable(false);
					for (Component item : UI.generalPanel.getComponents()) {
						if (item.toString().contains("JTextField")) {
							String name = ((JTextField) item).getName();
//							System.out.println(name);

							if (myNode.isMatch(name)) {
								((JTextField) item).setVisible(true);
							} else {
								((JTextField) item).setVisible(false);
							}
						} else if (item.toString().contains("JLabel")) {
							String name = ((JLabel) item).getName();
							if (myNode.isMatch(name)) {
								((JLabel) item).setVisible(true);
							} else {
								((JLabel) item).setVisible(false);
							}
						}
					}
				} else {
					generalPanel.setVisible(false);
					treePanel.getJTree().setEditable(true);
				}
			}
		});
	}

	public void listNodes(TreeNode node) {
		int count = node.getChildCount();
		for (int x = 0; x < count; x++) {
			if (node.getChildAt(x).getChildCount() == 0) {
				System.out.println(node.getChildAt(x));
			} else {
				System.out.println(node.getChildAt(x));
				listNodes(node.getChildAt(x));
			}
		}
	}

	public void disableMenuItems(JMenu menu) {
		for (int x = 0; x < menu.getItemCount(); x++) {
			menu.getItem(x).setEnabled(false);
		}
	}

	public void enableMenuItems(JMenu menu) {
		for (int x = 0; x < menu.getItemCount(); x++) {
			menu.getItem(x).setEnabled(true);
		}
	}

	public void enableMenuItem(JMenuItem menuItem) {
		menuItem.setEnabled(true);
	}

	public void disableMenuItem(JMenuItem menuItem) {
		menuItem.setEnabled(false);
	}

	public void saveTree() {
		Object root = treePanel.getTreeModel().getRoot();
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(DefaultMutableTreeNode.class, new DefaultMutableTreeNodeSerializer())
				// .registerTypeAdapter(DefaultMutableTreeNode.class, new
				// DefaultMutableTreeNodeDeserializer())
				.setPrettyPrinting().create();

		String jsonString = gson.toJson(root);
		System.out.println(jsonString);
		JSONHandler.write(new File(FileHandler.getUserDir() + "/tree.json"), jsonString);
	}
}
