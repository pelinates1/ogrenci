package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class AdvisorAssignment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Users teacher;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String studentClass;

    public AdvisorAssignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Users getTeacher() { return teacher; }
    public void setTeacher(Users teacher) { this.teacher = teacher; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getStudentClass() { return studentClass; }
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }
}
