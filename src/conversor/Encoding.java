package conversor;

import java.io.*;
import java.net.*;
//import java.util.Properties;
import java.util.Properties;

import javax.faces.bean.RequestScoped;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Classe responsável por inserir o vídeo na fila para conversão do
 *         encoding.com e verificar se o vídeo foi disponibilizado no servidor
 *         da Amazon
 */

@RequestScoped
public class Encoding {

	public Encoding() {

	}

	// Conversão do arquivo e envio do video convertido para o repositório no
	// Amazon S3
	public static int startEncodingWorkflow() throws IOException {

		Properties prop = new ManipulatorProp().getPro();

		String userID = prop.getProperty("prop.encoding.userId");
		String userKey = prop.getProperty("prop.encoding.useKey");

		// endereço para receber a notificação do encoding.com
		String EndNotificacao = "[endereço para recepção da notificação]";

		StringBuffer xml = new StringBuffer();

		xml.append("<query>");
		xml.append("<userid>" + userID + "</userid>");
		xml.append("<userkey>" + userKey + "</userkey>");
		xml.append("<action>addMedia</action>");
		xml.append("<source>http://conversorfiles.s3.amazonaws.com/video.dv</source>");
		xml.append("<notify_format>json</notify_format>");
		xml.append("<notify>" + EndNotificacao + "</notify>");
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

		}

		try {
			String sRequest = "xml=" + URLEncoder.encode(xml.toString(), "UTF8");
			System.out.println("Open new connection to tunnel");
			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
			out.writeBytes(sRequest);
			out.flush();
			out.close();

			urlConnection.connect();
			urlConnection.getInputStream();

		} catch (Exception exp) {
			return 1;

		}
		return 0;
	}

	// Verifica se o vídeo já está disponível para reprodução
	public static int linkVerify() {

		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			System.out.println("Connecting to:" + url);
			server = new URL(url);

		} catch (MalformedURLException mfu) {
			return 1;

		}

		try {

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();

			urlConnection.connect();

			// fica em loop até que o vídeo convertido esteja disponível
			while ((urlConnection.getResponseCode()) != 200) {
			urlConnection.disconnect();
				urlConnection = (HttpURLConnection) server.openConnection();
				urlConnection.connect();
				System.out.println("Response:" + urlConnection.getResponseCode());
				Thread.sleep(2000);
			}
			urlConnection.disconnect();

		} catch (Exception exp) {
			return 1;
		}
		return 0;
	}

}
