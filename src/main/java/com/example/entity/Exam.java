package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
public class Exam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Hangi dersin sınavı?

    @Column(nullable = false)
    private String examType; // Vize, Final, Bütünleme

    @Column(nullable = false)
    private LocalDateTime examDateTime; // Sınav tarihi ve saati

    private String classroom; // Sınav salonu / Sınıf

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public LocalDateTime getExamDateTime() { return examDateTime; }
    public void setExamDateTime(LocalDateTime examDateTime) { this.examDateTime = examDateTime; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
}
