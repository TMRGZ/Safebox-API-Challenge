package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
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
public class UserDaoMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserHistoryDaoMapper userHistoryDaoMapper;

    @PostConstruct
    public void setup() {
        TypeMap<UserDao, SafeboxUser> propertyMapper = modelMapper.createTypeMap(UserDao.class, SafeboxUser.class);
        Converter<List<UserHistoryDao>, List<SafeboxUserHistory>> daoToDomain = dao -> userHistoryDaoMapper.map(dao.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(daoToDomain).map(UserDao::getUserHistory, SafeboxUser::setSafeboxUserHistory)
        );
    }

    public SafeboxUser map(UserDao userDao) {
        return modelMapper.map(userDao, SafeboxUser.class);
    }

    public List<SafeboxUser> map(List<UserDao> userDaoList) {
        return userDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
