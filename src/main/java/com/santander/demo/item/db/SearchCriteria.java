package com.santander.demo.item.db;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
