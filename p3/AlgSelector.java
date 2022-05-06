package p3;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Este es el panel de selección de algortimo de cifrado
 * @author Diego Gallardo Zancada
 *
 */
public class AlgSelector extends JDialog {

	
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	String selection;

	public String getSelection() {
		return selection;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AlgSelector dialog = new AlgSelector();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AlgSelector() {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 284, 260);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JButton btnNewButton = new JButton("SHA1withRSA"); //$NON-NLS-1$
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selection = "SHA1withRSA";
				setVisible(false);
			}
		});
		JButton btnNewButton_1 = new JButton("MD2withRSA"); //$NON-NLS-1$
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selection = "MD2withRSA";
				setVisible(false);
			}
		});
		JButton btnNewButton_2 = new JButton("MD5withRSA"); //$NON-NLS-1$
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selection = "MD5withRSA";
				setVisible(false);
			}
		});

		JLabel lblSeleccioneElAlgoritmo = new JLabel("Seleccione el algoritmo de firma"); //$NON-NLS-1$
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(21, Short.MAX_VALUE)
					.addComponent(lblSeleccioneElAlgoritmo)
					.addGap(11))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
							.addGap(33)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(25, Short.MAX_VALUE)
					.addComponent(lblSeleccioneElAlgoritmo)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addGap(18)
					.addComponent(btnNewButton_1)
					.addGap(18)
					.addComponent(btnNewButton_2)
					.addGap(58))
		);
		contentPanel.setLayout(gl_contentPanel);
	}
}
