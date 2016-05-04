package conversor;

import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.IOException;

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

			// try {

			Properties prop = new ManipulatorProp().getPro();
			// InputStream is = uploadedFile.getInputStream();

			// teste para verificar se os arquivos de entrada não são vídeos
			if (uploadedFile.getContentType().contains("text"))
				return "Erro";
			else if (uploadedFile.getContentType().contains("image"))
				return "Erro";

			// Autenticação no servidor da Amazon
			AWSCredentials credentials = new BasicAWSCredentials(prop.getProperty("prop.amazon.keyId"),
					prop.getProperty("prop.amazon.KeySecret"));
			AmazonS3 s3client = new AmazonS3Client(credentials);
			ObjectMetadata obj = new ObjectMetadata();
			obj.setContentLength(uploadedFile.getSize());
			s3client.putObject(new PutObjectRequest("conversorfiles", "video.dv", uploadedFile.getInputStream(), obj)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			// Envia o arquivo para fila de conversão, aguarda resposta e
			// disponibilidade do video convertido

			Encoding encoding = new Encoding();

			if (encoding.startEncodingWorkflow() == 1)
				return "Erro";

			Servidor servidor = new Servidor();
			if (servidor.esperaNotificacao() == 1)
				return "Erro";

			if (encoding.linkVerify() == 1) {
				return "Erro";
			}

			// } catch (IOException ex) {
			// ex.printStackTrace();
			// }
		}
		return "playFile";
	}

}
