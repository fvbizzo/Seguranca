package util;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;



public class Autentic{

    public static byte[] decriptaArquivo(HashMap user, String caminho, String filename, PrivateKey chavePrivada) {
		try {
			
			byte[] arqEnv = Files.readAllBytes(Paths.get(caminho + "/" + filename + ".env"));
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			
			cipher.init(Cipher.DECRYPT_MODE, chavePrivada);
			cipher.update(arqEnv);
			
			byte [] semente = cipher.doFinal();
			
			byte[] arqEnc = Files.readAllBytes(Paths.get(caminho + "/" + filename + ".enc"));			
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
			rand.setSeed(semente);
			
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56, rand);
			Key chaveSecreta = keyGen.generateKey();
			
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			byte[] index;
			try {
				cipher.init(Cipher.DECRYPT_MODE, chaveSecreta);
				index = cipher.doFinal(arqEnc);
			} catch(Exception ey) {
				System.out.println("cypher");
				JOptionPane.showMessageDialog(null, "O arquivo não foi decripitado: erro na cypher");
				DBManager.insereRegistro(8011, (String) user.get("email"), caminho+"/"+filename);
				return null;
			}
			X509Certificate cert = Autentic.leCertificadoDigital(((String) user.get("certificado")).getBytes());
			Signature assinatura = Signature.getInstance("MD5withRSA");
			assinatura.initVerify(cert.getPublicKey());
			assinatura.update(index);
			
			byte[] arqAsd = Files.readAllBytes(Paths.get(caminho + "/" + filename + ".asd"));
			if (assinatura.verify(arqAsd) == false) {
				JOptionPane.showMessageDialog(null, "O arquivo não foi decripitado pois pode ter sido adulterado");
				DBManager.insereRegistro(8012, (String) user.get("email"), caminho+"/"+filename);
				System.out.println(filename + " pode ter sido adulterado");
				return null;
			}
			else {
				System.out.println("Decriptou index ok");
				return index;
			}
		} 
		catch (Exception IOError) {
			DBManager.insereRegistro(8004, (String) user.get("email"));
			return null;
		}
    }
    public static PrivateKey leChavePrivada(String fraseSecreta, String pathString, HashMap user) {
        try {	
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
            rand.setSeed(fraseSecreta.getBytes());
        
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56, rand);
            Key chave = keyGen.generateKey();
        
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            try {
                cipher.init(Cipher.DECRYPT_MODE, chave);
            }
            catch (Exception e) {
                DBManager.insereRegistro(4005, (String) user.get("email"));
                return null;
            }
        
            byte[] bytes = null;
            try {
                Path path = Paths.get(pathString);
                bytes = Files.readAllBytes(path);
            }
            catch (Exception e) {
                DBManager.insereRegistro(4004, (String) user.get("email"));
                return null;
            }

            String chavePrivadaBase64 = new String(cipher.doFinal(bytes), "UTF8");
            chavePrivadaBase64 = chavePrivadaBase64.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").trim();
            byte[] chavePrivadaBytes = DatatypeConverter.parseBase64Binary(chavePrivadaBase64);
        
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(chavePrivadaBytes));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static X509Certificate leCertificadoDigital(byte[] bytes) {
		try {
			
			InputStream stream = new ByteArrayInputStream(bytes);
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) factory.generateCertificate(stream);
			stream.close();
			return cert;
		} 
		catch (IOException | CertificateException e) {
			System.out.println("Certificado digital inválido");
			return null;
		}
    }
    public static String certToString(X509Certificate cert) {
	    StringWriter sw = new StringWriter();
	    try {
	        sw.write("-----BEGIN CERTIFICATE-----\n");
	        sw.write(DatatypeConverter.printBase64Binary(cert.getEncoded()).replaceAll("(.{64})", "$1\n"));
	        sw.write("\n-----END CERTIFICATE-----\n");
	    } catch (CertificateEncodingException e) {
	        e.printStackTrace();
	    }
	    return sw.toString();
    }

    public static boolean cadastraUsuario(String grupo, String senha, String pathCert) {
		if (Autentic.verificaRegrasSenha(senha) == false)
			return false;
		
		Path cdPath = Paths.get(pathCert);
		byte[] certDigBytes = null;
		try {
			certDigBytes = Files.readAllBytes(cdPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		X509Certificate cert = leCertificadoDigital(certDigBytes);
		String subjectDN = cert.getSubjectDN().getName();
		int start = subjectDN.indexOf("=");
		int end = subjectDN.indexOf(",");
		String email = subjectDN.substring(start + 1, end);
		
		start = subjectDN.indexOf("=", end);
		end = subjectDN.indexOf(",", start);
		String nome = subjectDN.substring(start + 1, end);
		
		String salt = Autentic.geraSalt();
		String senhaProcessada = Autentic.geraSenhaProcessada(senha, salt);    
		
		boolean ret = DBManager.addUser(nome, email, grupo, salt, senhaProcessada, certToString(cert));
		
		return ret;
    }

    public static HashMap autenticaEmail(String email) {
		List<HashMap> list = null;
		try {
			list = DBManager.getUser(email);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (list.size() == 1)
			return list.get(0);
		return null;
	}
	
	public static boolean autenticaSenha(String senha, HashMap user)  {
		String senhaDigest = Autentic.geraSenhaProcessada(senha, (String) user.get("salt"));
		if (user.get("passwordDigest").equals(senhaDigest))
			return true;
		return false;
	}
	
	public static boolean veriRepet(String senha){

		String part1 = senha.substring(0, 2);
		String part2 = senha.substring(2, 4);
		String part3 = senha.substring(4, 6);
		System.out.println(part1);
		System.out.println(part2);
		System.out.println(part3);
		if(part1.equals(part2)) {
			return false;
		} else if(part2.equals(part3))
		{
			return false;
		}
		else if(part1.equals(part3)) {
				return false;
			}
		else {
			return true;
		}
		
	}
	
	public static String geraSenhaProcessada(String senha, String salt) {
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Não encontrou algoritmo SHA1");
			return null;
		}
		sha1.update((senha + salt).getBytes());
		return Util.toHex(sha1.digest());
	}
	
	private static String geraSalt() {
		String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rand = new SecureRandom();
		StringBuffer salt = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			salt.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
		return salt.toString();
	}
	
	public static boolean verificaRegrasSenha(String senha) {
		int len = senha.length();
		if (len != 6 )
			return false;
		if(!Autentic.veriRepet(senha)) {
			return false;
		}
		
		return true;
    }

    public static boolean verificaVetorSenha(HashMap user, String[] senhas) {
    	for (String senha: senhas) {
    		if (Autentic.autenticaSenha(senha, user) ) {
    			return true;
    		}
    	}
    	return false;

    }

    public static boolean acessarArquivo(HashMap user, String index, String nomeArquivo, PrivateKey chavePrivada, String pastaArquivos) {
		try {
			String[] linhasIndex = index.split("\n");
			for (String linha: linhasIndex) {
				String[] params = linha.split(" ");
				String nomeSecreto = params[1];
				
				if (nomeSecreto.equals(nomeArquivo)) {
					String email = params[2];
					String grupo = params[3];
					System.out.println(grupo);
					System.out.println(user.get("groupName"));
					if (user.get("email").equals(email) || user.get("groupName").equals(grupo)) {
						DBManager.insereRegistro(8007, (String) user.get("email"), pastaArquivos+"/"+nomeArquivo);
						String nomeCodigoArquivo = params[0];
						byte[] conteudoArquivo = Autentic.decriptaArquivo(user, pastaArquivos, nomeCodigoArquivo, chavePrivada);
						if(conteudoArquivo == null) {
							return false;
						}
						DBManager.insereRegistro(8010, (String) user.get("email"), pastaArquivos+"/"+nomeArquivo);
						Files.write(Paths.get(pastaArquivos + "/" + nomeSecreto), conteudoArquivo);
						return true;
					} else {
						DBManager.insereRegistro(8008, (String) user.get("email"), pastaArquivos+"/"+nomeArquivo);
						JOptionPane.showMessageDialog(null, "O usuário não tem permissão para abrir este arquivo.");
						return false;
					}
				}
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;	
    }

    public static boolean testaChavePrivada(PrivateKey chavePrivada, HashMap user) {
		try {
			byte[] teste = new byte[1024];
			SecureRandom.getInstanceStrong().nextBytes(teste);
			Signature assinatura = Signature.getInstance("MD5withRSA");
			assinatura.initSign(chavePrivada);
			assinatura.update(teste);
			byte[] resp = assinatura.sign();
			
			PublicKey chavePublica = Autentic.leCertificadoDigital(((String) user.get("certificado")).getBytes()).getPublicKey();
			assinatura.initVerify(chavePublica);
			assinatura.update(teste);
			
			if (assinatura.verify(resp)) {
				System.out.println("Chave válida!");
				return true;
			}
			else {
				System.out.println("Chave rejeitada!");
				return false;
			}
		}
		catch (Exception e) {
			System.out.println("Erro ao testar chave privada");
			return false;
		}
    }

}