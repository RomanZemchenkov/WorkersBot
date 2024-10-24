package com.roman.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Entity(name = "personal_token")
@Getter
@Setter
@ToString(exclude = {})
@EqualsAndHashCode(of = {"id","token"})
public class PersonalToken implements BaseEntity<Long>{

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(name = "token")
    private String token;

    @Column(name = "password")
    private String password;

    public PersonalToken(){}

    public PersonalToken(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public void setWorker(Worker worker){
        this.worker = worker;
        worker.setPersonalToken(this);
    }
}
