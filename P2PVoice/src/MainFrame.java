import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * Class for the GUI
 */
public class MainFrame extends JFrame {
    private JPanel callPanel, statusPanel;

    private JTextField ipField;
    private JTextField portField;
    private JButton callButton;

    private JLabel statusLabel;
    private JLabel fromIPLabel;
    private JButton acceptButton;
    private JButton denyButton;
    private JButton endButton;
    private JLabel timerLabel;
    private JLabel userIPLabel;
    private JLabel userPortLabel;

    private Client client;
    private int port;
    private Status mode;

    /**
     * starts us off
     */
    public void start() {
        JFrame frame = this;
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

    /**
     * constructor
     */
    public MainFrame() {
        port = Integer.parseInt(JOptionPane.showInputDialog("Which port would you like to run on?"));
        //client = new Client(port, this);

        init();
        setMode(Status.OFFLINE); //default to offline to begin with

        //now that everything has instantiated, run client
        //client.runClient();
    }

    public int getPort() {
        return this.port;
    }

    public void setClient(Client c) {
        this.client = c;
    }
    /**
     * inits all gui elements
     */
    private void init(){
        callPanel = new JPanel();
        statusPanel = new JPanel();

        ipField = new JTextField(10);
        portField = new JTextField(10);
        callButton = new JButton("Call");
        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port: ");

        //set layout
        callPanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = new GridBagConstraints();

        loc.gridx = 0;
        loc.gridy = 0;

        //ip label and field
        loc.insets = new Insets(5, 5, 5, 5);
        callPanel.add(ipLabel, loc);
        loc.gridx++;//go to next column
        callPanel.add(ipField, loc);

        //port label and field
        loc.gridx--;//go back one column
        loc.gridy++;//go to next row
        callPanel.add(portLabel, loc);
        loc.gridx++;
        callPanel.add(portField, loc);

        //call button;
        loc.gridy++;
        loc.gridx--;
        loc.gridwidth = 2;
        loc.fill= GridBagConstraints.HORIZONTAL;
        callPanel.add(callButton, loc);

        //add action listeners
        callButton.addActionListener(new CallPanelListener());

        //init variables
        statusLabel = new JLabel("Status: ");
        fromIPLabel = new JLabel("From: ");
        acceptButton = new JButton("Accept");
        denyButton = new JButton("Deny");
        endButton = new JButton("End");
        timerLabel = new JLabel("00:00");
        userIPLabel = new JLabel("My IP:");
        userPortLabel = new JLabel("My Port:");

        //set layout
        statusPanel.setLayout(new GridBagLayout());
        loc = new GridBagConstraints();

        loc.insets = new Insets(5, 5, 5, 5);
        loc.gridx = 0;
        loc.gridy = 0;

        //status section
        statusPanel.add(statusLabel, loc);
        loc.gridy++;
        statusPanel.add(fromIPLabel, loc);
        loc.gridy++;

        //subpanel buttons and timer section
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridBagLayout());
        GridBagConstraints subLoc = new GridBagConstraints();
        subLoc.gridx=0;
        subLoc.gridy=0;

        subLoc.fill = GridBagConstraints.HORIZONTAL;
        subPanel.add(acceptButton, subLoc);
        subLoc.gridx++;
        subLoc.fill = GridBagConstraints.HORIZONTAL;
        subPanel.add(denyButton, subLoc);
        subLoc.gridy++;
        subLoc.gridx--;
        subLoc.fill = GridBagConstraints.HORIZONTAL;
        subLoc.gridwidth = 2;
        subPanel.add(endButton, subLoc);
        subLoc.gridy++;
        subLoc.fill = GridBagConstraints.HORIZONTAL;
        //TODO: center the timer on the screen and make the font larger
        subPanel.add(timerLabel, subLoc);
        subLoc.gridy++;

        //subpanel added
        statusPanel.add(subPanel, loc);
        loc.gridy++;

        //user info section
        statusPanel.add(userIPLabel, loc);
        loc.gridy++;
        statusPanel.add(userPortLabel, loc);

        //listeners
        acceptButton.addActionListener(new StatusPanelListener());
        denyButton.addActionListener(new StatusPanelListener());
        endButton.addActionListener(new StatusPanelListener());

        //adds the previous panels to this main panel
        setLayout(new FlowLayout());
        add(callPanel);
        add(statusPanel);
    }


