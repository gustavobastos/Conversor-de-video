package conversor;

import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

/**
 *
 * @author Gustavo Bastos
 * 
 *         Classe responsável pelo upload no servidor da Amazon e chamar os
 *         métodos relacionados à classe de conversão
 */

@RequestScoped
@ManagedBean(name = "fileUploadBean")
public class FileUploadBean {
	private Part uploadedFile;

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public FileUploadBean() {
	}

	public String upload() throws IOException {

		if (uploadedFile != null) {

			try {

				Properties prop = new ManipulatorProp().getPro();

				// teste para verificar se os arquivos de entrada não são vídeos
				if (uploadedFile.getContentType().contains("text"))
					return "Erro";
				else if (uploadedFile.getContentType().contains("image"))
					return "Erro";

				// Autenticação no servidor da Amazon
				AWSCredentials credentials = new BasicAWSCredentials(prop.getProperty("prop.amazon.keyId"),
						prop.getProperty("prop.amazon.KeySecret"));
				AmazonS3 s3client = new AmazonS3Client(credentials);

				// Se já existe um arquivo com o nome a ser inserido ele deve
				// ser excluído
				if (verificaExistencia() == true) {
					try {
						s3client.deleteObject(new DeleteObjectRequest("conversorfiles", "video.mp4"));
					} catch (AmazonClientException ace) {
						System.out.println("Caught an AmazonClientException.");
						System.out.println("Error Message: " + ace.getMessage());
					}
				}

				ObjectMetadata obj = new ObjectMetadata();
				obj.setContentLength(uploadedFile.getSize());
				s3client.putObject(
						new PutObjectRequest("conversorfiles", "video.dv", uploadedFile.getInputStream(), obj)
								.withCannedAcl(CannedAccessControlList.PublicRead));

				// Envia o arquivo para fila de conversão, aguarda resposta e
				// disponibilidade do video convertido
				if (Encoding.startEncodingWorkflow() == 1)
					return "Erro";

				Servidor servidor = new Servidor();
				if (servidor.esperaNotificacao() == 1)
					return "Erro";

				if (Encoding.linkVerify() == 1) {
					return "Erro";
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return "playFile";
	}

	public boolean verificaExistencia() throws IOException {
		URL server = null;

		try {
			String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
			server = new URL(url);

			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.connect();
			if (urlConnection.getResponseCode() == 200)
				return true;

		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();

		}
		return false;
	}

}
