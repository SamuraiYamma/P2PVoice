import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CallPanel extends JPanel {
    private JTextField ipField;
    private JTextField portField;
    private JButton callButton;
    private JPanel statusPanel;

    public CallPanel(JPanel statusPanel){
        this.statusPanel = statusPanel; //we need this
        init();
    }

    private void init(){
        //instantiate fields
        ipField = new JTextField(10);
        portField = new JTextField(10);
        callButton = new JButton("Call");
        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port: ");

        //set layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints loc = new GridBagConstraints();

        loc.gridx = 0;
        loc.gridy = 0;

        //ip label and field
        loc.insets = new Insets(5, 5, 5, 5);
        this.add(ipLabel, loc);
        loc.gridx++;//go to next column
        this.add(ipField, loc);

        //port label and field
        loc.gridx--;//go back one column
        loc.gridy++;//go to next row
        this.add(portLabel, loc);
        loc.gridx++;
        this.add(portField, loc);

        //call button;
        loc.gridy++;
        loc.gridx--;
        loc.gridwidth = 2;
        loc.fill= GridBagConstraints.HORIZONTAL;
        this.add(callButton, loc);

        //add action listeners
        callButton.addActionListener(new CallPanelListener());

    }
    class CallPanelListener implements ActionListener{

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
            }
        }
    }


}
