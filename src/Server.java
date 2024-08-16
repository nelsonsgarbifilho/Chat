import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7000);
            System.out.println("Servidor iniciado na porta "     + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Novo usuário conectado: " + socket.getInetAddress());

                NewClient newClient = new NewClient(socket);

                Thread thread = new Thread(newClient);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ocorreu um erro ao inicializar o servidor.");
        }
    }
}
