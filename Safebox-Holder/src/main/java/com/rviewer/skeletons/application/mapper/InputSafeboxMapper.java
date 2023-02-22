package com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.domain.model.Safebox;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputSafeboxMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SafeboxDto map(Safebox safebox) {
        return modelMapper.map(safebox, SafeboxDto.class);
    }
}
