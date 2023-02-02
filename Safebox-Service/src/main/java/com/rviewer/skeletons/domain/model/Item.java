package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private String id;

    private String detail;

    public Item(String detail) {
        this.detail = detail;
    }
}
