package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controllers.Singleton;
import util.Autentic;
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
	
	HashMap user;
	
	String senha;
	
	
	public PasswordView(HashMap user) {
		
		this.user = user;
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setSize (250, 300);
		
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
		DBManager.insereRegistro(3001, (String) user.get("email"));
	
		
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
			tryPass();
			count = 0;			
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
	
	private void tryPass() {
		HashMap updatedUser = Autentic.autenticaEmail((String) user.get("email"));
		Integer acessosNegados = ((Integer) updatedUser.get("numAcessoErrados"));
		if (acessosNegados >= 3) {		
			DBManager.insereRegistro(3007, (String) updatedUser.get("email"));
			JOptionPane.showMessageDialog(null, "Senha incorreta. Número total de erros atingido. Aguarde até 2 minutos para tentar novamente.");
			DBManager.insereRegistro(3002, (String) updatedUser.get("email"));
			LoginView loginView = new LoginView();
			loginView.setVisible(true);
			dispose();
			setVisible(false);
		}
		
		if (Autentic.verificaVetorSenha(updatedUser, possiblePasswords.toArray(new String[possiblePasswords.size()])) ) {
			DBManager.insereRegistro(3003, (String) updatedUser.get("email"));
			DBManager.insereRegistro(3002, (String) updatedUser.get("email"));
			DBManager.zeraAcessoErrado((String)updatedUser.get("email"));
			DBManager.incrementaTotalAcessos((String)updatedUser.get("email"));
			dispose();
			
			Singleton.getInstance().setName((String) user.get("name"));
			Singleton.getInstance().setGroup((String) user.get("groupName"));
			Singleton.getInstance().setLoginName((String) user.get("email"));
			Singleton.getInstance().setTotalAccess((int) user.get("totalAcessos") + 1);
			
			new MenuView();
		}
		else {
			DBManager.incrementaAcessoErrado((String)updatedUser.get("email"));
			updatedUser = Autentic.autenticaEmail((String) updatedUser.get("email"));
			acessosNegados = ((Integer) updatedUser.get("numAcessoErrados"));
			
			if (acessosNegados == 1) {
				DBManager.insereRegistro(3004, (String) updatedUser.get("email"));
				JOptionPane.showMessageDialog(null, "Senha incorreta");
			}
			else if (acessosNegados == 2) {
				DBManager.insereRegistro(3005, (String) updatedUser.get("email"));
				JOptionPane.showMessageDialog(null, "Senha incorreta");
			}
			else if (acessosNegados == 3) {		
				DBManager.insereRegistro(3006, (String) updatedUser.get("email"));
				DBManager.insereRegistro(3007, (String) updatedUser.get("email"));
				JOptionPane.showMessageDialog(null, "Senha incorreta. Número total de erros atingido. Aguarde até 2 minutos para tentar novamente.");
				DBManager.insereRegistro(3002, (String) updatedUser.get("email"));
				LoginView loginView = new LoginView();
				loginView.setVisible(true);
				dispose();
				setVisible(false);
			}
			
		}
	}

}
