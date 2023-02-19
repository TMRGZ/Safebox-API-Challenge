package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.persistence.dao.ItemDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SafeboxDaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemDaoMapper itemDaoMapper;

    @PostConstruct
    public void setup() {
        TypeMap<SafeboxDao, Safebox> propertyMapper = modelMapper.createTypeMap(SafeboxDao.class, Safebox.class);
        Converter<List<ItemDao>, List<Item>> daoToDomain = dao -> itemDaoMapper.map(dao.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(daoToDomain).map(SafeboxDao::getItemList, Safebox::setItemList)
        );
    }

    public Safebox map(SafeboxDao safeboxDao) {
        return modelMapper.map(safeboxDao, Safebox.class);
    }

    public List<Safebox> map(List<SafeboxDao> safeboxDaoList) {
        return safeboxDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
