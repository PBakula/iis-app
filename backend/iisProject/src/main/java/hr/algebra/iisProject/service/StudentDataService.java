package hr.algebra.iisProject.service;

import hr.algebra.iisProject.dto.StudentDataDTO;
import hr.algebra.iisProject.dto.StudentsDataDTO;
import hr.algebra.iisProject.model.ValidationType;
import jakarta.xml.bind.JAXBException;

import java.util.List;

public interface StudentDataService {


    List<StudentDataDTO> findAll();
    void saveStudent(StudentDataDTO studentDataDto);
    StudentsDataDTO mapXmlToDto(String xml) throws JAXBException;
    List<StudentDataDTO> findByGpa(double gpa);
    String generateXml(List<StudentDataDTO> students) throws JAXBException;
    String filterXmlWithXPath(String xml, String xPathExpression) throws Exception;
    void validateAndSave(String xmlContent, ValidationType validationType);
}