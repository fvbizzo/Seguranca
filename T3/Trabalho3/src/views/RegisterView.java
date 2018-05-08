package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controllers.Singleton;

public class RegisterView extends JFrame {
	
	Singleton singleton = new Singleton().getInstance();
	
	String[] groups = {"usuario", "admin"};

	public RegisterView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (500, 500);
		
		String email = singleton.getLoginName();
		String name = singleton.getName();
		String group = singleton.getGroup();
		int totalUsers = singleton.getTotalUsers();
		
		
		JLabel emailLabel = new JLabel(String.format("Email: %s", email));
		JLabel groupLabel = new JLabel(String.format("Grupo: %s", group));
		JLabel nameLabel = new JLabel(String.format("Nome: %s", name));
		
		JLabel totalUsersLabel = new JLabel(String.format("Total de usuários do sistema: %d", totalUsers));
		
		JLabel mainMenu = new JLabel("Formulario de cadastro:");
		
		JFileChooser certificateFileChooser = new JFileChooser();
		certificateFileChooser.setControlButtonsAreShown(false);
		
		JComboBox userGroups = new JComboBox(groups);
		
		
		JButton registerButton = new JButton("Cadastrar!");		
		JButton backButton = new JButton("Voltar para o Menu Principal");
		registerButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				String path = ".";
				try  {
					path = certificateFileChooser.getSelectedFile().getPath();
				} catch (Exception exep ) {
					System.out.println("no file chosen");
					return;
				}
					
				String selectedGroup = userGroups.getSelectedItem().toString();
				register(path, selectedGroup);
			}
		});
		backButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				back();
			}
		});

		
		getContentPane().add(emailLabel);
		getContentPane().add(groupLabel);
		getContentPane().add(nameLabel);
		
		getContentPane().add(totalUsersLabel);
		
		getContentPane().add(mainMenu);
		
		getContentPane().add(certificateFileChooser);
		getContentPane().add(userGroups);
		
		getContentPane().add(registerButton);
		getContentPane().add(backButton);
		
	
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Register");
	}
	
	public void register(String path, String group) {
		System.out.println(path +" "+ group);
	}
	
	public void back() {
		new MenuView();
		dispose();
		setVisible(false);
	}
}
