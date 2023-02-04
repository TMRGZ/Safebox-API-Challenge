package com.rviewer.skeletons.infrastructure.mapper.domain;

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
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserHistoryMapper userHistoryMapper;

    @PostConstruct
    public void setup() {
        TypeMap<User, UserDao> propertyMapper = modelMapper.createTypeMap(User.class, UserDao.class);
        Converter<List<UserHistory>, List<UserHistoryDao>> domainToDao = domain -> userHistoryMapper.map(domain.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(domainToDao).map(User::getUserHistory, UserDao::setUserHistory)
        );
    }

    public UserDao map(User user) {
        return modelMapper.map(user, UserDao.class);
    }

    public List<UserDao> map(List<User> userList) {
        return userList.stream().map(this::map).collect(Collectors.toList());
    }
}
