package p3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class nombreLlave extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombreDeLa;
	
	private String nombreKey = "";
	private boolean valid = false;
	
	public String getNombreKey() {
		return nombreKey;
	}
	
	public boolean getValid() {
		return valid;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			nombreLlave dialog = new nombreLlave();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public nombreLlave() {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 202);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtNombreDeLa = new JTextField();
		txtNombreDeLa.setText("Nombre de la llave");
		txtNombreDeLa.setBounds(43, 66, 225, 19);
		contentPanel.add(txtNombreDeLa);
		txtNombreDeLa.setColumns(10);
		
		JLabel lblIntroduzcaElNombre = new JLabel("Introduzca el nombre de la llave");
		lblIntroduzcaElNombre.setBounds(43, 39, 379, 15);
		contentPanel.add(lblIntroduzcaElNombre);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
						nombreKey = txtNombreDeLa.getText();
						
						if(nombreKey != null) {
							valid=true;
						}else {
							valid=false;
						}
						setVisible(false);
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						valid = false;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
