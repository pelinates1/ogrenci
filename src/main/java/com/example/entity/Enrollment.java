package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Enrollment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi öğrenci?
    @ManyToOne
    private Users ogrenci;

    // Hangi dersi alıyor?
    @ManyToOne
    private Course ders;

    // Notları (Sınav olmadıysa boş kalabilmesi için Integer kullandık)
    private Integer vizeNotu;
    private Integer finalNotu;

    public Enrollment() {}

    // Getter ve Setter metotları
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Users getOgrenci() { return ogrenci; }
    public void setOgrenci(Users ogrenci) { this.ogrenci = ogrenci; }
    public Course getDers() { return ders; }
    public void setDers(Course ders) { this.ders = ders; }
    public Integer getVizeNotu() { return vizeNotu; }
    public void setVizeNotu(Integer vizeNotu) { this.vizeNotu = vizeNotu; }
    public Integer getFinalNotu() { return finalNotu; }
    public void setFinalNotu(Integer finalNotu) { this.finalNotu = finalNotu; }
}
