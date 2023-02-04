package com.rviewer.skeletons.domain.model.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SafeboxUser {

    private String id;

    private String username;

    private String password;

    private List<SafeboxUserHistory> safeboxUserHistory;

}
