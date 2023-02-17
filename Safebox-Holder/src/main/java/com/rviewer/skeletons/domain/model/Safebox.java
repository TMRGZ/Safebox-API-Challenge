package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Safebox {

    private String id;

    private String owner;

    private List<Item> itemList;

}
