package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.rest.safebox.service.SafeboxServiceApi;
import com.rviewer.skeletons.infrastructure.service.impl.SafeboxServiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SafeboxServiceServiceImplUnitTest {

    @InjectMocks
    private SafeboxServiceServiceImpl safeboxServiceService;

    @Mock
    private SafeboxServiceApi safeboxServiceApi;

    @Test
    void getSafeboxUnitTest() {
        String id = "TEST";

        safeboxServiceService.getSafebox(id);

    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";

        safeboxServiceService.getSafeboxItems(id);

    }

    @Test
    void putSafeboxItemsUnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));

        safeboxServiceService.putSafeboxItems(id, itemList);
    }
}
