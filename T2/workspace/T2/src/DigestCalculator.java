import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.*;


public class DigestCalculator {

	private class FileInfo {
		private String hashMD5 = null;
		private String hashSHA1 = null;
		private String fileName = null;
	}

	List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
	
	String digestListFilePath = null; //Arquivo contendo Caminho_ArqListaDigest
	String digestType = null; // MD5 ou SHA1
	MessageDigest mDigest; 

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		if (args.length < 3) {
			System.err.println("Realizar comando: java DigestCalculator Tipo_Digest Caminho_ArqListaDigest Caminho_Arq1... Caminho_ArqN");
			System.exit(1);
		}

		DigestCalculator calculator = new DigestCalculator();
		calculator.setDigestType(args[0]);
		calculator.setDigestListFilePath(args[1]);

		List<FileInfo> fileInfoList = calculator.getFileInfoList();
		List<String> fileDigests = new ArrayList<String>();
		List<String> filePaths = new ArrayList<String>();
		List<String> status = new ArrayList<String>();
				
		for (int i = 2; i < args.length; i++) {
			filePaths.add(args[i]);
			fileDigests.add(calculator.checkFilesDigest(args[i]));
		}

		status = calculator.checkDigest(filePaths, fileDigests, fileInfoList);
		calculator.printStatus(filePaths, fileDigests, status);
		calculator.rewriteFile(filePaths, fileDigests, status);

	}

	/**
	 * Dado um arquivo de entrada le ele e cria uma lista de objetos que contem nome/hashmd5/hashsha1
	 * @param filePath arquivo contendo informações de arquivos e suas hashs
	 */
	public void setDigestListFilePath(String filePath) {
		this.digestListFilePath = filePath;
		try {
            File f = new File(filePath);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            while ((readLine = b.readLine()) != null) {
            	String[] lineInfo = readLine.split(" ");
    			FileInfo info = new FileInfo();
    			info.fileName = lineInfo[0];
    			
    			System.out.print(lineInfo.length);
    			
    			if (lineInfo[1].equals("MD5")) info.hashMD5 = lineInfo[2];
    			else if (lineInfo[1].equals("SHA1")) info.hashSHA1 = lineInfo[2];

    			if (lineInfo.length > 3) {
    				if (lineInfo[3].equals("MD5")) info.hashMD5 = lineInfo[4];
    				else if (lineInfo[3].equals("SHA1")) info.hashSHA1 = lineInfo[4];
    			}

    			this.fileInfoList.add(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	/**
	 * Para um arquivo dado na entrada se le o conteudo e chama as funcoes de update
	 * @param fPath o arquivo a ser lido e traduzido
	 * @return o valor em string apos a funcao update
	 */
	public String checkFilesDigest(String fPath) {
		String hashedValue = null;
		try {
			File f = new File(fPath);
			BufferedReader b = new BufferedReader(new FileReader(f));
			String readLine = "";
			String fileInfo = "";
			while ((readLine = b.readLine()) != null) {
				fileInfo = fileInfo + readLine;
			}
			byte[] ftext = fileInfo.getBytes("UTF8");
			hashedValue = update(ftext);
			return hashedValue;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashedValue;
	}

	/**
	 * utiliza a funcao update do message digest e retorna um valor string dela
	 * @param data data a ser passada como parametro no update
	 * @return o valor em string apos a funcao update
	 */
	public String update(byte[] data) {
	    this.mDigest.update(data);
		String value = byteArrayToHex(this.mDigest.digest());
		return value;
	}

	/**
	 * Dado um conjunto de listas com Caminho dos arquivos, o valor apos o update compara com o valor esperado na lista 
	 * Decide para cada valor se ele esta OK NAO_OK COLISION ou NOT_FOUND
	 * @param FP - lista de caminhos dos arquivos
	 * @param MD - lista de valores apos o update
	 * @param HSH - lista valores a serem comparados com arquivos
	 * @return
	 */
	public List<String> checkDigest(List<String> FP, List<String> MD, List<DigestCalculator.FileInfo> HSH) {
		List<String> status = new ArrayList<String>();
		String buffer;
		String name;
		int flag_ok = 0;
		int flag_achou = 0;
		int flag_achou_algoritmo = 0;
		int flag_colidiu = 0;

		for(int i = 0; i < FP.size() ; i++) {
			buffer = MD.get(i);
			name = FP.get(i);
			for( int j = 0; j < HSH.size(); j++ ) {
				if(FP.get(i).equals( HSH.get(j).fileName )) {
					flag_achou = 1;
					if(this.digestType.equals( "MD5" )) {
						if(HSH.get(j).hashMD5 != null){
							if(MD.get(i).equals( HSH.get(j).hashMD5 )){
								flag_ok = 1;
							}
						} else {
							flag_achou = 0;
						}
					}
					if(this.digestType.equals( "SHA1" )) {
						if(HSH.get(j).hashSHA1 != null){
							if(MD.get(i).equals( HSH.get(j).hashSHA1 )){
								flag_ok = 1;
							}
						} else {
							flag_achou = 0;
						}
					}
				}
			}
						
			for(int j = 0; j < HSH.size(); j++ ) {
				
				if(this.digestType.equals( "MD5" )) {
					if(buffer.equals( HSH.get(j).hashMD5 ) && !name.equals( HSH.get(j).fileName )) {
						flag_colidiu = 1;
					}
				}
				if(this.digestType.equals( "SHA1" )) {
					if(buffer.equals( HSH.get(j).hashSHA1 ) && !name.equals( HSH.get(j).fileName )) {
						flag_colidiu = 1;
					}
				}
			}
			for(int j = 0; j < HSH.size(); j++ ) {
				if(buffer.equals( MD.get(j)) && !name.equals( FP.get(j) )) {
					flag_colidiu = 1;
				}
			}
			
			if ( flag_colidiu == 1 ) {
				status.add("COLISION");
			} else if ( flag_achou == 0 ) {
				status.add("NOT_FOUND");
			} else if ( flag_ok == 0 ) {
				status.add("NOT_OK");
			} else {
				status.add("OK");
			}
			
			flag_ok = 0;
			flag_achou = 0;
			flag_colidiu = 0;
		}
		return status;
	}
	
	/**
	 * Imprime os status calculados pela funcao checkDigest
	 * @param FP - lista de caminhos dos arquivos
	 * @param MD - lista de valores apos o update
	 * @param status - lista contendo o status de cada um retornado
	 */
	public void printStatus(List<String> FP, List<String> MD, List<String> status){
		System.out.println("\n\n-------------------");
		for (int i = 0; i< FP.size(); i++) {
			System.out.println(FP.get(i) +" "+ this.digestType +" "+ MD.get(i) +" "+ "("+status.get(i)+")");
		}
		System.out.println("-------------------\n\n");
	}
	
	/**
	 * Edita o arquivo original adicionando o que faltava
	 * @param FP - lista de caminhos dos arquivos
	 * @param MD - lista de valores apos o update
	 * @param status - lista contendo o status de cada um retornado
	 */
	public void rewriteFile(List<String> FP, List<String> MD, List<String> status) {
		String filePath = this.digestListFilePath;
		List<String> lines = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		try {
			File f = new File(filePath);
		    BufferedReader b = new BufferedReader(new FileReader(f));
		    String readLine = "";
		   
		    while ((readLine = b.readLine()) != null) {
		    	String line = readLine;
		    	String[] lineInfo = readLine.split(" ");
		    	String name = lineInfo[0];
				
		    	if(FP.contains(name)){
		    		int index = FP.indexOf(name);
		    		if(status.get(index).equals("NOT_FOUND")){
		    			line = line + " " + this.digestType + " " + MD.get(index);
		    		}
		    	}
			   			   
		    	lines.add(line);
		    	names.add(name);
		
		   }
        } catch (IOException e) {
            e.printStackTrace();
        }
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		    for (String line: lines) {
		    	writer.write(line + "\n");
		    	System.out.println(line);
		    }
		    
		    for (String name: FP) {
		    	if (!names.contains(name)){
		    		int index = FP.indexOf(name);
		    		if(status.get(index).equals("NOT_FOUND")){
		    			String newLine = name + " " + this.digestType + " " + MD.get(index);
		    			writer.write(newLine + "\n");
				    	System.out.println(newLine);
		    		}
		    	}
		    }
		    
		    writer.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
		

	}

	public String getDigestType() {
		return digestType;
	}
	public void setDigestType(String dType) throws NoSuchAlgorithmException {
		this.digestType = dType;
		this.mDigest = MessageDigest.getInstance(dType);
	}

	public List<FileInfo> getFileInfoList() {
		return fileInfoList;
	}
	public void setFileInfoList(List<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}
	
	/**
	 * Realiza a conversão de uma array de bytes para uma string
	 * @param a array de bytes a ser convertido byte[]
	 * @return representacao em string de um array de bytes
	 * @throws NegativeArraySizeException
	 */
	public static String byteArrayToHex(byte[] a) throws NegativeArraySizeException {
	   StringBuilder sb = new StringBuilder(a.length * 2);
	   for(byte b: a)
	      sb.append(String.format("%02x", b));
	   return sb.toString();
	}
}
