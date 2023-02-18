package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.repository.SafeboxRepository;
import com.rviewer.skeletons.domain.service.SafeboxService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SafeboxServiceImpl implements SafeboxService {

    private SafeboxRepository safeboxRepository;

    @Override
    public Safebox getSafebox(String owner) {
        return safeboxRepository.findByOwner(owner).orElseThrow(SafeboxDoesNotExistException::new);
    }

    @Override
    public Safebox createSafebox(String owner) {
        safeboxRepository.findByOwner(owner).ifPresent(safebox -> {
            throw new SafeboxAlreadyExistsException();
        });

        Safebox safebox = new Safebox();
        safebox.setOwner(owner);

        return safeboxRepository.save(safebox);
    }

    @Override
    public void addItemsToSafebox(String safeboxId, List<Item> itemList) {
        Safebox safebox = safeboxRepository.findByOwner(safeboxId).orElseThrow(SafeboxDoesNotExistException::new);
        safebox.getItemList().addAll(itemList);
        safeboxRepository.save(safebox);
    }

    @Override
    public List<Item> getSafeboxItems(String safeboxId) {
        Safebox safebox = safeboxRepository.findByOwner(safeboxId).orElseThrow(SafeboxDoesNotExistException::new);
        return safebox.getItemList();
    }
}
