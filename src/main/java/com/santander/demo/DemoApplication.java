package com.santander.demo;

import com.santander.demo.item.db.Item;
import com.santander.demo.item.db.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;

@RequiredArgsConstructor
@SpringBootApplication
public class DemoApplication {

	private final ItemRepository itemRepository;



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initApplication(){
		itemRepository.findByNameIgnoreCase("wheel").orElseGet(()->{
			Item item = Item.builder().id(1).price(new BigDecimal(1.0)).name("wheel").build();
			return itemRepository.save(item);
		});

		itemRepository.findByNameIgnoreCase("headlight").orElseGet(()->{
			Item item2 = Item.builder().id(2).price(new BigDecimal(2.0)).name("headlight").build();
			return itemRepository.save(item2);
		});

		itemRepository.findByNameIgnoreCase("fender").orElseGet(()->{
			Item item3 = Item.builder().id(3).price(new BigDecimal(3.0)).name("fender").build();
			return itemRepository.save(item3);
		});


	}
}
