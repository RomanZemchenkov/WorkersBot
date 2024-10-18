package com.roman.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Table
@Entity(name = "personal_info")
@Getter
@Setter
@ToString(exclude = {"worker"})
@EqualsAndHashCode(of = {"id"})
public class PersonalInfo implements BaseEntity<Long>{

    @Id
    @Column(name = "worker_id")
    private Long id;

    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "username")
    private String username;
    @Column(name = "birthday")
    private LocalDate birthday;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "worker_id")
    private Worker worker;

    public PersonalInfo(){}

    public PersonalInfo(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }

    public void setCompany(Company company){
        this.company = company;
        company.getPersonalInfo().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getWorkers().add(this);
    }

    public void setWorker(Worker worker){
        this.worker = worker;
        worker.setPersonalInfo(this);
    }
}
