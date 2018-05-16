package views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import controllers.Singleton;
import util.Autentic;
import util.DBManager;

public class AlterView extends JFrame {
	private final int width = 450;
	private final int height = 630;
		
	Singleton singleton = new Singleton().getInstance();
	
	ArrayList<JButton> teclasSenha =  new ArrayList<JButton>();
	ArrayList<JButton> teclasConfirma =  new ArrayList<JButton>();
	
	String[] fonetics = {"BA","CA","DA","FA","GA","BE","CE","DE","FE","GE","BO","CO","DO","FO","GO"};
	
	public AlterView () {
		String email = singleton.getLoginName();
		String name = singleton.getName();
		String group = singleton.getGroup();
		int totalAccess = singleton.getTotalAccess();
		
		HashMap user = Autentic.autenticaEmail(singleton.getLoginName());
		DBManager.insereRegistro(7001, (String) user.get("email"));
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (500, 500);
		
		JLabel emailLabel = new JLabel(String.format("Email: %s", email));
		JLabel groupLabel = new JLabel(String.format("Grupo: %s", group));
		JLabel nameLabel = new JLabel(String.format("Nome: %s", name));
		JLabel totalAccessLabel = new JLabel(String.format("Total de acessos do usuário: %d", totalAccess));
		
		JLabel mainMenu = new JLabel("Caminho do certificado digital:");
		JFileChooser certificateFileChooser = new JFileChooser();
		certificateFileChooser.setPreferredSize(new Dimension (300,100));

		certificateFileChooser.setControlButtonsAreShown(false);
		
		JButton registerButton = new JButton("Fazer alterações!");		
		JButton backButton = new JButton("Voltar para o Menu Principal");
		
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
		
		registerButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				String path = ".";
				try  {
					path = certificateFileChooser.getSelectedFile().getPath();
				} catch (Exception exep ) {
					System.out.println("no file chosen");
					path = ".";
				}
					
				DBManager.insereRegistro(7007, (String) user.get("email"));
				
				String errorMsg = "";
				String senhaResult = new String(senha.getPassword());
				if (senhaResult.isEmpty() == false) {
					String confirmacao = new String(confirma.getPassword());
					System.out.println(senhaResult);
					System.out.println(confirmacao);
					if (senhaResult.equals(confirmacao)) {
						if (Autentic.verificaRegrasSenha(senhaResult) == false) {
							errorMsg = "Senha não está de acordo com a regra.\n";
							DBManager.insereRegistro(7002, (String) user.get("email"));
							JOptionPane.showMessageDialog(null, errorMsg);

						} 
						else {
							senhaResult = Autentic.geraSenhaProcessada(senhaResult, (String) user.get("salt"));
							DBManager.alterarSenha(senhaResult, (String) user.get("email")) ;
							JOptionPane.showMessageDialog(null, "Senha alterada com sucesso");
						}
					} else {
						errorMsg = "Senha e confirmação de senha não são iguais.\n";
						DBManager.insereRegistro(7002, (String) user.get("email"));
						JOptionPane.showMessageDialog(null, errorMsg);
					}
				}
				
				String pathCertificado = path;
				if (pathCertificado.isEmpty() == false && !pathCertificado.equals(".")) {
					Path cdPath = Paths.get(pathCertificado);
					byte[] certDigBytes = null;
					try {
						certDigBytes = Files.readAllBytes(cdPath);
					} catch (Exception a) {
						a.printStackTrace();
						DBManager.insereRegistro(7003, (String) user.get("email"));
						JOptionPane.showMessageDialog(null, "Caminho do certificado digital inválido");

					}
					
					X509Certificate cert = Autentic.leCertificadoDigital(certDigBytes);
					if (cert ==  null) {
						DBManager.insereRegistro(7003, (String) user.get("email"));
						errorMsg = "Não foi possível ler o certificado, formato invalido";
						JOptionPane.showMessageDialog(null, errorMsg);

					} else {
					
						Principal subjectDN = cert.getSubjectDN();
						int start = subjectDN.getName().indexOf("=");
						int end = subjectDN.getName().indexOf(",");
						String newUserEmail = subjectDN.getName().substring(start + 1, end);
						
						String infoString = "Versão: "+ cert.getVersion() +"\n"+ "Validade: " +cert.getNotBefore() +"\n"+ "Tipo: "+cert.getType() +"\n"+ "Assinatura Emissor: "+cert.getIssuerDN() +"\n"+ "Sujeito: "+cert.getSubjectDN()+"\n"+ "Email: "+newUserEmail;
						int ret = JOptionPane.showConfirmDialog(null, infoString);
						
						if (ret != JOptionPane.YES_OPTION) {
							System.out.println("Cancelou");
							errorMsg = "Confirmação de dados rejeitada";
							DBManager.insereRegistro(7005, (String) user.get("email"));
							JOptionPane.showMessageDialog(null, errorMsg);
						} else {
							DBManager.insereRegistro(7004, (String) user.get("email"));
							String certString = Autentic.certToString(cert);
							if(DBManager.alterarCertificadoDigital(certString, (String) user.get("email"), newUserEmail)) {
								JOptionPane.showMessageDialog(null, "Certificado alterado com sucesso");
								HashMap usuario = Autentic.autenticaEmail(newUserEmail);
								Singleton.getInstance().setName((String) usuario.get("name"));
								Singleton.getInstance().setGroup((String) usuario.get("groupName"));
								Singleton.getInstance().setLoginName((String) usuario.get("email"));
								Singleton.getInstance().setTotalAccess((int) usuario.get("totalAcessos"));

							} else {
								JOptionPane.showMessageDialog(null, "Já existe um usuário cadastrado com esse email");
							}
						}
					}
				}
				
				if (errorMsg.isEmpty() == true) {
					dispose();
					new MenuView();
				}
			}
		});
		
		getContentPane().add(emailLabel);
		getContentPane().add(groupLabel);
		getContentPane().add(nameLabel);
		
		getContentPane().add(totalAccessLabel);
		
		getContentPane().add(mainMenu);
		
		getContentPane().add(certificateFileChooser);
		
		getContentPane().add(senhaPanel);
		getContentPane().add(senhaButtons);
		
		getContentPane().add(confirmaPanel);
		getContentPane().add(confirmaButtons);
		
		getContentPane().add(registerButton);
		getContentPane().add(backButton);
		
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Alterar");
		
		DBManager.insereRegistro(7001, (String) Singleton.getInstance().getLoginName());

		
	}	
	
	public void back() {
		DBManager.insereRegistro(7006, (String) Singleton.getInstance().getLoginName());
		new MenuView();
		dispose();
		setVisible(false);
	}
}
