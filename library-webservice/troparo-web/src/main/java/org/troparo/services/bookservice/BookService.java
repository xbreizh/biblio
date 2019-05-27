package org.troparo.services.bookservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-27T11:06:01.596+02:00
 * Generated source version: 3.2.7
 *
 */
@WebServiceClient(name = "BookService",
                  wsdlLocation = "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/BookService.wsdl",
                  targetNamespace = "http://troparo.org/services/BookService/")
public class BookService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://troparo.org/services/BookService/", "BookService");
    public final static QName BookServicePort = new QName("http://troparo.org/services/BookService/", "BookServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/BookService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(BookService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/john/Documents/openClassrooms/Project_10/biblio/library-webservice/troparo-web/src/main/resources/org/troparo/web/services/BookService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public BookService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BookService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BookService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public BookService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public BookService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public BookService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns IBookService
     */
    @WebEndpoint(name = "BookServicePort")
    public IBookService getBookServicePort() {
        return super.getPort(BookServicePort, IBookService.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IBookService
     */
    @WebEndpoint(name = "BookServicePort")
    public IBookService getBookServicePort(WebServiceFeature... features) {
        return super.getPort(BookServicePort, IBookService.class, features);
    }

}
