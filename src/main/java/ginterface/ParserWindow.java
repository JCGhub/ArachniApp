package ginterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import database.ConnectDB;
import htmlparser.InfoOrganizator;

public class ParserWindow extends JFrame implements Runnable{

	private JPanel contentPane;
	Thread thread;
	JButton btnParseWeb;
	private static final int TIME_VISIBLE = 10000;

	public ParserWindow(){

		setTitle("ArachniApp · Parser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblParser = new JLabel("Extract the HTML information");
		lblParser.setHorizontalAlignment(SwingConstants.CENTER);
		lblParser.setBounds(10, 16, 279, 14);
		contentPane.add(lblParser);

		btnParseWeb = new JButton("Download");
		btnParseWeb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				startThread();
				btnParseWeb.setEnabled(false);

				/*String rerun = InfoOrganizator.iO.confFile_array.get(3);
				String time_string = InfoOrganizator.iO.confFile_array.get(4);
				int timeMinutes = Integer.parseInt(time_string);
				int timeSeconds = timeMinutes*60;

				while(rerun.contains("update") || rerun.contains("accumulate")){
					InfoOrganizator.iO.mainExecution();

					try{
						System.out.println("Next download in "+timeMinutes+" minutes...");
						Thread.sleep(timeSeconds*1000);
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}

				if(rerun.contains("no")){
					InfoOrganizator.iO.mainExecution();
				}*/
			}
		});
		btnParseWeb.setBounds(96, 38, 110, 23);
		contentPane.add(btnParseWeb);

		JButton btnExit = new JButton("Exit / Abort");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectDB.db.closeConnection();
				System.exit(0);
			}
		});
		btnExit.setBounds(75, 82, 150, 23);
		contentPane.add(btnExit);
	}

	public void startThread(){
		thread = new Thread(this);
		thread.start();
	}

	public void run(){
		String rerun = InfoOrganizator.iO.confFile_array.get(3);
		String time_string = InfoOrganizator.iO.confFile_array.get(4);
		final int timeMinutes = Integer.parseInt(time_string);
		int timeSeconds = timeMinutes*60;

		while(rerun.contains("update") || rerun.contains("accumulate")){
			InfoOrganizator.iO.mainExecution();

			Thread t = new Thread(new Runnable(){
				public void run(){
					//JOptionPane.showMessageDialog(null, "Success: "+InfoOrganizator.iO.countNodes()+" nodes have been stored in database!!\nNext download in "+timeMinutes+"minutes.");
					JOptionPane pane = new JOptionPane(InfoOrganizator.iO.countNodes()+" nodes have been stored in database!!\nNext download in "+timeMinutes+" minute/s.", JOptionPane.INFORMATION_MESSAGE);
					final JDialog dialog = pane.createDialog(null, "SUCCESS");
					dialog.setModal(false);
					dialog.setVisible(true);

					new Timer(TIME_VISIBLE, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
						}
					}).start();
				}
			});
			t.start();

			try{
				System.out.println("Next download in "+timeMinutes+" minutes...");
				Thread.sleep(timeSeconds*1000);
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}

		if(rerun.contains("no")){
			InfoOrganizator.iO.mainExecution();
			
			Thread t = new Thread(new Runnable(){
				public void run(){
					//JOptionPane.showMessageDialog(null, "Success: "+InfoOrganizator.iO.countNodes()+" nodes have been stored in database!!\nNext download in "+timeMinutes+"minutes.");
					JOptionPane pane = new JOptionPane(InfoOrganizator.iO.countNodes()+" nodes have been stored in database!!", JOptionPane.INFORMATION_MESSAGE);
					final JDialog dialog = pane.createDialog(null, "SUCCESS");
					dialog.setModal(false);
					dialog.setVisible(true);

					new Timer(TIME_VISIBLE, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
						}
					}).start();
				}
			});
			t.start();
		}

		btnParseWeb.setEnabled(true);
	}
}