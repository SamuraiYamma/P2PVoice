import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JLabel callPanel;//pretty sure we don't need this
    private JPanel statusPanel;


    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("P2PVoice");
        frame.setMinimumSize(new Dimension(400, 150));
        frame.setPreferredSize(new Dimension(400, 150));
        frame.setVisible(true);

        //relocates it to the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2,
                dim.height/2-frame.getSize().height/2);
    }

    public MainFrame() {
        //set layout
        setLayout(new FlowLayout());

        //make status panel variable
        statusPanel = new StatusPanel(Status.OFFLINE);

        //add panels to mainframe
        add(new CallPanel(statusPanel));
        add(statusPanel);
    }
}
