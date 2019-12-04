public class Controller {
    MainFrame mf = new MainFrame();
    Client client = new Client(mf.getPort(), mf);
    mf.setClient(client);
}
