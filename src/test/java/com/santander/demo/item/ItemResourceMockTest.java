package com.santander.demo.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.demo.ItemDTO;
import com.santander.demo.item.db.Item;
import com.santander.demo.item.db.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemResourceMockTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @Test
    void shouldDeleteItemByIdAndReturn204() throws Exception {
        //given
        int id = 1;

        doNothing().when(itemService).deleteItemById(any());
        //then
        Assertions.assertEquals(204, mvc.perform(delete("/item/{id}", id).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(204))
                .andReturn()
                .getResponse()
                .getStatus());
    }

    @Test
    public void shouldUpdateItem() throws Exception {


        int id = 1;
        Item itemToUpdate = mock(Item.class);
        when(itemToUpdate.getId()).thenReturn(1);
        when(itemToUpdate.getName()).thenReturn("any");
        when(itemToUpdate.getPrice()).thenReturn(new BigDecimal(1));
        when(itemService.updateItem(any(), any())).thenReturn(itemToUpdate);
        Assertions.assertEquals(200, mvc.perform(
                        put("/item/{id}", id)
                                .content(
                                        new ObjectMapper()
                                                .writeValueAsBytes(
                                                        itemToUpdate
                                                )
                                ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getStatus());
    }

    @Test
    public void shouldAddNewItem() throws Exception {
        ItemDTO itemDTO = mock(ItemDTO.class);
        when(itemDTO.getId()).thenReturn(1);
        when(itemDTO.getName()).thenReturn("any");
        when(itemDTO.getPrice()).thenReturn(new BigDecimal(1));
        doNothing().when(itemService).addNewItem(anyList());
        Assertions.assertEquals(201, mvc.perform(
                        post("/item")
                                .content(
                                        new ObjectMapper()
                                                .writeValueAsBytes(
                                                        Arrays.asList(itemDTO)
                                                )
                                ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getStatus());
    }

    @Test
    public void shouldReturnAllItems() throws Exception {

        when(itemService.getAllItems(any())).thenReturn(mock(List.class));
        Assertions.assertEquals(200, mvc.perform(
                        get("/item")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getStatus());
    }
}
