package com.roman.dao.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

@Table
@Entity(name = "worker")
@Getter
@Setter
@ToString(exclude = {"personalToken", "state","company"})
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

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PersonalInfo personalInfo;

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PersonalToken personalToken;

    @OneToOne(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private State state;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Worker() {
    }

    public Worker(Long id) {
        this.id = id;
    }

    public void setCompany(Company company){
        this.company = company;
        company.getWorkers().add(this);
    }


}
