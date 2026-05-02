package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dersKodu;
    private String dersAdi;

    // Bir dersin BİR öğretmeni olur (ManyToOne ilişkisi)
    @ManyToOne
    private Users ogretmen;

    public Course() {}

    // Getter ve Setter metotları
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDersKodu() { return dersKodu; }
    public void setDersKodu(String dersKodu) { this.dersKodu = dersKodu; }
    public String getDersAdi() { return dersAdi; }
    public void setDersAdi(String dersAdi) { this.dersAdi = dersAdi; }
    public Users getOgretmen() { return ogretmen; }
    public void setOgretmen(Users ogretmen) { this.ogretmen = ogretmen; }
}
