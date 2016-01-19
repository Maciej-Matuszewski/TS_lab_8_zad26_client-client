import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;


public class Main {

	private static Window window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		window = new Window();
		window.frame.setVisible(true);
		Thread tS = new Thread(new Server(window));
		tS.start();
		
	}
	
	public static void calling(String ip){
		Thread tC = new Thread(new Client(ip,window));
		tC.start();
	}
	
	public static void sendMessage(String message, ObjectOutputStream output)
    {
        try{
            output.writeObject(message);
            output.flush();
        	window.print("WYSŁANO:\t" + message);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
	
	public static Boolean responseHendler(Boolean isServer, String message, ObjectOutputStream output, ObjectInputStream input, String partnerIP){
		switch (message) {
		////////////////////////////////////////////////
		
		case "100 Trying":
		case "180 Ringing":
			break;
		
		case "200 OK":
			sendMessage("ACK", output);
			sendMessage(JOptionPane.showInputDialog("Podaj treść wiadomości:"), output);
			break;
			
		case "486 Busy Here":
			sendMessage("BYE", output);
			return true;
			
		case "INVITE":
			sendMessage("100 Trying", output);
			sendMessage("180 Ringing", output);
			if(JOptionPane.showConfirmDialog(null, "Czy chcesz odebraæ po³¹czenie od " + partnerIP + "?", "", 0) == 0) sendMessage("200 OK", output);
			else sendMessage("486 Busy Here", output);
			break;
		
		case "ACK":
			break;
			
		case "BYE":
			sendMessage("200 OK", output);
			return true;

		default:
			sendMessage(JOptionPane.showInputDialog("Podaj treść wiadomości:"), output);
			break;
		}
		
		////////////////////////////////////////////////
		return false;
	}
}
