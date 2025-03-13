package hr.algebra.iisproject.iisprojectdhmzfinal.service;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;
import org.springframework.stereotype.Service;

@Service
public class XmlRpcService {

    private final DhmzService dhmzService;

    public XmlRpcService(DhmzService dhmzService) {
        this.dhmzService = dhmzService;
    }

    public void startXmlRpcServer() {
        try {
            WebServer server = new WebServer(8085); // Port za XML-RPC poslužitelj
            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("WeatherService", dhmzService.getClass());
            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();
            System.out.println("XML-RPC poslužitelj pokrenut na portu 8085");
        } catch (Exception e) {
            throw new RuntimeException("Greška pri pokretanju XML-RPC poslužitelja", e);
        }
    }
}
