package com.fcdnipro.dniprolab.smsNotification;

import com.fcdnipro.dniprolab.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class to perform XML request and getting response
 */
@Component
public class RequestBuilder {

    private final static Logger logger = LoggerFactory.getLogger(RequestBuilder.class);

    private final static String CONNECTION_PARAM = "XML";

    @Inject
    private JHipsterProperties jHipsterProperties;

    /*
    * This method getting string with string query in XML format,
    * invoking connection and returning result string
    * @throws IOException in case appearing any IO exception
    * @param xml - string with query in XML format
    * @return string with service response
     */
    public String doXMLQuery(String xml) {

        logger.debug("Invoke daXMLQuery method.");

        StringBuilder responseString = new StringBuilder();

        Map<String, String> params = new HashMap();
        params.put(CONNECTION_PARAM, xml);
            try {
                EpochtaVendorConnection.sendPostRequest(jHipsterProperties.getSmsNotificationProperties().getURL(), params);
                String[] response = EpochtaVendorConnection.readMultipleLinesResponse();
                for (String line : response) {
                    responseString.append(line);
                }
            } catch (IOException e) {
                logger.error("IOException in doXMLQueryMethod.", e);
            }
        EpochtaVendorConnection.disconnect();
        return responseString.toString();
    }

    public JHipsterProperties getjHipsterProperties() {
        return jHipsterProperties;
    }

    public void setjHipsterProperties(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }
}
