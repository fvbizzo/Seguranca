package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controllers.Singleton;
import util.DBManager;

public class ExitView extends JFrame {
	

	Singleton singleton = new Singleton().getInstance();

	public ExitView(){
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
		
		JLabel exitMenu = new JLabel("Pressione o botão Sair para confirmar:");
		JButton exitButton = new JButton("Sair");
		JButton backButton = new JButton("Voltar de Sair para o Menu Principal");
		
		exitButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				exit();
			}
		});
		backButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				back();
			}
		});
		DBManager.insereRegistro(9001, (String) Singleton.getInstance().getLoginName());
		getContentPane().add(emailLabel);
		getContentPane().add(groupLabel);
		getContentPane().add(nameLabel);
		
		getContentPane().add(totalAccessLabel);
		
		getContentPane().add(exitButton);
		getContentPane().add(backButton);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Menu");
	}
	
	public void exit() {
		DBManager.insereRegistro(9003, (String) Singleton.getInstance().getLoginName());
		DBManager.insereRegistro(1002);
		dispose();
		setVisible(false);
	}
	public void back() {
		DBManager.insereRegistro(9004, (String) Singleton.getInstance().getLoginName());
		new MenuView();
		exit();
	}
}
