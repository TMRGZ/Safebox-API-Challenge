package com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.persistence.dao.ItemDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SafeboxMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemMapper itemMapper;

    @PostConstruct
    public void setup() {
        TypeMap<Safebox, SafeboxDao> propertyMapper = modelMapper.createTypeMap(Safebox.class, SafeboxDao.class);
        Converter<List<Item>, List<ItemDao>> domainToDao = domain -> itemMapper.map(domain.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(domainToDao).map(Safebox::getItemList, SafeboxDao::setItemList)
        );
    }

    public SafeboxDao map(Safebox safebox) {
        return modelMapper.map(safebox, SafeboxDao.class);
    }

    public List<SafeboxDao> map(List<Safebox> safeboxList) {
        return safeboxList.stream().map(this::map).toList();
    }
}
