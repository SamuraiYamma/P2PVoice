import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Target;

public class AudioCapture {
    private TargetDataLine mic;
    private DataLine.Info info;
    private AudioFormat format;
    private ByteArrayOutputStream out;
    private SourceDataLine speakers;

    public AudioCapture(/* TODO: uncomment //ByteArrayOutputStream out */) {
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);

        try {
            mic = (TargetDataLine) AudioSystem.getLine(info);
            mic.open(format);

            /* TODO: This block is for testing */
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);

        } catch(LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void readAudio() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int bRead;
        byte[] data = new byte[mic.getBufferSize()/5];

        mic.start();

        /* TODO: This block is for testing */
        speakers.start();

        while(true) {
            bRead = mic.read(data, 0, data.length);

            out.write(data, 0, bRead);
            speakers.write(data, 0, bRead);
        }
    }

    public static void main(String[] args) throws Exception {
        AudioCapture ac = new AudioCapture();

        ac.readAudio();
    }
}
