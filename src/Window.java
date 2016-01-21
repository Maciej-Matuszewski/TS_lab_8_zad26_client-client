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

public class Window{
	
	JFrame frame;
	private JTextField adrField;
	private JLabel adrLabel;
	private JTextArea consloleField;
	private JScrollPane scrollPane;
	private JButton callBtn;

	public Window() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 505, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		callBtn = new JButton("Zadzwoñ");
		callBtn.setBounds(370, 10, 120, 24);
		frame.getContentPane().add(callBtn);
		callBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.calling(adrField.getText());
			}
		});
		
		adrField = new JTextField();
		adrField.setBounds(80, 10, 280, 25);
		frame.getContentPane().add(adrField);
		adrField.setColumns(10);
		
		adrLabel = new JLabel("Adres IP:");
		adrLabel.setBounds(10, 10, 60, 25);
		frame.getContentPane().add(adrLabel);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.DARK_GRAY);
		canvas.setBounds(10, 45, 480, 1);
		frame.getContentPane().add(canvas);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 480, 310);
		frame.getContentPane().add(scrollPane);
		
		consloleField = new JTextArea();
		consloleField.setEditable(false);
		consloleField.setText("");
		scrollPane.setViewportView(consloleField);
	}
	
	void print(String text){
		consloleField.append(text+"\n");
		scrollToBottom();
	}
	
	public void scrollToBottom(){
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
	}

	public void inputSetEneble(Boolean state){
		adrField.setEnabled(state);
		callBtn.setEnabled(state);
	}
}
