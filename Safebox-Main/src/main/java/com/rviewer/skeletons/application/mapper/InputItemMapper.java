package com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.domain.model.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputItemMapper {

    public Item map(String itemDetail) {
        Item item = new Item();
        item.setDetail(itemDetail);
        return item;
    }

    public List<Item> map(ItemListDto itemListDto) {
        return itemListDto.getItems().stream().map(this::map).toList();
    }

    public ItemListDto map(List<Item> itemList) {
        List<String> itemDetailList = itemList.stream().map(Item::getDetail).toList();
        return new ItemListDto().items(itemDetailList);
    }
}
