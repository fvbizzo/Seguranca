package util;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBManager {
	
///////////////////////////////////////////////////
// 
//                Public methods
//_________________________________________________
	
	public static Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:dbwork.db");
		}
		catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return null;
	}
	
	public static List getLog() {
		return selectFromDb("select Registro.id, email, filename, texto from Registro JOIN Mensagem ON Mensagem.id = Registro.messageId order by Registro.id, created;");
	}
	
	public static boolean addUser(String name, String email, String group, String salt, String senha, String certDig) {
		int grupo = 1;
		if (group.equals("administrador")) {
			grupo = 0;
		}
		return insertIntoDb(String.format("INSERT INTO User VALUES "
				+ "('%s', '%s', '%s', '%s', '%s', 1, 0, null, 0, 0, '%s', 0)"
				, name, email, grupo, salt, senha, certDig)
			);
	}

	public static boolean insereRegistro(int idMsg) {
		return insereRegistro(idMsg, null, null);
	}
	
	public static boolean insereRegistro(int idMsg, String email) {
		return insereRegistro(idMsg, email, null);
	}
	
	public static boolean insereRegistro(int idMsg, String email, String arquivo) {
		return insertIntoDb(String.format("INSERT INTO Registro (messageId, email, filename) VALUES ('%d', '%s', '%s')", idMsg, email, arquivo));
	}
	
	public static int retornaNumUsuarios() {
		return selectFromDb(String.format("SELECT * FROM User")).size();
	}
	
	public static List getUser(String email) throws ClassNotFoundException {
		return selectFromDb(String.format("SELECT u.name as name, u.email as email, g.nome as groupName, u.salt as salt, u.passwordDigest as passwordDigest, u. acesso as acesso, u.numAcessoErrados as numAcessoErrados, u.ultimaTentativa as ultimaTentativa, u.totalAcessos as totalAcessos, u.totalConsultas as totalConsultas, u.certificado as certificado, u.numChavePrivadaErrada as numChavePrivadaErrada FROM User as u JOIN Grupo as g ON u.groupId = g.id WHERE email = '%s'", email));
	}
	
	public static boolean alterarCertificadoDigital(String certificado, String email, String newEmail) {
		if (email.equals(newEmail)) {
			updateDb(String.format("UPDATE User SET certificado = '%s' WHERE email = '%s'", certificado, email));
			return true;
		} else {
			try {
				if (getUser(newEmail).isEmpty()) {
					updateDb(String.format("UPDATE User SET certificado = '%s', email = '%s' WHERE email = '%s'", certificado, newEmail, email));
					return true;
				} else {
					return false;
				}
			} catch (Exception excp) {
				return false;
			}
		}
	}
	
	public static void alterarSenha(String novaSenha, String email) {
		updateDb(String.format("UPDATE User SET passwordDigest = '%s' WHERE email = '%s'", novaSenha, email));
	}
	
	public static void incrementaAcessoErrado(String email) {
		updateDb(String.format("UPDATE User SET numAcessoErrados = numAcessoErrados + 1, ultimaTentativa = datetime('now', 'localtime') WHERE email = '%s'", email));
	}
	
	public static void zeraAcessoErrado(String email) {
		updateDb(String.format("UPDATE User SET numAcessoErrados = 0 WHERE email = '%s'", email));
	}
	
	public static void incrementaNumChavePrivadaErrada(String email) {
		updateDb(String.format("UPDATE User SET numChavePrivadaErrada = numChavePrivadaErrada + 1, ultimaTentativa = datetime('now', 'localtime') WHERE email = '%s'", email));
	}
	
	public static void zeraNumChavePrivadaErrada(String email) {
		updateDb(String.format("UPDATE User SET numChavePrivadaErrada = 0 WHERE email = '%s'", email));
	}	
	
	public static void incrementaTotalAcessos(String email) {
		updateDb(String.format("UPDATE User SET totalAcessos = totalAcessos + 1 WHERE email = '%s'", email));
	}
	
	
///////////////////////////////////////////////////
//	
//                Private methods 
//_________________________________________________
	
	private static boolean insertIntoDb(String query) {
		Connection conn = connect();
		try {
			Statement stat = conn.createStatement();
			stat.setQueryTimeout(30);
			stat.executeUpdate(query);
			stat.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			closeConn(conn);
			return false;
		}
		closeConn(conn);
		return true;
	}
	
	private static void updateDb(String query) {
		Connection conn = connect();
		try {
			Statement stat = conn.createStatement();
			stat.setQueryTimeout(30);
			stat.executeUpdate(query);
			stat.close();			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		closeConn(conn);
	}
	
	private static List<HashMap<String,Object>> selectFromDb(String query) {
		Connection conn = connect();
		try {
			Statement stat = conn.createStatement();
			stat.setQueryTimeout(30);
			ResultSet res = stat.executeQuery(query);
			List<HashMap<String,Object>> lst = convertResultSetToList(res);
			stat.close();
			closeConn(conn);
			return lst;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			closeConn(conn);
			return null;
		}
	}
	
	private static boolean closeConn(Connection conn) {
		try {
			if (conn != null) 
				conn.close();
		}
		catch (SQLException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	
	private static List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

	    while (rs.next()) {
	        HashMap<String,Object> row = new HashMap<String, Object>(columns);
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        list.add(row);
	    }

	    return list;
	}

}