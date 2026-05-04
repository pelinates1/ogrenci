package com.example.entity;

import com.example.enums.FreezeStatusEnum;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class FreezeRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Users student;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    private String reason;

    @Enumerated(EnumType.STRING)
    private FreezeStatusEnum status;

    public FreezeRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Users getStudent() { return student; }
    public void setStudent(Users student) { this.student = student; }
    
    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public FreezeStatusEnum getStatus() { return status; }
    public void setStatus(FreezeStatusEnum status) { this.status = status; }
}
