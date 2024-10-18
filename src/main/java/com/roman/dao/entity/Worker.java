package com.roman.dao.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity(name = "worker")
@Getter
@Setter
@ToString(exclude = {"personalToken", "state"})
@EqualsAndHashCode(of = "id")
public class Worker implements BaseEntity<Long>{

    @Id
    private Long id;

    @OneToOne(mappedBy = "worker",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PersonalInfo personalInfo;

    @OneToOne(mappedBy = "worker",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PersonalToken personalToken;

    @OneToOne(mappedBy = "worker",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private State state;

    public Worker(){}

    public Worker(Long id){
        this.id = id;
    }

}
