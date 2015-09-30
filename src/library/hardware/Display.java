package library.hardware;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import library.interfaces.hardware.IDisplay;

public class Display extends JFrame implements IDisplay {
	private static final long serialVersionUID = 1L;
	private JPanel current;

	public Display() {
		this.setTitle("Display");
		this.setBounds(500, 50, 470, 680);
		this.setDefaultCloseOperation(3);
		this.getContentPane().setLayout(new CardLayout(0, 0));
	}

	public JPanel getDisplay() {
		return this.current;
	}

	public void setDisplay(JPanel panel, String id) {
		this.getContentPane().add(panel, id);
		CardLayout cardLayout = (CardLayout)this.getContentPane().getLayout();
		cardLayout.show(this.getContentPane(), id);
		if(this.current != null) {
			System.out.println("Display.setDisplay: removing current");
			cardLayout.removeLayoutComponent(this.current);
		}

		this.current = panel;
	}
}

