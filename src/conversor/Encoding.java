package conversor;

import java.io.*;
import java.net.*;
//import java.util.Properties;
import java.util.Properties;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Classe respons�vel por inserir o v�deo na fila para convers�o do
 *         encoding.com e verificar se o v�deo foi disponibilizado no servidor
 *         da Amazon
 */

public class Encoding {

	public Encoding() {

	}

	// Convers�o do arquivo e envio do video convertido para o reposit�rio no
	// Amazon S3
	public int startEncodingWorkflow() throws IOException {

		Properties prop = new ManipulatorProp().getPro();

		String userID = prop.getProperty("prop.encoding.userId");
		String userKey = prop.getProperty("prop.encoding.useKey");

		StringBuffer xml = new StringBuffer();

		xml.append("<query>");
		xml.append("<userid>" + userID + "</userid>");
		xml.append("<userkey>" + userKey + "</userkey>");
		xml.append("<action>addMedia</action>");
		xml.append("<source>http://conversorfiles.s3.amazonaws.com/video.dv</source>");
		xml.append("<notify_format>xml</notify_format>");
		xml.append("<notify>http://52.67.84.207:5000</notify>");
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
				return 1;

			}

			System.out.println(strbuf.toString());

		} catch (Exception exp) {
			exp.printStackTrace();
			return 1;

		}
		return 0;
	}

	// Verifica se o v�deo j� est� dispon�vel para reprodu��o
	public int linkVerify() {

		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();
			return 1;

		}

		try {

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();

			urlConnection.connect();

			// fica em loop at� que o v�deo convertido esteja dispon�vel
			while ((urlConnection.getResponseCode()) != 200) {
				urlConnection = (HttpURLConnection) server.openConnection();
				urlConnection.connect();
				System.out.println("Response:" + urlConnection.getResponseCode());
				Thread.sleep(2000);
			}

		} catch (Exception exp) {
			exp.printStackTrace();
			return 1;
		}
		return 0;
	}

}
