package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Etkinlik Başlığı (Örn: Güz Dönemi Başlangıcı)

    @Column(nullable = false)
    private LocalDate eventDate; // Etkinlik Tarihi

    private String description; // Detaylı bilgi

    @Column(name = "event_type")
    private String type; // Tatil, Duyuru, Kayıt Haftası vb.

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
