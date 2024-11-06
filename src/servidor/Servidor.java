package servidor;

public class Servidor {

	public static String gerarToken() {
		int quantCaracteres = 20;
		
		StringBuilder token = new StringBuilder(quantCaracteres);
		
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890" + "abcdefghijklmnopqrstuvwxyz";
		
		for(int i = 1; i <= quantCaracteres; i++) { 
			int indice = (int) (Math.random() * caracteres.length());
			token.append(caracteres.charAt(indice));
		}
		
		return token.toString();
	}
	
}
