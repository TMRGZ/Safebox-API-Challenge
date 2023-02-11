package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private String detail;

    public Item(String detail) {
        this.detail = detail;
    }
}
