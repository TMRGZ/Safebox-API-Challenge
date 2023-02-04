package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.UserHistory;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserHistoryDaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserHistory map(UserHistoryDao userHistoryDao) {
        return modelMapper.map(userHistoryDao, UserHistory.class);
    }

    public List<UserHistory> map(List<UserHistoryDao> userHistoryDaoList) {
        return userHistoryDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
