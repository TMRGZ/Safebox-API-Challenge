package com.rviewer.skeletons.infrastructure.persistence.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "ITEM")
public class ItemDao {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "DETAIL", nullable = false)
    private String detail;

}
