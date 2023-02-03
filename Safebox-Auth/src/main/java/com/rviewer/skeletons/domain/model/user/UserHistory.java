package com.rviewer.skeletons.domain.model.user;

import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserHistory {

    private String id;

    private Date eventDate;

    private EventTypeEnum eventTypeEnum;

    private EventResultEnum eventResultEnum;

    private Boolean locked;

    private Long currentTries;

    private String userId;

}
