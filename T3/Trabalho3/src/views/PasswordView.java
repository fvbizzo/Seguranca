package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import controllers.Singleton;
import util.DBManager;

public class PasswordView extends JFrame {
	
	private ArrayList<String> buttonNames = new ArrayList<String>(Arrays.asList("BA","CA","DA","FA","GA","BE","CE","DE","FE","GE","BO","CO","DO","FO","GO"));
	
	private JButton b1 = new JButton("...");
	private JButton b2 = new JButton("...");
	private JButton b3 = new JButton("...");
	private JButton b4 = new JButton("...");
	private JButton b5 = new JButton("...");
	
	private int count = 0;
	
	private ArrayList<String> clicks = new ArrayList<String>();
	
	private ArrayList<String> possiblePasswords = new ArrayList<String>();
	
	
	
	public PasswordView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setSize (250, 300);
		
		int x = (int) (500);
		int y = (int) (500);
		
		getContentPane().add(b1);
		getContentPane().add(b2);
		getContentPane().add(b3);
		getContentPane().add(b4);
		getContentPane().add(b5);
		
		shuffle();
		
		ActionListener action = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton buttonClicked = (JButton)e.getSource();
				clicked(buttonClicked.getText());
				shuffle();
			}
		};
		
		b1.addActionListener(action);
		b2.addActionListener(action);
		b3.addActionListener(action);
		b4.addActionListener(action);
		b5.addActionListener(action);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Password");
	
		
	}
	
	private void shuffle() {
		Collections.shuffle(this.buttonNames);
		b1.setText(buttonNames.get(0) + "-" + buttonNames.get(1) + "-" + buttonNames.get(2));
		b2.setText(buttonNames.get(3) + "-" + buttonNames.get(4) + "-" + buttonNames.get(5));
		b3.setText(buttonNames.get(6) + "-" + buttonNames.get(7) + "-" + buttonNames.get(8));
		b4.setText(buttonNames.get(9) + "-" + buttonNames.get(10) + "-" + buttonNames.get(11));
		b5.setText(buttonNames.get(12) + "-" + buttonNames.get(13) + "-" + buttonNames.get(14));
	}
	
	private void clicked(String value) {
		if (count == 0) {
			possiblePasswords = new ArrayList<String>();
			clicks = new ArrayList<String>();
		}
		count += 1;
		clicks.add(value);
		if (count == 3) {
			generatePossiblePasswords();
			count = 0;
			Singleton singleton = new Singleton().getInstance();
			singleton.setGroup("Administrador");
			singleton.setLoginName("esse@login.name");
			singleton.setName("noome");
			new MenuView();
			this.dispose();
			this.setVisible(false);
			
		}
	}
	
	private void generatePossiblePasswords() {
		String[] first = clicks.get(0).split("-");
		String[] second = clicks.get(1).split("-");
		String[] third = clicks.get(2).split("-");
		
		for (String i: first) {
			for (String j: second) {
				for (String k: third) {
					possiblePasswords.add(i+j+k);
				}
			}
		}
		//Do something with the generated passwords, check if its right...
		System.out.println(possiblePasswords);
		
	}

}
