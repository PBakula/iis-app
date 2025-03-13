package hr.algebra.iisProject.repository;

import hr.algebra.iisProject.model.StudentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentDataRepository extends JpaRepository<StudentData, Integer>, JpaSpecificationExecutor<StudentData> {

    boolean existsByStudentId(Integer studentId);

    Optional<Object> findByStudentId(Integer studentId);

    List<StudentData> findByGpa(Double gpa);



}
