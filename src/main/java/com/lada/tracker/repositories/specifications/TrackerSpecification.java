package com.lada.tracker.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.function.Function;

public class TrackerSpecification<T> {

    protected Specification<T> specification;

    public TrackerSpecification() {
        specification = Specification.where(null);
    }

    protected <FIELD> TrackerSpecification<T> and(FIELD field, Function<FIELD, Specification<T>> specGenerator) {
        if (field != null) {
            specification = specification.and(specGenerator.apply(field));
        }
        return this;
    }

    protected <FIELD> TrackerSpecification<T> eq(String key, FIELD field) {
        if (field != null) {
            specification = specification.and((request, cq, cb) -> cb.equal(request.get(key), field));
        }
        return this;
    }

    protected TrackerSpecification<T> contains(String key, String subString) {
        if (subString != null) {
            specification = specification.and((request, cq, cb) -> cb.like(request.get(key), "%" + subString
                    + "%"));
        }
        return this;
    }

    protected TrackerSpecification<T> greater(String key, Timestamp ts) {
        if (ts != null) {
            specification = specification.and((request, cq, cb) -> cb.greaterThan(request.get(key), ts));
        }
        return this;
    }

    protected TrackerSpecification<T> less(String key, Timestamp ts) {
        if (ts != null) {
            specification = specification.and((request, cq, cb) -> cb.lessThan(request.get(key), ts));
        }
        return this;
    }


    public Specification<T> end() {
        return specification;
    }

}
