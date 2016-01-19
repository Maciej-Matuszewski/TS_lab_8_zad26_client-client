import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;


public class Main {

	private Window window;
	
	private ServerSocket server;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		Main main = new Main();
		
		main.window = new Window(main);
		main.window.frame.setVisible(true);
		try {
			main.initializeServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeServer() throws IOException{
		window.print("Inicjacja servera...");
		try{
			server = new ServerSocket(9666, 10);
			window.print("Oczekiwanie na klienta...");
	        Socket client = server.accept();
	        window.print("Otrzymano połączenie przychodzące od "+ client.getInetAddress().getHostName());
	        
	        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            Boolean bye = false;
            while(!bye){
            	String info = (String)input.readObject();
            	window.print("OTRZYMANO:\t" + info);
            	String message = info;
            	bye = responseHendler(true, message, output, input);
            }
	        
		}catch (BindException e){
			window.print("BŁĄD: Podany port jest już wykorzystywany!");
		} catch (ClassNotFoundException e) {
			window.print("BŁĄD: " + e.getMessage());
		}
	}
	
	void calling(String ip){
		window.print("Dzwonienie pod adres: " + ip);
		Socket client = null;
		try {
			client = new Socket(ip, 9666);
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			sendMessage("INVITE", output);
			
			Boolean bye = false;
            while(!bye){
            	String info = (String)input.readObject();
            	window.print("OTRZYMANO:\t" + info);
            	String message = info;
            	bye = responseHendler(false, message, output, input);
            }
			
		} catch (UnknownHostException e) {
			window.print("BŁĄD: Nieprawidłowy adres IP!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void sendMessage(String message, ObjectOutputStream output)
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
	
	Boolean responseHendler(Boolean isServer, String message, ObjectOutputStream output, ObjectInputStream input){
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
			try {
				if(JOptionPane.showConfirmDialog(null, "Czy chcesz odebraæ po³¹czenie od "+(String)input.readObject()+"?", "", 0) == 0) sendMessage("200 OK", output);
				else sendMessage("486 Busy Here", output);
			} catch (HeadlessException | ClassNotFoundException | IOException e) {
				window.print("BŁĄD: "+e.getMessage());
			}
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
