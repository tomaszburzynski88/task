package com.santander.demo.item.db;

import com.santander.demo.item.exception.ItemNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemSpecification itemSpecification;
    private final ItemRepository itemRepository;

    public List<Item> getAllItems(String filter) {
        return StringUtils.isBlank(filter) ? itemRepository.findAll() :
                itemRepository.findAll(itemSpecification.getItemsByFilter(filter));
    }

    public void addNewItem(List<Item> itemList) {
        itemRepository.saveAll(itemList);
    }

    public void deleteItemById(Integer id) {
        itemRepository.deleteById(id);
    }

    public Item updateItem(Integer id, Item item) {
        return itemRepository.findById(id)
                .map(
                        itemToUpdate -> itemToUpdate
                                .toBuilder()
                                .name(item.getName())
                                .price(item.getPrice())
                                .build())
                .map(itemRepository::save)
                .orElseThrow(() -> new ItemNotFoundException("Can't find item with id: %s".formatted(id)));
    }

    public Item getItemById(Integer id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Can't find item with id: %s".formatted(id)));
    }
}
