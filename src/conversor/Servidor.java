package conversor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public Servidor() {

	}

	public int esperaNotificacao() throws IOException {

		{
			try {

				ServerSocket ss = new ServerSocket(7500);

				for (;;) {

					System.out.println("Servidor iniciado");
					Socket client = ss.accept();
					System.out.println("Notificação recebida!");
					InputStream in = client.getInputStream();

					StringBuffer strbuf = new StringBuffer();

					byte[] buffer = new byte[1024 * 4];
					try {
						int n = 0;
						while (-1 != (n = in.read(buffer))) {
							strbuf.append(new String(buffer, 0, n));
						}

						in.close();
						String result = java.net.URLDecoder.decode(strbuf.toString(), "UTF-8");
						System.out.println(result);
						client.close();
						if (result.contains("Finished"))
							return 0;
						return 1;

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}
			}

			catch (Exception e) {
				System.err.println(e);
				System.err.println("Usage: java HttpMirror <port>");
			}

		}
		return 1;
	}
}
