package ginterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import database.ConnectDB;
import htmlparser.InfoDownloader;
import htmlparser.InfoOrganizator;
import htmlparser.XMLReader;

public class ParserWindow extends JFrame {

	private JPanel contentPane;
	
	public ParserWindow(XMLReader xR, InfoOrganizator iO, ConnectDB db){		
		setTitle("ArachniApp · Parser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInformation = new JLabel("Information");
		lblInformation.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformation.setBounds(106, 11, 88, 14);
		contentPane.add(lblInformation);
		
		JLabel lblParser = new JLabel("Parser");
		lblParser.setHorizontalAlignment(SwingConstants.CENTER);
		lblParser.setBounds(106, 66, 88, 14);
		contentPane.add(lblParser);
		
		JButton btnSummary = new JButton("Summary");
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*xR.showConfFile_array();
				xR.showMainEntity_array();
				xR.showNextPage_array();
				xR.showAttributes_array();*/
				xR.showXmlFileContent();
			}
		});
		btnSummary.setBounds(37, 30, 99, 23);
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
		btnWebContent.setBounds(164, 30, 99, 23);
		contentPane.add(btnWebContent);
		
		JButton btnParseWeb = new JButton("Parse web");
		btnParseWeb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String rerun = iO.confFile_array.get(3);
				String time_string = iO.confFile_array.get(4);
				int timeMinutes = Integer.parseInt(time_string);
				int timeSeconds = timeMinutes*60;
				
				while(rerun.contains("update") || rerun.contains("accumulate")){
					iO.mainExecution();
					
					try{
						System.out.println("Next download in "+timeMinutes+" minutes...");
						Thread.sleep(timeSeconds*1000);
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
				
				if(rerun.contains("no")){
					iO.mainExecution();
				}				
				
				//iS.showArrayData();
			}
		});
		btnParseWeb.setBounds(96, 87, 110, 23);
		contentPane.add(btnParseWeb);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.closeConnection();
				System.exit(0);
			}
		});
		btnExit.setBounds(75, 127, 150, 23);
		contentPane.add(btnExit);
	}
}