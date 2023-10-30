package com.capstone.workspace.controllers.store;

import com.capstone.workspace.annotations.AllowedUsers;
import com.capstone.workspace.dtos.store.QrCodeDto;
import com.capstone.workspace.dtos.store.SearchStoreDto;
import com.capstone.workspace.entities.store.Menu;
import com.capstone.workspace.entities.store.QrCode;
import com.capstone.workspace.entities.store.Store;
import com.capstone.workspace.enums.user.UserType;
import com.capstone.workspace.helpers.store.StoreHelper;
import com.capstone.workspace.models.shared.PaginationResponseModel;
import com.capstone.workspace.models.shared.ResponseModel;
import com.capstone.workspace.models.store.MenuModel;
import com.capstone.workspace.models.store.QrCodeModel;
import com.capstone.workspace.models.store.StoreModel;
import com.capstone.workspace.services.store.MenuService;
import com.capstone.workspace.services.store.QrCodeService;
import com.capstone.workspace.services.store.StoreService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping( "/api/stores")
@RequiredArgsConstructor
public class StoreController {
    @NonNull
    private final StoreService storeService;

    @NonNull
    private final MenuService menuService;

    @NonNull
    private final QrCodeService qrCodeService;

    @NonNull
    private final ModelMapper mapper;

    @NonNull
    private final StoreHelper storeHelper;

    @PostMapping("/customer/search")
    public ResponseModel<PaginationResponseModel<StoreModel>> customerSearch(@Valid @RequestBody SearchStoreDto dto) {
        return searchStore(dto);
    }

    @AllowedUsers(userTypes = {UserType.OWNER, UserType.ADMIN})
    @PostMapping("/search")
    public ResponseModel<PaginationResponseModel<StoreModel>> search(@Valid @RequestBody SearchStoreDto dto) {
        return searchStore(dto);
    }

    @GetMapping("/{id}/menu")
    public ResponseModel<MenuModel> getStoreMenu(@PathVariable(name = "id") String storeId) {
        Menu entity = menuService.getStoreMenu(storeId);
        MenuModel model = mapper.map(entity, MenuModel.class);
        return ResponseModel.<MenuModel>builder().data(model).build();
    }

    @AllowedUsers(userTypes = {UserType.OWNER, UserType.STORE_MANAGER})
    @PostMapping("/{id}/qrcodes")
    public ResponseModel<QrCodeModel> createQrCode(@PathVariable(name = "id") String storeId, @Valid @RequestBody QrCodeDto dto) {
        QrCode entity = qrCodeService.create(storeId, dto);
        QrCodeModel model = mapper.map(entity, QrCodeModel.class);
        return ResponseModel.<QrCodeModel>builder().data(model).build();
    }

    @AllowedUsers(userTypes = {UserType.OWNER, UserType.STORE_MANAGER})
    @GetMapping("/{id}/qrcodes")
    public ResponseModel<List<QrCodeModel>> getStoreQrCodes(@PathVariable(name = "id") String storeId) {
        List<QrCodeModel> data = qrCodeService.getStoreQrCodes(storeId);
        return ResponseModel.<List<QrCodeModel>>builder().data(data).build();
    }

    @GetMapping("/{id}")
    public ResponseModel<StoreModel> getStoreById(@PathVariable(name = "id") UUID storeId) {
        Store entity = storeService.getStoreById(storeId);
        StoreModel model = mapper.map(entity, StoreModel.class);
        model.setIsOpen(storeHelper.isStoreOpening(model.getOperationalHours()));
        return ResponseModel.<StoreModel>builder().data(model).build();
    }

    private ResponseModel<PaginationResponseModel<StoreModel>> searchStore(SearchStoreDto dto) {
        PaginationResponseModel<StoreModel> data = storeService.search(dto);

        if (data.getResults() != null) {
            data.getResults().forEach(item -> {
                item.setIsOpen(storeHelper.isStoreOpening(item.getOperationalHours()));
            });
        }

        return ResponseModel.<PaginationResponseModel<StoreModel>>builder().data(data).build();
    }
}
