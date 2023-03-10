package com.rviewer.skeletons.infrastructure.persistence.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "SAFEBOX_USER")
public class UserDao {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "USERNAME", nullable = false, unique = true, updatable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @JoinColumn(name = "USER_ID")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserHistoryDao> userHistory;

}
