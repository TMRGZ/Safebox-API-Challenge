package com.rviewer.skeletons.infrastructure.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "ITEM")
public class ItemDao {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private UUID id;

    @Column(name = "DETAIL", nullable = false)
    private String detail;

}
