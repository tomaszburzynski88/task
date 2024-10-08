package com.santander.demo.item;

import com.santander.demo.ItemDTO;
import com.santander.demo.ShopApi;
import com.santander.demo.item.db.Item;
import com.santander.demo.item.db.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemResource implements ShopApi {

    private final ItemService itemService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<Void> addItem(List<@Valid ItemDTO> items) {
        itemService.addNewItem(items.stream().map(item -> modelMapper.map(item, Item.class)).toList());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteItemById(Integer id) {
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ItemDTO>> getAllItems(String filter) {
        return ResponseEntity.ok(
                itemService.getAllItems(filter).stream()
                        .map(
                                item -> modelMapper.map(item, ItemDTO.class)
                        ).toList());
    }

    @Override
    public ResponseEntity<ItemDTO> getItemById(Integer id) {
        return ResponseEntity.ok(modelMapper.map(itemService.getItemById(id), ItemDTO.class));
    }

    @Override
    public ResponseEntity<ItemDTO> updateItem(Integer id, ItemDTO item) {
        return ResponseEntity.ok(
                modelMapper.map(
                        itemService.updateItem(id,
                                modelMapper.map(item, Item.class)
                        ), ItemDTO.class));
    }
}
