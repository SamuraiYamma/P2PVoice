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
	
	//
}