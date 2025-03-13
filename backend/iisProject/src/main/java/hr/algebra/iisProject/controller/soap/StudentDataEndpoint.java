package hr.algebra.iisProject.controller.soap;

import hr.algebra.iisProject.controller.soap.request.FilterByXPathRequest;
import hr.algebra.iisProject.controller.soap.request.SearchByGpaRequest;
import hr.algebra.iisProject.controller.soap.request.ValidateXmlRequest;
import hr.algebra.iisProject.controller.soap.response.FilterByXPathResponse;
import hr.algebra.iisProject.controller.soap.response.SearchByGpaResponse;
import hr.algebra.iisProject.controller.soap.response.ValidateXmlResponse;
import hr.algebra.iisProject.dto.StudentDataDTO;
import hr.algebra.iisProject.dto.StudentsDataDTO;
import hr.algebra.iisProject.service.StudentDataService;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static hr.algebra.iisProject.controller.rest.XmlValidationController.logger;
import static hr.algebra.iisProject.utils.ValidationXML.validateAgainstXsdSoap;


@Endpoint
public class StudentDataEndpoint {

    private static final String NAMESPACE_URI = "http://interoperabilnost.hr/students";
    private final StudentDataService studentDataService;

    @Autowired
    public StudentDataEndpoint(StudentDataService studentDataService) {
        this.studentDataService = studentDataService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchByGpaRequest")
    @ResponsePayload
    public SearchByGpaResponse searchByGpa(@RequestPayload SearchByGpaRequest request) throws JAXBException {
        double gpa = request.getGpa();


        List<StudentDataDTO> students = studentDataService.findByGpa(gpa);
        String generatedXml = studentDataService.generateXml(students);


        String decodedXml = StringEscapeUtils.unescapeXml(generatedXml);

        SearchByGpaResponse response = new SearchByGpaResponse();
        response.setXmlContent(decodedXml);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "filterByXPathRequest")
    @ResponsePayload
    public FilterByXPathResponse filterByXPath(@RequestPayload FilterByXPathRequest request) throws Exception {
        String xpathExpression = request.getXPathExpression();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.compile(xpathExpression);

            List<StudentDataDTO> allStudents = studentDataService.findAll();
            String allStudentsXml = studentDataService.generateXml(allStudents);

            String filteredXml = studentDataService.filterXmlWithXPath(allStudentsXml, xpathExpression);

            FilterByXPathResponse response = new FilterByXPathResponse();
            response.setXmlContent(filteredXml);
            return response;

        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Neispravan XPath izraz. Provjerite unos i pokušajte ponovo.");
        }
    }

    private static String removeFilteredResultsWrapper(String xmlContent) {
        if (xmlContent.startsWith("<filteredResults")) {
            return xmlContent.replaceFirst("<filteredResults.*?>", "").replaceFirst("</filteredResults>", "").trim();
        }
        return xmlContent;
    }

    private String extractXmlContent(String soapMessage) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(soapMessage)));

            Node contentNode = document.getElementsByTagName("ns2:xmlContent").item(0);
            if (contentNode != null) {
                return contentNode.getTextContent();
            }
        } catch (Exception e) {
            logger.error("Error extracting XML content: ", e);
        }
        return null;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "validateXmlRequest")
    @ResponsePayload
    public ValidateXmlResponse validateXml(@RequestPayload ValidateXmlRequest request) {
        List<String> errors = new ArrayList<>();
        String xmlToValidate = request.getXml();

        ValidateXmlResponse response = new ValidateXmlResponse();

        if (xmlToValidate == null || xmlToValidate.isEmpty()) {
            response.setValid(false);
            response.setMessage("Nema XML-a za validaciju. Molimo pošaljite ispravan XML.");
            return response;
        }
        logger.info("Input XML before applying XPath: {}", xmlToValidate);


        xmlToValidate = removeFilteredResultsWrapper(xmlToValidate);
        logger.info("XML nakon uklanjanja wrappera: {}", xmlToValidate);
        boolean isValid = validateAgainstXsdSoap(xmlToValidate,StudentsDataDTO.class, errors);

        response.setValid(isValid);
        if (isValid) {
            response.setMessage("XML je validan.");
        } else {
            StringBuilder errorMessage = new StringBuilder("XML nije validan. Greške:\n");
            for (String error : errors) {
                errorMessage.append("- ").append(error).append("\n");
            }
            response.setMessage(errorMessage.toString());
        }

        return response;
    }
}