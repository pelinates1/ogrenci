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
    private String bolum; // Yeni: Bölüm bilgisi
    private String sinif; // Yeni: Öğrenci için sınıf bilgisi
    private String unvan; // Yeni: Öğretmen için unvan (Prof, Doç vb.)

    @Enumerated(EnumType.STRING)
    private RoleEnum rol; // ADMIN, TEACHER veya STUDENT

    private boolean aktif = true; 

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
    public String getBolum() { return bolum; }
    public void setBolum(String bolum) { this.bolum = bolum; }
    public String getSinif() { return sinif; }
    public void setSinif(String sinif) { this.sinif = sinif; }
    public String getUnvan() { return unvan; }
    public void setUnvan(String unvan) { this.unvan = unvan; }
    public RoleEnum getRol() { return rol; }
    public void setRol(RoleEnum rol) { this.rol = rol; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}