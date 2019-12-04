import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class AudioCapture {
    private TargetDataLine mic;
    private DataLine.Info info;
    private AudioFormat format;
    private Socket socket;
    private OutputStream out;

    public AudioCapture(Socket socket) {
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);

        try {
            mic = (TargetDataLine) AudioSystem.getLine(info);
            mic.open(format);


        } catch(LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void readAudio() {
        int bRead;
        byte[] data = new byte[mic.getBufferSize()/5];
        mic.start();

        //TODO: Instead of looping forever, loop only until call is ended
        // or push to talk disengaged. Again, not sure which applies here yet
        while(true) {
            bRead = mic.read(data, 0, data.length);
            try {
                this.sendAudio(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAudio(byte[] msg) throws Exception {
        byte[] msgLen = ByteBuffer.allocate(4).putInt(msg.length).array();
        this.out.write(msgLen, 0, 4);

        //Next write the message.
        this.out.write(msg, 0, msg.length);
    }

    public static void main(String[] args) throws Exception {




    }
}
