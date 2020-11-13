package com.lada.tracker.repositories.specifications;

import com.lada.tracker.entities.Request;

import java.sql.Timestamp;

public class RequestSpecification extends TrackerSpecification<Request> {

    public static RequestSpecification start() {
        return new RequestSpecification();
    }

    public RequestSpecification eqRequestTypeId(Integer requestTypeId) {
        eq("requestTypeId", requestTypeId);
        return this;
    }

    public RequestSpecification bodyContains(String subText) {
        contains("body", subText);
        return this;
    }

    public RequestSpecification registerAfter(Timestamp timestamp) {
        greater("registrationDate", timestamp);
        return this;
    }

    public RequestSpecification registerBefore(Timestamp timestamp) {
        less("registrationDate", timestamp);
        return this;
    }

}
