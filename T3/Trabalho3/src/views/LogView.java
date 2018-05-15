package views;

import java.util.HashMap;
import java.util.List;

import util.DBManager;

public class LogView {

	public static void main(String[] args) {
		List<HashMap> logs = DBManager.getLog();
		
		for (HashMap log: logs) {
			String user = (String)log.get("email");
			String nwstr = ((String)log.get("texto")).replaceAll("<login_name>", user);
			String newstr = nwstr.replaceAll("<arq_name>", (String)log.get("filename"));
			System.out.println(String.format("%s) %s - %s", (Integer)log.get("id"), newstr, log.get("created")));
			
		}
	}

}