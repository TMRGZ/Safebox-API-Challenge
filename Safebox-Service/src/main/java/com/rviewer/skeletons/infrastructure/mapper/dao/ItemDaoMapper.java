package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.persistence.dao.ItemDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemDaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Item map(ItemDao itemDao) {
        return modelMapper.map(itemDao, Item.class);
    }

    public List<Item> map(List<ItemDao> itemDaoList) {
        return itemDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
