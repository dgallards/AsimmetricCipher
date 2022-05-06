package p3;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import p2.Messages;

import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class BSSGuiP3 {

	private JFrame frmValidarFirma;

	Practica3 pract;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BSSGuiP3 window = new BSSGuiP3();
					window.frmValidarFirma.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BSSGuiP3() {
		initialize();
		pract = new Practica3();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmValidarFirma = new JFrame();
		frmValidarFirma.setResizable(false);
		frmValidarFirma.setTitle("BSS Entrega 3");
		frmValidarFirma.setBounds(100, 100, 598, 392);
		frmValidarFirma.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmValidarFirma.getContentPane().setLayout(null);

		JLabel lblClaveImportada = new JLabel("Clave importada:");
		lblClaveImportada.setBounds(38, 0, 483, 15);
		frmValidarFirma.getContentPane().add(lblClaveImportada);

		JButton btnNewButton = new JButton("Importar llaves");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					File selectedPubKey = null;

					File selectedPrivKey = null;

					JFileChooser fileChooser = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																								// carpeta mostrar,
																								// normalmente el
																								// directorio home del
																								// usuario
					fileChooser.setDialogTitle("Seleccione la llave pública");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Clave pública (.pubkey)", "pubkey");
					fileChooser.setFileFilter(filter);
					int result = fileChooser.showOpenDialog(fileChooser); // Lanzamos el popup
					if (result == JFileChooser.APPROVE_OPTION) {
						selectedPubKey = fileChooser.getSelectedFile();
					}

					JFileChooser fileChooser2 = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																								// carpeta mostrar,
																								// normalmente el
																								// directorio home del
																								// usuario
					fileChooser2.setDialogTitle("Seleccione la llave privada");
					FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Clave privada (.privkey)", "privkey");
					fileChooser2.setFileFilter(filter2);
					int result2 = fileChooser2.showOpenDialog(fileChooser); // Lanzamos el popup
					if (result2 == JFileChooser.APPROVE_OPTION) {
						selectedPrivKey = fileChooser2.getSelectedFile();
					}

					if (selectedPrivKey != null && selectedPubKey != null) {
						if (pract.loadKeys(selectedPubKey, selectedPrivKey)) {
							lblClaveImportada.setText("Clave importada:" + selectedPubKey.getPath());
							lblClaveImportada.updateUI();
							System.out.println("claves correctamente escogidas");
						} else {
							Frame frame = new Frame();
							JOptionPane.showMessageDialog(frame,
									Messages.getString("ERROR: claves erróneas. Elija claves válidas."));
						}
					} else {
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame,
								Messages.getString("ERROR: Tiene que elegir clave pública y privada"));

					}
				} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(38, 125, 152, 25);
		frmValidarFirma.getContentPane().add(btnNewButton);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(BSSGuiP3.class.getResource("/Resources/llave.png")));
		label.setBounds(92, 59, 51, 54);
		frmValidarFirma.getContentPane().add(label);

		JButton btnNewButton_1 = new JButton("Generar llaves");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nombreKey = null;
				nombreLlave pantallaLlave = new nombreLlave();
				pantallaLlave.setVisible(true);
				try {
					if (pantallaLlave.getValid()) {
						nombreKey = pantallaLlave.getNombreKey();
						pract.saveKeys(nombreKey);
					} else {
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame,
								Messages.getString("ERROR: Generacion de llaves errónea."));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(38, 256, 152, 25);
		frmValidarFirma.getContentPane().add(btnNewButton_1);

		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(BSSGuiP3.class.getResource("/Resources/llave2.png")));
		label_1.setBounds(86, 190, 57, 54);
		frmValidarFirma.getContentPane().add(label_1);

		JButton btnFirmarArchivo = new JButton("Firmar archivo");
		btnFirmarArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File fileToSign = null;

				JFileChooser fileChooser = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																							// carpeta mostrar,
																							// normalmente el
																							// directorio home del
																							// usuario

				int result = fileChooser.showOpenDialog(fileChooser); // Lanzamos el popup
				if (result == JFileChooser.APPROVE_OPTION) {
					fileToSign = fileChooser.getSelectedFile();
					
					p3.AlgSelector algSelector = new p3.AlgSelector(); 							
					algSelector.setVisible(true); 
					String selection = algSelector.getSelection(); 
					if (!pract.doSignFileWithData(fileToSign,selection)) {
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame, Messages
								.getString("ERROR: No hay llaves elegidas. Ha de elegir un par de claves válidas"));
					}
				}
			}
		});
		btnFirmarArchivo.setBounds(226, 326, 143, 25);
		frmValidarFirma.getContentPane().add(btnFirmarArchivo);

		JButton btnNewButton_2 = new JButton("Validar firma");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File fileToVerify = null;

				JFileChooser fileChooser = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																							// carpeta mostrar,
																							// normalmente el
																							// directorio home del
																							// usuario
				FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Archivo firmado (.sigdat)", "sigdat");
				fileChooser.setFileFilter(filter2);
				int result = fileChooser.showOpenDialog(fileChooser); // Lanzamos el popup
				if (result == JFileChooser.APPROVE_OPTION) {
					fileToVerify = fileChooser.getSelectedFile();
					int ret = pract.doVerifyFileWithData(fileToVerify);
					if (ret == 0) {

						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame, Messages
								.getString("ERROR: No hay llaves elegidas. Ha de elegir un par de claves válidas"));
					} else if (ret == 1) {

						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame,
								Messages.getString("Se ha verificado y descomprimido el archivo correctamente"));
					} else {
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame,
								Messages.getString("Error en la verificación de la firma"));
					}

				}
			}
		});
		btnNewButton_2.setBounds(391, 326, 152, 25);
		frmValidarFirma.getContentPane().add(btnNewButton_2);

		JButton btnCifradoRsa = new JButton("Cifrado RSA");
		btnCifradoRsa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File fileToCiph = null;

				JFileChooser fileChooser = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																							// carpeta mostrar,
																							// normalmente el
																							// directorio home del
																							// usuario

				int result = fileChooser.showOpenDialog(fileChooser); // Lanzamos el popup
				if (result == JFileChooser.APPROVE_OPTION) {
					fileToCiph = fileChooser.getSelectedFile();
					if (!pract.doEncryptFile(fileToCiph)) {
						System.out.println("algo mal hay");
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame, Messages
								.getString("ERROR: No hay llaves elegidas. Ha de elegir un par de claves válidas"));
					}
				}
			}
		});
		btnCifradoRsa.setBounds(226, 172, 143, 25);
		frmValidarFirma.getContentPane().add(btnCifradoRsa);

		JButton btnDescifradoRsa = new JButton("Descifrado RSA");
		btnDescifradoRsa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File fileToCiph = null;

				JFileChooser fileChooser = new JFileChooser(); // Aquí creamos un popup para elegir el archivo
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));// Aquí se le dice cual
																							// carpeta mostrar,
																							// normalmente el
																							// directorio home del
																							// usuario
				FileNameExtensionFilter filter = new FileNameExtensionFilter("archivo cifrado (.cif)", "cif");
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showOpenDialog(fileChooser); // Lanzamos el popup
				if (result == JFileChooser.APPROVE_OPTION) {
					fileToCiph = fileChooser.getSelectedFile();
					if (!pract.doDecryptFile(fileToCiph)) {
						System.out.println("algo mal hay");
						Frame frame = new Frame();
						JOptionPane.showMessageDialog(frame, Messages
								.getString("ERROR: No hay llaves elegidas. Ha de elegir un par de claves válidas"));
					}
				}
			}
		});
		btnDescifradoRsa.setBounds(391, 172, 152, 25);
		frmValidarFirma.getContentPane().add(btnDescifradoRsa);

		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(BSSGuiP3.class.getResource("/Resources/sign.png")));
		label_2.setBounds(271, 246, 70, 68);
		frmValidarFirma.getContentPane().add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon(BSSGuiP3.class.getResource("/Resources/validate.png")));
		label_3.setBounds(436, 246, 64, 68);
		frmValidarFirma.getContentPane().add(label_3);

		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(BSSGuiP3.class.getResource("/Resources/love-locks-clipart-16.jpg")));
		label_4.setBounds(271, 23, 250, 137);
		frmValidarFirma.getContentPane().add(label_4);
	}
}