    /**
     * listener for all buttons on call panel
     */
    class CallPanelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == callButton){
                String ip = ipField.getText();
                int port = Integer.parseInt(portField.getText());

                //TODO: validate these inputs
                //TODO: connect to client/server and attempt call
                //change statusPanel to reflect new statuses

                //TODO: development only. delete this when moving to production
                System.out.printf("Address: %s:%d\n", ip, port);
                client.makeCall(ip, port);
            }
        }
    }

    /**
     * listener for all buttons on status panel
     */
    class StatusPanelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == acceptButton){
                client.accept();
            }
            else if(actionEvent.getSource() == denyButton){
                client.deny();
            }
            else if(actionEvent.getSource() == endButton){
                client.end();
            }
        }
    }

    /**
     * set the program to different modes
     * OFFLINE
     * ONLINE
     * CALLING
     * RECEIVING
     * CONNECTED
     * @param status enum of type Status
     */
    public void setMode(Status status){
        mode = status;

        switch (status) {
            case OFFLINE:
                offlineMode();
                break;
            case ONLINE:
                onlineMode();
                break;
            case CALLING:
                callingMode();
                break;
            case RECEIVING:
                receivingMode();
                break;
            case CONNECTED:
                connectedMode();
                break;
        }
    }

    public Status getMode(){
        return mode;
    }

    /**
     * All functionality is disabled until user is connected to their own server instance
     * and is connected to the internet. This is the default state.
     */
    private void offlineMode(){
        //call button
        callButton.setEnabled(false);

        //status and info
        statusLabel.setText("Status: Offline");
        fromIPLabel.setText("From: N/A");

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(false);
        timerLabel.setText("00:00");

        //user info
        //userIPLabel.setText("My IP: " + ipStub);
        userPortLabel.setText("My Port: " + port);

        //TODO: Implement some way of getting to online mode/connected mode. May not happen in this class
    }

    /**
     * All functionality besides ending a call is enabled
     */
    private void onlineMode(){
        //call button
        callButton.setEnabled(true);

        //status and info
        statusLabel.setText("Status: Online");
        fromIPLabel.setText("From: N/A");

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(false);
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + client.getSysIP());
        userPortLabel.setText("My Port: " + port);
    }

    /**
     * Functionality is disabled besides accepting or denying calls,
     * with a 30 second countdown timer
     */
    private void receivingMode(){
        //call button
        callButton.setEnabled(true);

        //status and info
        statusLabel.setText("Status: Receiving Call");
        fromIPLabel.setText("From: " + client.getPeerIP());

        //buttons and timer
        acceptButton.setEnabled(true);
        denyButton.setEnabled(true);
        endButton.setEnabled(false);
        //TODO: connect backend to enable countdown timer display
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + client.getSysIP());
        userPortLabel.setText("My Port: " + port);
    }

    /**
     * Functionality disabled besides the end button
     * Timer increments until it reaches a max of 30s
     */
    private void callingMode(){
        //call button
        callButton.setEnabled(false);

        //status and info
        statusLabel.setText("Status: Calling");
        //TODO: we need to ensure that peerIP in Client.java at this point the correct target, and not just a past
        fromIPLabel.setText("Target: " + client.getPeerIP());

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(true);

        //TODO: implement backend to limit timer to 30s
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + client.getSysIP());
        userPortLabel.setText("My Port: " + port);
    }

    /**
     * Only functionality is a continuous timer and the ability to end call
     */
    private void connectedMode(){
        //call button
        callButton.setEnabled(false);

        //status and info
        statusLabel.setText("Status: Connected");
        fromIPLabel.setText("To: " + client.getPeerIP());

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(true);
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + client.getSysIP());
        userPortLabel.setText("My Port: " + port);
    }
}
