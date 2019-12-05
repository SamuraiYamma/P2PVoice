
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        //start listening for incoming connections
        try{
            ServerSocket ss = new ServerSocket(port);
            ServerSocket listener = ss;
            Socket local;
            ExecutorService threadPool = Executors.newFixedThreadPool(2);

            local = listener.accept();
            while (true){
                //assign incoming connection to new thread
                threadPool.execute(new RemoteHandler(listener.accept(), local, port));

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
        private Socket remote;
        private Socket local;
        private AudioPlayback pb;
        private AudioCapture ac;
        private int port;

        private Thread capThread;
        private Thread playThread;

        // These streams are for communicating with the local client.
        private OutputStream serverOut;
        private InputStream serverIn;


        RemoteHandler(Socket remote, Socket local, int port) throws Exception {
            this.remote = remote;
            this.local = local;
            this.port = port;
            serverOut = local.getOutputStream();
            serverIn = local.getInputStream();

            pb = new AudioPlayback(remote);
            ac = new AudioCapture(remote);
        }

        @Override
        public void run() {
            String peerInfo = local.getInetAddress().toString().substring(1) + ":" + port;
            String response = "";
            try {
                sendMessage(peerInfo.getBytes(), this.serverOut);
                response = getMessage();
                sendMessage(response.getBytes(), remote.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(response.equals("YES")) {
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
        }

        /**
         *
         * @param msg
         * @throws Exception
         */
        private void sendMessage(byte[] msg, OutputStream out) throws Exception {
            byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();
            out.write(msgLen, 0, 4);
            out.write(msg, 0, msg.length);
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


