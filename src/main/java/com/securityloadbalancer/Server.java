package com.securityloadbalancer;

public class Server {
    private final String id;
    private final String host;
    private final int port;
    private boolean healthy;

    public Server(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.healthy = true;
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public String toString() {
        return String.format("Server{id='%s', host='%s', port=%d, healthy=%s}", 
            id, host, port, healthy);
    }
}
