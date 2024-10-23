package com.roman.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity(name = "meeting")
@Getter
@Setter
@ToString(exclude = {"workers"})
@EqualsAndHashCode(of = {"id"})
public class Meeting implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "worker_meeting",
            joinColumns = {@JoinColumn(name = "meeting_id")},
            inverseJoinColumns = {@JoinColumn(name = "worker_id")}
    )
    private List<Worker> workers = new ArrayList<>();

    public Meeting(){}

    public Meeting(String title, LocalDateTime time, List<Worker> workers) {
        this.title = title;
        this.time = time;
        this.workers = workers;
    }

    //Подумать, нужно ли делать сеттер, чтобы сразу назначать каждому работнику встречу
}
