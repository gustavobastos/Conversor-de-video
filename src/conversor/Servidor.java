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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name = "servidor")
public class Servidor {
	public static String texto;
	public Servidor() {

	}

	public String getTexto(){
		return texto;
	}
	public void setTexto(String text){
		this.texto = text;
	}
	
	public int esperaNotificacao() throws IOException {

		try {

				ServerSocket ss = new ServerSocket(5000);
				System.out.println("Servidor iniciado");
				ss.setSoTimeout(100000);
				Socket client = ss.accept();
				
				String ip = InetAddress.getLocalHost().getHostAddress();
				System.out.println("Notificação recebida!");
				InputStream in = client.getInputStream();
				ip = ip+" "+InetAddress.getLocalHost();
				setTexto(ip);
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
					if (result.contains("Finished")) {
						ss.close();
						return 1;
					}
					ss.close();
					return 1;

				} catch (IOException ioe) {
					ioe.printStackTrace();
					texto.concat(ioe.getMessage());
				}

			
		}

		catch (Exception e) {
			System.err.println(e);
			System.err.println("Usage: java HttpMirror <port>");
			texto.concat("\n"+e.getMessage());
		}

		return 1;
	}
}
