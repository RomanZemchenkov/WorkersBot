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
@Entity(name = "post")
@Getter
@Setter
@ToString(exclude = {"personalInfos"})
@EqualsAndHashCode(of = {"title"})
public class Post implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private Set<PersonalInfo> personalInfos = new HashSet<>();

    public Post(){}

    public Post(String title) {
        this.title = title;
    }
}
