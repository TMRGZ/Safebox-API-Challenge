package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Safebox {

    private Long id;

    private String owner;

    private Boolean locked;

    private List<Item> itemList;

}
