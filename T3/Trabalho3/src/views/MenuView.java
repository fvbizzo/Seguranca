package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controllers.Singleton;
import util.DBManager;

public class MenuView  extends JFrame {
	
	Singleton singleton = new Singleton().getInstance();
	
	public MenuView() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (500, 500);
		
		String email = singleton.getLoginName();
		String name = singleton.getName();
		String group = singleton.getGroup();
		int totalAccess = singleton.getTotalAccess();
		
		
		JLabel emailLabel = new JLabel(String.format("Email: %s", email));
		JLabel groupLabel = new JLabel(String.format("Grupo: %s", group));
		JLabel nameLabel = new JLabel(String.format("Nome: %s", name));
		
		JLabel totalAccessLabel = new JLabel(String.format("Total de acessos do usuário: %d", totalAccess));
		
		JLabel mainMenu = new JLabel("Menu principal:");
		JButton registerButton = new JButton("Cadastrar novo usuário");
		JButton alterButton = new JButton("Alterar senha pessoal e certificado digital do usuário");
		JButton consultButton = new JButton("Consultar pasta de arquivos secretos do usuário");
		JButton exitButton = new JButton("Sair do Sistema");
		
		registerButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				register();
			}
		});
		alterButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				alter();
			}
		});
		consultButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				consult();
			}
		});
		exitButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				exit();
			}
		});
		
		getContentPane().add(emailLabel);
		getContentPane().add(groupLabel);
		getContentPane().add(nameLabel);
		
		getContentPane().add(totalAccessLabel);
		
		getContentPane().add(mainMenu);
		if(group.equals("administrador")) {
			getContentPane().add(registerButton);
		}
		getContentPane().add(alterButton);
		getContentPane().add(consultButton);
		getContentPane().add(exitButton);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Menu");
		DBManager.insereRegistro(5001, (String) Singleton.getInstance().getLoginName());				
	}
	
	private void register() {
		System.out.println("Cadastrar novo usuário");
		DBManager.insereRegistro(5002, (String) Singleton.getInstance().getLoginName());
		new RegisterView();
		dispose();
		setVisible(false);
	}
	private void alter() {
		System.out.println("Alterar senha pessoal e certificado digital do usuário");
		DBManager.insereRegistro(5003, (String) Singleton.getInstance().getLoginName());
		new AlterView();
		dispose();
		setVisible(false);
	}
	private void consult() {
		System.out.println("Consultar pasta de arquivos secretos");
		DBManager.insereRegistro(5004, (String) Singleton.getInstance().getLoginName());

	}
	private void exit() {
		System.out.println("Sair do Sistema");
		DBManager.insereRegistro(5005, (String) Singleton.getInstance().getLoginName());
		new ExitView();
		dispose();
		setVisible(false);
	}
	
}
