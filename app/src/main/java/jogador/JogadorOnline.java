package jogador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.image.Image;
import partida.Cor;

public class JogadorOnline extends Jogador {
    private Socket socket;
    private ServerSocket serverSocket;  // Para criar o servidor
    
    public JogadorOnline() {
    }

    public JogadorOnline(Cor cor, String nome, Image imagem) {
        super(cor, nome, imagem);
    }
    
    public Socket criarServidor(int porta) {
        try {
            System.out.println("Iniciando servidor...");
            serverSocket = new ServerSocket(porta);
            InetAddress localHost = InetAddress.getLocalHost();
            String ipServidor = localHost.getHostAddress();
            System.out.println("Servidor criado. Aguardando conexão...");
            System.out.println("IP do servidor: " + ipServidor + " Porta: " + porta);
            
            // Aguardando conexão do cliente (Jogador 2)
            socket = serverSocket.accept();  // Este comando bloqueia até a conexão ser estabelecida
            System.out.println("Conexão estabelecida com: " + socket.getInetAddress());
            
            return socket;
        } catch (IOException e) {
            System.out.println("Erro ao criar servidor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }     

    public Socket getSocket() {
        return socket;
    }

    public void desconectar() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor desconectado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao desconectar o servidor: " + e.getMessage());
        }
    }

    public boolean conectar(String enderecoServidor, int porta) {
        try {
            this.socket = new Socket(enderecoServidor, porta);
            System.out.println("Conectado ao servidor!");
            System.out.println("Socket: " + socket);
            enviarDadosParaServidor();
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    private void enviarDadosParaServidor() {
        try {
            DataOutputStream output = new DataOutputStream(this.getSocket().getOutputStream());
            output.writeUTF(this.getNome());
            output.writeUTF(this.getCor().toString());
            output.writeUTF(this.getImagem().getUrl());
            output.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar dados para o servidor: " + e.getMessage());
        }
    }
}