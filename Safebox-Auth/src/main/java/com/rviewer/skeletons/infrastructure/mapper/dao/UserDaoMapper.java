package com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.model.user.UserHistory;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
        TypeMap<UserDao, User> propertyMapper = modelMapper.createTypeMap(UserDao.class, User.class);
        Converter<List<UserHistoryDao>, List<UserHistory>> daoToDomain = dao -> userHistoryDaoMapper.map(dao.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(daoToDomain).map(UserDao::getUserHistory, User::setUserHistory)
        );
    }

    public User map(UserDao userDao) {
        return modelMapper.map(userDao, User.class);
    }

    public List<User> map(List<UserDao> userDaoList) {
        return userDaoList.stream().map(this::map).collect(Collectors.toList());
    }
}
