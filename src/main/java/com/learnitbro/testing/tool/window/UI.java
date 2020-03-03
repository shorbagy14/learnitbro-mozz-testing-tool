package com.learnitbro.testing.tool.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;
import com.learnitbro.testing.tool.run.Control;
import com.learnitbro.testing.tool.stream.StreamHandler;
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
	static JPanel generalPanel;
	String config;

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

		config = StreamHandler.inputStreamTextBuilder(getClass().getResourceAsStream("/config.json"));

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

		// <---- Tree Panel

		treePanel = new DynamicTree();
		treePanel.setBackground(Color.WHITE);
		treePanel.setBounds(0, 0, 276, 607);
		frame.getContentPane().add(treePanel);
//		treePanel.setRootUserObject("Test Suite");
		addNode(0, "suite", "Test Suite", treePanel.getDefaultMutableTreeNode());

		// End of Tree Panel ---->
		// <---- Menu Bar

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu lblGeneralText = new JMenu("File");
		menuBar.add(lblGeneralText);

		JMenuItem mntmSave = new JMenuItem("Save");
		lblGeneralText.add(mntmSave);

		JMenuItem mntmLoad = new JMenuItem("Load");
		lblGeneralText.add(mntmLoad);

		List<String> action = new ArrayList<String>();
		List<String> waiting = new ArrayList<String>();
		List<String> asserting = new ArrayList<String>();
		List<String> adding = new ArrayList<String>();

		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {
			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCat = obj.getString("category");
			
			if (objCat.equalsIgnoreCase("add"))
				adding.add(objName);
			else if (objCat.equalsIgnoreCase("wait"))
				waiting.add(objName);
			else if (objCat.equalsIgnoreCase("assert"))
				asserting.add(objName);
			else if (objCat.equalsIgnoreCase("action"))
				action.add(objName);


		}

		// Add Menu
		final JMenu mnAdd = new JMenu("Add");
		menuBar.add(mnAdd);
		for (int x = 0; x < adding.size(); x++) {
			JMenuItem mntm = new JMenuItem(adding.get(x));
			mntm.putClientProperty("category", adding.get(x));
			mnAdd.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAdd);
		enableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));

		/**
		 * Condition Menu - To be added
		 */

		// Action Menu
		final JMenu mnAction = new JMenu("Action");
		menuBar.add(mnAction);
		for (int x = 0; x < action.size(); x++) {
			JMenuItem mntm = new JMenuItem(action.get(x));
			mntm.putClientProperty("category", "action");
			mnAction.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAction);

		// Wait Menu
		final JMenu mnWait = new JMenu("Wait");
		menuBar.add(mnWait);
		for (int x = 0; x < waiting.size(); x++) {
			JMenuItem mntm = new JMenuItem(waiting.get(x));
			mntm.putClientProperty("category", "wait");
			mnWait.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnWait);

		// Assert Menu
		final JMenu mnAssert = new JMenu("Assert");
		menuBar.add(mnAssert);
		for (int x = 0; x < asserting.size(); x++) {
			JMenuItem mntm = new JMenuItem(asserting.get(x));
			mntm.putClientProperty("category", "assert");
			mnAssert.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAssert);

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

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePanel.getJTree()
						.getLastSelectedPathComponent();
				if (node != null) {
					MyTreeNode myNode = new MyTreeNode(node);
					setVisibilty(myNode);
				}

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
					enableMenuItems(mnWait);
					enableMenuItems(mnAssert);
					disableMenuItems(mnAdd);
				} else {
					disableMenuItems(mnAction);
					disableMenuItems(mnWait);
					disableMenuItems(mnAdd);
					disableMenuItems(mnAssert);
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
				Object root = treePanel.getTreeModel().getRoot();
				int count = treePanel.getTreeModel().getChildCount(root);
				if (count != 0) {
					treePanel.removeAll();
					UI.generalPanel.removeAll();
				}
				loadTree();

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePanel.getDefaultMutableTreeNode();
				MyTreeNode myNode = new MyTreeNode(node);
				setVisibilty(myNode);
//				generalPanel.setVisible(false);
			}
		});

		treePanel.getJTree().addMouseListener(mouseListener);

		// Handles changes in the tree (Control what the user see based on selection)
		treePanel.getJTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePanel.getJTree()
						.getLastSelectedPathComponent();

				/* if nothing is selected */
				if (node == null) {
					generalPanel.setVisible(false);
					return;
				}

				if (node.getChildCount() == 0)
					btnRemove.setEnabled(true);
				else
					btnRemove.setEnabled(false);

				/* retrieve the node that was selected */
				// Object nodeInfo = node.getUserObject();
				int level = node.getLevel();

				MyTreeNode myNode = new MyTreeNode(node);
