package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.infrastructure.mapper.ItemMapper;
import com.rviewer.skeletons.infrastructure.mapper.SafeboxMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Slf4j
@Service
public class SafeboxHolderServiceImpl implements SafeboxHolderService {

    @Autowired
    private SafeboxHolderApi safeboxHolderApi;

    @Autowired
    private SafeboxMapper safeboxMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Safebox getSafebox(String id) {
        Safebox safebox;

        try {
            HolderSafeboxDto safeboxDto = safeboxHolderApi.getSafebox(id);
            safebox = safeboxMapper.map(safeboxDto);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SafeboxDoesNotExistException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return safebox;
    }

    @Override
    public List<Item> getSafeboxItems(String id) {
        List<Item> itemList;

        try {
            HolderItemListDto safeboxItems = safeboxHolderApi.getSafeboxItems(id);
            itemList = itemMapper.map(safeboxItems);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SafeboxDoesNotExistException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return itemList;
    }

    @Override
    public void putSafeboxItems(String id, List<Item> itemList) {

        try {
            log.info("Attempting to add {} items to safebox {}", itemList.size(), id);

            safeboxHolderApi.putSafeboxItems(id, itemMapper.map(itemList));

        } catch (HttpClientErrorException e) {
            log.error("Client error {} while attempting to add {} items to {}", e.getStatusCode(), itemList.size(), id);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SafeboxDoesNotExistException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            log.error("Server error {} while attempting to add {} items to {}", e.getStatusCode(), itemList.size(), id);

            throw new ExternalServiceException();
        } catch (ResourceAccessException e) {
            log.error("Unknown error while attempting to add items", e);
            throw new ExternalServiceException();
        }

        log.info("Added {} items to {} successfully", itemList.size(), id);
    }
}
