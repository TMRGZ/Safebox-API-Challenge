package com.rviewer.skeletons.infrastructure.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "SAFEBOX")
public class SafeboxDao {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private UUID id;

    @Column(name = "OWNER", unique = true, nullable = false)
    private String owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SAFEBOX_ID")
    private List<ItemDao> itemList;


}
