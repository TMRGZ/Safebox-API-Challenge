package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;

import java.util.List;

public interface SafeboxHolderService {

    void getSafebox(String id);

    void getSafeboxItems(String id);

    void putSafeboxItems(String id, List<Item> itemList);

}
