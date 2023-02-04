package com.rviewer.skeletons.infrastructure.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "USER")
public class UserDao {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @JoinColumn(name = "USER_ID")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserHistoryDao> userHistory;

}
