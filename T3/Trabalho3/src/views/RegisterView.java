package views;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controllers.Singleton;

public class RegisterView extends JFrame {
	
	Singleton singleton = new Singleton().getInstance();
	
	String[] groups = {"Usuário", "Administrador"};
	
	ArrayList<JButton> teclasSenha =  new ArrayList<JButton>();
	ArrayList<JButton> teclasConfirma =  new ArrayList<JButton>();
	
	String[] fonetics = {"BA","CA","DA","FA","GA","BE","CE","DE","FE","GE","BO","CO","DO","FO","GO"};

	public RegisterView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (1024, 860);
		
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
		
		JLabel senhaLabel = new JLabel("Senha:"); 
		JPasswordField senha = new JPasswordField();
		JPanel senhaButtons = new JPanel(new FlowLayout());
		for (String str: fonetics) {
			JButton fonema = new JButton(str);
			senhaButtons.add(fonema);
			teclasSenha.add(fonema);
			fonema.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonClicked = (JButton)e.getSource();
					senha.setText(senha.getText() + buttonClicked.getText());
					if (senha.getText().length() >= 6) {
						for (JButton but: teclasSenha) {
							but.setEnabled(false);
						}
					}
				}
			});
		}
		JButton clearSenha = new JButton("apagar");
		senhaButtons.add(clearSenha);
		clearSenha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				senha.setText("");
				for (JButton but: teclasSenha) {
					but.setEnabled(true);
				}
			}
		});
		
		JLabel confirmaLabel = new JLabel("Confirmar Senha:"); 
		JPasswordField confirma = new JPasswordField();
		JPanel confirmaButtons = new JPanel(new FlowLayout());
		for (String str: fonetics) {
			JButton fonema = new JButton(str);
			confirmaButtons.add(fonema);
			teclasConfirma.add(fonema);
			fonema.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton buttonClicked = (JButton)e.getSource();
					confirma.setText(confirma.getText() + buttonClicked.getText());
					if (confirma.getText().length() >= 6) {
						for (JButton but: teclasConfirma) {
							but.setEnabled(false);
						}
					}
				}
			});
		}
		JButton clearConfirma = new JButton("apagar");
		confirmaButtons.add(clearConfirma);
		clearConfirma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirma.setText("");
				for (JButton but: teclasConfirma) {
					but.setEnabled(true);
				}
			}
		});
		
		

		
		getContentPane().add(emailLabel);
		getContentPane().add(groupLabel);
		getContentPane().add(nameLabel);
		
		getContentPane().add(totalUsersLabel);
		
		getContentPane().add(mainMenu);
		
		getContentPane().add(certificateFileChooser);
		getContentPane().add(userGroups);
		
		getContentPane().add(senhaLabel);
		getContentPane().add(senha);
		getContentPane().add(senhaButtons);
		
		getContentPane().add(confirmaLabel);
		getContentPane().add(confirma);
		getContentPane().add(confirmaButtons);
		
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
