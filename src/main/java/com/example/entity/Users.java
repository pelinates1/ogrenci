package com.example.entity;

import com.example.enums.RoleEnum;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String okulNo; // Giriş yaparken bunu ve şifreyi kullanacağız

    private String ad;
    private String soyad;
    private String sifre;

    @Enumerated(EnumType.STRING)
    private RoleEnum rol; // ADMIN, TEACHER veya STUDENT

    // Boş kurucu (JPA için zorunludur)
    public Users() {}

    // Getter ve Setter metotları
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOkulNo() { return okulNo; }
    public void setOkulNo(String okulNo) { this.okulNo = okulNo; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }
    public RoleEnum getRol() { return rol; }
    public void setRol(RoleEnum rol) { this.rol = rol; }
}