package conversor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Classe responsável por receber as notificações enviadas pelo
 *         encoding.com
 */

public class Servidor {
	int porta = 5000;

	public Servidor() {

	}

	// Inicia um servidor em uma porta para receber a notificação sobre o estado
	// do arquivo enviada pelo encoding.com
	public int esperaNotificacao() throws IOException {

		try {

			ServerSocket ss = new ServerSocket(porta);
			System.out.println("Servidor iniciado");

			Socket client = ss.accept();
			StringBuffer strbuf = new StringBuffer();

			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				strbuf.append(inputLine);
			}

			String result = java.net.URLDecoder.decode(strbuf.toString(), "UTF-8");
			System.out.println(result);
			client.close();

			// Se a resposta contém a informação "Finished" então a conversão
			// ocorreu com sucesso
			if (result.contains("Finished")) {
				in.close();
				ss.close();
				return 0;
			}

			in.close();
			ss.close();
			return 1;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return 1;
		}
	}

}
