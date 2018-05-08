
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
				clicked();
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
