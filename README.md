# Security-Focused Load Balancer Simulation

This project implements a security-focused load balancer simulation in Java that demonstrates various security features including:

- DDoS protection through request rate limiting
- Malicious payload detection
- Request validation
- Round-robin load balancing

## Features

- Request rate limiting per IP address
- Detection of suspicious patterns in request payloads
- Basic round-robin load balancing
- Server health tracking
- Concurrent request handling
- Logging of security events

## Requirements

- Java 11 or higher
- Maven

## Building the Project

```bash
mvn clean install
```

## Running the Simulation

```bash
mvn exec:java -Dexec.mainClass="com.securityloadbalancer.Main"
```

## Security Features

1. **DDoS Protection**: Monitors request frequency per IP address within a configurable time window
2. **Payload Validation**: Checks for common malicious patterns in request payloads
3. **Request Validation**: Ensures all requests contain required fields and valid data
4. **Concurrent Request Handling**: Thread-safe implementation for handling multiple requests

## Architecture

- `LoadBalancer`: Main class handling load balancing and security checks
- `Server`: Represents a backend server with health status
- `Request`: Encapsulates request data and type
- `Main`: Demo class showing usage of the load balancer
