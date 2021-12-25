package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuIdsExistRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private static final String BASE_URL = "/api/menus";

    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping(BASE_URL)
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
        final MenuResponse menuResponse = menuService.create(menuRequest);
        final URI uri = URI.create(BASE_URL + "/" + menuResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping(BASE_URL)
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }

    @GetMapping(BASE_URL + "/exist")
    public ResponseEntity<Void> checkExistMenuIds(@RequestBody final MenuIdsExistRequest menuIdsExistRequest) {
        menuService.isExistMenuIds(menuIdsExistRequest);
        return ResponseEntity.ok()
                .build();
    }
}