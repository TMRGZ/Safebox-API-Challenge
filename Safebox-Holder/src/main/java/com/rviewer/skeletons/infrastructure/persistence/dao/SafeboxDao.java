package com.rviewer.skeletons.infrastructure.persistence.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "SAFEBOX")
public class SafeboxDao {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "OWNER", unique = true, nullable = false)
    private String owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SAFEBOX_ID")
    private List<ItemDao> itemList;


}
