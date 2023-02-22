package com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {

    public Item map(String itemString) {
        Item item = new Item();
        item.setDetail(itemString);
        return item;
    }

    public List<Item> map(HolderItemListDto itemListDto) {
        return itemListDto.getItems().stream().map(this::map).toList();
    }

    public HolderItemListDto map(List<Item> itemList) {
        List<String> itemDetailList = itemList.stream().map(Item::getDetail).toList();
        return new HolderItemListDto().items(itemDetailList);
    }

}
