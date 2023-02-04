package com.rviewer.skeletons.infrastructure.persistence.dao;

import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "USER_HISTORY")
public class UserHistoryDao {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "EVENT_DATE", nullable = false, updatable = false)
    private Date eventDate;

    @Column(name = "EVENT_TYPE", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private EventTypeEnum eventTypeEnum;

    @Column(name = "EVENT_RESULT", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private EventResultEnum eventResultEnum;

    @Column(name = "LOCKED", nullable = false, updatable = false)
    private Boolean locked;

    @Column(name = "CURRENT_TRIES", nullable = false, updatable = false)
    private Long currentTries;

}
