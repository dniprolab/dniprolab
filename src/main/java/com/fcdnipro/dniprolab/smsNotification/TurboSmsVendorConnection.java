package com.fcdnipro.dniprolab.smsnotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Connection with sms service API
 */
public class TurboSmsVendorConnection {

    private final static Logger logger = LoggerFactory.getLogger(TurboSmsVendorConnection.class);

    private final static Boolean USE_CACHES = false;
    private final static Boolean DO_INPUT = true;
    private final static Boolean DO_OUTPUT = true;
    private final static String CONNECTION_ERROR = "Connection is not established!";
    private final static String ENCODING = "UTF-8";
    private final static String PARAM_SEPARATOR = "&";
    private final static String PARAM_EQUAL = "=";

    private static HttpURLConnection httpURLConnection;

    /*
    * Makes HTTP request using POST method to specified URL
    * @param requestURL - message service URL
    * @param params - map contains POST data in pairs key-value
    * @throws IOException
    * @return httpURLConnection instance
     */
    public static HttpURLConnection sendPostRequest(String requestUrl, Map<String, String> params) throws IOException {
        logger.debug("Invoke sendPostRequest method");
        URL url = new URL(requestUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setUseCaches(USE_CACHES);
        httpURLConnection.setDoInput(DO_INPUT);
        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {
            httpURLConnection.setDoOutput(DO_OUTPUT); // true indicates POST request
            Iterator<String> paramIterator = params.keySet().iterator();

            boolean isFirst=true;
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                if(!isFirst){  requestParams.append(PARAM_SEPARATOR);}
                requestParams.append(key);
                requestParams.append(PARAM_EQUAL).append(URLEncoder.encode(value, ENCODING));
                isFirst=false;
            }
            // sends POST request
            OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
            URLEncoder.encode(requestParams.toString(), ENCODING);
            writer.write(requestParams.toString());
            writer.flush();
        }
        return httpURLConnection;
    }

    /*
    * Pulls string with response from httpURLConnection
    * @throws IOException
     */
    public static String readSingleLine() throws IOException {
        logger.debug("Invoke readSingleLine method.");
        InputStream inputStream = null;
        if(httpURLConnection != null){
            inputStream = httpURLConnection.getInputStream();
        }else {
            logger.error("HttpURLConnection not established.");
            throw new IOException(CONNECTION_ERROR);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String response = bufferedReader.readLine();
        bufferedReader.close();
        return response;
    }

    /*
    * Pulls multiple strings from httpURLConnection
    * @return divided by lines response in a string array
    * @throws IOException
     */
    public static String[] readMultipleLinesResponse() throws IOException {
        logger.debug("Invoke readMultipleLinesResponse method.");
        InputStream inputStream = null;
        if (httpURLConnection != null) {
            inputStream = httpURLConnection.getInputStream();
        } else {
            logger.error("HttpURLConnection is not established.");
            throw new IOException(CONNECTION_ERROR);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> response = new ArrayList<String>();

        String line = "";
        while ((line = reader.readLine()) != null) {
            response.add(line);
        }
        reader.close();

        return (String[]) response.toArray(new String[0]);
    }

    /*
    * Disconnect method
     */
    public static void disconnect() {
        if(httpURLConnection != null){
            httpURLConnection.disconnect();
        }
    }

    public static HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public static void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        TurboSmsVendorConnection.httpURLConnection = httpURLConnection;
    }
}
