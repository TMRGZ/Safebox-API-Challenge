package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyLockedException;
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
    public Safebox createSafebox(String owner) {
        safeboxRepository.findByOwner(owner).ifPresent(safebox -> {
            throw new SafeboxAlreadyExistsException();
        });

        Safebox safebox = new Safebox();
        safebox.setOwner(owner);
        safebox.setLocked(false);

        return safeboxRepository.save(safebox);
    }

    @Override
    public void addItemsToSafebox(Long safeboxId, List<Item> itemList) {
        Safebox safebox = safeboxRepository.findById(safeboxId).orElseThrow(SafeboxDoesNotExistException::new);
        safebox.getItemList().addAll(itemList);
        safeboxRepository.save(safebox);
    }

    @Override
    public void lockSafebox(Long id) {
        Safebox safeboxToLock = safeboxRepository.findById(id).orElseThrow(SafeboxDoesNotExistException::new);

        if (safeboxToLock.getLocked()) {
            throw new SafeboxAlreadyLockedException();
        }

        safeboxToLock.setLocked(true);

        safeboxRepository.save(safeboxToLock);
    }
}
