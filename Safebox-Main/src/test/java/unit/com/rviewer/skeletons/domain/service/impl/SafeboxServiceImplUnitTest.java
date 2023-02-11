package unit.com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.SafeboxServiceService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.domain.service.impl.SafeboxServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SafeboxServiceImplUnitTest {

    @InjectMocks
    private SafeboxServiceImpl safeboxService;

    @Mock
    private UserService userService;

    @Mock
    private SafeboxServiceService safeboxServiceService;

    @Mock
    private TokenService tokenService;

    @Test
    void openSafeboxUnitTest() {
        String id = "TEST";

        safeboxService.openSafebox(id);

        Mockito.verify(safeboxServiceService).getSafebox(id);
        Mockito.verify(tokenService).retrieveCurrenUserToken();
    }

    @Test
    void createSafeboxUnitTest() {
        User user = new User();
        user.setUsername("TEST");
        user.setPassword("TEST");

        safeboxService.createSafebox(user);

        Mockito.verify(userService).createUser(user.getUsername(), user.getPassword());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";

        safeboxService.getSafeboxItems(id);

        Mockito.verify(safeboxServiceService).getSafeboxItems(id);
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        String safeboxId = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));

        safeboxService.addItemsToSafebox(safeboxId, itemList);

        Mockito.verify(safeboxServiceService).putSafeboxItems(safeboxId, itemList);
    }
}
