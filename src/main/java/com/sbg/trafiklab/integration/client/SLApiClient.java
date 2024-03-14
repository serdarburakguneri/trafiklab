package com.sbg.trafiklab.integration.client;

import com.sbg.trafiklab.integration.exception.SLApiClientException;
import com.sbg.trafiklab.integration.exception.SlApiServerException;
import com.sbg.trafiklab.integration.model.DataModelType;
import com.sbg.trafiklab.integration.model.DefaultTransportModeCode;
import com.sbg.trafiklab.integration.model.QueryParamConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@Service
public class SLApiClient {

    @Value("${trafiklab.api.key}")
    private String apiKey;
    @Value("${trafiklab.api.url}")
    private String apiUrl;

    private WebClient webClient;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder().baseUrl(apiUrl).build();
    }

    public ResponseSpec fetchStopPoints() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(QueryParamConstants.KEY, apiKey)
                        .queryParam(QueryParamConstants.DEFAULT_TRANSPORT_MODE_CODE,
                                DefaultTransportModeCode.BUS.getCode())
                        .queryParam(QueryParamConstants.MODEL, DataModelType.STOP_POINT.getType())
                        .build())
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new SLApiClientException(
                                "Sl API raised a client error.")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new SlApiServerException("SL API raised a server error.")));
    }

    public ResponseSpec fetchJourneyPatterns() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(QueryParamConstants.KEY, apiKey)
                        .queryParam(QueryParamConstants.DEFAULT_TRANSPORT_MODE_CODE,
                                DefaultTransportModeCode.BUS.getCode())
                        .queryParam(QueryParamConstants.MODEL, DataModelType.JOURNEY_PATTERN.getType())
                        .build())
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new SLApiClientException(
                                "Sl API raised a client error.")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new SlApiServerException("SL API raised a server error.")));
    }

    public ResponseSpec fetchLines() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(QueryParamConstants.KEY, apiKey)
                        .queryParam(QueryParamConstants.DEFAULT_TRANSPORT_MODE_CODE,
                                DefaultTransportModeCode.BUS.getCode())
                        .queryParam(QueryParamConstants.MODEL, DataModelType.LINE.getType())
                        .build())
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new SLApiClientException(
                                "Sl API raised a client error.")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new SlApiServerException("SL API raised a server error.")));
    }

}
