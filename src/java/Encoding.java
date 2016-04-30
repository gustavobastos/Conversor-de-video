import javax.inject.Named;
import javax.enterprise.context.Dependent;
import java.io.*;
import java.net.*;
/**
 *
 * @author Gustavo-lap
 */
@Named(value = "encoding")
@Dependent
public class Encoding {
        private String texto;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
	
        public Encoding(){
            
        }
        public void startEncodingWorkflow() {
		// replace ID and key with your own
		texto = "teste";
                String userID = "72638";
		String userKey = "ae5ab3badffe3e20e61d66146fd8a38c";
		
		
                StringBuffer xml = new StringBuffer();
		
                xml.append("<query>");
                xml.append("<userid>"+userID+"</userid>");
                xml.append("<userkey>"+userKey+"</userkey>");
                xml.append("<action>addMedia</action>");
                xml.append("<source>http://conversorfiles.s3.amazonaws.com/video.dv</source>");
                xml.append("<format>");
                xml.append("<output>mp4</output>");
                xml.append("mpeg4");
                xml.append("<destination>http://conversorfiles.s3.amazonaws.com/video.mp4?acl=public-read</destination>");
                xml.append("</format>");
                xml.append("</query>");
 
		URL server = null;
 
		try {
			String url = "http://manage.encoding.com";
			System.out.println("Connecting to:"+url);
			server = new URL(url);
 
		} catch (MalformedURLException mfu) {
			mfu.printStackTrace();
			
		}
 
		try {
			String sRequest = "xml=" + URLEncoder.encode(xml.toString(), "UTF8");
			System.out.println("Open new connection to tunnel");
			HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.setRequestMethod( "POST" );
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(60000);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			BufferedWriter out = new BufferedWriter( new OutputStreamWriter( urlConnection.getOutputStream() ) );
			out.write(sRequest);
			out.flush();
			out.close();
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			String str = urlConnection.getResponseMessage();
			System.out.println("Response:"+urlConnection.getResponseCode());
			System.out.println("Response:"+urlConnection.getResponseMessage());
			texto = urlConnection.getResponseMessage();
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
        public static void linkVerify() {
	// replace ID and key with your own
	
    

	URL server = null;

	try {
		String url = "http://s3-sa-east-1.amazonaws.com/conversorfiles/video.mp4";
		System.out.println("Connecting to:"+url);
		server = new URL(url);

	} catch (MalformedURLException mfu) {
		mfu.printStackTrace();
		
	}

	try {
		
		System.out.println("Open new connection to tunnel");
		HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
		
		
		urlConnection.connect();
		
		String str = urlConnection.getResponseMessage();
		
		
		while((urlConnection.getResponseCode()) != 200){
			urlConnection = (HttpURLConnection) server.openConnection();
			urlConnection.connect();
			System.out.println("Response:"+urlConnection.getResponseCode());
			Thread.sleep(2000);
		}
		
		

		

	} catch (Exception exp) {
		exp.printStackTrace();
                    
	}

	
}
 
        
}