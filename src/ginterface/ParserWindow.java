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

import htmlparser.InfoStorer;

public class ParserWindow extends JFrame {

	private JPanel contentPane;
	private InfoStorer iS;
	
	public ParserWindow(InfoStorer iS){
		this.iS = iS;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 234);
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
		lblParser.setBounds(106, 85, 88, 14);
		contentPane.add(lblParser);
		
		JButton btnSummary = new JButton("Summary");
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnSummary.setBounds(37, 36, 99, 23);
		contentPane.add(btnSummary);
		
		JButton btnShowData = new JButton("Show data");
		btnShowData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnShowData.setBounds(164, 36, 99, 23);
		contentPane.add(btnShowData);
		
		JButton btnParseWeb = new JButton("Parse web");
		btnParseWeb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xPathMainEnt = iS.getXPathMainEnt();
				
				System.out.println("\n"+xPathMainEnt);
				
				iS.downloadArray(xPathMainEnt);
				
				iS.showArray();
			}
		});
		btnParseWeb.setBounds(96, 110, 110, 23);
		contentPane.add(btnParseWeb);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(75, 162, 150, 23);
		contentPane.add(btnExit);
	}
}