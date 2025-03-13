package hr.algebra.iisProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.xml.validation.SchemaFactory;

@SpringBootApplication
public class IisProjectApplication {

    public static void main(String[] args) {
        System.setProperty(
                SchemaFactory.class.getName() + ":" + "http://relaxng.org/ns/structure/1.0",
                "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory"
        );
        SpringApplication.run(IisProjectApplication.class, args);
    }



}
