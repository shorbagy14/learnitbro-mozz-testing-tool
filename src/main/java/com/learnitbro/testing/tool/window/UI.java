package com.learnitbro.testing.tool.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

	String config;

	static JPanel generalPanel;

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

		if (level == 3) {
			JSONArray arr = new JSONArray(config);
			for (int x = 0; x < arr.length(); x++) {
				JSONObject obj = arr.getJSONObject(x);
				String objName = obj.getString("name");
				JSONArray req = obj.getJSONArray("require");

				if (objName.equalsIgnoreCase(name)) {
					int posY = 100;
//					int posX = 50;
					for (int y = 0; y < req.length(); y++) {

						String v = req.getString(y);

						JTextField jtf = new JTextField();
						jtf.setBounds(150, 55 + posY, 450, 35);
						jtf.setColumns(10);
						jtf.setName(uuid.toString());
						jtf.getDocument().putProperty("type", v);
						generalPanel.add(jtf);

						JLabel lbl = new JLabel();
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBounds(275, 25 + posY, 160, 15);
						lbl.setName(uuid.toString());
						lbl.setText(v);
						generalPanel.add(lbl);

						if (req.getString(y).equals("locator")) {
							JComboBox<String> jcb = new JComboBox<String>(
									new String[] { "xpath", "css", "class", "id", "name" });
							jcb.setName(uuid.toString());
							jcb.setBounds(40, 60 + posY, 100, 25);
							generalPanel.add(jcb);
						}

						posY += 100;
					}
				}
			}
		}

		myNode.build();
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
		List<String> wait = new ArrayList<String>();
		List<String> add = new ArrayList<String>();

		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {
			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCat = obj.getString("category");
			if (objCat.equalsIgnoreCase("action"))
				action.add(objName);
			else if (objCat.equalsIgnoreCase("wait"))
				wait.add(objName);
			else if (objCat.equalsIgnoreCase("add"))
				add.add(objName);

		}

		// Add Menu
		final JMenu mnAdd = new JMenu("Add");
		menuBar.add(mnAdd);
		for (int x = 0; x < add.size(); x++) {
			JMenuItem mntm = new JMenuItem(add.get(x));
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
		 * Condition Menu - To be added
		 */

		// Action Menu
		final JMenu mnAction = new JMenu("Action");
		menuBar.add(mnAction);
		for (int x = 0; x < action.size(); x++) {
			JMenuItem mntm = new JMenuItem(action.get(x));
			mnAction.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnAction);

		// Wait Menu
		JMenu mnWait = new JMenu("Wait");
		menuBar.add(mnWait);
		for (int x = 0; x < wait.size(); x++) {
			JMenuItem mntm = new JMenuItem(wait.get(x));
			mnWait.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnWait);

//		// Assert Menu
//		JMenu mnAssert = new JMenu("Assert");
//		menuBar.add(mnAssert);

		/**
		 * Assert Menu - To be added
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
					enableMenuItems(mnWait);
					disableMenuItems(mnAdd);
				} else {
					disableMenuItems(mnAction);
					disableMenuItems(mnWait);
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
					content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/tree.json"));
				} catch (Exception ex) {
					throw new ReadFileException("Can't read file", ex);
				}

				Object root = treePanel.getTreeModel().getRoot();
				int count = treePanel.getTreeModel().getChildCount(root);
//				System.out.println(count);
				if (count != 0) {
					// ADD REMOVE FUNCTION
					treePanel.removeAll();
					UI.generalPanel.removeAll();
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

								for (Component item : UI.generalPanel.getComponents()) {
									MyTreeNode myNode = new MyTreeNode(p3);
									if (item.toString().contains("JTextField")) {
										String uuid = ((JTextField) item).getName();
										if (myNode.isMatch(uuid)) {
											JTextField jtf = ((JTextField) item);
											String type = jtf.getDocument().getProperty("type").toString();
											jtf.setText(run.getString(type));
										}
									} else if (item.toString().contains("JComboBox")) {
										String uuid = ((JComboBox) item).getName();
										if (myNode.isMatch(uuid)) {
											JComboBox jcb = ((JComboBox) item);
											String type = "locatorType";
											jcb.setSelectedItem(run.getString(type));
										}
									}
								}
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

				if (node.getChildCount() == 0)
					btnRemove.setEnabled(true);
				else
					btnRemove.setEnabled(false);

				/* retrieve the node that was selected */
				// Object nodeInfo = node.getUserObject();
				int level = node.getLevel();

				MyTreeNode myNode = new MyTreeNode(node);
//				System.out.println(myNode);

				if (level == 3) {
					generalPanel.setVisible(true);
					treePanel.getJTree().setEditable(false);
					for (Component item : UI.generalPanel.getComponents()) {
						if (item.toString().contains("JTextField")) {
							String name = ((JTextField) item).getName();
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
						} else if (item.toString().contains("JComboBox")) {
							String name = ((JComboBox) item).getName();
							if (myNode.isMatch(name)) {
								((JComboBox) item).setVisible(true);
							} else {
								((JComboBox) item).setVisible(false);
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
		JSONHandler.write(new File(FileHandler.getUserDir() + "/temp/tree.json"), jsonString);
	}
}
