package com.roman.dao.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Table
@Entity(name = "company")
@Getter
@Setter
@ToString(exclude = {"workers"})
@EqualsAndHashCode(of = {"id"})
public class Company implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
    private Set<Worker> workers = new HashSet<>();

    public Company(){}

    public Company(String name) {
        this.name = name;
    }

}
