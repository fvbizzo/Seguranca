package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controllers.Singleton;
import util.Autentic;
import util.DBManager;

public class FilesView extends JFrame {
	Singleton singleton = new Singleton().getInstance();
	
	private HashMap user = null;
	PrivateKey chavePrivada = null;
	
	String caminhoArq = ".";
	String indexArq = "";
	
	public FilesView() {
		
		HashMap updatedUser = Autentic.autenticaEmail(singleton.getLoginName());
		this.user = updatedUser;
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize (600, 650);
		DBManager.insereRegistro(8001, (String) Singleton.getInstance().getLoginName());
		
		String email = singleton.getLoginName();
		String name = singleton.getName();
		String group = singleton.getGroup();
		int totalConsult = (int) updatedUser.get("totalConsultas");
		
		JLabel emailLabel = new JLabel(String.format("Email: %s", email));
		JLabel groupLabel = new JLabel(String.format("Grupo: %s", group));
		JLabel nameLabel = new JLabel(String.format("Nome: %s", name));
		JLabel totalConsultLabel = new JLabel(String.format("Total de consultas do usuário: %d", totalConsult));
		
		JLabel mainMenu = new JLabel("Caminho da pasta:");
		JFileChooser folderChooser = new JFileChooser();
		folderChooser.setPreferredSize(new Dimension (300,100));
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		folderChooser.setControlButtonsAreShown(false);
		
		JButton listButton = new JButton("Listar arquivos!");
		
		String[] columnNames = {"Nome código","Nome secreto", "Dono", "Grupo"};
		Object[][] data = {};
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		JTable table;
		table = new JTable(tableModel){
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension (200,100));
		table.setPreferredSize(new Dimension (250,100));
		
		JButton decripButton = new JButton("Decriptar arquivo!");
		JButton backButton = new JButton("Voltar para o Menu Principal");

		
		listButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DBManager.insereRegistro(8003, (String) user.get("email"));
				caminhoArq = ".";
				try {
					caminhoArq = folderChooser.getSelectedFile().getPath();
				} catch (Exception excp) {
					caminhoArq = ".";
				}
				System.out.println(caminhoArq);
				if (caminhoArq.isEmpty() || caminhoArq.equals(".")) {
					DBManager.insereRegistro(8004, (String) user.get("email"));
					JOptionPane.showMessageDialog(null, "Caminho de pasta inválido fornecido");
					return;
				}
				
				indexArq = "";
				try {
					chavePrivada = Singleton.getInstance().getChavePrivada();
					indexArq = new String(Autentic.decriptaArquivo(user, caminhoArq, "index", chavePrivada), "UTF8");
				} catch (UnsupportedEncodingException | NullPointerException e1) {
					JOptionPane.showMessageDialog(null, "Não foi possível listar os arquivos com credenciais deste usuario.");
					return;
				}
				
				String[] listaArquivos = indexArq.split("\n");
				int rowcount = tableModel.getRowCount();
				for (int i = 0; i < rowcount; i++) {
					tableModel.removeRow(0);
				}
				for (String arq: listaArquivos) {
					String[] items = arq.split(" ");
					tableModel.addRow(items);
				}
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				table.setModel(tableModel);
				tableModel.fireTableDataChanged();
				
				DBManager.insereRegistro(8005, (String) user.get("email"));
			}
		});
		
		decripButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DBManager.insereRegistro(8006, (String) user.get("email"), caminhoArq+"/"+indexArq);
				System.out.println("Inicio de decriptacao de arquivo");
				int index = table.getSelectedRow();
				String nomeArquivo = (String) table.getValueAt(index, 1);
				if (Autentic.acessarArquivo(user, indexArq, nomeArquivo, chavePrivada, caminhoArq)) {
					System.out.println("Decriptou arquivo com sucesso!");
					DBManager.insereRegistro(8009, (String) user.get("email"), caminhoArq+"/"+indexArq);

				}
				else {
					JOptionPane.showMessageDialog(null, "Usuário não possui permissão para ler o arquivo selecionado");
					DBManager.insereRegistro(8011, (String) user.get("email"), caminhoArq+"/"+indexArq);

				}
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
		getContentPane().add(totalConsultLabel);
		
		getContentPane().add(folderChooser);
		getContentPane().add(listButton);
		
		//getContentPane().add(table.getTableHeader());
		getContentPane().add(scrollPane);
		
		getContentPane().add(decripButton);
		getContentPane().add(backButton);
		
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("Consulta");
		
		
	}
	
	public void back() {
		DBManager.insereRegistro(8002, (String) Singleton.getInstance().getLoginName());
		new MenuView();
		dispose();
		setVisible(false);
	}
	
}
