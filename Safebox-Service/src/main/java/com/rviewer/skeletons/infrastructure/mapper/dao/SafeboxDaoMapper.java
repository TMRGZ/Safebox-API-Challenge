package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SafeboxDaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Safebox map(SafeboxDao safeboxDao) {
        return modelMapper.map(safeboxDao, Safebox.class);
    }

    public List<Safebox> map(List<SafeboxDao> safeboxDaoList) {
        return safeboxDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
