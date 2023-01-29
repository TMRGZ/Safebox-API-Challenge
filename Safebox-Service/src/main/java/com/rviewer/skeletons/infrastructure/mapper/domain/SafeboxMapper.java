package com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SafeboxMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SafeboxDao map(Safebox safebox) {
        return modelMapper.map(safebox, SafeboxDao.class);
    }

    public List<SafeboxDao> map(List<Safebox> safeboxList) {
        return safeboxList.stream().map(this::map).collect(Collectors.toList());
    }
}
