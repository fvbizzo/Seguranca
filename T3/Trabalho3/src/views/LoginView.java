package views;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.DBManager;
import util.Autentic;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class LoginView extends JFrame {
	
	private final int FRAME_WIDTH = 300; 
	private final int FRAME_HEIGHT = 200;
	
	JLabel loginLabel = new JLabel("Login:");
	JTextField loginField = new JTextField();
	JButton loginButton = new JButton("Login");
	
	
	public LoginView(){
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setSize (250, 100);
		
		
		getContentPane().add(loginLabel);
		getContentPane().add(loginField);
		getContentPane().add(loginButton);
		
		loginField.setSize(200, 40);
		
		
		
		
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				HashMap user = Autentic.autenticaEmail(loginField.getText());
				if (user == null) {
					DBManager.insereRegistro(2005, loginField.getText());
					JOptionPane.showMessageDialog(null, "Usuário não identificado.");
				}
				else {
					Integer acessosNegados = ((Integer) user.get("numAcessoErrados"));
					System.out.println(acessosNegados);
					if (acessosNegados >= 3) {	
						String ultimaTentativa = (String) user.get("ultimaTentativa");
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						Date horario = null;
						try {
							horario = formatter.parse(ultimaTentativa);
						} catch (ParseException e1) {
							e1.printStackTrace();
							System.exit(1);
						}
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.MINUTE, -2);
						cal.add(Calendar.HOUR, 2);// fuso horario
						System.out.println(horario);
						System.out.println(cal.getTime());
						if (horario.before(cal.getTime())) {
							DBManager.zeraAcessoErrado((String) user.get("email"));
							user = Autentic.autenticaEmail((String) user.get("email"));
						}
						else {
							DBManager.insereRegistro(2004, (String) user.get("email"));
							JOptionPane.showMessageDialog(null, "Usuário com acesso bloquado.");
						}
					}
					else {
						DBManager.insereRegistro(2003, (String) user.get("email"));
						DBManager.insereRegistro(2002);
						dispose();
						new PasswordView();
					}
				}
			}
			
		});
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Login");
						
	}
	
	private void clicked() {
		System.out.println("hii");
		
		PasswordView pass = new PasswordView();
		this.dispose();
		this.setVisible(false);
		pass.setVisible(true);
	}

	
}
