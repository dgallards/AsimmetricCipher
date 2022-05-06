package p3;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import javax.crypto.Cipher;

public class Operations {
	private Cipher cipher;
	private Signature signature;

	public Operations() {
		cipher = null;
		signature = null;
	}

	public Cipher getCipher() throws Exception {
		if (this.cipher != null) {
			return this.cipher;
		} else {
			return null;
		}
	}

	public void prepareCypher(String cipherAlgorithm) throws Exception {
		cipher = Cipher.getInstance(cipherAlgorithm);
	}

	public void initEncrypt(PublicKey publicKey) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	}

	public void initDecrypt(PrivateKey privateKey) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
	}

	public byte[] asimetricOperation(byte[] inputData) throws Exception {
		return cipher.doFinal(inputData);
	}

	public void prepareSignature(String signatureAlgorithm) throws Exception {
		signature = Signature.getInstance(signatureAlgorithm);
	}

	public void initSign(PrivateKey privateKey) throws Exception {
		signature.initSign(privateKey);
	}

	public void initVerify(PublicKey publicKey) throws Exception {
		signature.initVerify(publicKey);
	}

	public byte[] signData(byte[] inputData) throws Exception {
		signature.update(inputData);
		return signature.sign();
	}

	public boolean VerifyData(byte[] data, byte[] signature2verify) throws Exception {
		signature.update(data);
		return signature.verify(signature2verify);
	}

}
