package Gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.UserData;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import java.awt.Color;

public class ChatMenu extends JFrame {

	private JPanel contentPane;
	private JTextField messageTextField;
	private JTextField searchTextField;
	private JTextArea messagesTextArea;
	private JButton btnSend;
	private JButton btnSearch;
	private JComboBox comboBox;
	private JPanel horizontalSeparator;
	private JPanel backgroundMessages;
	private JPanel lineMessages;
	private JPanel backgroundFriends;
	private JList friendsList;
	private UserData userdata;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatMenu frame = new ChatMenu();
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
	public ChatMenu() {
		createJFrame();
		createTextAreas();
		createSearchButton();
		createComboBox();
		createHorizontalSeparator();
		createBackgrounds();
		createTxtFields();
		createSendButton();	
	}
	
	private void createTxtFields(){
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("");
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
		setBounds(100, 100, 560, 370);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	private void createTextAreas(){
		
		friendsList = new JList();
		friendsList.setBounds(405, 74, 120, 193);
		contentPane.add(friendsList);	
		
		messagesTextArea = new JTextArea();
		messagesTextArea.setBounds(25, 74, 350, 193);
		messagesTextArea.setEditable(false);
		contentPane.add(messagesTextArea);
	}
	
	private void createSearchButton(){
		
		btnSearch = new JButton("Search ");
		btnSearch.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		btnSearch.setBorder(new LineBorder(new Color(0, 0, 0))); 
		btnSearch.setBackground(new Color (8, 83, 148));
		btnSearch.setForeground(Color.WHITE);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//VERIFICAR SE O NOME EXISTE NA BASE DE DADOS E MOSTRAR
			}
		});
		btnSearch.setBounds(167, 12, 64, 20);
		contentPane.add(btnSearch);
	}
	
	private void createComboBox(){
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"", "Settings", "Log Out"}));
		comboBox.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		comboBox.setBorder(new LineBorder(new Color(0, 0, 0))); 
		comboBox.setBackground(new Color (8, 83, 148));
		comboBox.setForeground(Color.WHITE);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getSelectedItem().equals("Settings")){
					SettingsMenu settings = new SettingsMenu();
					settings.setVisible(true);
				} else if (comboBox.getSelectedItem().equals("Log Out")){
					//LOG OUT
				}
			}
		});
		comboBox.setBounds(384, 12, 126, 20);
		contentPane.add(comboBox);	
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
		backgroundMessages.setBorder(BorderFactory.createTitledBorder(null, "Friend Name (Online)", TitledBorder.LEFT, TitledBorder.TOP, new Font("Kristen ITC", Font.BOLD, 9), Color.WHITE));
		backgroundMessages.setBounds(15, 57, 370, 220);
		contentPane.add(backgroundMessages);
		
		backgroundFriends = new JPanel();
		backgroundFriends.setForeground(Color.WHITE);
		backgroundFriends.setBorder(new EmptyBorder(10,10,10,10));
		backgroundFriends.setBorder(BorderFactory.createTitledBorder(null, "Online Friends", TitledBorder.LEFT, TitledBorder.TOP, new Font("Kristen ITC", Font.BOLD, 9), Color.WHITE));
		backgroundFriends.setBackground(new Color(8, 83, 148));
		backgroundFriends.setBounds(394, 57, 140, 220);
		contentPane.add(backgroundFriends);
	}
	
	private void createSendButton(){
		
		btnSend = new JButton("Send");
		btnSend.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		btnSend.setBorder(new LineBorder(new Color(0, 0, 0))); 
		btnSend.setBackground(new Color (8, 83, 148));
		btnSend.setForeground(Color.WHITE);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ENVIAR MENSAGEM
			}
		});
		btnSend.setBounds(405, 297, 64, 23);
		contentPane.add(btnSend);
	}
}