
package library.hardware;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class Scanner extends JFrame implements IScanner {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JButton btnScan;
	private IScannerListener listener;

	public Scanner() {
		this.setTitle("Scanner");
		this.setBounds(50, 250, 400, 200);
		this.setDefaultCloseOperation(3);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder((Border)null, "Scanner", 4, 2, (Font)null, (Color)null));
		panel.setBounds(10, 10, 400, 200);
		this.getContentPane().add(panel);
		panel.setLayout((LayoutManager)null);
		final JLabel lblErrorMesg = new JLabel("");
		lblErrorMesg.setForeground(Color.RED);
		lblErrorMesg.setBounds(12, 21, 358, 16);
		panel.add(lblErrorMesg);
		JLabel lblNewLabel = new JLabel("Enter Book Barcode: ");
		lblNewLabel.setFont(new Font("Tahoma", 0, 14));
		lblNewLabel.setBounds(30, 50, 150, 25);
		panel.add(lblNewLabel);
		this.textField = new JTextField();
		this.textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				lblErrorMesg.setText("");
			}
		});
		this.textField.setBounds(190, 50, 150, 25);
		panel.add(this.textField);
		this.textField.setColumns(10);
		this.textField.setEditable(false);
		this.btnScan = new JButton("Scan Book Barcode");
		this.btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Scanner.this.listener == null) {
					throw new RuntimeException("Scanner: listener is null");
				} else {
					String text = Scanner.this.textField.getText();

					try {
						int e = (new Integer(text)).intValue();
						if(e <= 0) {
							throw new NumberFormatException();
						}

						Scanner.this.listener.bookScanned(e);
					} catch (NumberFormatException var4) {
						lblErrorMesg.setText("Book barcode must be a positive intger");
					}

					Scanner.this.textField.setText("");
				}
			}
		});
		this.btnScan.setFont(new Font("Tahoma", 0, 14));
		this.btnScan.setBounds(85, 88, 215, 39);
		panel.add(this.btnScan);
	}

	public void setEnabled(boolean enabled) {
		this.btnScan.setEnabled(enabled);
		this.textField.setEditable(enabled);
	}

	public void addListener(IScannerListener listener) {
		this.listener = listener;
	}
}