//				System.out.println(myNode);

				setVisibilty(myNode);

				if (level == 3) {
					treePanel.getJTree().setEditable(false);
				} else {
//					generalPanel.setVisible(false);
					treePanel.getJTree().setEditable(true);
				}
			}
		});
	}

	private void loadTree() {

		String content = null;
		try {
			content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/tree.json"));
		} catch (Exception ex) {
			throw new ReadFileException("Can't read file", ex);
		}

		JSONObject suite = new JSONObject(content);
		DefaultMutableTreeNode p0;
		treePanel.setRootUserObject(suite.getString("userObject"));
		p0 = treePanel.getDefaultMutableTreeNode();
		setValues(p0, suite);	

		JSONArray group = suite.getJSONArray("children");
		for (int x = 0; x < group.length(); x++) {
			DefaultMutableTreeNode p1;
			JSONObject cat = group.getJSONObject(x);
			p1 = treePanel.addObject(null, cat.getString("userObject"));
			addNode(1, "category", cat.getString("userObject"), p1);

			JSONArray testCase = cat.getJSONArray("children");
			for (int y = 0; y < testCase.length(); y++) {
				DefaultMutableTreeNode p2;
				JSONObject test = testCase.getJSONObject(y);
				p2 = treePanel.addObject(p1, test.getString("userObject"));
				addNode(2, "testCase", test.getString("userObject"), p2);
				JSONArray input = test.getJSONArray("children");
				try {
					for (int i = 0; i < input.length(); i++) {
						DefaultMutableTreeNode p3;
						JSONObject run = input.getJSONObject(i);
						p3 = treePanel.addObject(p2, run.getString("userObject"));
						addNode(3, run.getString("category"), run.getString("userObject"), p3);
						setValues(p3, run);	
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setValues(DefaultMutableTreeNode p3, JSONObject run) {
		for (Component item : UI.generalPanel.getComponents()) {
			MyTreeNode myNode = new MyTreeNode(p3);
			if (item.toString().contains("JTextField")) {
				String uuid = ((JTextField) item).getName();
				if (myNode.isMatch(uuid)) {
					JTextField jtf = ((JTextField) item);
					String type = jtf.getClientProperty("type").toString();
					int index = Integer.parseInt(jtf.getClientProperty("index").toString());
					jtf.setText(run.getJSONArray(type).getString(index));
				}
			} else if (item.toString().contains("JComboBox")) {
				String uuid = ((JComboBox) item).getName();
				if (myNode.isMatch(uuid)) {
					JComboBox jcb = ((JComboBox) item);
					String type = jcb.getClientProperty("type").toString();
					int index = Integer.parseInt(jcb.getClientProperty("index").toString());
					jcb.setSelectedItem(run.getJSONArray(type).getString(index));
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void setVisibilty(MyTreeNode myNode) {

		generalPanel.setVisible(true);
		for (Component item : UI.generalPanel.getComponents()) {
			if (item.toString().contains("JTextField")) {
				String uuid = ((JTextField) item).getName();
				if (myNode.isMatch(uuid)) {
					((JTextField) item).setVisible(true);
				} else {
					((JTextField) item).setVisible(false);
				}
			} else if (item.toString().contains("JLabel")) {
				String uuid = ((JLabel) item).getName();
				if (myNode.isMatch(uuid)) {
					((JLabel) item).setVisible(true);
				} else {
					((JLabel) item).setVisible(false);
				}
			} else if (item.toString().contains("JComboBox")) {
				String uuid = ((JComboBox) item).getName();
				if (myNode.isMatch(uuid)) {
					((JComboBox) item).setVisible(true);
				} else {
					((JComboBox) item).setVisible(false);
				}
			}
		}
	}

//	private void listNodes(TreeNode node) {
//		int count = node.getChildCount();
//		for (int x = 0; x < count; x++) {
//			if (node.getChildAt(x).getChildCount() == 0) {
//				System.out.println(node.getChildAt(x));
//			} else {
//				System.out.println(node.getChildAt(x));
//				listNodes(node.getChildAt(x));
//			}
//		}
//	}

	private void disableMenuItems(JMenu menu) {
		for (int x = 0; x < menu.getItemCount(); x++) {
			menu.getItem(x).setEnabled(false);
		}
	}

	private void enableMenuItems(JMenu menu) {
		for (int x = 0; x < menu.getItemCount(); x++) {
			menu.getItem(x).setEnabled(true);
		}
	}

	private void enableMenuItem(JMenuItem menuItem) {
		menuItem.setEnabled(true);
	}

	private void disableMenuItem(JMenuItem menuItem) {
		menuItem.setEnabled(false);
	}

	private void saveTree() {
		Object root = treePanel.getTreeModel().getRoot();
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(DefaultMutableTreeNode.class, new DefaultMutableTreeNodeSerializer())
				// .registerTypeAdapter(DefaultMutableTreeNode.class, new
				// DefaultMutableTreeNodeDeserializer())
				.setPrettyPrinting().create();

		String jsonString = gson.toJson(root);
		System.out.println(jsonString);
		JSONHandler.write(new File(FileHandler.getUserDir() + "/temp/tree.json"), jsonString);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (ADD_COMMAND.equals(command)) {
			// Add button clicked
			String name = ((JMenuItem) e.getSource()).getText();
			DefaultMutableTreeNode node = treePanel.addObject(name);
			String category = ((JMenuItem) e.getSource()).getClientProperty("category").toString();
			int level = node.getLevel();
			addNode(level, category, name, node);

		} else if (REMOVE_COMMAND.equals(command)) {
			// Remove button clicked
			treePanel.removeCurrentNode();
		} else if (LAUNCH_COMMAND.equals(command)) {
			saveTree();
			Control control = new Control();
			control.start();
		}
	}

	private void addNode(int level, String category, String name, DefaultMutableTreeNode node) {

		String uuid = UUID.randomUUID().toString();

		MyTreeNode myNode = new MyTreeNode(node);
		myNode.setUUID(uuid);
		
		JLabel lblTop = new JLabel();
		lblTop.setHorizontalAlignment(SwingConstants.CENTER);
		lblTop.setBounds(275, 0, 160, 15);
		lblTop.setFont(lblTop.getFont().deriveFont(Font.BOLD, 14f));
		lblTop.setName(uuid);
		lblTop.setText(name);

		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {

			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCategory = obj.getString("category");
			JSONArray req = obj.getJSONArray("require");

			if (level == 0) {

				if (objCategory.equalsIgnoreCase("suite")) {
					int posX = 0;
					
					List<String> list = new ArrayList<String>();
					for (int y = 0; y < req.length(); y++) {

						String v = req.getString(y);
						list.add(v);

						JLabel lbl = new JLabel();
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBounds(10 + posX, 25, 160, 15);
						lbl.setName(uuid);
						lbl.setText(v);
						generalPanel.add(lbl);

						JComboBox<String> jcb = null;
						if (v.equals("browserName")) {
							jcb = new JComboBox<String>(
									new String[] { "chrome", "firefox", "edge", "ie", "safari", "opera" });
						} else if (v.equals("headless")) {
							jcb = new JComboBox<String>(new String[] { "false", "true" });
						} else if (v.equals("platform")) {
							jcb = new JComboBox<String>(new String[] { "web" });
						} else {
							throw new IllegalArgumentException("Wrong argument in the configuration file");
						}

						jcb.setName(uuid);
						jcb.setBounds(40 + posX, 60, 100, 25);
						jcb.putClientProperty("type", v);
						jcb.putClientProperty("category", objCategory);
						jcb.putClientProperty("index", Collections.frequency(list, v) - 1);
						generalPanel.add(jcb);

						posX += 250;
					}
				}
			}

			else if (level == 3) {

				if (objName.equalsIgnoreCase(name) && objCategory.equalsIgnoreCase(category)) {
					int posY = 20;
					
					if(req.length() == 0) {
						lblTop.putClientProperty("category", objCategory);
					}
					
					List<String> list = new ArrayList<String>();
					for (int y = 0; y < req.length(); y++) {

						String v = req.getString(y);
						list.add(v);
						
						JTextField jtf = new JTextField();
						jtf.setBounds(150, 55 + posY, 450, 35);
						jtf.setColumns(10);
						jtf.setName(uuid);
						jtf.putClientProperty("type", v);
						jtf.putClientProperty("category", objCategory);
						jtf.putClientProperty("index", Collections.frequency(list, v) - 1);
						generalPanel.add(jtf);

						JLabel lbl = new JLabel();
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBounds(275, 25 + posY, 160, 15);
						lbl.setName(uuid);
						lbl.setText(v);
						generalPanel.add(lbl);

						if (v.equals("locator")) {
							JComboBox<String> jcb = new JComboBox<String>(
									new String[] { "xpath", "css", "class", "id", "name" });
							jcb.setName(uuid);
							jcb.setBounds(40, 60 + posY, 100, 25);
							jcb.putClientProperty("type", "locatorType");
							jcb.putClientProperty("category", objCategory);
							jcb.putClientProperty("index", Collections.frequency(list, v) - 1);
							generalPanel.add(jcb);
						}

						posY += 100;
					}
				}
			}
		}

		generalPanel.add(lblTop);
		myNode.build();
	}
}
