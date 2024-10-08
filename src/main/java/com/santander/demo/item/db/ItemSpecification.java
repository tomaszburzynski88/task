package com.santander.demo.item.db;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class ItemSpecification {

    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String SPACE_SEPARATOR = " ";

    public Specification<Item> getItemsByFilter(String filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<String> filterSplittedWithAnd = new ArrayList<>();
            final List<String> filterSplittedWithOr = new ArrayList<>();
            if (filter.contains(AND)) {
                filterSplittedWithAnd.addAll(asList(filter.split(AND)));
                List<String> filterOrs = filterSplittedWithAnd.stream().filter(element -> element.contains(OR)).toList();
                filterSplittedWithAnd.removeAll(filterOrs);
                filterOrs.forEach(element -> {
                    List<String> orList = new ArrayList<>(asList(element.split(OR)));
                    filterSplittedWithOr.addAll(orList);
                });

            } else if (filter.contains(OR)) {
                filterSplittedWithOr.addAll(asList(filter.split(OR)));
            } else {
                String[] filterSearchOperationArray = filter.split(SPACE_SEPARATOR);

                predicates.add(getPredicate(criteriaBuilder, root, SearchCriteria.builder()
                        .key(filterSearchOperationArray[0])
                        .operation(filterSearchOperationArray[1])
                        .value(filterSearchOperationArray[2])
                        .build()));
            }
            List<Predicate> andPredicates = new ArrayList<>();
            if (!CollectionUtils.isEmpty(filterSplittedWithAnd)) {
                for (String items : filterSplittedWithAnd) {
                    String[] itemArray = items.split(SPACE_SEPARATOR);

                    andPredicates.add(criteriaBuilder.and(getPredicate(criteriaBuilder, root, SearchCriteria.builder()
                            .value(itemArray[2])
                            .operation(itemArray[1])
                            .key(itemArray[0])
                            .build())
                    ));
                }
                predicates.addAll(andPredicates);
            }
            List<Predicate> orPredicates = new ArrayList<>();
            if (!CollectionUtils.isEmpty(filterSplittedWithOr)) {
                for (String items : filterSplittedWithOr) {
                    String[] itemArray = items.split(SPACE_SEPARATOR);

                    orPredicates.add(criteriaBuilder.or(getPredicate(criteriaBuilder, root, SearchCriteria.builder()
                            .value(itemArray[2])
                            .operation(itemArray[1])
                            .key(itemArray[0])
                            .build())
                    ));
                }
                predicates.addAll(orPredicates);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root<Item> root, SearchCriteria searchCriteria) {
        if ("gt".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.greaterThan(
                    root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if ("lt".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.lessThan(
                    root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if ("ge".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if ("le".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if ("ne".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.notEqual(
                    root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if ("eq".equalsIgnoreCase(searchCriteria.getOperation())) {
            return criteriaBuilder.equal(
                    root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }
        return criteriaBuilder.and();
    }
}
