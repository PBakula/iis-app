package hr.algebra.iisProject.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "student")
public class StudentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "study_hours_per_day")
    private Double studyHoursPerDay;

    @Column(name = "extracurricular_hours_per_day")
    private Double extracurricularHoursPerDay;

    @Column(name = "sleep_hours_per_day")
    private Double sleepHoursPerDay;

    @Column(name = "social_hours_per_day")
    private Double socialHoursPerDay;

    @Column(name = "physical_activity_hours_per_day")
    private Double physicalActivityHoursPerDay;

    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "stress_level")
    private String stressLevel;


}
