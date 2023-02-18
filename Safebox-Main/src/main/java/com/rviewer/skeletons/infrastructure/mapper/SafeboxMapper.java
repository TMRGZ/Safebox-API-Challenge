package com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SafeboxMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Safebox map(HolderSafeboxDto safeboxDto) {
        return modelMapper.map(safeboxDto, Safebox.class);
    }
}
