import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

	String ip;
	Window window;
	
	public Client(String ip, Window window) {
		this.ip = ip;
		this.window = window;
	}
	
	@Override
	public void run() {
		window.print("Dzwonienie pod adres: " + ip);
        window.inputSetEneble(false);
		Socket client = null;
		try {
			client = new Socket(ip, 9666);
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			Main.sendMessage(Main.generateRequest("INVITE", ip, ""), output);
			
			Boolean bye = false;
            while(!bye){
            	String message = (String)input.readObject();
            	window.print("\nOTRZYMANO:\n" + message + "\n");
            	bye = Main.responseHendler(false, message, output, input, ip);
            }

	        window.inputSetEneble(true);
			
		} catch (UnknownHostException e) {
			window.print("B��D: Nieprawid�owy adres IP!");
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

}
