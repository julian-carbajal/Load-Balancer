package com.securityloadbalancer;

public class Main {
    public static void main(String[] args) {
        // Create a load balancer with max 100 requests per IP in a 60-second window
        LoadBalancer loadBalancer = new LoadBalancer(100, 60000);

        // Add some sample servers
        loadBalancer.addServer(new Server("server1", "192.168.1.10", 8080));
        loadBalancer.addServer(new Server("server2", "192.168.1.11", 8080));
        loadBalancer.addServer(new Server("server3", "192.168.1.12", 8080));

        // Simulate some requests
        try {
            // Normal request
            Request validRequest = new Request(
                "192.168.0.1",
                "GET /api/data",
                Request.RequestType.HTTP
            );
            Server server = loadBalancer.getNextServer(validRequest);
            System.out.println("Request routed to: " + server);

            // Malicious request
            Request maliciousRequest = new Request(
                "192.168.0.2",
                "SELECT * FROM users",
                Request.RequestType.HTTP
            );
            loadBalancer.getNextServer(maliciousRequest);
        } catch (SecurityException e) {
            System.out.println("Security check failed: " + e.getMessage());
        }
    }
}
