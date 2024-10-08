package com.santander.demo;

import com.santander.demo.item.ItemResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ContextLoadTests {

    @Autowired(required = false)
    private ItemResource itemResource;


    @Test
    void when_context_is_loaded_then_all_expected_beans_are_created() {
        assertNotNull(itemResource);
    }
}