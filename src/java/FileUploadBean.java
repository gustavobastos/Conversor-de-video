

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.servlet.http.Part;

/**
 *
 * @author Gustavo-lap
 */
@Named(value = "fileUploadBean")
@RequestScoped
public class FileUploadBean {
    private Part uploadedFile;
    private String text;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    /**
     * Creates a new instance of FileUploadBean
     */
    public FileUploadBean() {
    }
    public String upload() {

        if (uploadedFile != null) {
            try {
                           
                AWSCredentials credentials = new BasicAWSCredentials("AKIAIV52M4ZQGLMOKBIA", "Oh6FuivQUm33MCJzWeR8nzaTLDjlMzUgh5xljiIL");
                AmazonS3 s3client = new AmazonS3Client(credentials);
                
                
                ObjectMetadata obj = new ObjectMetadata();
                obj.setContentLength(uploadedFile.getSize());
                s3client.putObject(new PutObjectRequest("conversorfiles", "video.dv", uploadedFile.getInputStream(), obj).withCannedAcl(CannedAccessControlList.PublicRead));
                
                
                
                
                
            } catch (IOException ex) {
            }
        }
    return "encodingFile";
    }
    
}
