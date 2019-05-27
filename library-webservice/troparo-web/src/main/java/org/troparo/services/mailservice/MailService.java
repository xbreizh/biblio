package org.troparo.services.mailservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-27T11:06:02.165+02:00
 * Generated source version: 3.2.7
 *
 */
@WebServiceClient(name = "MailService",
                  wsdlLocation = "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MailService.wsdl",
                  targetNamespace = "http://troparo.org/services/MailService/")
public class MailService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://troparo.org/services/MailService/", "MailService");
    public final static QName MailServicePort = new QName("http://troparo.org/services/MailService/", "MailServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MailService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MailService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MailService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MailService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MailService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MailService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public MailService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public MailService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public MailService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns IMailService
     */
    @WebEndpoint(name = "MailServicePort")
    public IMailService getMailServicePort() {
        return super.getPort(MailServicePort, IMailService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IMailService
     */
    @WebEndpoint(name = "MailServicePort")
    public IMailService getMailServicePort(WebServiceFeature... features) {
        return super.getPort(MailServicePort, IMailService.class, features);
    }

}
