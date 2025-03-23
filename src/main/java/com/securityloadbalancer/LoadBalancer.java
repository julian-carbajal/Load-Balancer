package com.securityloadbalancer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadBalancer {
    private static final Logger logger = LoggerFactory.getLogger(LoadBalancer.class);
    private final List<Server> servers;
    private final Map<String, Integer> requestCount;
    private final Map<String, Long> lastRequestTime;
    private final int requestThreshold;
    private final long timeWindowMs;
    private AtomicInteger currentServerIndex;

    public LoadBalancer(int requestThreshold, long timeWindowMs) {
        this.servers = new ArrayList<>();
        this.requestCount = new ConcurrentHashMap<>();
        this.lastRequestTime = new ConcurrentHashMap<>();
        this.requestThreshold = requestThreshold;
        this.timeWindowMs = timeWindowMs;
        this.currentServerIndex = new AtomicInteger(0);
    }

    public void addServer(Server server) {
        servers.add(server);
        logger.info("Added new server: {}", server.getId());
    }

    public Server getNextServer(Request request) throws SecurityException {
        if (!isRequestValid(request)) {
            throw new SecurityException("Invalid request detected");
        }

        if (isDDoSAttack(request.getClientIp())) {
            throw new SecurityException("Potential DDoS attack detected from IP: " + request.getClientIp());
        }

        updateRequestMetrics(request.getClientIp());
        return roundRobin();
    }

    private boolean isRequestValid(Request request) {
        // Basic request validation
        return request != null 
            && request.getClientIp() != null 
            && !request.getClientIp().isEmpty()
            && !containsSuspiciousPatterns(request.getPayload());
    }

    private boolean containsSuspiciousPatterns(String payload) {
        if (payload == null) return false;
        // Check for common malicious patterns
        String[] suspiciousPatterns = {
            "<script>", "SELECT *", "DROP TABLE", "DELETE FROM", "--",
            "../", "cmd.exe", "/bin/sh", "eval("
        };
        
        for (String pattern : suspiciousPatterns) {
            if (payload.toLowerCase().contains(pattern.toLowerCase())) {
                logger.warn("Suspicious pattern detected: {}", pattern);
                return true;
            }
        }
        return false;
    }

    private boolean isDDoSAttack(String clientIp) {
        long currentTime = System.currentTimeMillis();
        int count = requestCount.getOrDefault(clientIp, 0);
        Long lastRequest = lastRequestTime.get(clientIp);

        if (lastRequest != null) {
            if (currentTime - lastRequest < timeWindowMs && count > requestThreshold) {
                logger.warn("DDoS protection triggered for IP: {}", clientIp);
                return true;
            }
            if (currentTime - lastRequest > timeWindowMs) {
                requestCount.put(clientIp, 0);
            }
        }
        return false;
    }

    private void updateRequestMetrics(String clientIp) {
        requestCount.merge(clientIp, 1, Integer::sum);
        lastRequestTime.put(clientIp, System.currentTimeMillis());
    }

    private Server roundRobin() {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }
        int index = currentServerIndex.getAndIncrement() % servers.size();
        return servers.get(index);
    }
}
