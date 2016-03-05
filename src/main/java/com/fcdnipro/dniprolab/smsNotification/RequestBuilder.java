package com.fcdnipro.dniprolab.smsNotification;

import com.fcdnipro.dniprolab.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * //TODO refactoring
 */
@Component
public class RequestBuilder {

    private final static Logger logger = LoggerFactory.getLogger(RequestBuilder.class);

    private final static String CONNECTION_PARAM = "XML";

    @Inject
    private JHipsterProperties jHipsterProperties;

    public String doXMLQuery(String xml) {
        StringBuilder responseString = new StringBuilder();

        Map<String, String> params = new HashMap();
        params.put(CONNECTION_PARAM, xml);
            try {
            TurboSmsVendorConnection.sendPostRequest(jHipsterProperties.getSmsNotification().getURL(), params);
            String[] response = TurboSmsVendorConnection.readMultipleLinesResponse();
            for (String line : response) {
                responseString.append(line);
                }
            } catch (Exception e) {
            e.printStackTrace();
            }
        TurboSmsVendorConnection.disconnect();
        return responseString.toString();
    }
}
