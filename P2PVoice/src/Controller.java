public class Controller {
    MainFrame mf;
    Client client;
    static Thread mfThread;
    static Thread clientThread;
    public Controller() {
        mf = new MainFrame();
        client = new Client(mf.getPort(), mf);
        mf.setClient(client);
        mfThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                mf.start();
            }
        }
        );
        clientThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                client.runClient();
            }
        }
        );




    }

    public static void main(String[] args) {
        Controller c = new Controller();
        mfThread.start();
        clientThread.start();

    }
}
