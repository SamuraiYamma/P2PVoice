import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * Class for capturing audio from microphone,
 * and for sending the audio over a socket
 */
public class AudioCapture {
    private TargetDataLine mic;
    private DataLine.Info info;
    private AudioFormat format;
    private Socket socket;
    private OutputStream out;

    /**
     *  Constructor, sets up audio format to read from mic
     * @param socket
     */
    public AudioCapture(Socket socket) {
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        format = new AudioFormat(10000.0f, 8, 1, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);

        try {
            mic = (TargetDataLine) AudioSystem.getLine(info);
            mic.open(format);


        } catch(LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    /**
     * Primary function, reads audio from microphone and
     * sends it to the socket
     */
    public void readAudio() {

        byte[] data = new byte[mic.getBufferSize() * 2 / 10];
        int bRead;
        mic.start();

        //TODO: Instead of looping forever, loop only until call is ended
        // or push to talk disengaged. Again, not sure which applies here yet
        while(true) {
            mic.read(data, 0, data.length);
            try {
                this.out.write(data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}