package com.frame.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static <T> T get(String url, Class<T> classType, Map<String, ?> params) {
        try {
            ResponseEntity<T> response = getRestTemplate().getForEntity(url, classType, params);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public static <T> T get(String url, Class<T> classType, Object... uriVariables) {
        try {
            ResponseEntity<T> response = getRestTemplate().getForEntity(url, classType, uriVariables);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public static <T> T post(String url, @Nullable Map<String, Object> requestBody, Class<T> classType, Map<String, ?> params) {
        return post(url, null, requestBody, classType, params);
    }

    public static <T> T post(String url, @Nullable Map<String, Object> requestBody, Class<T> classType, Object... uriVariables) {
        return post(url, null, requestBody, classType, uriVariables);
    }

    public static <T> T post(String url, @Nullable Map<String, String> headers, @Nullable Map<String, Object> requestBody, Class<T> classType, Map<String, ?> params) {
        try {
            HttpHeaders headerParam = new HttpHeaders();
            headerParam.setContentType(MediaType.APPLICATION_JSON);
            headers.forEach((key, val) -> headerParam.add(key, val));
            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(requestBody);
            ResponseEntity<T> response = getRestTemplate().postForEntity(url, request, classType, params);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public static <T> T post(String url, @Nullable Map<String, String> headers, @Nullable Map<String, Object> requestBody, Class<T> classType, Object... uriVariables) {
        try {
            HttpHeaders headerParam = new HttpHeaders();
            headerParam.setContentType(MediaType.APPLICATION_JSON);
            Optional.ofNullable(headers).orElse(new HashMap<>()).forEach((key, val) -> headerParam.add(key, val));
            ObjectMapper mapper = new ObjectMapper();
            String request = mapper.writeValueAsString(requestBody);
            ResponseEntity<T> response = getRestTemplate().postForEntity(url, request, classType, uriVariables);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public static <T> T uploadFile(String url, File file, Class<T> classType,Object... uriVariables) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", fileSystemResource);
            form.add("filename", file.getName());
            HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
            ResponseEntity<T> response = getRestTemplate().postForEntity(url, files, classType,uriVariables);
            return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
