package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
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

    public SafeboxUserHistory map(UserHistoryDao userHistoryDao) {
        return modelMapper.map(userHistoryDao, SafeboxUserHistory.class);
    }

    public List<SafeboxUserHistory> map(List<UserHistoryDao> userHistoryDaoList) {
        return userHistoryDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
