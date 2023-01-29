package com.rviewer.skeletons.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Safebox {

    private UUID id;

    private String owner;

    private List<Item> itemList;

}
