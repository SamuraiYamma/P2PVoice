import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.*;

public class P2PVoiceClient implements ActionListener {

	private JFrame frame;
	private JTextField serverIP;
	private JTextField port;
	
	private static int locSoc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 1){
			locSoc = 10500;
		}
		else{
			locSoc = Integer.parseInt(args[0]);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					P2PVoiceClient window = new P2PVoiceClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	private void connect(String serverHost,int port) {
		try {
			this.sock = new Socket(serverHost, port);
			this.clientOut = sock.getOutputStream();
			this.clientIn = sock.getInputStream();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getMessage(InputStream in) throws Exception {
    	byte[] msgLength = new byte[4];
		in.read(msgLength, 0, 4);
		int len = ByteBuffer.wrap(msgLength).getInt();
		byte[] msg = new byte[len];
		in.read(msg, 0, len);
		return new String(msg);
    }
	
	
	void sendMessage(String send, OutputStream out) throws Exception {
		byte[] msg = send.getBytes();
		byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();		
		out.write(msgLen, 0, 4);
		out.write(msg, 0, msg.length);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == this.callButton) {
			
			//Read in connections details from the GUI,
			//and then pass them to connect()
			String receiverIP = String.valueOf(this.receiverIP.getText());
			int port = Integer.parseInt(this.port.getText());
			this.connect("127.0.0.1", (locSoc + 1));
			try{
				Socket newSock = new Socket (receiverIP, port);
				BufferedInputStream InP2P = new BufferedInputStream(newSock.getInputStream());
				OutputStream OutP2P = newSock.getOutputStream();
			}catch(Exception e){e.printStackTrace()};
			
		} /* 	else if(event.getSource() == this.dscBtn) {
			try {
				this.sendMessage("disc", this.clientOut);
				this.cntBtn.setEnabled(true);
				this.dscBtn.setEnabled(false);
				this.sock.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}*/
		
	}
}