
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
    public static void main(String[] args) {
        int port = 44444;

        //start listening for incoming connections
        try{
            ServerSocket ss = new ServerSocket(port);
            ServerSocket listener = ss;
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            while (true){
                //assign incoming connection to new thread
                threadPool.execute(new ClientHandler(listener.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * client handler controls each thread
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
                byte[] fbytes = Files.readAllBytes(Paths.get(request));
                this.sendMessage(fbytes);
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
            byte[] msg = new byte[len];
            this.serverIn.read(msg, 0, len);
            return new String(msg);
        }
    }
}


