package com.rviewer.skeletons.infrastructure.persistence.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Entity(name = "SAFEBOX_USER")
public class UserDao {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "USERNAME", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @JoinColumn(name = "USER_ID")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserHistoryDao> userHistory;

}
