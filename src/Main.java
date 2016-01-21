import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JOptionPane;


public class Main {

	private static Window window;
	public static String currentIP = "0.0.0.0";
	public static String callID = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		window = new Window();
		window.frame.setVisible(true);
		Thread tS = new Thread(new Server(window));
		tS.start();
		try {
			currentIP=Inet4Address.getLocalHost().getHostAddress();
			callID = randomString(6) + "-" + randomString(4) + "-" + randomString(4) + "-" + randomString(8) + "@" + currentIP; 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static final String AB = "0123456789ABCDEF";
    static Random rnd = new Random();

    static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
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
        	window.print("WYS�ANO:\t" + message);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
	
	public static String generateRequest(String method, String partnerIP, String data){
		
		String block ="Request:" + " ";
		block+=method+" ";
		block+="sip:" + partnerIP + " ";
		block+="SIP/2.0" + " ";
		block+="\n";
		block+="From:<sip:" + currentIP+"> ";
		block+="To:<sip:" + partnerIP+"> ";
		block+="Call-ID:" + callID + " ";
		block+="CSeq:1_"+method + " ";
		block+="Contact:<sip:" + currentIP+"> ";
		block+="\n";
		if(!data.equals(""))block+="\n"+data;
		
		return block;
	}
	
	public static String generateResponse(String code, String method, String partnerIP, String data){
		
		String block ="Status:" + " ";
		block+="SIP/2.0" + " ";
		block+=code + " ";
		block+="\n";
		block+="From:<sip:" + currentIP+"> ";
		block+="To:<sip:" + partnerIP+"> ";
		block+="Call-ID:" + callID + " ";
		block+="CSeq:1_"+method + " ";
		block+="Contact:<sip:" + currentIP+"> ";
		block+="\n";
		if(!data.equals(""))block+="\n"+data;
		
		return block;
	}
	
	public static Boolean responseHendler(Boolean isServer, String message, ObjectOutputStream output, ObjectInputStream input, String partnerIP){
		switch (message) {
		////////////////////////////////////////////////
		/*
		case "100 Trying":
		case "180 Ringing":
			break;
		
		case "200 OK":
			sendMessage("ACK", output);
			String sendMsg = JOptionPane.showInputDialog("Podaj tre�� wiadomo�ci:"); 
			if(sendMsg == null)sendMsg = "BYE";
			sendMessage(sendMsg, output);
			if(sendMsg.equals("BYE"))return true;
			break;
			
		case "486 Busy Here":
			sendMessage("BYE", output);
			return true;
			
		case "INVITE":
			sendMessage("100 Trying", output);
			sendMessage("180 Ringing", output);
			if(JOptionPane.showConfirmDialog(null, "Czy chcesz odebra� po��czenie od " + partnerIP + "?", "", 0) == 0) sendMessage("200 OK", output);
			else sendMessage("486 Busy Here", output);
			break;
		
		case "ACK":
			break;
			
		case "BYE":
			sendMessage("ACK", output);
			return true;
		//*/
		default:
			/*
			String sendMsg1 = JOptionPane.showInputDialog("Podaj tre�� wiadomo�ci:");
			if(sendMsg1 == null)sendMsg1 = "BYE";
			sendMessage(sendMsg1, output);
			if(sendMsg1.equals("BYE"))return true;
			//*/
			break;
		}
		
		////////////////////////////////////////////////
		return false;
	}
}
