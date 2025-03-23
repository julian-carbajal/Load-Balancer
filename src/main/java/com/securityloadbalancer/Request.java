package com.securityloadbalancer;

public class Request {
    private final String clientIp;
    private final String payload;
    private final RequestType type;

    public Request(String clientIp, String payload, RequestType type) {
        this.clientIp = clientIp;
        this.payload = payload;
        this.type = type;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getPayload() {
        return payload;
    }

    public RequestType getType() {
        return type;
    }

    public enum RequestType {
        HTTP, HTTPS, TCP
    }
}
