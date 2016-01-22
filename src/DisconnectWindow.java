import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class DisconnectWindow extends JFrame {

	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	public ObjectOutputStream output = null;
	public String partnerIP = "";

	public DisconnectWindow(String partnerIP, ObjectOutputStream output) {
		this.output = output;
		this.partnerIP = partnerIP;
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 222, 134);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnRozcz = new JButton("ROZ£¥CZ");
		contentPane.add(btnRozcz, BorderLayout.CENTER);
		DisconnectWindow dw = this;
		btnRozcz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.sendBye(partnerIP, output);
				dw.dispose();
				
			}
		});
		
		this.setVisible(true);
	}

}
