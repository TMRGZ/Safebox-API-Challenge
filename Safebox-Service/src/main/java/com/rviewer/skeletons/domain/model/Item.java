package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private UUID id;

    private String detail;

    public Item(String detail) {
        this.detail = detail;
    }
}
