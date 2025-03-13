package hr.algebra.iisProject.utils;

import com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEventLocator;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static hr.algebra.iisProject.controller.rest.XmlValidationController.logger;

public class ValidationXML {

    public static boolean validateAgainstXsd(String xml, List<String> errors) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(
                    new ClassPathResource("schemas/schema2.xsd").getInputStream()
            ));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (SAXParseException e) {
            String message = e.getMessage();
            String cleanedMessage = cleanErrorMessage(message);


            errors.add("XML line " + e.getLineNumber() + ": " + cleanedMessage);
            return false;
        } catch (SAXException | IOException e) {
            errors.add("XSD validation error - " + e.getMessage());
            return false;
        }
    }

    private static String cleanErrorMessage(String message) {
        if (message.contains("cvc-complex-type.2.4.a")) {
            return message.replaceAll(
                    ".*Invalid content was found starting with element '(.*?)'.*One of '(.*?)' is expected.*",
                    "Invalid content: unexpected element '<$1>', expected one of <$2>."
            ).replaceAll("<>", "[missing element]");
        } else if (message.contains("cvc-datatype-valid.1.2.1")) {
            return message.replaceAll(
                    ".*'(.*?)' is not a valid value for '(.*?)'.*",
                    "Invalid value '<$1>' for field '<$2>'."
            );
        } else if (message.contains("unexpected element")) {
            return message.replaceAll(
                    ".*unexpected element '(.*?)'.*expected one of '(.*?)'.*",
                    "Unexpected element '<$1>', expected one of <$2>."
            );
        }
        return message;
    }

    public static boolean validateAgainstRng(String xml, List<String> errors) {
        try {
            SchemaFactory factory = new XMLSyntaxSchemaFactory();
            Schema schema = factory.newSchema(new StreamSource(new ClassPathResource("schemas/schema2.rng").getInputStream()));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            return true;
        } catch (SAXParseException e) {
            String message = e.getMessage();
            String cleanedMessage = cleanErrorMessage(message);


            errors.add("XML line " + e.getLineNumber() + ": " + cleanedMessage + ". ");
            return false;
        } catch (SAXException | IOException e) {
            errors.add("RNG validation error - " + e.getMessage() + ". ");
            return false;
        }
    }

    public static boolean validateAgainstXsdSoap(String soapMessage, Class<?> clazz, List<String> errors) {

        try {

            logger.info("Received SOAP Message: {}", soapMessage);

            String xmlContent = extractSoapBodyContent(soapMessage);

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(new ClassPathResource("schemas/schema2.xsd").getInputStream()));

            unmarshaller.setSchema(schema);

            logger.info("XML content extracted for validation: {}", xmlContent);

            unmarshaller.setEventHandler(event -> {
                ValidationEventLocator locator = event.getLocator();
                String errorMessage = String.format("Validation error at line %d, column %d: %s",
                        locator.getLineNumber(),
                        locator.getColumnNumber(),
                        event.getMessage());
                errors.add(errorMessage);
                return false;
            });

            unmarshaller.unmarshal(new StringReader(xmlContent));
            return true;

        } catch (JAXBException | SAXException | IOException e) {
            errors.add("JAXB XSD validation error: " + e.getMessage());
            return false;
        }
    }


    private static String extractSoapBodyContent(String soapMessage) {
        try {

            if (!soapMessage.contains("<soapenv:Envelope")) {
                return soapMessage.trim();
            }


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(soapMessage)));

            Node bodyNode = document.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body").item(0);
            if (bodyNode != null) {
                return bodyNode.getTextContent().trim();
            }
            throw new IllegalArgumentException("SOAP Body not found in the message.");
        } catch (Exception e) {
            throw new RuntimeException("Error extracting SOAP Body content: " + e.getMessage(), e);
        }
    }





}
