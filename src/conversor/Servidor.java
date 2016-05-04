package conversor;

import java.io.IOException;
import java.io.InputStream;

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

	public Servidor() {

	}

	// Inicia um servidor em uma porta para receber a notificação sobre o estado
	// do arquivo enviada pelo encoding.com
	public int esperaNotificacao() throws IOException {

		try {

			ServerSocket ss = new ServerSocket(8000);
			System.out.println("Servidor iniciado");

			Socket client = ss.accept();
			StringBuffer strbuf = new StringBuffer();

			byte[] buffer = new byte[1024 * 4];
			try {
				InputStream in = client.getInputStream();
				int n = 0;
				while (-1 != (n = in.read(buffer))) {
					strbuf.append(new String(buffer, 0, n));
				}

				String result = java.net.URLDecoder.decode(strbuf.toString(), "UTF-8");
				System.out.println(result);
				client.close();
				
				//Se a resposta contém a informação "Finished" então a conversão ocorreu com sucesso
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

			} finally {
				if (ss != null) {
					try {
						ss.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}

		catch (Exception e) {
			System.err.println(e);

		}

		return 1;
	}

}
