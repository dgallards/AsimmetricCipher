package p3;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.encoders.Hex;

import srt.Header;
import srt.Options;
/**
 * Esta clase es la que se encarga de cifrar y firmar los archivos
 * Es usada por la interfaz gráfica. Se pueden mejorar ambas. Esto es solo
 * una aproximación. Nuestra intención era hacer un programa parecido a 
 * PGP con Java Swing, pero no hemos tenido tiempo.
 * 
 * @author Diego Gallardo Zancada
 *
 */
public class Practica3 {
	private String algorithm1 = "RSA/ECB/PKCS1Padding";
	private String algorithm2 = "SHA1withRSA";
	private byte[] input = Hex.decode("a0a1a2a3a4a5a6a7a0a1a2a3a4a5a6a7" + "a0a1a2a3a4a5a6a7a0");
	private byte output[] = null;

	// private Options options = new Options();
	private Operations operations = null;
	private KeyOperations keys = null;

	public Practica3() {
		operations = new Operations();
		keys = new KeyOperations();
	}
	
	/** 
	 * [DEPRECATED]
	 * Es la función main del programa. Actualmente no tiene funcionalidad.
	 * @param args No tiene funcionalidad.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Practica3 practica3 = new Practica3();
		System.out.println("BySS, Ejemplo de cifrado y firma de mensajes.");
		practica3.doMenu();
	}

	/**
	 * [DEPRECATED]
	 * Es el menu del programa. Actualmente no tiene funcionalidad.
	 * @throws Exception
	 */
	public void doMenu() throws Exception {
		do {
			System.out.println();
			System.out.println("Operaciï¿œn a realizar:");
			System.out.println("1-Cifrado KU y descifrado KR");
			System.out.println("2-Firma y verificación de mensaje");
			System.out.println("3-Crear par de claves");
			System.out.println("4-Cifrado KU en fichero");
			System.out.println("5-Descifrado KR en fichero");
			System.out.println("6-Guardar par de claves en ficheros");
			System.out.println("7-Cargar par de claves en ficheros");
			System.out.println("8-Firmar fichero con firma separado");
			System.out.println("9-Firmar fichero con firma integrada");
			System.out.println("a-Comprobar firma de archivo separada");
			System.out.println("b-Comprobar firma de archivo integrada");
			System.out.println("0-Exit.");
			System.out.print("operation:-");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int c = br.read();
			switch (c) {
			case '1':
				doEncrypt();
				break;
			case '2':
				doSign();
				break;
			case '3':
				createPairKeys();
				break;
			case '4':
				//doEncryptFile();
				break;
			case '5':
				//doDecryptFile();
				break;
			case '6':
				//saveKeys();
				break;
			case '7':
				//loadKeys();
				break;
			case '8':
				doSignFileWithoutData();
				break;
			case '9':
				//doSignFileWithData();
				break;
			case 'a':
				doVerifyFileWithoutData();
				break;
			case 'b':
				//doVerifyFileWithData();
				break;
			case '0':
				return;
			}
		} while (true);
	}
	
