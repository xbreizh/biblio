package org.troparo.services.memberservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-06-04T17:46:27.032+02:00
 * Generated source version: 3.2.7
 *
 */
@WebServiceClient(name = "MemberService",
                  wsdlLocation = "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MemberService.wsdl",
                  targetNamespace = "http://troparo.org/services/MemberService/")
public class MemberService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://troparo.org/services/MemberService/", "MemberService");
    public final static QName MemberServicePort = new QName("http://troparo.org/services/MemberService/", "MemberServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MemberService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MemberService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/MemberService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MemberService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MemberService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MemberService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public MemberService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public MemberService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public MemberService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns IMemberService
     */
    @WebEndpoint(name = "MemberServicePort")
    public IMemberService getMemberServicePort() {
        return super.getPort(MemberServicePort, IMemberService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IMemberService
     */
    @WebEndpoint(name = "MemberServicePort")
    public IMemberService getMemberServicePort(WebServiceFeature... features) {
        return super.getPort(MemberServicePort, IMemberService.class, features);
    }

}
