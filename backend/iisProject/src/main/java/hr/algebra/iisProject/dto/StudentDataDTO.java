package hr.algebra.iisProject.dto;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "student", namespace = "http://interoperabilnost.hr/students")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentDataDTO {

    @XmlElement(name = "Student_ID", namespace = "http://interoperabilnost.hr/students")
    private Integer studentId;

    @XmlElement(name = "Study_Hours_Per_Day", namespace = "http://interoperabilnost.hr/students")
    private Double studyHoursPerDay;

    @XmlElement(name = "Extracurricular_Hours_Per_Day", namespace = "http://interoperabilnost.hr/students")
    private Double extracurricularHoursPerDay;

    @XmlElement(name = "Sleep_Hours_Per_Day", namespace = "http://interoperabilnost.hr/students")
    private Double sleepHoursPerDay;

    @XmlElement(name = "Social_Hours_Per_Day", namespace = "http://interoperabilnost.hr/students")
    private Double socialHoursPerDay;

    @XmlElement(name = "Physical_Activity_Hours_Per_Day", namespace = "http://interoperabilnost.hr/students")
    private Double physicalActivityHoursPerDay;

    @XmlElement(name = "GPA", namespace = "http://interoperabilnost.hr/students")
    private Double gpa;

    @XmlElement(name = "Stress_Level", namespace = "http://interoperabilnost.hr/students")
    private String stressLevel;
}