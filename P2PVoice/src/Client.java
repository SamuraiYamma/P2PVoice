import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {

    //class stuff
//    private String locIP; //im not sure we need this
    private String sysIP;
    private int port;
    private MainFrame mainFrame;

    //socket stuff
    private InputStream clientIn;
    private OutputStream clientOut;
    private Socket socket;

    /**
     * constructor
     * inits a lot of local variables that the gui may ask for
     * @param p
     */
    public Client(int p, MainFrame mF){
        port = p;
        sysIP = myIP();
        mainFrame = mF;
    }

    //GETTERS
    public String getSysIP(){
        return sysIP;
    }

    public int getPort(){
        return port;
    }

    //METHODS
    public void runClient(){
        //TODO: get the client connected to all necessary processes
        /*
        We send the server our IP and PORT info.
         */
        try {
            //TODO: we eventually want localhost here to be sysIP
            socket = new Socket("localhost", port);
            clientIn = socket.getInputStream();
            //clientOut = new BufferedOutputStream(socket.getOutputStream());
            clientOut = socket.getOutputStream();
            //sendMyInfo();




        } catch (IOException e) {
            e.printStackTrace();
        }


        mainFrame.setMode(Status.ONLINE);

        while(true) {
            try {
                String connect = getMessage(clientIn);
            } catch(Exception e){};
        }
    }

    //HELPERS
    /**
     * converts a string address to an InetAddress type variable
     * @param address string representation of a network or IP
     * @return an InetAddress IP
     * @throws UnknownHostException
     */
    private InetAddress convertToIP(String address) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(address);
        return ip;
    }

    /**
     * check if an integer that represents a port is good
     * @param port some integer
     * @return a null integer
     */
    private int validatePort(String port){
        int p;
        try {
            p = Integer.parseInt(port);
            if (p < 1024 || p > 65535){
                System.out.println("Not a valid port\n" +
                        "Closing client...\n");
                System.exit(1);
            }
            else{
                return p;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(null);
    }

    /**
     * prints your system(NAT) and public IP to the console
     * @return a string of your public ip
     */
    private String myIP(){
        // Returns the instance of InetAddress containing
        // local host name and address
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("System IP Address : " +
                (localhost.getHostAddress()).trim());

        // Find public IP address
        String sysIP = "";
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            sysIP = sc.readLine().trim();
        }
        catch (Exception e)
        {
            sysIP = "Cannot Execute Properly";
        }
        System.out.println("Public IP Address: " + sysIP +"\n");

        return sysIP;
    }

    /**
     *
     * @param send
     * @param out
     * @throws Exception
     */
    void sendMessage(String send, OutputStream out) throws Exception {
        byte[] msg = send.getBytes();
        byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();
        out.write(msgLen, 0, 4);
        out.write(msg, 0, msg.length);
    }

    /**
     *
     * @param in
     * @return
     * @throws Exception
     */
    private String getMessage(InputStream in) throws Exception {
        byte[] msgLength = new byte[4];
        in.read(msgLength, 0, 4);
        int len = ByteBuffer.wrap(msgLength).getInt();
        byte[] msg = new byte[len];
        in.read(msg, 0, len);
        return new String(msg);
    }

    /**
     * Sends my info in the output stream
     */
    private void sendMyInfo(){
        String info = String.format("%s,%d",sysIP, port);
        try {
            sendMessage(info, clientOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
