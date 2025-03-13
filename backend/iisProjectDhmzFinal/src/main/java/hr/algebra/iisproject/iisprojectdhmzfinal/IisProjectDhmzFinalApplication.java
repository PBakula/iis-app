package hr.algebra.iisproject.iisprojectdhmzfinal;

import hr.algebra.iisproject.iisprojectdhmzfinal.service.XmlRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class IisProjectDhmzFinalApplication {

    private final XmlRpcService xmlRpcService;

    public IisProjectDhmzFinalApplication(XmlRpcService xmlRpcService) {
        this.xmlRpcService = xmlRpcService;
    }

    public static void main(String[] args) {
        SpringApplication.run(IisProjectDhmzFinalApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startServer() {
        xmlRpcService.startXmlRpcServer();
    }
}
