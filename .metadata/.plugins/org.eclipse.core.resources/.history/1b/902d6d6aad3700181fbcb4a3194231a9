import java.security.*;
import java.util.Arrays;
import javax.crypto.*;


public class Signature {

    private PrivateKey key;
	private MessageDigest mDigest;
    private Cipher ciph; 

    public static void main (String[] args) throws Exception {
		
		if (args.length !=1) {
			System.err.println("Usage: java MySignature text");
			System.exit(1);
		}
        byte[] Text = args[0].getBytes("UTF8");

        System.out.println( "\nStart generating RSA key" );
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	    keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();

        Signature sig = new Signature();
        sig.initSign(key.getPrivate());
        sig.update(plainText);
	    byte[] signature = sig.sign();
	    System.out.println("Signature:");
	    hexPrint(signature);
	    sig.initVerify(key.getPublic());
	    sig.update(plainText);
	    
	    if (sig.verify(signature)) {
	    	System.out.println("Signature verified");
	    }
	    else {
	    	System.out.println("Signature failed");
        }


    }




}