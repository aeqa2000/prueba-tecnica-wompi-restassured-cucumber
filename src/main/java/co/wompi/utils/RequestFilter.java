package co.wompi.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.util.List;

public final class RequestFilter {

    private RequestFilter() {
    }

    public static List<Filter> logRequestAndResponse() {
        return List.of(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}