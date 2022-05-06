/**
 * 
 */
package p3;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @author lorenzom
 *
 */
public class KeyOperations {

	protected boolean bKeys = false;
	protected KeyPair keyPair;
	private static int _PUBLIC_KEY_SIZE = 512;
	
	public KeyOperations() {
		bKeys = false;
		keyPair   = null;
	}
	
	public boolean haveKeys() {
		return bKeys;
	}
	
	public PublicKey getPublic(){
		return keyPair.getPublic();
	}
	
	public PrivateKey getPrivate(){
		return keyPair.getPrivate();
	}
	
	public void setKeyPair(KeyPair keyPair){
		this.keyPair = keyPair;
		this.bKeys=true;		
	}
	
	public void generateKeyPair() throws Exception {
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		 kpg.initialize(_PUBLIC_KEY_SIZE);
		 keyPair = kpg.generateKeyPair();
		 bKeys = doKeyTest(keyPair);
	 }
	    
	 public boolean doKeyTest(KeyPair kp) {
		 boolean bReturn = false;
		 try {
			 Signature dsa = Signature.getInstance("SHA1withRSA");
			 dsa.initSign(kp.getPrivate());
			 String msg = "Texto de prueba para la firma";
			 dsa.update(msg.getBytes());
			 byte[] sig = dsa.sign();
			 
			 dsa.initVerify(kp.getPublic());
			 dsa.update(msg.getBytes());
			 boolean verifies = dsa.verify(sig);
			 bReturn = verifies;
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 return bReturn;
	 }
}
