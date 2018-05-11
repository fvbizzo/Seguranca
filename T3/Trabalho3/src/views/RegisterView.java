package views;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controllers.Singleton;
import util.Autentic;
import util.DBManager;

public class RegisterView extends JFrame {
	
	Singleton singleton = new Singleton().getInstance();
	
	String[] groups = {"Usuário", "Administrador"};
	
	ArrayList<JButton> teclasSenha =  new ArrayList<JButton>();
	ArrayList<JButton> teclasConfirma =  new ArrayList<JButton>();
	
	String[] fonetics = {"BA","CA","DA","FA","GA","BE","CE","DE","FE","GE","BO","CO","DO","FO","GO"};

	public RegisterView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (1000, 650);
		DBManager.insereRegistro(6001, (String) Singleton.getInstance().getLoginName());
		String email = singleton.getLoginName();
		String name = singleton.getName();
		String group = singleton.getGroup();
		int totalUsers = DBManager.retornaNumUsuarios();
		
		
		JLabel emailLabel = new JLabel(String.format("Email: %s", email));
		JLabel groupLabel = new JLabel(String.format("Grupo: %s", group));
		JLabel nameLabel = new JLabel(String.format("Nome: %s", name));
		
		JLabel totalUsersLabel = new JLabel(String.format("Total de usuários do sistema: %d", totalUsers));
		
		JLabel mainMenu = new JLabel("Formulario de cadastro:");
		
		JFileChooser certificateFileChooser = new JFileChooser();
		certificateFileChooser.setPreferredSize(new Dimension (300,100));

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
		
		JPanel senhaPanel = new JPanel(new GridLayout(1, 2, 0, 0));
		JLabel senhaLabel = new JLabel("Senha:"); 
		JPasswordField senha = new JPasswordField();
		senha.setPreferredSize(new Dimension(200, 30));
		senha.setMaximumSize(new Dimension(200, 30));
		senhaPanel.add(senhaLabel);
		senhaPanel.add(senha);
		JPanel senhaButtons = new JPanel(new GridLayout(1, 16, 0, 0));
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
		
		JPanel confirmaPanel = new JPanel(new GridLayout(1, 2, 0, 0));
		JLabel confirmaLabel = new JLabel("Confirmar Senha:"); 
		JPasswordField confirma = new JPasswordField();
		confirma.setPreferredSize(new Dimension(200,30));
		confirma.setMaximumSize(new Dimension(200,30));
		confirmaPanel.add(confirmaLabel);
		confirmaPanel.add(confirma);
		JPanel confirmaButtons = new JPanel(new GridLayout(1, 16, 0, 0));
		for (String str: fonetics) {
			JButton fonema = new JButton(str);
			fonema.setSize(new Dimension(15,15));
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
		
		getContentPane().add(senhaPanel);
		getContentPane().add(senhaButtons);
		
		getContentPane().add(confirmaPanel);
		getContentPane().add(confirmaButtons);
		
		getContentPane().add(registerButton);
		getContentPane().add(backButton);
		
		registerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DBManager.insereRegistro(6002, (String) Singleton.getInstance().getLoginName());
				
				String resultSenha = new String( senha.getPassword() );
				String resultConfirmacao = new String(confirma.getPassword());
				String grupo = (String) userGroups.getSelectedItem().toString().toLowerCase();
				
				Path cdPath;
				try {
					cdPath = Paths.get(certificateFileChooser.getSelectedFile().getPath());
				} catch (Exception ey) {
					JOptionPane.showMessageDialog(null, "Certificado digital não selecionado.");
					return;
				}
				byte[] certDigBytes = null;
				try {
					certDigBytes = Files.readAllBytes(cdPath);
				} catch (Exception a) {
					a.printStackTrace();
					DBManager.insereRegistro(6004, (String) Singleton.getInstance().getLoginName());
					return;
				}
				
				X509Certificate cert = Autentic.leCertificadoDigital(certDigBytes);
				if (cert == null) {
					DBManager.insereRegistro(6004, Singleton.getInstance().getLoginName());
					JOptionPane.showMessageDialog(null, "Certificado digital inválido.");
					return;
				}
				Principal subjectDN = cert.getSubjectDN();
								int start = subjectDN.getName().indexOf("=");
				int end = subjectDN.getName().indexOf(",");
				String newUserEmail = subjectDN.getName().substring(start + 1, end);
				
				String infoString = "Versão: "+ cert.getVersion() +"\n"+ "Validade: " +cert.getNotBefore() +"\n"+ "Tipo: "+cert.getType() +"\n"+ "Assinatura Emissor: "+cert.getIssuerDN() +"\n"+ "Sujeito: "+cert.getSubjectDN()+"\n"+ "Email: "+newUserEmail;
				int ret = JOptionPane.showConfirmDialog(null, infoString);
				
				if (ret != JOptionPane.YES_OPTION) {
					System.out.println("Cancelou");
					DBManager.insereRegistro(6007, Singleton.getInstance().getLoginName());
					return;
				}
				else {
					DBManager.insereRegistro(6006, Singleton.getInstance().getLoginName());
				}
			
				
				if (resultSenha.equals(resultConfirmacao)) {
					//TODO: PORBLEMA AQUI NAO TA CADASTRANDO
					if (Autentic.cadastraUsuario(grupo, resultSenha, cdPath.toString())) {
						JOptionPane.showMessageDialog(null, "Usuário cadastrado!");
						dispose();
						setVisible(false);
						new RegisterView();
					}
					else {
						DBManager.insereRegistro(6003, Singleton.getInstance().getLoginName());
						JOptionPane.showMessageDialog(null, "Não foi possível cadastrar novo usuário.");
					}
				}
				else {
					DBManager.insereRegistro(6003, (String) "jjj"/*user.get("email")*/);
					JOptionPane.showMessageDialog(null, "Senha e confirmação de senha não são iguais.");
				}
				
			}
		});
		
	
		
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
