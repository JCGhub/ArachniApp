package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.ConnectDB;
import htmlparser.DisplayInfo;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class DatabaseWindow extends JFrame{

	private JPanel contentPane;
	private DisplayInfo dI;
	private ConnectDB db = new ConnectDB();

	public DatabaseWindow(DisplayInfo dI){
		this.dI = dI;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//db.MySQLConnection("adminqeF7LPl", "K9arhYdIy9Ir", "lookneat");
		db.MySQLConnection("root", "", "lookneat_db");
		
		JLabel lblParseView = new JLabel("Parse view");
		lblParseView.setHorizontalAlignment(SwingConstants.CENTER);
		lblParseView.setBounds(141, 15, 150, 14);
		contentPane.add(lblParseView);
		
		JButton btnSummary = new JButton("Summary");
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(null, dI.namePortal+" has "+dI.getNumPagesRest()+" pages of restaurants and "+dI.getNumRestaurants()+" restaurants!");
			}
		});
		btnSummary.setBounds(24, 42, 175, 23);
		contentPane.add(btnSummary);
		
		JButton btnShowRestaurants = new JButton("Show restaurants");
		btnShowRestaurants.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dI.displayMapNameURL();
			}
		});
		btnShowRestaurants.setBounds(231, 42, 175, 23);
		contentPane.add(btnShowRestaurants);
		
		JLabel lblDatabaseFunctions = new JLabel("Database functions");
		lblDatabaseFunctions.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatabaseFunctions.setBounds(141, 86, 150, 14);
		contentPane.add(lblDatabaseFunctions);
		
		JButton btnCreateNamesTables = new JButton("Create names table");
		btnCreateNamesTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.createTableNames(dI.table_names);
			}
		});
		btnCreateNamesTables.setBounds(24, 111, 100, 23);
		contentPane.add(btnCreateNamesTables);
		
		JButton btnCreateCommentsTable = new JButton("Create comments table");
		btnCreateCommentsTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.createTableComm(dI.table_comm);
			}
		});
		btnCreateCommentsTable.setBounds(306, 111, 100, 23);
		contentPane.add(btnCreateCommentsTable);
		
		JButton btnInsertNames = new JButton("Insert names");
		btnInsertNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dI.insertNameURL(db);
			}
		});
		btnInsertNames.setBounds(24, 145, 100, 23);
		contentPane.add(btnInsertNames);
		
		JButton btnInsertComments = new JButton("Insert comments");
		btnInsertComments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dI.insertComm(db);
			}
		});
		btnInsertComments.setBounds(306, 145, 100, 23);
		contentPane.add(btnInsertComments);
		
		JButton btnEraseNames = new JButton("Erase names");
		btnEraseNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.deleteTable(dI.table_names);
			}
		});
		btnEraseNames.setBounds(24, 179, 100, 23);
		contentPane.add(btnEraseNames);
		
		JButton btnEraseComments = new JButton("Erase comments");
		btnEraseComments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.deleteTable(dI.table_comm);
			}
		});
		btnEraseComments.setBounds(306, 179, 100, 23);
		contentPane.add(btnEraseComments);
		
		JButton btnCreateInfoTables = new JButton("Create info tables");
		btnCreateInfoTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.createTableInfo(dI.table_info);
			}
		});
		btnCreateInfoTables.setBounds(164, 111, 100, 23);
		contentPane.add(btnCreateInfoTables);
		
		JButton btnInsertInfo = new JButton("Insert info");
		btnInsertInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dI.insertInfo(db);
			}
		});
		btnInsertInfo.setBounds(164, 145, 100, 23);
		contentPane.add(btnInsertInfo);
		
		JButton btnEraseInfo = new JButton("Erase info");
		btnEraseInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.deleteTable(dI.table_info);
			}
		});
		btnEraseInfo.setBounds(164, 179, 100, 23);
		contentPane.add(btnEraseInfo);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.closeConnection();
				System.exit(0);
			}
		});
		btnExit.setBounds(141, 221, 150, 23);
		contentPane.add(btnExit);
	}
}