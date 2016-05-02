package conversor;

import java.io.*;
import java.net.*;

/**
 *
 * @author Gustavo Bastos
 */

public class Encoding {

	public Encoding() {

	}

	// Conversão do arquivo e envio para o repositório no Amazon S3
	public void startEncodingWorkflow() {

		String userID = "72818";
		String userKey = "c296b34de4f5b532bec5dc5c640af1ac";

		StringBuffer xml = new StringBuffer();

		xml.append("<query>");
		xml.append("<userid>" + userID + "</userid>");
		xml.append("<userkey>" + userKey + "</userkey>");
		xml.append("<action>addMedia</action>");
		xml.append("<source>http://conversorfiles.s3.amazonaws.com/video.dv</source>");
		xml.append("<notify_format>xml</notify_format>");
		xml.append("<notify>http://186.206.183.165:7500</notify>");
		xml.append("<format>");
		xml.append("<output>mp4</output>");
		xml.append("mpeg4");
		xml.append("<destination>http://conversorfiles.s3.amazonaws.com/video.mp4?acl=public-read</destination>");
		xml.append("</format>");
		xml.append("</query>");

		URL server = null;

		try {
			String url = "http://manage.encoding.com";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}

		try {
			String sRequest = "xml=" + URLEncoder.encode(xml.toString(), "UTF8");
			System.out.println("Open new connection to tunnel");
			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(60000);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
			out.write(sRequest);
			out.flush();
			out.close();
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			StringBuffer strbuf = new StringBuffer();
			byte[] buffer = new byte[1024 * 4];

			try {
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					strbuf.append(new String(buffer, 0, n));
				}

				is.close();

			} catch (IOException ioe) {
				ioe.printStackTrace();

			}

			System.out.println(strbuf.toString());

		} catch (Exception exp) {
			exp.printStackTrace();

		}

	}

	// Verifica se o vídeo já está disponível para reprodução
	public void linkVerify() {

		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}

		try {

			System.out.println("Open new connection to tunnel");
			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();

			urlConnection.connect();

			// fica em loop até que o vídeo convertido esteja disponível
			while ((urlConnection.getResponseCode()) != 200) {
				urlConnection = (HttpURLConnection) server.openConnection();
				urlConnection.connect();
				System.out.println("Response:" + urlConnection.getResponseCode());
				Thread.sleep(2000);
			}

		} catch (Exception exp) {
			exp.printStackTrace();

		}

	}

	
}
