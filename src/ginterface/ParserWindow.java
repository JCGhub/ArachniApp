package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import database.ConnectDB;
import htmlparser.InfoDownloader;
import htmlparser.InfoOrganizator;
import htmlparser.XMLReader;

public class ParserWindow extends JFrame {

	private JPanel contentPane;
	private XMLReader xR;
	ConnectDB db;
	
	public ParserWindow(XMLReader xR, ConnectDB db){
		this.xR = xR;
		this.db = db;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 234);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInformation = new JLabel("Information");
		lblInformation.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformation.setBounds(106, 8, 88, 14);
		contentPane.add(lblInformation);
		
		JLabel lblParser = new JLabel("Parser");
		lblParser.setHorizontalAlignment(SwingConstants.CENTER);
		lblParser.setBounds(106, 99, 88, 14);
		contentPane.add(lblParser);
		
		JButton btnSummary = new JButton("Summary");
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				xR.showArrayConfAtt();
				xR.showArrayMainEnt();
				xR.showArrayPredEnt();
				xR.showArrayAtt();
			}
		});
		btnSummary.setBounds(37, 29, 99, 23);
		contentPane.add(btnSummary);
		
		JButton btnWebContent = new JButton("Web Content");
		btnWebContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InfoDownloader iD = new InfoDownloader();
				
				try {
					iD.downloadContent(xR.getUrl());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnWebContent.setBounds(164, 29, 99, 23);
		contentPane.add(btnWebContent);
		
		JButton btnCreateTables = new JButton("Create tables");
		btnCreateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.createTables();
			}
		});
		btnCreateTables.setBounds(37, 61, 98, 23);
		contentPane.add(btnCreateTables);
		
		JButton btnDeleteTables = new JButton("Delete tables");
		btnDeleteTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.deleteTables();
			}
		});
		btnDeleteTables.setBounds(164, 61, 98, 23);
		contentPane.add(btnDeleteTables);
		
		JButton btnParseWeb = new JButton("Parse web");
		btnParseWeb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				InfoOrganizator iO = xR.infoReady();
				
				iO.mainExec();
				
				//iS.showArrayData();
			}
		});
		btnParseWeb.setBounds(96, 121, 110, 23);
		contentPane.add(btnParseWeb);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.closeConnection();
				System.exit(0);
			}
		});
		btnExit.setBounds(75, 162, 150, 23);
		contentPane.add(btnExit);
	}
}