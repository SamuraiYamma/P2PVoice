import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * class for reading audio data from socket,
 * and for playing it over speakers
 */
public class AudioPlayback {
    private Socket socket;
    private InputStream in;
    private DataLine.Info info;
    private AudioFormat format;
    private SourceDataLine speakers;

    /**
     * Constructor, sets up speaker format
     * @param socket
     */
    public AudioPlayback(Socket socket) {
        this.socket = socket;
        try {
            this.in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        info = new DataLine.Info(SourceDataLine.class, format);

        try {
            speakers = (SourceDataLine) AudioSystem.getLine(info);
            speakers.open(format);
        } catch(LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Primary function, reads audio from socket
     */
    public void playAudio() {
        byte[] data = new byte[speakers.getBufferSize()/5];
        speakers.start();

        //TODO: Instead of looping infinitely, loop until call is ended
        // or push to talk disengaged. Not sure which applies here yet
        while(true) {
            try {

             this.in.read(data, 0, data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            speakers.write(data, 0, data.length);
        }
    }
}
