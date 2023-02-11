package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.service.impl.SafeboxHolderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SafeboxHolderServiceImplUnitTest {

    @InjectMocks
    private SafeboxHolderServiceImpl safeboxHolderService;

    @Mock
    private SafeboxHolderApi safeboxHolderApi;

    @Test
    void getSafeboxUnitTest() {
        String id = "TEST";

        safeboxHolderService.getSafebox(id);

    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";

        safeboxHolderService.getSafeboxItems(id);

    }

    @Test
    void putSafeboxItemsUnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));

        safeboxHolderService.putSafeboxItems(id, itemList);
    }
}
