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
		
		JButton registerButton = new JButton("Cadastrar!");		
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
		
//		Container c = getContentPane();
//		
//		c.add(new Header((String)user.get("email"), (String)user.get("groupName"), (String)user.get("name")));
//		List<HashMap> tanList = DBManager.retornaTanList((String)user.get("email"));
//		c.add(new FirstBody("Total de OTPS", tanList.size()));
//		
//		
//		JLabel certificadoDigitalLabel = new JLabel();
//		certificadoDigitalLabel .setBounds(30, 130, 300, 30);
//		c.add(certificadoDigitalLabel);
//		JButton certificadoDigitalButton = new JButton("Escolha o arquivo do Certificado Digital");
//		certificadoDigitalButton .setBounds(30, 170, 300, 30);
//		c.add(certificadoDigitalButton);
//		certificadoDigitalButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser certificadoDigitalchooser = new JFileChooser(); 
//				certificadoDigitalchooser.setCurrentDirectory(new java.io.File("."));
//				certificadoDigitalchooser.setDialogTitle("Caminho do Certificado Digital");
//				certificadoDigitalchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//				
//				if (certificadoDigitalchooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//					certificadoDigitalLabel.setText(certificadoDigitalchooser.getSelectedFile().getAbsolutePath());
//				}
//			    else {
//			      System.out.println("No Selection ");
//			    }
//			}
//		});
//		
//		JLabel tanListLabel = new JLabel();
//		tanListLabel .setBounds(30, 210, 300, 30);
//		c.add(tanListLabel);
//		JButton tanListButton = new JButton("Escolha uma pasta para a TAN List");
//		c.add(tanListButton);
//		tanListButton .setBounds(30, 250, 300, 30);
//		tanListButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser tanListchooser = new JFileChooser(); 
//				tanListchooser.setCurrentDirectory(new java.io.File("."));
//				tanListchooser.setDialogTitle("Caminho da TAN List");
//				tanListchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				
//				if (tanListchooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//					tanListLabel.setText(tanListchooser.getSelectedFile().getAbsolutePath());
//				}
//			    else {
//			      System.out.println("No Selection ");
//			    }
//			}
//		});
//		
//		JLabel senhaLabel = new JLabel("Senha:");
//		senhaLabel.setBounds(30, 290, 300, 40);
//		c.add(senhaLabel);
//		JPasswordField senhaField = new JPasswordField(); 
//		senhaField.setBounds(30, 330, 300, 40);
//		c.add(senhaField);
//		
//		JLabel senhaConfirmacaoLabel = new JLabel("Confirme a senha:");
//		senhaConfirmacaoLabel.setBounds(30, 370, 300, 40);
//		c.add(senhaConfirmacaoLabel);
//		JPasswordField senhaConfirmacaoField = new JPasswordField(); 
//		senhaConfirmacaoField.setBounds(30, 410, 300, 40);
//		c.add(senhaConfirmacaoField);
//		
//		JButton alterarButton = new JButton("Alterar e voltar");
//		alterarButton.setBounds(30, 450, 300, 40);
//		c.add(alterarButton);
//		alterarButton.addActionListener(new ActionListener () {
//			public void actionPerformed (ActionEvent e) {
//				DBManager.insereRegistro(7007, (String) user.get("email"));
//				
//				String errorMsg = "";
//				String senha = new String( senhaField.getPassword());
//				if (senha.isEmpty() == false) {
//					String confirmacao = new String(senhaConfirmacaoField.getPassword());
//					if (senha.equals(confirmacao)) {
//						if (Auth.verificaRegrasSenha(senha) == false) {
//							errorMsg += "Senha não está de acordo com a regra.\n";
//							DBManager.insereRegistro(7002, (String) user.get("email"));
//						} 
//						else {
//							senha = Auth.geraSenhaProcessada(senha, (String) user.get("salt"));
//							DBManager.alterarSenha(senha, (String) user.get("email")) ;
//						}
//					}
//					else {
//						errorMsg += "Senha e confirmação de senha não são iguais.\n";
//						DBManager.insereRegistro(7002, (String) user.get("email"));
//					}
//				}
//				
//				String pathCertificado = certificadoDigitalLabel.getText();
//				if (pathCertificado.isEmpty() == false) {
//					Path cdPath = Paths.get(pathCertificado);
//					byte[] certDigBytes = null;
//					try {
//						certDigBytes = Files.readAllBytes(cdPath);
//					} catch (Exception a) {
//						a.printStackTrace();
//						DBManager.insereRegistro(7003, (String) user.get("email"));
//						return;
//					}
//					
//					X509Certificate cert = Auth.leCertificadoDigital(certDigBytes);
//					if (cert ==  null) {
//						DBManager.insereRegistro(7003, (String) user.get("email"));
//						return;
//					}
//					String infoString = cert.getVersion() +"\n"+ cert.getNotBefore() +"\n"+ cert.getType() +"\n"+ cert.getIssuerDN() +"\n"+ cert.getSubjectDN();
//					int ret = JOptionPane.showConfirmDialog(null, infoString);
//					
//					if (ret != JOptionPane.YES_OPTION) {
//						System.out.println("Cancelou");
//						DBManager.insereRegistro(7006, (String) user.get("email"));
//						return;
//					}
//					else {
//						DBManager.insereRegistro(7005, (String) user.get("email"));
//					}
//					
//					String certString = Auth.certToString(cert);
//					DBManager.alterarCertificadoDigital(certString, (String) user.get("email"));
//				}
//				
//				String pathTanList = tanListLabel.getText();
//				if (pathTanList.isEmpty() == false) {
//					DBManager.descartaTanList((String) user.get("email"));
//					List<String> list = Auth.geraTanList(pathTanList, 10,  (String) user.get("email"));
//					if (list == null) {
//						DBManager.insereRegistro(7004, (String) user.get("email"));
//						return;
//					}
//				}
//				
//				if (errorMsg.isEmpty() == false) {
//					JOptionPane.showMessageDialog(null, errorMsg);
//				}
//				dispose();
//				new MainView(Auth.autenticaEmail((String) user.get("email")));
//			}
//		});
		
	}	
	
	public void back() {
		new MenuView();
		dispose();
		setVisible(false);
	}
}
