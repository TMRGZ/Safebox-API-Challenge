package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.repository.SafeboxRepository;
import com.rviewer.skeletons.domain.service.SafeboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SafeboxServiceImpl implements SafeboxService {

    private SafeboxRepository safeboxRepository;

    @Override
    public Safebox getSafebox(String owner) {
        log.info("Finding safebox with owner {}", owner);

        return safeboxRepository.findByOwner(owner).orElseThrow(SafeboxDoesNotExistException::new);
    }

    @Override
    public Safebox createSafebox(String owner) {
        log.info("Trying to create safebox for {}", owner);

        safeboxRepository.findByOwner(owner).ifPresent(safebox -> {
            log.error("{} already has a safebox with id {}", owner, safebox.getId());
            throw new SafeboxAlreadyExistsException();
        });

        Safebox safebox = new Safebox();
        safebox.setOwner(owner);

        log.info("Saving new safebox for {}", owner);

        return safeboxRepository.save(safebox);
    }

    @Override
    public void addItemsToSafebox(String owner, List<Item> itemList) {
        log.info("Adding to safebox owned by {}, {} items", owner, itemList.size());

        Safebox safebox = safeboxRepository.findByOwner(owner).orElseThrow(SafeboxDoesNotExistException::new);

        log.info("Found safebox with id {}, adding {} items...", safebox.getId(), itemList.size());

        safebox.getItemList().addAll(itemList);
        safeboxRepository.save(safebox);
    }

    @Override
    public List<Item> getSafeboxItems(String owner) {
        log.info("Fetching items for safebox owned by {}", owner);

        Safebox safebox = safeboxRepository.findByOwner(owner).orElseThrow(SafeboxDoesNotExistException::new);

        log.info("{}'s safebox found with ID {}, fetching items...", owner, safebox.getId());

        return safebox.getItemList();
    }
}
