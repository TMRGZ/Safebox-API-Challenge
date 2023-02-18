package unit.com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.repository.SafeboxRepository;
import com.rviewer.skeletons.domain.service.impl.SafeboxServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SafeboxServiceImplUnitTest {

    @InjectMocks
    private SafeboxServiceImpl safeboxService;

    @Mock
    private SafeboxRepository safeboxRepository;

    @Test
    void createSafeboxUnitTest() {
        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.empty());

        safeboxService.createSafebox("TEST");

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
        Mockito.verify(safeboxRepository).save(Mockito.any());
    }

    @Test
    void createSafebox_alreadyExistingSafeboxException_UnitTest() {
        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.of(new Safebox()));

        Assertions.assertThrows(SafeboxAlreadyExistsException.class, () -> safeboxService.createSafebox("TEST"));

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
        Mockito.verify(safeboxRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        Safebox safebox = new Safebox();
        safebox.setItemList(new ArrayList<>());

        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.of(safebox));

        safeboxService.addItemsToSafebox("TEST", Collections.singletonList(new Item("TEST")));

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
        Mockito.verify(safeboxRepository).save(Mockito.any());
    }

    @Test
    void addItemsToSafebox_safeboxDoesNotExist_UnitTest() {
        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.empty());
        List<Item> itemList = Collections.singletonList(new Item("TEST"));

        Assertions.assertThrows(SafeboxDoesNotExistException.class,
                () -> safeboxService.addItemsToSafebox("TEST", itemList));

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
        Mockito.verify(safeboxRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.of(new Safebox()));

        safeboxService.getSafeboxItems("TEST");

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
    }

    @Test
    void getSafeboxItems_safeboxDoesNotExist_UnitTest() {
        Mockito.when(safeboxRepository.findByOwner(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(SafeboxDoesNotExistException.class,
                () -> safeboxService.getSafeboxItems("TEST"));

        Mockito.verify(safeboxRepository).findByOwner(Mockito.anyString());
    }
}
