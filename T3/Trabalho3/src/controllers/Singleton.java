package controllers;

import java.security.PrivateKey;
import java.util.HashMap;

public class Singleton {
	private static Singleton singleton = null;
	
	private String loginName = "as";
	private String group = "aa";
	private String name = "bb";
	private int totalAccess = -1;
	private int totalUsers = -1;
	private PrivateKey chavePrivada;
	
	public static Singleton getInstance() {
		if(singleton == null) {
			singleton = new Singleton();
		}
		return singleton;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalAccess() {
		return totalAccess;
	}

	public void setTotalAccess(int totalAccess) {
		this.totalAccess = totalAccess;
	}

	public int getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}

	public PrivateKey getChavePrivada() {
		return chavePrivada;
	}

	public void setChavePrivada(PrivateKey chavePrivada) {
		this.chavePrivada = chavePrivada;
	}
	
	
}
