package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import htmlparser.XMLReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame{

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	JFileChooser fC = new JFileChooser();
	File fil;

	public MainWindow(){
		setTitle("ArachniApp Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(52, 11, 175, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSelectFile = new JButton("Select file");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				int selection = fC.showOpenDialog(contentPane);
				
				if(selection == JFileChooser.APPROVE_OPTION){
				    fil = fC.getSelectedFile();
				    textField.setText(fil.getAbsolutePath());
				 
				    try(FileReader fr = new FileReader(fil)){
				        String str = "";
				        int val = fr.read();
				        
				        while(val != -1){
				        	str = str + (char)val;
				        	val = fr.read();
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
		textArea.setBounds(52, 42, 360, 156);
		
		JScrollPane scroll=new JScrollPane(textArea);
        scroll.setBounds(52, 42, 360, 156);
        contentPane.add(scroll);
        
        JButton btnProcFile = new JButton("Process file");
        btnProcFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(fil != null){
        			try{
        				String xml = textField.getText();
        				
        				XMLReader xR = new XMLReader(xml);
        				
        				if(xR.validateFile()){
        					xR.readFile();
        					
        					if(xR.urlExc == true){
        						JOptionPane.showMessageDialog(null, "Error: There is not an 'url_root' element for an incomplete URL");
            					JOptionPane.showMessageDialog(null, "Error: Xml file isn't valid to the schema!");
        					}
        					else{
        						JOptionPane.showMessageDialog(null, "SUCCESS!!");
            					
            					ParserWindow pW = new ParserWindow(xR);        					
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
				System.exit(0);
			}
		});
		btnExit.setBounds(340, 10, 70, 23);
		contentPane.add(btnExit);
	}
}