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
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	    keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();

        Signature sig = new Signature();
        sig.initSign(key.getPrivate());
        sig.update(Text);
	    byte[] signature = sig.sign();
	    
	    System.out.println("Signature:");
	    String stringBytes = byteArrayToHex(signature);
	    System.out.println(stringBytes);
	    
	    sig.initVerify(key.getPublic());
	    sig.update(Text);
	    
	    if (sig.verify(signature)) {
	    	System.out.println("Signature verified");
	    }
	    else {
	    	System.out.println("Signature failed");
        }


    }
    
    public Signature() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		this.mDigest = MessageDigest.getInstance("MD5");	
		this.ciph = Cipher.getInstance("RSA");
	}

    public void initSign(PrivateKey key) throws InvalidKeyException, UnsupportedOperationException {		
		this.ciph.init(Cipher.ENCRYPT_MODE, key);
	}
	  
	public void update(byte[] data) {
	    this.mDigest.update(data);
	}
	  
	public byte[] sign() throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		byte[] digest = this.mDigest.digest();
		return this.ciph.doFinal(digest);
		
	}
	  
	public void initVerify(PublicKey key) throws InvalidKeyException, UnsupportedOperationException {
		this.ciph.init(Cipher.DECRYPT_MODE, key);
	}
	  
	public boolean verify(byte[] signature) throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
		byte[] decryptedMessageDigest = ciph.doFinal(signature);
				
		return Arrays.equals(this.mDigest.digest(), decryptedMessageDigest);
	}
	
	public static String byteArrayToHex(byte[] a) throws NegativeArraySizeException {
	   StringBuilder sb = new StringBuilder(a.length * 2);
	   for(byte b: a)
	      sb.append(String.format("%02x", b));
	   return sb.toString();
	}


}