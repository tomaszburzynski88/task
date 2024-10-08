
package com.santander.demo.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemResourceTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void when_getting_the_item_by_proper_id_data_is_received() throws Exception {
        //given
        int id = 1;
        //then
        assert mvc.perform(get("/item/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("wheel");
    }

    @Test
    void when_getting_the_non_existing_item_NotFoundStatus_is_returned() throws Exception {
        //given
        int id = 111;

        //then
        assert mvc.perform(get("/item/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .isBlank();
    }
}