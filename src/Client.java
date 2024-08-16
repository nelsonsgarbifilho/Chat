import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username= username;
        }catch (IOException e){
            closeObjects(socket, bufferedReader, bufferedWriter);

        }
    }
    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeObjects(socket, bufferedReader, bufferedWriter);
        }
    }


    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;

                while (socket.isConnected()) {
                    try {
                        msg = bufferedReader.readLine();
                        System.out.println(msg);
                    } catch (IOException e) {
                        closeObjects(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeObjects(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {


        Socket socket = new Socket("000.000.0.00", 7000); /* inserir o endereço ip do servidor */

        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite seu nome de usuário: ");
        String username = scanner.nextLine();
        Client client = new Client(socket, username);

        client.listenForMessage();
        client.sendMessage();

    }
}