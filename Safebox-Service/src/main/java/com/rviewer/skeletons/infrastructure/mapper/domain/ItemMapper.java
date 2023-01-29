package com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.persistence.dao.ItemDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ItemDao map(Item item) {
        return modelMapper.map(item, ItemDao.class);
    }

    public List<ItemDao> map(List<Item> itemList) {
        return itemList.stream().map(this::map).collect(Collectors.toList());
    }
}
