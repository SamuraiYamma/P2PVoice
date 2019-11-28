import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.*;

public class P2PVoiceServer {
	public static int locSoc;
	
	public static void main(String[] args){
		try{
			if (args.length != 1)
				locSoc = 10501;
			else
				locSoc = Integer.parseInt(args[0]);
			ServerSocket ss = new ServerSocket(locSoc);
			ServerSocket listenter = ss;
			ExecutorService threadPool = Executors.newFixedThreadPool(15);
			while(true){
				threadPool.execute(new ClientHandler(listener.accept()));
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	private static class ClientHandler implements Runnable{
		private Socket sock;
		private InputStream serverIn;
		private BufferedOutputStream serverOut;
	
		ClientHandler(Socket sock) throws Exception{
			this.sock = sock;
			this.serverIn = serverIn;
			this.serverOut = new BufferedOutputStream(sock.getOutputStream());
		}
		private String getMessage() throws Exception{
			byte[] msgLength = new byte[4];
			this.serverIn.read(msgLength,0,4);
			int len = ByteBuffer.wrap(msgLength).getInt();
			byte[] msg = new byte[len];
			this.serverIn.read(msg, 0, len);
			return new String(msg);
		}
		
		private void sendMessage(byte[] msg) throws Exception{
			byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();
			this.serverOut.write(msgLen, 0, 4);
			this.serverOut.write(msg, 0, msg.length);
		}
		
		@Override
		public void run(){
			String request;
			try{
				request = this.getMessage();
				byte[] rbytes = new request.getBytes();
				this.sendMessage(rbytes);	
				this.socket.close();
			} catch (Exception e){e.printStackTrace();}
		}
	}
}