	/**
	 * [DEPRECATED]
	 * Este método realiza la encriptación y descifrado de un mensaje escrito por consola.
	 */
	public void doEncrypt() {
		
		if (keys.haveKeys()) {
			try {
				operations.prepareCypher(algorithm1);
				operations.initEncrypt(keys.getPublic());

				input = readMsgToEncrypt().getBytes();
				output = operations.asimetricOperation(input);

				System.out.println("input    : " + Hex.toHexString(input));
				if (output != null) {
					System.out.println("output   : " + Hex.toHexString(output));

					System.out.println("Decrypt operation with KR: ");
					operations.initDecrypt(keys.getPrivate());
					byte output2[] = operations.asimetricOperation(output);
					System.out.println("input     : " + Hex.toHexString(output));
					System.out.println("output    : " + Hex.toHexString(output2));
					System.out.println("text      : " + new String(output2));
				} else {
					System.out.println("No hay salida.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No hay claves.");
		}
	}

	/**
	 * Este método es utilizado para encriptar el contenido de un archivo cuya ruta es pasada por parámetro.
	 * 
	 * @param fileToCiph ruta del fichero de entrada que se quiere encriptar
	 */
	public boolean doEncryptFile(File fileToCiph) {
		boolean result = false;
		if (keys.haveKeys()) {
			try {
				result=true;
				operations.prepareCypher(algorithm1);
				operations.initEncrypt(keys.getPublic());
				FileInputStream fis = new FileInputStream(fileToCiph);
				FileOutputStream fos = new FileOutputStream(fileToCiph.getAbsolutePath()+".cif");
				Header hdr = new Header(Options.OP_PUBLIC_CIPHER, algorithm1, "none", keys.getPublic().getEncoded());
				hdr.save(fos);
				boolean finFichero = false;
				byte[] ibuf = new byte[53];
				while (!finFichero) {
					ibuf = fis.readNBytes(53);
					if (ibuf.length > 0) {
						byte[] obuf = operations.asimetricOperation(ibuf);
						fos.write(obuf);
					} else {
						finFichero = true;
						fis.close();
						fos.close();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result=false;
			System.out.println("No hay claves.");
		}
		return result;
	}

	/**
	 * Este método es utilizado para desencriptar el contenido de un archivo cuya
	 * ruta es pasada por parámetro.
	 * 
	 * @param fileToCiph ruta del fichero de entrada que se quiere desencriptar
	 */
	public boolean doDecryptFile(File fileToCiph) {
		boolean result = false;
		if (keys.haveKeys()) {
			result=true;
			try {
				FileInputStream fis = new FileInputStream(fileToCiph.getAbsolutePath());
				String outFile = fileToCiph.getAbsolutePath().replace(".cif", "");
				FileOutputStream fos = new FileOutputStream(outFile);
				Header hdr = new Header();
				hdr.load(fis);
				operations.prepareCypher(hdr.getAlgorithm1());
				operations.initDecrypt(keys.getPrivate());
				byte[] ibuf = new byte[64];
				boolean finFichero = false;
				while (!finFichero) {
					ibuf = fis.readNBytes(64);
					if (ibuf.length > 0) {
						byte[] obuf = operations.asimetricOperation(ibuf);
						fos.write(obuf);
					} else {
						finFichero = true;
						fis.close();
						fos.close();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result=false;
			System.out.println("No hay claves.");
		}
		return result;
	}
	
	/**
	 * Este método es utilizado para guardar las claves en el sistema.
	 * 
	 * @param nombreKey nombre de las claves elegido.
	 */
	public void saveKeys(String nombreKey) throws Exception {
		do {
			System.out.println();
			System.out.println("Generación de un par de claves RSA de 512 bits:");
			keys.generateKeyPair();
			if (keys.haveKeys()) {
				System.out.print("Claves generadas correctamente.");
				PublicKey pub = keys.getPublic();
				PrivateKey priv = keys.getPrivate();
				FileOutputStream fosPub = new FileOutputStream(nombreKey+".pubkey");
				FileOutputStream fosPriv = new FileOutputStream(nombreKey+".privkey");

				fosPub.write(pub.getEncoded());
				fosPriv.write(priv.getEncoded());

				fosPub.close();
				fosPriv.close();
				return;
			} else {
				System.out.print("Error en la generación de claves.");
				System.out.print("¿Cancelar? (s/n)");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int c = br.read();
				if ((c == 's') || (c == 'S')) {
					return;
				}
			}
		} while (true);
	}

	/**
	 * Este método es utilizado para cargar las claves en el sistema.
	 * 
	 * @param pubKey llave publica
	 * @param privKey llave privada
	 */
	public boolean loadKeys(File pubKey, File privKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		boolean result=false;
		byte[] bytesPub = Files.readAllBytes(pubKey.toPath());
		X509EncodedKeySpec ksPub = new X509EncodedKeySpec(bytesPub);
		KeyFactory kfPub = KeyFactory.getInstance("RSA");
		PublicKey pub = kfPub.generatePublic(ksPub);

		byte[] bytesPriv = Files.readAllBytes(privKey.toPath());
		PKCS8EncodedKeySpec ksPriv = new PKCS8EncodedKeySpec(bytesPriv);
		KeyFactory kfPriv = KeyFactory.getInstance("RSA");
		PrivateKey pvt = kfPriv.generatePrivate(ksPriv);

		KeyPair kp = new KeyPair(pub, pvt);

		keys.setKeyPair(kp);
		if (keys.doKeyTest(kp)) {
			result=true;
			System.out.println("Claves correctas\n");
		}
		return result;

	}
	
	/**
	 * Este método es utilizado para firmar un mensaje.
	 * 
	 */
	public void doSign() {
		if (keys.haveKeys()) {
			try {
				// algorithm = readAlgorithm();
				input = readMsgToEncrypt().getBytes();
				operations.prepareSignature(algorithm2);
				operations.initSign(keys.getPrivate());
				byte signature[] = operations.signData(input);

				System.out.println("input    : " + Hex.toHexString(input));
				System.out.println("signature  : " + Hex.toHexString(signature));

				System.out.println("Verify signature with KU: ");
				operations.initVerify(keys.getPublic());
				boolean signVerify = operations.VerifyData(input, signature);
				if (signVerify)
					System.out.println("Signature verified.");
				else
					System.out.println("Signature failed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No hay claves.");
		}
	}

	/**
	 * Este método es utilizado para firmar un fichero. Version datos y firma integrados.
	 * 
	 * @param fileToSign archivo a firmar
	 * @param algorithm algoritmo elegido para firmar
	 */
	public boolean doSignFileWithData(File fileToSign, String algorithm) {
		boolean result = false;
		if (keys.haveKeys()) {
			result = true;
			try {

				operations.prepareSignature(algorithm);
				operations.initSign(keys.getPrivate());
				byte signature[];

				FileInputStream fis = new FileInputStream(fileToSign.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(fileToSign.getAbsolutePath()+".sigdat");
				
				byte[] ibuf;

				ibuf = fis.readAllBytes();
				signature = operations.signData(ibuf);				
				
				Header hdr = new Header(Options.OP_SIGNED,"none",algorithm,signature);
				hdr.save(fos);
				
				fos.write(ibuf);
				
				fis.close();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = false;
			System.out.println("No hay claves.");
		}
		return result;
	}
	
	/**
	 * [DEPRECATED]
	 * Este método es utilizado para firmar un fichero. Version datos y firma separados.
	 * 
	 */
	public void doSignFileWithoutData() {
		if (keys.haveKeys()) {
			try {

				operations.prepareSignature(algorithm2);
				operations.initSign(keys.getPrivate());
				byte signature[];

				FileInputStream fis = new FileInputStream("hola.txt");
				FileOutputStream fos = new FileOutputStream("hola.txt.sig");
				
				
				byte[] ibuf;

				ibuf = fis.readAllBytes();
				signature = operations.signData(ibuf);	
				Header hdr = new Header(Options.OP_SIGNED,"none",algorithm2,signature);
				hdr.save(fos);
				
				fis.close();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No hay claves.");
		}
	}
	
	/**
	 * [DEPRECATED]
	 * Este método es utilizado para autenticar un fichero firmado. Version datos y firma separados.
	 * 
	 */
	public void doVerifyFileWithoutData() {
		if (keys.haveKeys()) {
			try {
				
				byte signature[];
				byte[] ibuf;
				
				FileInputStream fis1 = new FileInputStream("hola.txt.sig");
				FileInputStream fis2 = new FileInputStream("hola.txt");
				
				
				Header hdr1 = new Header();
				hdr1.load(fis1);
				signature = hdr1.getData();
				operations.prepareSignature(hdr1.getAlgorithm2());
				operations.initVerify(keys.getPublic());
				ibuf = fis2.readAllBytes();
				
				if(operations.VerifyData(ibuf, signature)) {
					System.out.println("Se ha verificado el archivo correctamente");
				}else {
					System.out.println("Error de verificación");
				}
				fis1.close();
				fis2.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No hay claves.");
		}
	}
	
	/**
	 * Este método es utilizado para autenticar un fichero firmado. Version datos y firma integrados.
	 * 
	 * @param fileToVerify fichero elegido para verificar y descomprimir.
	 * 
	 */
	public int doVerifyFileWithData(File fileToVerify) {
		int result = 0;
		if (keys.haveKeys()) {
			result=1;
			try {
				
				byte signature[];
				byte[] ibuf;
				
				FileInputStream fis = new FileInputStream(fileToVerify.getAbsolutePath());
				String outFile = fileToVerify.getAbsolutePath().replace(".sigdat", "");
				FileOutputStream fos = new FileOutputStream(outFile);
				
				Header hdr1 = new Header();
				hdr1.load(fis);
				signature = hdr1.getData();
				operations.prepareSignature(hdr1.getAlgorithm2());
				operations.initVerify(keys.getPublic());
				ibuf = fis.readAllBytes();
				
				
				if(operations.VerifyData(ibuf, signature)) {
					fos.write(ibuf);
					result =1 ;
					System.out.println("Se ha verificado y descomprimido el archivo correctamente");
				}else {
					result = -1;
					System.out.println("Error de verificación");
					
				}
				fis.close();
				fos.close();
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = -1;
			System.out.println("No hay claves.");
		}
		return result;
	}
	
	/**
	 * [DEPRECATED]
	 * Este método es utilizado para generar un par de firmas.
	 * 
	 * @throws Exception
	 */
	public void createPairKeys() throws Exception {
		do {
			System.out.println();
			System.out.println("Generación de un par de claves RSA de 512 bits:");
			keys.generateKeyPair();
			if (keys.haveKeys()) {
				System.out.print("Claves generadas correctamente.");
				return;
			} else {
				System.out.print("Error en la generación de claves.");
				System.out.print("¿Cancelar? (s/n)");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int c = br.read();
				if ((c == 's') || (c == 'S')) {
					return;
				}
			}
		} while (true);
	}

	/**
	 * [DEPRECATED]
	 * Este método es utilizado para ingresar un dato por teclado
	 * 
	 * @throws Exception
	 */
	public String readMsgToEncrypt() throws Exception {
		do {
			System.out.print("Mensaje a cifrar:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String msg = br.readLine();
			if (msg != null)
				return msg;
		} while (true);
	}

	/**
	 * [DEPRECATED]
	 * Este método es utilizado para ingresar un dato por teclado
	 * 
	 * @throws Exception
	 */
	public String readMsgToDecrypt() throws Exception {
		do {
			System.out.print("Mensaje a descifrar (en Hexadecimal, sin espacios, ejemplo 0a56340f):");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String msg = br.readLine();
			if (msg != null)
				return msg;
		} while (true);
	}

}
