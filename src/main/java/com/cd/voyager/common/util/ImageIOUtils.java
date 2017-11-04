package com.cd.voyager.common.util;
import java.io.File;
import java.util.Date;
import java.util.Locale;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.status.Status;


public class ImageIOUtils {
	private static final Logger logger = LoggerFactory.getLogger(ImageIOUtils.class);

	/*public static Response returnFile(File file, String modified) {
	    if (!file.exists()) {
	        return Response.status(status.NOT_FOUND).build();
	    }

	    // do we really need to send the file or can send "not modified"?
	    if (modified != null) {
	        Date modifiedDate = null;

	        // we have to switch the locale to ENGLISH as parseDate parses in the default locale
	        Locale old = Locale.getDefault();
	        Locale.setDefault(Locale.ENGLISH);
	        try {
	            modifiedDate = DateUtils.parseDate(modified, org.apache.http.impl.cookie.DateUtils.DEFAULT_PATTERNS);
	        } catch (ParseException e) {
	            logger.error(e.getMessage(), e);
	        }
	        Locale.setDefault(old);

	        if (modifiedDate != null) {
	            // modifiedDate does not carry milliseconds, but fileDate does
	            // therefore we have to do a range-based comparison
	            // 1000 milliseconds = 1 second
	            if (file.lastModified()-modifiedDate.getTime() < DateUtils.MILLIS_PER_SECOND) {
	                return Response.status(Status.NOT_MODIFIED).build();
	            }
	        }
	    }        
	    // we really need to send the file

	    try {
	        Date fileDate = new Date(file.lastModified());
	        return Response.ok(new FileInputStream(file)).lastModified(fileDate).build();
	    } catch (FileNotFoundException e) {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	}
	 */


}
