package com.cd.voyager.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Configurable
@PropertySource("classpath:messages_en.properties")
public class DisplayImage extends HttpServlet {
	
	@Autowired
	private Environment env;

	  public void init(ServletConfig config) throws ServletException {	
		    super.init(config);
		    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
		      config.getServletContext());
		  }

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String imagesBase = env.getProperty("documents.dir");
		if(env.getProperty("local").equals("1")){
			imagesBase  = env.getProperty("local.documents.dir");
		}

        String URLAfterWebDomain = request.getRequestURI();

        //Only accept mappings as src="/images/whatever.jpg", even if web.xml has other mappings to this servlet.
        if(URLAfterWebDomain.startsWith("/uc/images/") == false)   
            return;

        //get the image name, or even directory and image, e.g. /images/music/beethoven.jpg:
        String relativeImagePath = URLAfterWebDomain.substring("/uc/images/".length());  //will get "music/beethoven.jpg"

        System.out.println("\nFetching image from "+imagesBase+relativeImagePath);
        response.setContentType("image/jpeg"); //as far as I know, this works for PNG as well. You might want to change the mapping to /images/*.jpg if it's giving problems

        ServletOutputStream outStream;
        outStream = response.getOutputStream();
        FileInputStream fin = new FileInputStream(imagesBase+relativeImagePath);

        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(outStream);
        int ch =0; ;
        while((ch=bin.read())!=-1)
            bout.write(ch);

        bin.close();
        fin.close();
        bout.close();
        outStream.close();
    }
}
