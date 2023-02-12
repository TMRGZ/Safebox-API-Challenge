package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
public class SafeboxHolderServiceImpl implements SafeboxHolderService {

    @Autowired
    private SafeboxHolderApi safeboxHolderApi;

    @Override
    public Safebox getSafebox(String id) {
        Safebox safebox;

        try {
            HolderSafeboxDto safeboxDto = safeboxHolderApi.getSafebox(id);
            safebox = new Safebox();
            safebox.setId(safeboxDto.getId());

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
        HolderItemListDto safeboxItems;

        try {
            safeboxItems = safeboxHolderApi.getSafeboxItems(id);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SafeboxDoesNotExistException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return safeboxItems.getItems().stream().map(Item::new).toList();
    }

    @Override
    public void putSafeboxItems(String id, List<Item> itemList) {
        List<String> itemDetails = itemList.stream().map(Item::getDetail).toList();

        try {
            safeboxHolderApi.putSafeboxItems(id, new HolderItemListDto().items(itemDetails));

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SafeboxDoesNotExistException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }
    }
}
