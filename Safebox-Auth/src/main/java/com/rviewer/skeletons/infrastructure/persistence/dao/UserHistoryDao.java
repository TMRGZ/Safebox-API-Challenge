package com.rviewer.skeletons.infrastructure.persistence.dao;

import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "SAFEBOX_USER_HISTORY")
public class UserHistoryDao {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
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
