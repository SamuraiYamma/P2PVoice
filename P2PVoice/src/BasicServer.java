
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class exists to provide stubs of data to the client,
 * and is in no way shape or form a final product
 *
 * Use this for testing. Implementing these stubs should be done
 * in another class called Server
 *
 * IP: localhost
 * PORT: 44444
 *
 * TODO: Convert these stubs into an actual server that works
 */
public class BasicServer {
    public void start() {
        int port = 44444;
        int nmThread = 0;
        //start listening for incoming connections
        try{
            ServerSocket ss = new ServerSocket(port);
            ServerSocket listener = ss;
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            while (true){
                //assign incoming connection to new thread
                //TODO: Make this more robust. We need to add functionality
                // in the event that the call is ended

                if(nmThread == 0) {
                    threadPool.execute(new ClientHandler(listener.accept()));
                    nmThread = 1;
                } else if(nmThread == 1) {
                    threadPool.execute(new RemoteHandler(listener.accept()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  class to handle connections from remote clients
     *  and play back audio received from them.
     */
    private static class RemoteHandler implements Runnable {
        private Socket socket;
        private AudioPlayback pb;
        RemoteHandler(Socket s) throws Exception {
            socket = s;
            pb = new AudioPlayback(s);
        }

        @Override
        public void run() {
            pb.playAudio();
        }
    }
    /**
     * client handler controls the thread to connect
     * to it's local client
     */
    private static class ClientHandler implements Runnable{
        private Socket socket;
        private InputStream serverIn;
        private BufferedOutputStream serverOut;
        /**
         * constructor
         * @param s
         * @throws Exception
         */
        ClientHandler(Socket s) throws Exception{
            socket = s;
            serverIn = s.getInputStream();
            serverOut = new BufferedOutputStream(s.getOutputStream());
        }

        @Override
        /**
         * Start function for new threads
         * In our case, we want to listen for calls
         */
        public void run() {
            String request;
            try {
                request = this.getMessage();
                System.out.println(request);
                this.socket.close();

                //TODO: This line is for testing only
                System.out.println(request);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         *
         * @param msg
         * @throws Exception
         */
        private void sendMessage(byte[] msg) throws Exception {
            byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();
            this.serverOut.write(msgLen, 0, 4);
            this.serverOut.write(msg, 0, msg.length);
        }

        /**
         *
         * @return
         * @throws Exception
         */
        private String getMessage() throws Exception {
            byte[] msgLength = new byte[4];
            this.serverIn.read(msgLength, 0, 4);

            int len = ByteBuffer.wrap(msgLength).getInt();
            System.out.println(len + "");
            byte[] msg = new byte[len];
            this.serverIn.read(msg, 0, len);
            return new String(msg);
        }
    }
}


