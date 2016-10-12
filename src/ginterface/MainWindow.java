package ginterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import database.ConnectDB;
import htmlparser.InfoOrganizator;
import htmlparser.XMLReader;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class MainWindow extends JFrame{

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	JFileChooser fC = new JFileChooser();
	File file;
	ConnectDB db = new ConnectDB();

	public MainWindow(){
		setTitle("ArachniApp · Main menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		db.MySQLConnection("root", "", "arachniappdb");
		
		textField = new JTextField();
		textField.setBounds(27, 11, 200, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSelectFile = new JButton("Select file");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				int selection = fC.showOpenDialog(contentPane);
				
				if(selection == JFileChooser.APPROVE_OPTION){
					file = fC.getSelectedFile();
				    textField.setText(file.getAbsolutePath());
				 
				    try(FileReader fR = new FileReader(file)){
				        String str = "";
				        int val = fR.read();
				        
				        while(val != -1){
				        	str = str + (char)val;
				        	val = fR.read();
				        }
				        
				        textArea.setText(str);
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
			}
		});
		btnSelectFile.setBounds(235, 10, 100, 23);
		contentPane.add(btnSelectFile);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBounds(27, 42, 385, 156);
		
		JScrollPane scroll=new JScrollPane(textArea);
        scroll.setBounds(27, 42, 385, 156);
        contentPane.add(scroll);
        
        JButton btnCreateTables = new JButton("Create tables");
		btnCreateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.createTables();
			}
		});
		btnCreateTables.setBounds(27, 216, 112, 23);
		contentPane.add(btnCreateTables);
		
		JButton btnDeleteTables = new JButton("Delete tables");
		btnDeleteTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.deleteTables();
			}
		});
		btnDeleteTables.setBounds(147, 216, 112, 23);
		contentPane.add(btnDeleteTables);
        
        JButton btnProcFile = new JButton("Process file");
        btnProcFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(file != null){
        			try{
        				String xml = textField.getText();
        				
        				XMLReader xR = new XMLReader(xml, db);
        				
        				if(xR.validateFile()){
        					xR.readFile();
        					
        					InfoOrganizator iO = xR.infoReady();
            				ParserWindow pW = new ParserWindow(xR, iO, db);            				
            				
            				iO.prepareWebPortalParameters();
            				boolean b = iO.prepareConfFileParameters();
            				
            				if(b){
            					JOptionPane.showMessageDialog(null, "Error: The name of the file '"+xR.confFile_array.get(0)+"' already exists! Use another name.");
            				}
            				else{
            					JOptionPane.showMessageDialog(null, "SUCCESS!!");
            					
            					pW.setVisible(true);
                				dispose();
            				}
        				}
        				else{
        					JOptionPane.showMessageDialog(null, "Error: Xml file isn't valid to the schema!");
        				}
					}catch(Exception ex){
						ex.printStackTrace();
					}
        		}
        		else{
        			JOptionPane.showMessageDialog(null, "Error: Didn't load file or wrong file!");
        		}
        	}
        });
        btnProcFile.setBounds(294, 216, 118, 23);
        contentPane.add(btnProcFile);
        
        JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.closeConnection();
				System.exit(0);
			}
		});
		btnExit.setBounds(342, 10, 70, 23);
		contentPane.add(btnExit);
	}
}