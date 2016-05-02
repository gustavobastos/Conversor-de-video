package conversor;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.faces.bean.*;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

/**
 *
 * @author Gustavo Bastos
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

	/**
	 * Creates a new instance of FileUploadBean
	 */
	public FileUploadBean() {
	}

	@PostConstruct
	public String upload() throws IOException {

		if (uploadedFile != null) {

			try {

				AWSCredentials credentials = new BasicAWSCredentials("AKIAJMKKKHBMXB4TZRLA",
						"1UlJHAGQYTR+/ASUFt45N04/Sn2C0hECBu79RCwA");
				AmazonS3 s3client = new AmazonS3Client(credentials);
				ObjectMetadata obj = new ObjectMetadata();
				obj.setContentLength(uploadedFile.getSize());
				s3client.putObject(
						new PutObjectRequest("conversorfiles", "video.dv", uploadedFile.getInputStream(), obj)
								.withCannedAcl(CannedAccessControlList.PublicRead));
				Encoding encoding = new Encoding();
				
				encoding.startEncodingWorkflow();
				Servidor servidor = new Servidor();
				if(servidor.esperaNotificacao() == 1)
					return "Erro";
				encoding.linkVerify();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return "playFile";
	}

}
