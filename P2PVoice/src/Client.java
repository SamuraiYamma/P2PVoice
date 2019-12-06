import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {

    //class stuff
    private String sysIP;
    private int port;
    private MainFrame mainFrame;

    //socket stuff
    private InputStream clientIn;
    private OutputStream clientOut;
    private Socket socket;

    //connection info
    private String peerIP;
    private int peerPort;

    //audio capture
    private AudioCapture ac;
    private AudioPlayback pb;
    private Thread capThread;
    private Thread playThread;

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

            socket = new Socket("localhost", port);
            clientIn = socket.getInputStream();
            clientOut = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mainFrame.setMode(Status.ONLINE);

        waitForConnect();
    }

    /**
     *
     * @param message
     */
    public void receive(String message){
        mainFrame.setMode(Status.RECEIVING);
        String[] info = message.split(":");

        peerIP = info[0];
        peerPort = Integer.parseInt(info[1]);

    }

    /**
     * accept an incoming connect, begin next phase
     */
    public void accept()  {
        //TODO: Remove these lines when in production
        System.out.println("Accepting call...");
        try {
            sendMessage("YES", clientOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame.setMode(Status.CONNECTED);
        receiveCall(peerIP, peerPort);


    }

    /**
     * deny an incoming connection.
     * sends this response to local server
     * reset system to online
     */
    public void deny(){
        //TODO: Remove these lines when in production
        System.out.println("Denying call...");
        try {
            sendMessage("NO", clientOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame.setMode(Status.ONLINE);
        waitForConnect();
    }

    /**
     * Terminate a call
     */
    public void end(){
        //TODO: fill this method with useful stuff
    }

    public String getPeerIP(){
        return peerIP;
    }

    /**
     * call another server with these credentials
     *
     * wait for a response.
     * if yes, then continue with making a call
     * if no, reset stats
     *
     * @param peerIP the ip to connect to
     * @param peerPort the port on that ip to connect to
     */
    public void makeCall(String peerIP, int peerPort){
        //TODO: Add actual call functionality
        Socket remote = null;
        try {

            //THIS CONNECTION IS THE TRIGGER
            remote = new Socket(peerIP, peerPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //WAIT FOR A RESPONSE FROM REMOTE
        try {
            OutputStream remoteOut = remote.getOutputStream();
            InputStream remoteIn = remote.getInputStream();

            //THIS STRING IS THE RESPONSE FROM THE TRIGGER
            String response = "";
            try {
                response = getMessage(remoteIn);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(response.equals("YES")){
                mainFrame.setMode(Status.CONNECTED);
                this.ac = new AudioCapture(remote);
                this.pb = new AudioPlayback(remote);

                capThread = new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                        ac.readAudio();
                    }
                });

                playThread = new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                        pb.playAudio();
                    }
                });
                capThread.start();
                playThread.start();
            }
            else if(response.equals("NO")){
                //TODO: Remove in production
                System.out.println("Tried to make a call, they denied us");

                mainFrame.setMode(Status.ONLINE);
                waitForConnect();
            }
            else {
                //TODO: Remove in production
                System.out.println("Not sure what the client response was");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveCall(String peerIP, int peerPort){

    }


    /**
     * waits for incoming connections
     */
    private void waitForConnect(){
        //while we are in online mode, wait for connection
        while(mainFrame.getMode().equals(Status.ONLINE)) {
            try {
                String connect = getMessage(clientIn);
                receive(connect); //TODO: sketchy as hell

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
        String info = String.format("%s:%d",sysIP, port);
        try {
            sendMessage(info, clientOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
