package com.roman.dao.entity;

import com.roman.Stages;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity(name = "state")
@Getter
@Setter
@ToString(exclude = {})
@EqualsAndHashCode(of = {"id"})
public class State implements BaseEntity<Long>{

    @Id
    @Column(name = "worker_id")
    private Long id;

    @Column(name = "stage")
    @Enumerated(value = EnumType.STRING)
    private Stages stage;

    @Column(name = "state")
    private String state;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "worker_id")
    private Worker worker;

    public State(){}

    public void setWorker(Worker worker){
        this.worker = worker;
        worker.setState(this);
    }
}
