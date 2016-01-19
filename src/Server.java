import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	Window window;
	
	public Server(Window window) {
		this.window = window;
	}
	
	@Override
	public void run() {
		
		while(true){
	        window.inputSetEneble(true);
			window.print("Inicjacja servera...");
			try{
				ServerSocket server = new ServerSocket(9666, 10);
				window.print("Oczekiwanie na klienta...");
		        Socket client = server.accept();
		        window.inputSetEneble(false);
		        window.print("Otrzymano połączenie przychodzące od "+ client.getInetAddress().getHostName());
		        
		        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
	            output.flush();
	            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
	            Boolean bye = false;
	            while(!bye){
	            	String info = (String)input.readObject();
	            	window.print("OTRZYMANO:\t" + info);
	            	String message = info;
	            	bye = Main.responseHendler(true, message, output, input, client.getInetAddress().getHostAddress());
	            }
	            
	            server.close();
		        
			}catch (BindException e){
				window.print("BŁĄD: Podany port jest już wykorzystywany!");
			} catch (ClassNotFoundException e) {
				window.print("BŁĄD: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
