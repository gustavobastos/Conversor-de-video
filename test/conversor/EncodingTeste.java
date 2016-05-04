package conversor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Realiza testes na classe Encoding
 */
public class EncodingTeste extends TestCase {

	public void testLinkVerify() throws IOException {
		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.connect();
			Encoding teste = new Encoding();
			if (urlConnection.getResponseCode() == 200)
				assertTrue(teste.linkVerify() == 0);
			;

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}
	}
	
	public void testStartEncodingWorkflow() throws IOException{
		URL server = null;

		try {
			String url = "http://conversorfiles.s3.amazonaws.com/video.dv";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.connect();
			Encoding teste = new Encoding();
			if (urlConnection.getResponseCode() == 200)
				assertTrue(teste.startEncodingWorkflow() == 0);
			;

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}
	}

}
