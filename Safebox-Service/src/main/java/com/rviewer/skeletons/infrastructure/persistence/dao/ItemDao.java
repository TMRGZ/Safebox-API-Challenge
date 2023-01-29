package com.rviewer.skeletons.infrastructure.persistence.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class ItemDao {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "DETAIL", nullable = false)
    private String detail;

}
