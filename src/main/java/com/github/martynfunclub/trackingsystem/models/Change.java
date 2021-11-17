package com.github.martynfunclub.trackingsystem.models;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "changes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Change {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "change", cascade = CascadeType.ALL)
    private Set<Production> productions;

    public void setStartTime(LocalDateTime date){
        this.startTime = date;
    }

    public void setEndTime(LocalDateTime date){
        this.endTime = date;
    }

    public void setProductions(Set<Production> production){
        this.productions = production;
    }

    public void setUser(User user) {this.user = user;}

}