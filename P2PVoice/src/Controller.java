public class Controller {
    MainFrame mf;
    Client client;
    public Controller() {
        mf = new MainFrame();
        client = new Client(mf.getPort(), mf);
        mf.setClient(client);
        Thread mfThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                mf.start();
            }
        }
        );
    }

    public static void main(String[] args) {

    }
}
