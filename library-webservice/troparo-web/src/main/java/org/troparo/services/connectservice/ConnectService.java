package org.troparo.services.connectservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-28T16:50:13.520+02:00
 * Generated source version: 3.2.7
 *
 */
@WebServiceClient(name = "ConnectService",
                  wsdlLocation = "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/ConnectService.wsdl",
                  targetNamespace = "http://troparo.org/services/ConnectService/")
public class ConnectService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://troparo.org/services/ConnectService/", "ConnectService");
    public final static QName ConnectServicePort = new QName("http://troparo.org/services/ConnectService/", "ConnectServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/ConnectService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ConnectService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/ConnectService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ConnectService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ConnectService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConnectService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public ConnectService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public ConnectService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public ConnectService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns IConnectService
     */
    @WebEndpoint(name = "ConnectServicePort")
    public IConnectService getConnectServicePort() {
        return super.getPort(ConnectServicePort, IConnectService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IConnectService
     */
    @WebEndpoint(name = "ConnectServicePort")
    public IConnectService getConnectServicePort(WebServiceFeature... features) {
        return super.getPort(ConnectServicePort, IConnectService.class, features);
    }

}
