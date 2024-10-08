package com.santander.demo.item;

import com.santander.demo.ItemDTO;
import com.santander.demo.item.db.Item;
import com.santander.demo.item.db.ItemRepository;
import com.santander.demo.item.db.ItemService;
import com.santander.demo.item.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;


    ItemDTO itemDto =
            ItemDTO.builder()
                    .id(4)
                    .name("abc")
                    .build();

    List<Item> items = new ArrayList<Item>();
    Item item = Item.builder().id(1).price(new BigDecimal(1.0)).name("wheel").build();

    @BeforeEach
    public void setup() {


        items.add(item);

        Mockito.lenient().when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Mockito.lenient().when(itemRepository.save(any())).thenReturn(item);
        Mockito.lenient().when(itemRepository.saveAll(any())).thenReturn(items);

        Mockito.lenient().when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

    }

    @Test
    public void shouldAddItemWhenNewObjectItemPassesInRepository() {
        itemService.addNewItem(items);
        assertEquals(item, itemService.getItemById(1));
    }

    @Test
    public void shouldThrowItemNotExistsWhenNotFoundInRepository() {
        when(itemRepository.findById(any())).thenThrow(ItemNotFoundException.class);
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(999));
    }


    @Test
    public void shouldGetItemEntityById() {
        when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(mock(Item.class)));
        Item actualValue = itemService.getItemById(1);
        assertNotNull(actualValue);
    }

    @Test
    public void shouldDeleteItemById_ok() {
        doNothing().when(itemRepository).deleteById(anyInt());
        itemService.deleteItemById(1);
        verify(itemRepository, times(1)).deleteById(1);
    }

    @Test
    public void shouldDeleteItemById_nok() {
        doThrow(ItemNotFoundException.class).when(itemRepository).deleteById(anyInt());
        assertThrows(ItemNotFoundException.class, () -> {
            itemService.deleteItemById(999);
        });

    }

    @Test
    public void shouldUpdateItemEntity() {
        Item entity = Item.builder().id(1).name("wheel").build();
        Item body = Item.builder().id(1).name("tailgate").price(new BigDecimal(2.0)).build();
        when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        when(itemRepository.save(any())).thenReturn(body);
        Item actualValue = itemService.updateItem(1, body);
        assertEquals(body.getName(), actualValue.getName());
    }

    @Test
    public void shouldReturnAllItems() {
        assertNotNull(itemService.getAllItems(""));
    }
}
