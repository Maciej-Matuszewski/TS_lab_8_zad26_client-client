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
			callID = randomString(6, "0123456789ABCDEF") + "-" + randomString(4,"0123456789ABCDEF") + "-" + randomString(4,"0123456789ABCDEF") + "-" + randomString(8, "0123456789ABCDEF") + "@" + currentIP; 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    static Random rnd = new Random();

    static String randomString( int len, String ab ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( ab.charAt( rnd.nextInt(ab.length()) ) );
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
		block+="From:<sip:" + currentIP+">\n";
		block+="To:<sip:" + partnerIP+">\n";
		block+="Call-ID:" + callID + "\n";
		block+="CSeq:1_"+method + "\n";
		block+="Contact:<sip:" + currentIP+">\n";
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
	
	public static String generateRtp(){
		switch(rnd.nextInt(7)){
		case 0: return "0";
		case 1: return "4";
		case 2: return "8";
		case 3: return "9";
		case 4: return "13";
		case 5: return "15";
		case 6: return "18";
		case 7: return "2";
		default: return "-1";
		}
	}
	
	public static String generateData(String partnerIP){
		String Data="";
		Data+="Session Description Protocol Version (v): 0\n";
		Data+="Session Id (o): " + randomString(9, "123456789") + "IN IP4 " + currentIP + "\n";
		Data+="Session Name (s): SIPPER for " + partnerIP + "\n";
		Data+="Time Description (t): 0 0\n";
		Data+="Media Description (m): audio " + (rnd.nextInt(200)+5005) + " RTP/AVP " + generateRtp() + "\n";
		
		return Data;
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
