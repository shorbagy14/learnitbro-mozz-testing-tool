package com.learnitbro.testing.tool.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
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
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.learnitbro.testing.tool.exceptions.JSONFileNotValidException;
import com.learnitbro.testing.tool.exceptions.ReadFileException;
import com.learnitbro.testing.tool.file.FileHandler;
import com.learnitbro.testing.tool.file.JSONHandler;
import com.learnitbro.testing.tool.file.URLHandler;
import com.learnitbro.testing.tool.run.Control;
import com.learnitbro.testing.tool.stream.StreamHandler;
import com.learnitbro.testing.tool.window.jtextfield.JTextFieldNumberLimit;
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

		JButton btnClear = new JButton("Clear");
		btnClear.setEnabled(false);
		btnClear.setBounds(86, 18, 136, 29);
		btnPanel.add(btnClear);
		btnClear.setActionCommand(ADD_COMMAND);
		btnClear.addActionListener(this);

		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(316, 18, 136, 29);
		btnPanel.add(btnRemove);
		btnRemove.setActionCommand(REMOVE_COMMAND);
		btnRemove.addActionListener(this);
		btnRemove.setEnabled(false);

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

		// End of Tree Panel ---->
		// <---- Menu Bar

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);

		JMenuItem mntmReports = new JMenuItem("Reports");
		mnFile.add(mntmReports);

		JMenuItem mntmScreenshots = new JMenuItem("Screenshots");
		mnFile.add(mntmScreenshots);

		List<String> action = new ArrayList<String>();
		List<String> waiting = new ArrayList<String>();
		List<String> asserting = new ArrayList<String>();
		List<String> adding = new ArrayList<String>();
		List<String> video = new ArrayList<String>();
		List<String> script = new ArrayList<String>();
		List<String> picture = new ArrayList<String>();

		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {
			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCat = obj.getString("category");

			if (objCat.equalsIgnoreCase("configuration") || objCat.equalsIgnoreCase("group")
					|| objCat.equalsIgnoreCase("test"))
				adding.add(objName);
			else if (objCat.equalsIgnoreCase("wait"))
				waiting.add(objName);
			else if (objCat.equalsIgnoreCase("assert"))
				asserting.add(objName);
			else if (objCat.equalsIgnoreCase("action"))
				action.add(objName);
			else if (objCat.equalsIgnoreCase("video"))
				video.add(objName);
			else if (objCat.equalsIgnoreCase("script"))
				script.add(objName);
			else if (objCat.equalsIgnoreCase("picture"))
				picture.add(objName);

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

		// Video Menu
		final JMenu mnVideo = new JMenu("Video");
		menuBar.add(mnVideo);
		for (int x = 0; x < video.size(); x++) {
			JMenuItem mntm = new JMenuItem(video.get(x));
			mntm.putClientProperty("category", "video");
			mnVideo.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnVideo);

		// Script Menu
		final JMenu mnScript = new JMenu("Script");
		menuBar.add(mnScript);
		for (int x = 0; x < script.size(); x++) {
			JMenuItem mntm = new JMenuItem(script.get(x));
			mntm.putClientProperty("category", "script");
			mnScript.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnScript);

		// Picture Menu
		final JMenu mnPicture = new JMenu("Picture");
		menuBar.add(mnPicture);
		for (int x = 0; x < picture.size(); x++) {
			JMenuItem mntm = new JMenuItem(picture.get(x));
			mntm.putClientProperty("category", "picture");
			mnPicture.add(mntm);
			mntm.setActionCommand(ADD_COMMAND);
			mntm.addActionListener(this);
		}
		disableMenuItems(mnPicture);

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
				if (level == 0 || level == 1 || level == 2) {
					if (level == 0) {
						enableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(1));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(2));
					} else if (level == 1) {
						enableMenuItem((JMenuItem) mnAdd.getMenuComponent(1));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(2));
					} else {
						enableMenuItem((JMenuItem) mnAdd.getMenuComponent(2));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(0));
						disableMenuItem((JMenuItem) mnAdd.getMenuComponent(1));
					}

				}

				if (level == 3) {
					enableMenuItems(mnAction);
					enableMenuItems(mnWait);
					enableMenuItems(mnAssert);
					enableMenuItems(mnVideo);
					enableMenuItems(mnScript);
					enableMenuItems(mnPicture);
					disableMenuItems(mnAdd);
				}

				if (level == 4) {
					disableMenuItems(mnAdd);
					disableMenuItems(mnAction);
					disableMenuItems(mnWait);
					disableMenuItems(mnAssert);
					disableMenuItems(mnVideo);
					disableMenuItems(mnScript);
					disableMenuItems(mnPicture);
				}
			}
		};

		// Save Tree
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser j = new JFileChooser(FileHandler.getUserDir());

				// invoke the showsSaveDialog function to show the save dialog
				int r = j.showSaveDialog(null);

				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION)
					saveTree(j.getSelectedFile().getAbsolutePath());
			}
		});

		// Load Tree
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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

