import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Window {
	
	Main main; 
	JFrame frame;
	private JTextField adrField;
	private JLabel adrLabel;
	private JTextArea consloleField;
	private JScrollPane scrollPane;

	public Window(Main main) {
		this.main = main;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 452, 301);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton callBtn = new JButton("Zadzwoń");
		callBtn.setBounds(329, 6, 117, 29);
		frame.getContentPane().add(callBtn);
		callBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.calling(adrField.getText());
			}
		});
		
		adrField = new JTextField();
		adrField.setBounds(79, 6, 238, 26);
		frame.getContentPane().add(adrField);
		adrField.setColumns(10);
		
		adrLabel = new JLabel("Adres IP:");
		adrLabel.setBounds(6, 11, 61, 16);
		frame.getContentPane().add(adrLabel);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.DARK_GRAY);
		canvas.setBounds(6, 41, 440, 1);
		frame.getContentPane().add(canvas);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 47, 440, 226);
		frame.getContentPane().add(scrollPane);
		
		consloleField = new JTextArea();
		consloleField.setEditable(false);
		consloleField.setText("");
		consloleField.setWrapStyleWord(true);
		consloleField.setLineWrap(true);
		scrollPane.setViewportView(consloleField);
	}
	
	void print(String text){
		consloleField.setText(consloleField.getText()+text+"\n");
		scrollToBottom();
	}
	
	public void scrollToBottom(){
		try {
		    Thread.sleep(50);
		} catch(InterruptedException ex) {
		}
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
	}

}
