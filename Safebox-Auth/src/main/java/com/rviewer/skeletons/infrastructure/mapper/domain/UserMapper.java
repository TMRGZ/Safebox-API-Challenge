package com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
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
        TypeMap<SafeboxUser, UserDao> propertyMapper = modelMapper.createTypeMap(SafeboxUser.class, UserDao.class);
        Converter<List<SafeboxUserHistory>, List<UserHistoryDao>> domainToDao = domain -> userHistoryMapper.map(domain.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).using(domainToDao).map(SafeboxUser::getSafeboxUserHistory, UserDao::setUserHistory)
        );
    }

    public UserDao map(SafeboxUser safeboxUser) {
        return modelMapper.map(safeboxUser, UserDao.class);
    }

    public List<UserDao> map(List<SafeboxUser> safeboxUserList) {
        return safeboxUserList.stream().map(this::map).collect(Collectors.toList());
    }
}
