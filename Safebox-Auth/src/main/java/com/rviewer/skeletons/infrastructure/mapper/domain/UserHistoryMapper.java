package com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.user.UserHistory;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserHistoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserHistoryDao map(UserHistory userHistory) {
        return modelMapper.map(userHistory, UserHistoryDao.class);
    }

    public List<UserHistoryDao> map(List<UserHistory> userHistoryList) {
        return userHistoryList.stream().map(this::map).collect(Collectors.toList());
    }
}
