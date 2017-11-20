package com.code.arctouch.arctouchcodechallenge.data.source.remote.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Simple requester
 */
public final class Requester implements UrlReader {

    private static final Logger logger = LoggerFactory.getLogger(Requester.class);

    public static URLConnection openConnection(URL url) {
        try {
            URLConnection cnx = url.openConnection();
            return cnx;
        } catch (IOException ex) {
            throw new UpcomingMovieException(url.toString(), ex);
        }
    }

    private static Charset getCharset(URLConnection cnx) {
        Charset charset = null;
        // content type will be string like "text/html; charset=UTF-8" or "text/html"
        String contentType = cnx.getContentType();
        if (contentType != null) {
            // changed 'charset' to 'harset' in regexp because some sites send 'Charset'
            Matcher m = Pattern.compile("harset *=[ '\"]*([^ ;'\"]+)[ ;'\"]*").matcher(contentType);
            if (m.find()) {
                String encoding = m.group(1);
                try {
                    charset = Charset.forName(encoding);
                } catch (UnsupportedCharsetException e) {
                }
            }
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return charset;
    }

    public String request(URL url) {
        StringWriter content = null;
        try {
            content = new StringWriter();

            BufferedReader in = null;
            HttpURLConnection cnx = null;
            try {
                cnx = (HttpURLConnection) openConnection(url);
                if (cnx.getResponseCode() >= 400) {
                    if (cnx.getErrorStream() == null) {
                        throw new UpcomingMovieException("error stream was null");
                    }
                    in = new BufferedReader(new InputStreamReader(cnx.getErrorStream(), getCharset(cnx)));
                } else {
                    in = new BufferedReader(new InputStreamReader(cnx.getInputStream(), getCharset(cnx)));
                }
                String line;
                while ((line = in.readLine()) != null) {
                    content.write(line);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (cnx != null) {
                    cnx.disconnect();
                }
            }
            return content.toString();
        } catch (IOException ex) {
            throw new UpcomingMovieException(url.toString(), ex);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException ex) {
                    logger.debug("Failed to close connection: " + ex.getMessage());
                }
            }
        }
    }
}
