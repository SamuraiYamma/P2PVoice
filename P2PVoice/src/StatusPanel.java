import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel statusLabel;
    private JLabel fromIPLabel;
    private JButton acceptButton;
    private JButton denyButton;
    private JButton endButton;
    private JLabel timerLabel;
    private JLabel userIPLabel;
    private JLabel userPortLabel;

    public StatusPanel(Status status) { //upon creation, give it a current status and update methods accordingly
        init();
        setMode(status);
    }

    /**
     * Initialize all basic values
     */
    private void init(){
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
        this.setLayout(new GridBagLayout());
        GridBagConstraints loc = new GridBagConstraints();

        loc.insets = new Insets(5, 5, 5, 5);
        loc.gridx = 0;
        loc.gridy = 0;

        //status section
        this.add(statusLabel, loc);
        loc.gridy++;
        this.add(fromIPLabel, loc);
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
        add(subPanel, loc);
        loc.gridy++;

        //user info section
        this.add(userIPLabel, loc);
        loc.gridy++;
        this.add(userPortLabel, loc);

    }

    /**
     * This method has been made public to allow other classes to change the mode
     * based on the status of the app, and to be able to represent it here
     */
    public void setMode(Status status){
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

    /**
     * All functionality is disabled until user is connected to their own server instance
     * and is connected to the internet. This is the default state.
     */
    private void offlineMode(){
        //status and info
        statusLabel.setText("Status: Offline");
        fromIPLabel.setText("From: N/A");

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(false);
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: N/A");
        userPortLabel.setText("My Port: N/A");

        //TODO: Implement some way of getting to online mode/connected mode. May not happen in this class
    }

    /**
     * All functionality besides ending a call is enabled
     */
    private void onlineMode(){
        //TODO: Replace these stubs with real data
        String ipStub = "192.168.0.1";
        int portStub = 20022;


        //status and info
        statusLabel.setText("Status: Online");
        fromIPLabel.setText("From: N/A");

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(false);
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + ipStub);
        userPortLabel.setText("My Port: " + portStub);
    }

    /**
     * Functionality is disabled besides accepting or denying calls,
     * with a 30 second countdown timer
     */
    private void receivingMode(){
        //TODO: Replace these stubs with real data
        String ipStub = "192.168.0.1";
        int portStub = 20022;


        //status and info
        statusLabel.setText("Status: Receiving Call");
        fromIPLabel.setText("From: " + ipStub);

        //buttons and timer
        acceptButton.setEnabled(true);
        denyButton.setEnabled(true);
        endButton.setEnabled(false);
        //TODO: connect backend to enable countdown timer display
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + ipStub);
        userPortLabel.setText("My Port: " + portStub);
    }

    /**
     * Functionality disabled besides the end button
     * Timer increments until it reaches a max of 30s
     */
    private void callingMode(){
        //TODO: Replace these stubs with real data
        String ipStub = "192.168.0.1";
        int portStub = 20022;


        //status and info
        statusLabel.setText("Status: Calling");
        fromIPLabel.setText("Target: " + ipStub);

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(true);

        //TODO: implement backend to limit timer to 30s
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + ipStub);
        userPortLabel.setText("My Port: " + portStub);
    }

    /**
     * Only functionality is a continuous timer and the ability to end call
     */
    private void connectedMode(){
        //TODO: Replace these stubs with real data
        String ipStub = "192.168.0.1";
        int portStub = 20022;


        //status and info
        statusLabel.setText("Status: Online");
        fromIPLabel.setText("To: " + ipStub);

        //buttons and timer
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        endButton.setEnabled(true);
        timerLabel.setText("00:00");

        //user info
        userIPLabel.setText("My IP: " + ipStub);
        userPortLabel.setText("My Port: " + portStub);
    }
}


