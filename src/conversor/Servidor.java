package conversor;

import java.io.IOException;
import java.io.InputStream;

import java.net.ServerSocket;
import java.net.Socket;

import javax.faces.bean.RequestScoped;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Classe respons�vel por receber as notifica��es enviadas pelo
 *         encoding.com
 */

@RequestScoped
public class Servidor {

	public Servidor() {

	}

	// Inicia um servidor em uma porta para receber a notifica��o sobre o estado
	// do arquivo enviada pelo encoding.com
	public int esperaNotificacao() throws IOException {

		try {

			ServerSocket ss = new ServerSocket(5000);
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
				
				//Se a resposta cont�m a informa��o "Finished" ent�o a convers�o ocorreu com sucesso
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
