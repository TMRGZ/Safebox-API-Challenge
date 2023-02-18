package com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputUserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public User map(CreateSafeboxRequestDto createSafeboxRequestDto) {
        return modelMapper.map(createSafeboxRequestDto, User.class);
    }
}
