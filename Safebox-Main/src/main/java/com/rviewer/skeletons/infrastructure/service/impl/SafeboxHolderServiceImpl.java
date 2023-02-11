package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafeboxHolderServiceImpl implements SafeboxHolderService {

    @Autowired
    private SafeboxHolderApi safeboxHolderApi;

    @Override
    public Safebox getSafebox(String id) {
        HolderSafeboxDto safeboxDto = safeboxHolderApi.getSafebox(id);

        Safebox safebox = new Safebox();
        safebox.setId(safeboxDto.getId());

        return safebox;
    }

    @Override
    public List<Item> getSafeboxItems(String id) {
        HolderItemListDto safeboxItems = safeboxHolderApi.getSafeboxItems(id);
        return safeboxItems.getItems().stream().map(Item::new).toList();
    }

    @Override
    public void putSafeboxItems(String id, List<Item> itemList) {
        List<String> itemDetails = itemList.stream().map(Item::getDetail).toList();
        safeboxHolderApi.putSafeboxItems(id, new HolderItemListDto().items(itemDetails));
    }
}
