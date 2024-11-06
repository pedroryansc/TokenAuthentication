package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMain {
	public static void main(String[] args) {
		
		// Definição do número da porta da comunicação
		int porta = 1234;

		// Instanciação dos objetos que recebem e enviam mensagens ao usuário
		ObjectInputStream entrada;
		ObjectOutputStream saida;
		
		// Declaração de variáveis
		String tokenGerado;
		String tokenRecebido;
		String mensagem;
		
		try {
			// Criação do objeto do servidor (associando-o a uma porta e um endereço IP)
			ServerSocket servidor = new ServerSocket(porta, 0, InetAddress.getByName("192.168.56.1"));
			
			System.out.println("Servidor ouvindo a porta " + porta);
			
			// Loop infinito em que o servidor aguarda por algum usuário se conectar
			while(true) {
				// Servidor ouve e aceita a conexão de um usuário
				Socket usuario = servidor.accept();
				
				System.out.println("Usuário conectado: " + usuario.getInetAddress().getHostAddress());
				
				// Servidor gera o token de autenticação que o usuário deve informar
				tokenGerado = Servidor.gerarToken();
				System.out.println("Token gerado: " + tokenGerado);
				
				// Servidor recebe o token enviado pelo usuário na comunicação
				entrada = new ObjectInputStream(usuario.getInputStream());
				tokenRecebido = (String) entrada.readObject();
				
				saida = new ObjectOutputStream(usuario.getOutputStream());
				saida.flush();
				
				// Verifica se o token recebido é igual ao token gerado pelo servidor
				if(tokenRecebido.equals(tokenGerado)) {
					// Em caso afirmativo, o usuário é autorizado e o servidor envia uma confirmação ao usuário
					System.out.println("Usuário autorizado!");
					
					saida.writeObject(true);
					
					// Servidor fica recebendo as mensagens do usuário e as enviando de volta até o usuário encerrar
					do {
						mensagem = (String) entrada.readObject();
						
						if(!mensagem.equals("")) {
							System.out.println("Usuário: " + mensagem.toString());
							
							saida.writeObject(mensagem);
						}
					} while(!mensagem.equals(""));
				} else {
					// Caso o token recebido esteja incorreto, o servidor envia uma negação ao usuário
					System.out.println("Token incorreto. Autorização não concedida.");
					
					saida.writeObject(false);
				}
				
				// Fim da conexão com o usuário
				System.out.println("Conexão encerrada com o usuário.");
			}
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}
		
	}
}
