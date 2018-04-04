import java.security.*;
import java.util.Arrays;
import javax.crypto.*;


public class Signature {

    private PrivateKey key;
	private MessageDigest mDigest;
    private Cipher ciph; 

    public static void main (String[] args) throws Exception {
		
		if (args.length !=1) {
			System.err.println("Usage: java Signature text");
			System.exit(1);
		}
        byte[] Text = args[0].getBytes("UTF8");

        System.out.println( "\nStart generating RSA key" );
        //Gera um par de chaves publica e privada com o auxilio do KeyPaisGenerator
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	    keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();

        //Cria um objeto de assinatura no modo encriptacao com chave privada
        Signature sig = new Signature();
        sig.initSign(key.getPrivate());
        
        //Assina o objeto com chave privada
        sig.update(Text);
	    byte[] signature = sig.sign();
	    
	    //Imprime a assinatura como um string
	    System.out.println("Signature:");
	    String stringBytes = byteArrayToHex(signature);
	    System.out.println(stringBytes);
	    
	    //Verifica a assinatura com a chave publica
	    sig.initVerify(key.getPublic());
	    sig.update(Text);
	    if (sig.verify(signature)) {
	    	System.out.println("Signature verified");
	    }
	    else {
	    	System.out.println("Signature failed");
        }


    }
    
    /**
     * Função de inicialização da classe cria instancia para o digest e para cipher
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     */
    public Signature() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		this.mDigest = MessageDigest.getInstance("MD5");	
		this.ciph = Cipher.getInstance("RSA");
	}

    /**
     * inicializa a cipher com uma chave privada para modo encriptar
     * @param key uma chave privada
     * @throws InvalidKeyException
     * @throws UnsupportedOperationException
     */
    public void initSign(PrivateKey key) throws InvalidKeyException, UnsupportedOperationException {		
		this.ciph.init(Cipher.ENCRYPT_MODE, key);
		System.out.println("\n---- initSign ----");
        System.out.println("Cipher initalized encrypt mode: " + this.ciph);
        System.out.println("With Private Key: " + key);
	}
	  
    /**
     * pega um objeto message digest usando o md5
     * @param data array de bytes byte[] contendo text
     */
	public void update(byte[] data) {
	    this.mDigest.update(data);
	    System.out.println("\n---- update ----");
        System.out.println("Digest updated: " + this.mDigest);
        System.out.println("With data: " + byteArrayToHex(data));
	}
	  
	/**
	 * Funcao para assinar o texto com palavra chave privada
	 * @return data encriptada com a chave
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] sign() throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		byte[] digest = this.mDigest.digest();
		
		System.out.println("\n---- sign ----");
        System.out.println("digest() called: " + byteArrayToHex(digest));
		
		return this.ciph.doFinal(digest);
		
	}
	  
	/**
	 * inicializa a cipher com uma chave publica para modo decriptar
	 * @param key chave publica 
	 * @throws InvalidKeyException
	 * @throws UnsupportedOperationException
	 */
	public void initVerify(PublicKey key) throws InvalidKeyException, UnsupportedOperationException {
		this.ciph.init(Cipher.DECRYPT_MODE, key);
		System.out.println("\n---- initVerify ----");
        System.out.println("Cipher initalized decrypt mode: " + this.ciph);
        System.out.println("With Public Key: " + key);
	}
	  
	/**
	 * verirfica a assinatura
	 * @param signature
	 * @return
	 * @throws IllegalStateException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public boolean verify(byte[] signature) throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		byte[] decryptedMessageDigest = ciph.doFinal(signature);
				
		System.out.println("\n---- verify ----");
        System.out.println("doFinal called with signature: " + byteArrayToHex(signature));
        System.out.println("resulting in: " + byteArrayToHex(decryptedMessageDigest));
		
		return Arrays.equals(this.mDigest.digest(), decryptedMessageDigest);
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
