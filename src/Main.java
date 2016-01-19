import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


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
            	String message = (String)input.readObject();
            	window.print("OTRZYMANO:\t"+message);
            	sendMessage("ACK", output);
            }
	        
		}catch (BindException e){
			window.print("BŁĄD: Podany port jest już wykorzystywany!");
		} catch (ClassNotFoundException e) {

			window.print("BŁĄD: " + e.getMessage());
			
		}
	}
	
	void calling(String ip){
		window.print("Dzwonienie pod adres: " + ip);
		try {
			Socket client = new Socket(ip, 9666);
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			sendMessage("INVITE", output);
		} catch (UnknownHostException e) {
			window.print("BŁĄD: Nieprawidłowy adres IP!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void sendMessage(String message, ObjectOutputStream out)
    {
        try{
            out.writeObject(message);
            out.flush();
        	window.print("WYSŁANO:\t" + message);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
