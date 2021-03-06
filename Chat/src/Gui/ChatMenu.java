package Gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.FriendData;
import client.GetFriends;
import client.LogOut;
import client.ReceiveMessage;
import client.RemoveFriend;
import client.SearchFriend;
import client.SendMessage;
import client.UserData;
import client.WriteToFile;
import mySQLConnection.ConnectorFile;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class ChatMenu extends JFrame implements MouseWheelListener{

	private static JPanel contentPane;
	private static JTextField messageTextField;
	private JTextField searchTextField;
	private static JTextArea messagesTextArea;
	private static JButton btnSend;
	private JButton btnSearch;
	private JComboBox <String> comboBox;
	private JPanel horizontalSeparator;
	private static JPanel backgroundMessages;
	private JPanel lineMessages;
	private JPanel backgroundFriends;
	static JList <String> friendsList;
	private static UserData userdata;
	private SendMessage sendmessage;
	private JLabel lblWarning;
	private static DefaultListModel <String> model;
	private static ArrayList<FriendData> friends = new ArrayList<FriendData>();
	private static FriendData friendtoSendMessage;
	private String friendIp;
	private static TitledBorder titledBorder;
	private static WriteToFile escrever;
	private JScrollPane scroll1;
	private JScrollPane scroll2;
	private static String friendStatus;
	private static MyListCellRenderer highlight;
	private JButton btnRemove;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatMenu frame = new ChatMenu(userdata);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatMenu(UserData userdata) {
		this.userdata = userdata;
		highlight = new MyListCellRenderer();
		createJFrame();
		createTextAreas();
		createSearchButton();
		createComboBox();
		createWarnings();
		createHorizontalSeparator();
		createBackgrounds();
		createTxtFields();
		createSendButton();	
		sendMessage();
		createRemoveBtn();
		MonitorSelectedFriends monitor = new MonitorSelectedFriends();
		monitor.setUserdata(this.userdata);
		monitor.start();
		new ReceiveMessage().start();
	}
	
	private void createTxtFields(){
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("Select a friend");
		searchTextField.setBounds(31, 12, 126, 20);
		contentPane.add(searchTextField);
		searchTextField.setColumns(10);
		
		messageTextField = new JTextField();
		messageTextField.setBounds(10, 298, 375, 20);
		messageTextField.setBorder(null);
		contentPane.add(messageTextField);
		messageTextField.setColumns(10);
	}
	
	private void createJFrame(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				LogOut.logOutRequest(userdata);
			}
		});
		
		setBounds(100, 100, 580, 370);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	private void createFriendsList(){
		model = new DefaultListModel <String>();
		friendsList = new JList <String>(model);
		friendsList.setCellRenderer(highlight);
		
		scroll2 = new JScrollPane (friendsList, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2.setBounds(405, 74, 135, 193);

		contentPane.add(scroll2);	
	}
	
	public static void updateList(){
		ArrayList<FriendData> userFriends = userdata.getFriendsList().getFriendsList();
		model.clear();
		
		if (!friends.isEmpty()){
			friends.clear();
		}

		for (int i=0; i<userFriends.size(); i++){			
			String online = userFriends.get(i).getConectado();	
			model.addElement(userFriends.get(i).getName() + " (" + online + ")");
			friends.add(userFriends.get(i));
		}		
	}

	private void createTextAreas(){		
		messagesTextArea = new JTextArea("");
		messagesTextArea.setForeground(Color.BLACK);
		messagesTextArea.setBackground(Color.WHITE);
		messagesTextArea.setEditable(false);
		scroll1 = new JScrollPane (messagesTextArea, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll1.setBounds(25, 74, 350, 193);
		contentPane.add(scroll1);
		scroll1.addMouseWheelListener(this);
		createFriendsList();
	}
	
	private void createSearchButton(){
		
		btnSearch = new JButton("Search ");
		btnSearch.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		btnSearch.setBorder(new LineBorder(new Color(0, 0, 0))); 
		btnSearch.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		btnSearch.setForeground(Color.BLACK);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				searchAction();

			}
		});
		btnSearch.setBounds(167, 12, 64, 20);
		contentPane.add(btnSearch);
	}
	
	private void searchAction(){
		
		String friend = searchTextField.getText();
		
		if (friend!=""){
			ArrayList<FriendData> possibleFriends = SearchFriend.Search(friend, userdata);

			if (possibleFriends!=null){
				lblWarning.setVisible(false);
				PossibleFriends pFriends = new PossibleFriends(possibleFriends, userdata);
				pFriends.setVisible(true);
			} else{
				lblWarning.setVisible(true);
			}
		} else {
			lblWarning.setVisible(true);
		}
		
	}
	
	private void createComboBox(){
		
		comboBox = new JComboBox <String>();
		comboBox.setModel(new DefaultComboBoxModel <String>(new String[] {userdata.getUserName(), "Settings", "Log Out"}));
		comboBox.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		comboBox.setBorder(new LineBorder(new Color(0, 0, 0))); 
		comboBox.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		comboBox.setForeground(Color.BLACK);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxAction();
			}
		});
		comboBox.setBounds(410, 12, 126, 20);
		contentPane.add(comboBox);	
	}
	
	private void comboBoxAction(){
		
		if (comboBox.getSelectedItem().equals("Settings")){
			SettingsMenu settings = new SettingsMenu(userdata);
			settings.setVisible(true);
		} else if (comboBox.getSelectedItem().equals("Log Out")){
			LogOut.logOutRequest(userdata);
			
			LogInMenu login = new LogInMenu();
			login.setVisible(true);
			
			dispose();
		}
	}
	
	private void createHorizontalSeparator(){
		
		horizontalSeparator = new JPanel();
		horizontalSeparator.setBorder(new EmptyBorder(10,10,10,10));
		horizontalSeparator.setBackground(new Color(8, 83, 148, 70));
		horizontalSeparator.setBounds(15, 43, 510, 3);
		contentPane.add(horizontalSeparator);
		
		lineMessages = new JPanel();
		lineMessages.setBorder(new EmptyBorder(10,10,10,10));
		lineMessages.setBackground(Color.BLACK);
		lineMessages.setBounds(10, 317, 375, 3);
		contentPane.add(lineMessages);
	}
	
	private void createBackgrounds(){
		backgroundMessages = new JPanel();
		backgroundMessages.setForeground(Color.WHITE);
		backgroundMessages.setBorder(new EmptyBorder(10,10,10,10));
		backgroundMessages.setBackground(new Color(8, 83, 148));
		titledBorder = BorderFactory.createTitledBorder(null, "Select a friend", TitledBorder.LEFT, TitledBorder.TOP, new Font("Kristen ITC", Font.BOLD, 9), Color.WHITE);
		backgroundMessages.setBorder(titledBorder);
		backgroundMessages.setBounds(15, 57, 370, 220);
		contentPane.add(backgroundMessages);
		
		backgroundFriends = new JPanel();
		backgroundFriends.setForeground(Color.WHITE);
		backgroundFriends.setBorder(new EmptyBorder(10,10,10,10));
		backgroundFriends.setBorder(BorderFactory.createTitledBorder(null, "Friends List", TitledBorder.LEFT, TitledBorder.TOP, new Font("Kristen ITC", Font.BOLD, 9), Color.WHITE));
		backgroundFriends.setBackground(new Color(8, 83, 148));
		backgroundFriends.setBounds(394, 57, 155, 220);
		contentPane.add(backgroundFriends);
	}
	
	private void createSendButton(){
		
		btnSend = new JButton("Send");
		btnSend.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		btnSend.setBorder(new LineBorder(new Color(0, 0, 0))); 
		btnSend.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		btnSend.setForeground(Color.BLACK);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButtonAction();
			}
		});
		btnSend.setBounds(405, 297, 64, 23);
		contentPane.add(btnSend);		
		
	}
	
	private void createRemoveBtn(){
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RemoveFriend.add(friendtoSendMessage.getEmail(), userdata);
				messagesTextArea.setText("");
			}
		});
		btnRemove.setForeground(Color.BLACK);
		btnRemove.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		btnRemove.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnRemove.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		btnRemove.setBounds(476, 297, 64, 23);
		contentPane.add(btnRemove);
	}
	
	private void sendButtonAction(){
		String message = messageTextField.getText();
		sendmessage.setServerAddress(friendtoSendMessage.getIp());
		sendmessage.setDestinationEmail(friendtoSendMessage.getEmail());
		sendmessage.setFriendData(friendtoSendMessage);
		sendmessage.setMessage(message);
		sendmessage.newMessages(true);
		messageTextField.setText("");
		messagesTextArea.append(userdata.getUserName() + ": " + message + "\n");
		messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength());
	}
	
	private void sendMessage(){
		sendmessage = new SendMessage();
		sendmessage.setUserData(userdata);
		sendmessage.setServerAddress(friendIp); 	
		sendmessage.start();
		sendmessage.newMessages(false);
		
	}
	
	private void createWarnings(){
		lblWarning = new JLabel("User not found.");
		lblWarning.setBounds(241, 15, 80, 14);
		lblWarning.setFont(new Font("Kristen ITC", Font.PLAIN, 9));
		lblWarning.setForeground(new Color (8, 83, 148));
		lblWarning.setVisible(false);
		contentPane.add(lblWarning);
	}
	
	public static void changeTitle(FriendData friend){
		titledBorder.setTitle(friend.getName());
		backgroundMessages.repaint();
	}
	
	public static void changeFriend(FriendData friend) throws IOException{
		friendtoSendMessage =  friend;
		friendStatus = friendtoSendMessage.getConectado();
		
		int index = findFriendIndex(friend.getEmail());
		
		highlight.removeInxex(index);
		
		if (friendStatus.equals("offline")){
			btnSend.setEnabled(false);
			messageTextField.setText("");
			messagesTextArea.setText("");
			
			UpdataMessages updateMessages = new UpdataMessages(friendtoSendMessage.getEmail() + ".txt");
			int result = updateMessages.readFile();
			
			if (result == 1){
				ArrayList<String> messageList = new ArrayList<String>();
				messageList = updateMessages.getMessages();
				
				for (int i=0; i<messageList.size(); i++){
					messagesTextArea.append(messageList.get(i) + '\n');
				}
			}
			
			messagesTextArea.append('\n' + friendtoSendMessage.getName() + " is offline.");
			messageTextField.setEditable(false);
			messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength());
			
		} else if (friendStatus.equals("online")){
			btnSend.setEnabled(true);
			messagesTextArea.setText("");
			messageTextField.setText("");
			messageTextField.setEditable(true);

			UpdataMessages updateMessages = new UpdataMessages(friendtoSendMessage.getEmail() + ".txt");
			int result = updateMessages.readFile();
			
			if (result == 1){
				ArrayList<String> messageList = new ArrayList<String>();
				messageList = updateMessages.getMessages();
				
				for (int i=0; i<messageList.size(); i++){
					messagesTextArea.append(messageList.get(i) + '\n');
					messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength());
				}
			}
		}
	}
	
	public static void showReceivedMessages(String email, String m){
		
		if (friendtoSendMessage!=null){
			messagesTextArea.append(friendtoSendMessage.getName() + ": " + m + "\n");
			messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength());
		} else {			
			int index = findFriendIndex(email);
			
			highlight.addIndex(index);
		}

	}
	
	private static int findFriendIndex(String e){
		
		for (int i=0;i<friends.size();i++){
			
			if (friends.get(i).getEmail().equals(e)){
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	       if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	               scroll1.getVerticalScrollBar().getUnitIncrement(1);
	       } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
	    	   scroll1.getVerticalScrollBar().getBlockIncrement(1);
	       }
	}
}
