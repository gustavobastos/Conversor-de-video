/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    public void upload() {

        if (uploadedFile != null) {
            try {
                InputStream is = uploadedFile.getInputStream();
                text = new Scanner(is).useDelimiter("\\A").next();
            } catch (IOException ex) {
            }
        }
    }
    
}
