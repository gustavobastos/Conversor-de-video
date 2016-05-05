package conversor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

public class FileUploadBeanTest extends TestCase {

	public void testVerificaExistencia() throws IOException {
		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.connect();
			FileUploadBean teste = new FileUploadBean();
			if (urlConnection.getResponseCode() == 200)
				assertTrue(teste.verificaExistencia() == true);
			;

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}
	}

}
