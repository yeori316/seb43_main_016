package com.codestates.edusync.model.common.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriCreator {
    public static URI createUri(String defaultUrl, long resourceId) {
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUrl + "/{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

    public static URI createUri(String defaultUrl, String resource) {
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUrl + "/{resource}")
                .buildAndExpand(resource)
                .toUri();
    }
}
