package com.university.student_project_portal.repository;




import com.university.student_project_portal.model.Faculty;
import com.university.student_project_portal.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByFaculty(Faculty faculty);
}
