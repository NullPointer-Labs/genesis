import service.Service;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        Service service = new Service();
        service.execute(port);
    }
}
