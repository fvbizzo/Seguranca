package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controllers.Singleton;
import util.Autentic;
import util.DBManager;

public class KeyView extends JFrame {

	Singleton singleton = new Singleton().getInstance();
	
	private HashMap user = null;
	PrivateKey chavePrivada = null;
	
	JLabel numErrs;
	
	public KeyView(HashMap user) {
		this.user = user;
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (600, 650);
		DBManager.insereRegistro(4001, (String) Singleton.getInstance().getLoginName());
		
		numErrs = new JLabel("numero de accessos errados: " + this.user.get("numAcessoErrados"));
		
		JLabel chavePrivadaLabel = new JLabel("Chave privada");
		JFileChooser keyFileChooser = new JFileChooser();
		keyFileChooser.setPreferredSize(new Dimension (300,100));
		keyFileChooser.setControlButtonsAreShown(false);
		
		JLabel fraseSecretaLabel = new JLabel("Frase Secreta");
		JTextField fraseSecreta = new JTextField();
		fraseSecreta.setPreferredSize(new Dimension(200, 30));
		fraseSecreta.setMaximumSize(new Dimension(200, 30));
		
		JButton selecionar = new JButton("selecionar");
		selecionar.setPreferredSize(new Dimension(200, 30));
		selecionar.setMaximumSize(new Dimension(200, 30));
		
		selecionar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = ".";
				try  {
					path = keyFileChooser.getSelectedFile().getPath();
				} catch (Exception exep ) {
					System.out.println("no file chosen");
					JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado");
					DBManager.insereRegistro(4004, (String) user.get("email"));
					return;
				}
				
				if(fraseSecreta.getText().equals("") || fraseSecreta.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Frase secreta nao inserida");
					DBManager.insereRegistro(4005, (String) user.get("email"));
					return;
				}
				
				chavePrivada = Autentic.leChavePrivada(fraseSecreta.getText(), path, user);
				if (chavePrivada == null) {
					DBManager.incrementaNumChavePrivadaErrada((String)user.get("email"));
					HashMap updatedUser = Autentic.autenticaEmail((String) user.get("email"));
					numErrs.setText("numero de accessos errados: " + updatedUser.get("numChavePrivadaErrada"));
					JOptionPane.showMessageDialog(null, "Combinação de chave e frase invalida");
				} else {
					if (Autentic.testaChavePrivada(chavePrivada, user)) {
						DBManager.insereRegistro(4003, (String) user.get("email"));
						DBManager.insereRegistro(4002, (String) user.get("email"));
						DBManager.zeraNumChavePrivadaErrada((String) user.get("email"));
						DBManager.incrementaTotalAcessos((String) user.get("email"));
						
						HashMap updatedUser = Autentic.autenticaEmail((String) user.get("email"));
						
						Singleton.getInstance().setName((String) updatedUser.get("name"));
						Singleton.getInstance().setGroup((String) updatedUser.get("groupName"));
						Singleton.getInstance().setLoginName((String) updatedUser.get("email"));
						Singleton.getInstance().setTotalAccess((int) updatedUser.get("totalAcessos"));
						Singleton.getInstance().setChavePrivada(chavePrivada);
						
						new MenuView();
						dispose();
						setVisible(false);
					
					} else {
						DBManager.insereRegistro(4006, (String) user.get("email"));
						DBManager.incrementaNumChavePrivadaErrada((String)user.get("email"));
						HashMap updatedUser = Autentic.autenticaEmail((String) user.get("email"));
						numErrs.setText("numero de accessos errados: " + updatedUser.get("numChavePrivadaErrada"));
						JOptionPane.showMessageDialog(null, "Assinatura invalida");
					}
				}
				
				HashMap updatedUser = Autentic.autenticaEmail((String) user.get("email"));
				Integer acessosNegados = ((Integer) updatedUser.get("numChavePrivadaErrada"));
				numErrs.setText("numero de accessos errados: " + updatedUser.get("numChavePrivadaErrada"));
				
				if(acessosNegados == 1) {
					DBManager.insereRegistro(4008, (String) user.get("email"));
				}
				if(acessosNegados == 2) {
					DBManager.insereRegistro(4009, (String) user.get("email"));
				}
				if(acessosNegados >= 3) {
					DBManager.insereRegistro(4010, (String) user.get("email"));
					DBManager.insereRegistro(4007, (String) user.get("email"));
					DBManager.insereRegistro(4002, (String) user.get("email"));
					JOptionPane.showMessageDialog(null, "Senha incorreta. Número total de erros atingido. Aguarde até 2 minutos para tentar novamente.");
					LoginView loginView = new LoginView();
					loginView.setVisible(true);
					dispose();
					setVisible(false);
				}
				
			}
		});
		

		getContentPane().add(numErrs);
		
		getContentPane().add(chavePrivadaLabel);
		getContentPane().add(keyFileChooser);
		
		getContentPane().add(fraseSecretaLabel);
		getContentPane().add(fraseSecreta);
		
		getContentPane().add(selecionar);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Chave e frase:");
	
	}
}
