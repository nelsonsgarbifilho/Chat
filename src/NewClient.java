import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class NewClient implements Runnable{

    public static ArrayList<NewClient> clients = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String user;

    public NewClient(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
            this.user = bufferedReader.readLine();
            clients.add(this);
            messageToClients("entrei no bate-papo.");

        }catch (IOException e){
            System.out.println("Ocorreu um erro ao iniciar uma conexão");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (socket.isConnected()) {
            String msg;
            try {
                msg = bufferedReader.readLine();
                messageToClients(msg);
            } catch (IOException e) {
                closeObjects(socket, bufferedReader, bufferedWriter);
                break;
            }
        }


    }

    public void messageToClients(String msg){
        try {
            for (NewClient client : clients) {
                if (client != this) {
                    client.bufferedWriter.write(user + ": " + msg);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            messageToClients(user + "saiu do bate-papo.");
            clients.remove(this);
            closeObjects(socket, bufferedReader, bufferedWriter);
        }
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
}
