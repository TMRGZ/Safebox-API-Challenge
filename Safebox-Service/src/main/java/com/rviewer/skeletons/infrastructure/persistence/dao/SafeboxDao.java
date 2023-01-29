package com.rviewer.skeletons.infrastructure.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity(name = "SAFEBOX")
public class SafeboxDao {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "OWNER", unique = true, nullable = false)
    private String owner;

    @Column(name = "LOCKED", nullable = false)
    private Boolean locked;

    @OneToMany
    private List<ItemDao> itemList;


}
