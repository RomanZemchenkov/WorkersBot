package com.roman.dao.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Table
@Entity(name = "worker")
@Getter
@Setter
@ToString(exclude = {"personalToken", "state", "company", "meetings"})
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "Worker.withAll",
        attributeNodes = {
                @NamedAttributeNode(value = "personalInfo", subgraph = "personalInfoWithPost"),
                @NamedAttributeNode(value = "state")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "personalInfoWithPost",
                        attributeNodes = {
                                @NamedAttributeNode(value = "post")
                        })
        }
)
public class Worker implements BaseEntity<Long> {

    @Id
    private Long id;

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    private PersonalInfo personalInfo;

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL , optional = false)
    private PersonalToken personalToken;

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(mappedBy = "workers", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<Meeting> meetings = new HashSet<>();

    public Worker() {
    }

    public Worker(Long id) {
        this.id = id;
    }

    public void setCompany(Company company) {
        this.company = company;
        company.getWorkers().add(this);
    }


}
