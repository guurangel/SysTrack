package com.sys.sys.specification;

import com.sys.sys.model.Motorcycle;
import com.sys.sys.controllers.MotorcycleController.MotorcycleFilters;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

public class MotorcycleSpecification {

    public static Specification<Motorcycle> withFilters(MotorcycleFilters filters) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (filters.plate() != null && !filters.plate().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("placa")), "%" + filters.plate().toLowerCase() + "%")
                );
            }

            if (filters.brand() != null && !filters.brand().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("marca")), "%" + filters.brand().toLowerCase() + "%")
                );
            }

            if (filters.model() != null && !filters.model().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("modelo")), "%" + filters.model().toLowerCase() + "%")
                );
            }

            if (filters.status() != null) {
                predicates.add(cb.equal(root.get("status"), filters.status()));
            }

            if (filters.year() != null) {
                predicates.add(cb.equal(root.get("ano"), filters.year()));
            }

            if (filters.yearStart() != null && filters.yearEnd() != null) {
                predicates.add(cb.between(root.get("ano"), filters.yearStart(), filters.yearEnd()));
            } else if (filters.yearStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ano"), filters.yearStart()));
            } else if (filters.yearEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ano"), filters.yearEnd()));
            }

            if (filters.kmMin() != null && filters.kmMax() != null) {
                predicates.add(cb.between(root.get("km"), filters.kmMin(), filters.kmMax()));
            } else if (filters.kmMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("km"), filters.kmMin()));
            } else if (filters.kmMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("km"), filters.kmMax()));
            }

            if (filters.patioId() != null) {
                predicates.add(cb.equal(root.get("patio").get("id"), filters.patioId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
