package com.university.student_project_portal.repository;

import com.university.student_project_portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByUser_Username(String username);
}
