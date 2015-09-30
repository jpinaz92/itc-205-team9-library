
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
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;

public class CardReader extends JFrame implements ICardReader {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JButton btnReadCard;
	private ICardReaderListener listener;

	public CardReader() {
		this.setTitle("Card Reader");
		this.setBounds(50, 50, 400, 200);
		this.setDefaultCloseOperation(3);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder((Border)null, "Card Reader", 4, 2, (Font)null, (Color)null));
		panel.setBounds(10, 10, 400, 200);
		this.getContentPane().add(panel);
		panel.setLayout((LayoutManager)null);
		final JLabel lblErrorMesg = new JLabel("");
		lblErrorMesg.setForeground(Color.RED);
		lblErrorMesg.setBounds(12, 21, 358, 16);
		panel.add(lblErrorMesg);
		JLabel lblNewLabel = new JLabel("Enter Member Id:");
		lblNewLabel.setBounds(30, 50, 150, 25);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", 0, 14));
		this.textField = new JTextField();
		this.textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				lblErrorMesg.setText("");
			}
		});
		this.textField.setBounds(190, 50, 150, 25);
		panel.add(this.textField);
		this.textField.setFont(new Font("Tahoma", 0, 14));
		this.textField.setColumns(10);
		this.textField.setEditable(false);
		this.btnReadCard = new JButton("Swipe Card");
		this.btnReadCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(CardReader.this.listener == null) {
					throw new RuntimeException("CardReader: listener is null");
				} else {
					String text = CardReader.this.textField.getText();

					try {
						int e = (new Integer(text)).intValue();
						if(e <= 0) {
							throw new NumberFormatException();
						}

						CardReader.this.listener.cardSwiped(e);
					} catch (NumberFormatException var4) {
						lblErrorMesg.setText("Member Id must be a positive intger");
					}

					CardReader.this.textField.setText("");
				}
			}
		});
		this.btnReadCard.setFont(new Font("Tahoma", 0, 14));
		this.btnReadCard.setBounds(121, 88, 150, 40);
		this.btnReadCard.setEnabled(false);
		panel.add(this.btnReadCard);
	}

	public void setEnabled(boolean enabled) {
		this.btnReadCard.setEnabled(enabled);
		this.textField.setEditable(enabled);
	}

	public void addListener(ICardReaderListener listener) {
		this.listener = listener;
	}
}
