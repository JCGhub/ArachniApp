package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import htmlparser.DisplayInfo;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame{

	private JPanel contentPane;
	public static JComboBox<String> cBPortal;
	private DisplayInfo dI;

	public MainWindow(){
		setTitle("Look'n'Eat Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 242);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPortal = new JLabel("Web portal:");
		lblPortal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPortal.setBounds(30, 33, 89, 14);
		contentPane.add(lblPortal);
		
		cBPortal = new JComboBox<String>();
		cBPortal.setModel(new DefaultComboBoxModel<String>(new String[] {"TripAdvisor", "Yelp"}));
		cBPortal.setSelectedIndex(0);
		cBPortal.setBounds(125, 30, 254, 20);
		contentPane.add(cBPortal);
		
		JButton btnChangeData = new JButton("Change Data");
		btnChangeData.setBounds(110, 80, 202, 23);
		contentPane.add(btnChangeData);
		
		JButton btnParse = new JButton("Parse web portal");
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				long time_start, time_end;
				time_start = System.currentTimeMillis();
				
				// Tarea que se va a cronometrar
				
				String opt = cBPortal.getSelectedItem().toString();
				final int n;
				
				switch(opt){
				case "TripAdvisor":
					n = 1;
					break;
				case "11870":
					n = 2;
					break;
				case "Yelp":
					n = 3;
					break;
				default:
					n = 4;
					break;
				}
				
				dI = new DisplayInfo(n);
				dI.initializePortalParameters();				
				dI.restPagination();
				
				final DatabaseWindow dW = new DatabaseWindow(dI);
				
				// Tarea finalizada
				
				time_end = System.currentTimeMillis();
				
				JOptionPane.showMessageDialog(null, "You've parsed "+cBPortal.getSelectedItem().toString()+" successfully in "+(time_end-time_start)+" milliseconds!");
				
				dW.setVisible(true);
				dispose();
			}
		});
		btnParse.setBounds(110, 114, 202, 23);
		contentPane.add(btnParse);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(175, 158, 70, 23);
		contentPane.add(btnExit);
	}
}