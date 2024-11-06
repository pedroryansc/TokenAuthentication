package usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Usuario {
	public static void main(String[] args) {
		
		// Instanciação do scanner
		Scanner scanner = new Scanner(System.in);

		String mensagem;
		
		try {
			// Usuário se conecta ao endereço IP e porta da comunicação com o servidor
			Socket usuario = new Socket("192.168.56.1", 1234);
			
			// Usuário insere o token de autenticação e o envia ao servidor
			System.out.println("Insira o token de autenticação:");
			String token = scanner.nextLine();
			
			ObjectOutputStream entrada = new ObjectOutputStream(usuario.getOutputStream());
			entrada.flush();
			entrada.writeObject(token);
			
			// Usuário recebe o retorno do servidor para saber se foi autenticado
			ObjectInputStream saida = new ObjectInputStream(usuario.getInputStream());
			boolean autenticacao = (boolean) saida.readObject();
			
			if(autenticacao) {
				// Em caso verdadeiro, o usuário é autenticado
				System.out.println("\nAutorização concedida!");
				
				// Usuário envia mensagens ao servidor e as recebe de volta até encerrar a comunicação
				do {
					System.out.print("\nInsira sua mensagem (ou aperte ENTER para encerrar): ");
					mensagem = scanner.nextLine();
					
					entrada.writeObject(mensagem);
					
					if(!mensagem.equals(""))
						System.out.println("Servidor: " + saida.readObject());
				} while(!mensagem.equals(""));
			} else
				// Em caso falso, o usuário não é autenticado
				System.out.println("\nToken incorreto. Autorização não concedida.");
			
			// Fim da comunicação
			System.out.println("\nConexão encerrada.");
			
			usuario.close();
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}

		scanner.close();
	}
}
