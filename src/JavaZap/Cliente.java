package JavaZap;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    private String serverAddress;
    private int serverPort;

    public Cliente() throws UnknownHostException, IOException {
        this.serverAddress = ConfigConexao.SERVER_ADDRESS;
        this.serverPort = ConfigConexao.SERVER_PORT;
        iniciar();
    }

    private void iniciar() throws UnknownHostException, IOException {
        Socket clientSocket = new Socket(serverAddress, serverPort);
        System.out.println("Cliente conectado ao servidor!");

        //Mensagens do servidor
        new Thread(() -> {
            try {
                Scanner input = new Scanner(clientSocket.getInputStream());
                while (true) {
                    if (input.hasNextLine()) {
                        String message = input.nextLine();
                        System.out.println("Servidor: " + message);

                    }
                }
            } catch (IOException e) {
                System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar a conexão com o servidor: " + e.getMessage());
                }
            }
        }).start();
        // Envie mensagens para o servidor
        Scanner s = new Scanner(System.in);
        PrintStream out = new PrintStream(clientSocket.getOutputStream());
        while (s.hasNextLine()) {
            String message = s.nextLine();
            out.println(message);
        }

        s.close();
        clientSocket.close();
    }


}