//				int index = node.getParent().getIndex(node);
//				int number = node.getParent().getChildCount();
//				|| index+1 != number

				if (node.getChildCount() != 0 || node.isRoot())
					btnRemove.setEnabled(false);
				else
					btnRemove.setEnabled(true);

				/* retrieve the node that was selected */
				// Object nodeInfo = node.getUserObject();
				int level = node.getLevel();

				MyTreeNode myNode = new MyTreeNode(node);
//				System.out.println(myNode);				

				setVisibilty(myNode);

				if (level == 0 || level == 1 || level == 4) {
					treePanel.getJTree().setEditable(false);
				} else {
//					generalPanel.setVisible(false);
					treePanel.getJTree().setEditable(true);
				}
			}
		});

		mntmReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileHandler.open(FileHandler.getUserDir() + File.separator + "reports");
			}
		});

		mntmScreenshots.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileHandler.open(FileHandler.getUserDir() + File.separator + "screenshots");
			}
		});
	}

	private void loadTree() {

		JFileChooser j = new JFileChooser(FileHandler.getUserDir());

		j.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "JSON Files (*.json)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".json");
				}
			}
		});

		// invoke the showOpenDialog function to show the load dialog
		int r = j.showOpenDialog(null);

		// if the user selects a file
		if (r == JFileChooser.APPROVE_OPTION) {

			String content = null;
			try {
//				content = JSONHandler.read(new File(FileHandler.getUserDir() + "/temp/tree.json"));
				content = JSONHandler.read(new File(j.getSelectedFile().getAbsolutePath()));
			} catch (Exception ex) {
				throw new ReadFileException("Can't read file", ex);
			}

			if (!JSONHandler.isJSONValid(content)) {
				JOptionPane.showMessageDialog(null, "File is not a valid JSON", "Error", JOptionPane.ERROR_MESSAGE);
				throw new JSONFileNotValidException("File is not a valid JSON");
			}

			Object root = treePanel.getTreeModel().getRoot();
			int count = treePanel.getTreeModel().getChildCount(root);
			if (count != 0) {
				treePanel.removeAll();
				UI.generalPanel.removeAll();
			}

			JSONObject suite = new JSONObject(content);

			try {
				JSONArray configCase = suite.getJSONArray("children");
				for (int z = 0; z < configCase.length(); z++) {
					DefaultMutableTreeNode p1;
					JSONObject config = configCase.getJSONObject(z);
					p1 = treePanel.addObject(null, config.getString("userObject"));
					addNode(1, "configuration", config.getString("userObject"), p1);
					setValues(p1, config);
					JSONArray groupCase = config.getJSONArray("children");
					for (int x = 0; x < groupCase.length(); x++) {
						DefaultMutableTreeNode p2;
						JSONObject group = groupCase.getJSONObject(x);
						p2 = treePanel.addObject(p1, group.getString("userObject"));
						addNode(2, "group", group.getString("userObject"), p2);
						JSONArray testCase = group.getJSONArray("children");
						for (int y = 0; y < testCase.length(); y++) {
							DefaultMutableTreeNode p3;
							JSONObject test = testCase.getJSONObject(y);
							p3 = treePanel.addObject(p2, test.getString("userObject"));
							addNode(3, "testCase", test.getString("userObject"), p3);
							JSONArray input = test.getJSONArray("children");
							for (int i = 0; i < input.length(); i++) {
								DefaultMutableTreeNode p4;
								JSONObject run = input.getJSONObject(i);
								p4 = treePanel.addObject(p3, run.getString("userObject"));
								addNode(4, run.getString("category"), run.getString("userObject"), p4);
								setValues(p4, run);
							}
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "JSON format is not valid", "Error", JOptionPane.ERROR_MESSAGE);
				throw new JSONFileNotValidException("JSON format is not valid", ex);
			}
			JOptionPane.showMessageDialog(null, "Test has been loaded successfully", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@SuppressWarnings("rawtypes")
	private void setValues(DefaultMutableTreeNode p3, JSONObject run) {
		for (Component item : UI.generalPanel.getComponents()) {
			MyTreeNode myNode = new MyTreeNode(p3);
			if (item.toString().contains("JTextField")) {
				String uuid = ((JTextField) item).getName();
				if (myNode.isMatch(uuid) || (myNode.getLevel() == 0 && run.has("browserName"))) {
					JTextField jtf = ((JTextField) item);
					String type = jtf.getClientProperty("type").toString();
					int index = Integer.parseInt(jtf.getClientProperty("index").toString());
					jtf.setText(run.getJSONArray(type).getString(index));
				}
			} else if (item.toString().contains("JComboBox")) {
				String uuid = ((JComboBox) item).getName();
				if (myNode.isMatch(uuid) || (myNode.getLevel() == 0 && run.has("browserName"))) {
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

	private void saveTree(String filename) {
		Object root = treePanel.getTreeModel().getRoot();

		if (generalPanel.getComponents().length == 0) {
			JOptionPane.showMessageDialog(null, "Nothing to save here", "Error", JOptionPane.ERROR_MESSAGE);
		} else {

			Gson gson = new GsonBuilder()
					.registerTypeAdapter(DefaultMutableTreeNode.class, new DefaultMutableTreeNodeSerializer())
					// .registerTypeAdapter(DefaultMutableTreeNode.class, new
					// DefaultMutableTreeNodeDeserializer())
					.setPrettyPrinting().create();

			String jsonString = gson.toJson(root);
			System.out.println(jsonString);
			JSONHandler.write(new File(FileHandler.getUserDir() + "/temp/tree.json"), jsonString);
			if (filename != null) {
				JSONHandler.write(new File(filename), jsonString);
				JOptionPane.showMessageDialog(null, "Test has been saved successfully", "Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
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
			saveTree(null);
			frame.setVisible(false);

			try {
				Control control = new Control();
				control.start();
			} catch (AssertionError ex) {
				ex.printStackTrace();
			}

			frame.setVisible(true);
		}
	}

	private void addNode(int level, String category, String name, DefaultMutableTreeNode node) {

		String uuid = UUID.randomUUID().toString();

		MyTreeNode myNode = new MyTreeNode(node);
		myNode.setUUID(uuid);

		JLabel lblTop = new JLabel();
		lblTop.setHorizontalAlignment(SwingConstants.CENTER);
		lblTop.setBounds(200, 0, 280, 15);
		lblTop.setFont(lblTop.getFont().deriveFont(Font.BOLD, 14f));
		lblTop.setName(uuid);
//		lblTop.setText(name);

		JSONArray arr = new JSONArray(config);
		for (int x = 0; x < arr.length(); x++) {

			JSONObject obj = arr.getJSONObject(x);
			String objName = obj.getString("name");
			String objCategory = obj.getString("category");
			JSONArray req = obj.getJSONArray("require");
			JSONArray show = obj.getJSONArray("show");
//			JSONArray limit = obj.getJSONArray("limit");

			lblTop.setText(objName);

			if (level == 1) {

				if (objCategory.equalsIgnoreCase("configuration")) {
					int posX = 0;

					List<String> list = new ArrayList<String>();
					for (int y = 0; y < req.length(); y++) {

						String v = req.getString(y);
						String s = show.getString(y);
						list.add(v);

						JLabel lbl = new JLabel();
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBounds(10 + posX, 25, 160, 15);
						lbl.setName(uuid);
						lbl.setText(s);
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
					break;
				}
			}

			else if (level == 2) {
				if (objCategory.equalsIgnoreCase("group")) {
					break;
				}
			}

			else if (level == 3) {
				if (objCategory.equalsIgnoreCase("test")) {
					break;
				}
			}

			else if (level == 4) {

				if (objName.equalsIgnoreCase(name) && objCategory.equalsIgnoreCase(category)) {
					int posY = 20;

					if (req.length() == 0) {
						lblTop.putClientProperty("category", objCategory);
					}

					List<String> list = new ArrayList<String>();
					for (int y = 0; y < req.length(); y++) {

						String v = req.getString(y);
						String s = show.getString(y);
//						String l = limit.getString(y);
						list.add(v);

						JTextField jtf = new JTextField();
						jtf.setBounds(150, 55 + posY, 410, 35);
						jtf.setColumns(10);
						jtf.setName(uuid);
						jtf.putClientProperty("type", v);
						jtf.putClientProperty("category", objCategory);
						jtf.putClientProperty("index", Collections.frequency(list, v) - 1);
						generalPanel.add(jtf);

						JLabel lbl = new JLabel();
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setBounds(260, 25 + posY, 160, 15);
						lbl.setName(uuid);
						lbl.setText(s);
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

						if (v.equalsIgnoreCase("number") || v.equalsIgnoreCase("time"))
							jtf.setDocument(new JTextFieldNumberLimit(5));
						else if (v.equalsIgnoreCase("url")) {
							jtf.getDocument().addDocumentListener(new DocumentListener() {

								@Override
								public void insertUpdate(DocumentEvent e) {
									listen();
								}

								@Override
								public void removeUpdate(DocumentEvent e) {
									listen();
								}

								@Override
								public void changedUpdate(DocumentEvent e) {
									listen();
								}

								public void listen() {
									String value = jtf.getText();
									if((URLHandler.isURLValid(value)) || value.isEmpty()) {
										lbl.setText(s);
										lbl.setForeground(Color.BLACK);
									} else  {
										lbl.setText(s + " Is Not Valid");
										lbl.setForeground(Color.RED);
									}
								}

							});
						}

					}
					break;
				}
			} else {
				break;
			}
		}

		generalPanel.add(lblTop);
		myNode.build();
	}
}
