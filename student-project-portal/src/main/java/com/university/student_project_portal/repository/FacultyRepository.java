package com.university.student_project_portal.repository;

import com.university.student_project_portal.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    Faculty findByUser_Username(String username);
}
