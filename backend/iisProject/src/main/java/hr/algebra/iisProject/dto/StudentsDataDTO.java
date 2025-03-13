package hr.algebra.iisProject.dto;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@XmlRootElement(name = "students", namespace = "http://interoperabilnost.hr/students")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentsDataDTO {

    @XmlElement(name = "student", namespace = "http://interoperabilnost.hr/students")
    private List<StudentDataDTO> students;


}
