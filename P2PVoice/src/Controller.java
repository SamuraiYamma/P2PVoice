/**
 * Class which runs the server, GUI, and the client
 * Execute this class to run the program.
 */
public class Controller {
    MainFrame mf;
    Client client;
    BasicServer server;
    static Thread mfThread;
    static Thread clientThread;
    static Thread serverThread;
    public Controller() {
        mf = new MainFrame();
        client = new Client(mf.getPort(), mf);
        server = new BasicServer();
        mf.setClient(client);
        serverThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                server.start();
            }
        });
        mfThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                mf.start();
            }
        });
        clientThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                client.runClient();
            }
        });
    }

    public static void main(String[] args) {
        Controller c = new Controller();
        serverThread.start();
        mfThread.start();
        clientThread.start();

    }
}
