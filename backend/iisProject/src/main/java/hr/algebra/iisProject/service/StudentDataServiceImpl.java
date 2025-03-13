package hr.algebra.iisProject.service;

import hr.algebra.iisProject.dto.StudentDataDTO;
import hr.algebra.iisProject.dto.StudentsDataDTO;
import hr.algebra.iisProject.model.StudentData;
import hr.algebra.iisProject.model.ValidationType;
import hr.algebra.iisProject.repository.StudentDataRepository;
import hr.algebra.iisProject.utils.ValidationXML;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentDataServiceImpl implements StudentDataService {

   private final StudentDataRepository studentDataRepository;

    public StudentDataServiceImpl(StudentDataRepository studentDataRepository) {
        this.studentDataRepository = studentDataRepository;
    }

    public void validateAndSave(String xmlContent, ValidationType validationType) {
        List<String> errors = new ArrayList<>();

        boolean isValid = switch (validationType) {
            case XSD -> ValidationXML.validateAgainstXsd(xmlContent, errors);
            case RNG -> ValidationXML.validateAgainstRng(xmlContent, errors);
        };

        if (!isValid) {
            throw new IllegalArgumentException("Greške prilikom validacije:\n" + String.join("\n", errors));
        }

        StudentsDataDTO studentsDataDTO;
        try {
            studentsDataDTO = mapXmlToDto(xmlContent);

            for (StudentDataDTO studentDataDTO : studentsDataDTO.getStudents()) {
                saveStudent(studentDataDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException("Greška pri mapiranju XML-a: " + e.getMessage());
        }
    }


    @Override
    public void saveStudent(StudentDataDTO data) {

        StudentData studentData = mapDtoToEntity(data);
        studentDataRepository.save(studentData);
    }


    @Override
    public List<StudentDataDTO> findAll() {
        List<StudentData> students = studentDataRepository.findAll();
        return students.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


        @Override
        public List<StudentDataDTO> findByGpa(double gpa) {
            List<StudentData> students = studentDataRepository.findByGpa(gpa);
            return students.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        @Override
        public String generateXml(List<StudentDataDTO> students) throws JAXBException {
            JAXBContext context = JAXBContext.newInstance(StudentsDataDTO.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StudentsDataDTO wrapper = new StudentsDataDTO();
            wrapper.setStudents(students);

            StringWriter writer = new StringWriter();
            marshaller.marshal(wrapper, writer);
            return writer.toString();
        }

    @Override
    public String filterXmlWithXPath(String xml, String xPathExpression) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expression = xPath.compile(xPathExpression);

        NodeList nodes = (NodeList) expression.evaluate(document, XPathConstants.NODESET);


        Document filteredDoc = builder.newDocument();


        Element root = filteredDoc.createElementNS("http://interoperabilnost.hr/students", "students");
        filteredDoc.appendChild(root);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node importedNode = filteredDoc.importNode(nodes.item(i), true);


            if (importedNode.getNodeType() == Node.ELEMENT_NODE) {
                fixNamespace((Element) importedNode, "http://interoperabilnost.hr/students");
            }

            root.appendChild(importedNode);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(filteredDoc), new StreamResult(writer));

        return writer.toString();
    }


    private void fixNamespace(Element element, String namespaceUri) {
        if (!namespaceUri.equals(element.getNamespaceURI())) {
            element.setAttribute("xmlns", namespaceUri);
        }
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                fixNamespace((Element) children.item(i), namespaceUri);
            }
        }
    }


    private StudentDataDTO mapToDto(StudentData student) {

        return new StudentDataDTO(
                    student.getStudentId(),
                    student.getStudyHoursPerDay(),
                    student.getExtracurricularHoursPerDay(),
                    student.getSleepHoursPerDay(),
                    student.getSocialHoursPerDay(),
                    student.getPhysicalActivityHoursPerDay(),
                    student.getGpa(),
                    student.getStressLevel()
        );
    }

    public StudentData mapDtoToEntity(StudentDataDTO dto) {
        return new StudentData(
                null,
                dto.getStudentId(),
                dto.getStudyHoursPerDay(),
                dto.getExtracurricularHoursPerDay(),
                dto.getSleepHoursPerDay(),
                dto.getSocialHoursPerDay(),
                dto.getPhysicalActivityHoursPerDay(),
                dto.getGpa(),
                dto.getStressLevel()
        );
    }

    public StudentsDataDTO mapXmlToDto(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(StudentsDataDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();


        unmarshaller.setEventHandler(event -> {
            System.err.println("Validation Error: " + event.getMessage());
            if (event.getLocator() != null) {
                System.err.println("At line: " + event.getLocator().getLineNumber() +
                        ", column: " + event.getLocator().getColumnNumber());
            }
            return false;
        });

        return (StudentsDataDTO) unmarshaller.unmarshal(new StringReader(xml));
    }
}







