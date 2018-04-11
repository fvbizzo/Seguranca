import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.*;


public class DigestCalculator {

	private class FileInfo {
		private String hashMD5 = null;
		private String hashSHA1 = null;
		private String fileName = null;
	}

	String digestListFilePath = null;
	String digestType = null;
	MessageDigest digestMessage = null;
	static MessageDigest mDigest;

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		if (args.length < 3) {
			System.err.println("Realizar comando: java DigestCalculator Tipo_Digest Caminho_ArqListaDigest Caminho_Arq1... Caminho_ArqN");
			System.exit(1);
		}

		DigestCalculator calculator = new DigestCalculator();
		calculator.setDigestType(args[0]);
		calculator.setDigestListFilePath(args[1]);

		List<FileInfo> fileInfoList = calculator.getFileInfoList();

		List<String> filePaths = new ArrayList<String>();
		List<MessageDigest> fileDigests = new ArrayList<MessageDigest>();
		for (int i = 2; i < args.length; i++) {
			filePaths.add(args[i]);
			calculator.checkFilesDigest(args[i]);
			fileDigests.add(mDigest);
		}

		System.out.println("\n\nUm dos digests calculados:\n" + byteArrayToHex(fileDigests.get(0).digest()));


		System.out.println("\n\nLista de caminhos dos arquivos:\n" + filePaths);

		System.out.println("Conteudo lido do arquivo de lista de digests");
		for (FileInfo item : fileInfoList){
			System.out.println(item.fileName + " md5:" + item.hashMD5 + " sha1:" + item.hashSHA1);
		}


	}



	public String getDigestListFilePath() {
		return digestListFilePath;
	}

	public void setDigestListFilePath(String filePath) {
		 try {

            File f = new File(filePath);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            System.out.println("Reading file");
            while ((readLine = b.readLine()) != null) {
            	String[] lineInfo = readLine.split(" ");
    			FileInfo info = new FileInfo();
    			info.fileName = lineInfo[0];

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

	public void checkFilesDigest(String fPath) {
		try {
			File f = new File(fPath);
			BufferedReader b = new BufferedReader(new FileReader(f));
			String readLine = "";
			System.out.println("Reading file");
			String fileInfo = "";
			while ((readLine = b.readLine()) != null) {
				fileInfo = fileInfo + readLine;
			}
			byte[] ftext = fileInfo.getBytes("UTF8");
			update(ftext);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(byte[] data) {
	    this.mDigest.update(data);
	    System.out.println("\n---- update ----");
      System.out.println("Digest updated: " + this.mDigest);
			System.out.println("With data: " + byteArrayToHex(data));
			System.out.println("Digest hex: " + byteArrayToHex(this.mDigest.digest()));


	}

	public String getDigestType() {
		return digestType;
	}
	public void setDigestType(String dType) throws NoSuchAlgorithmException {
		this.mDigest = MessageDigest.getInstance(dType);
	}

	public MessageDigest getDigestMessage() {
		return digestMessage;
	}
	public void setDigestMessage(MessageDigest digestMessage) {
		this.digestMessage = digestMessage;
	}

	List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
	public List<FileInfo> getFileInfoList() {
		return fileInfoList;
	}
	public void setFileInfoList(List<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}
	public static String byteArrayToHex(byte[] a) throws NegativeArraySizeException {
	   StringBuilder sb = new StringBuilder(a.length * 2);
	   for(byte b: a)
	      sb.append(String.format("%02x", b));
	   return sb.toString();
	}
}
