package com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.domain.model.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InputItemMapper {


    public List<Item> map(ItemListDto itemListDto) {
        return itemListDto.getItems().stream().map(this::map).collect(Collectors.toList());
    }
    private Item map(String itemString) {
        Item item = new Item();
        item.setDetail(itemString);
        return item;
    }

    public ItemListDto map(List<Item> itemList) {
        List<String> itemDetails = itemList.stream().map(this::map).collect(Collectors.toList());
        return new ItemListDto().items(itemDetails);
    }

    private String map(Item item) {
        return item.getDetail();
    }
}
