
# **🚀 Microservices System – Spring Boot | Java 21 | MySQL | API Gateway | Consul Discovery**

This project is a microservices-based loan application system built using Spring Boot (Java 21).
Each service is independently deployable and communicates through a centralized API Gateway and Consul-based service discovery.

## 📦 Services Included

Service Name	Description
API Gateway	Entry point for all clients, handles routing, authentication, and service discovery via Consul.
Auth Service	User registration, login, JWT token generation.
Customer Service	Handles PAN, income, and address details.
Loan Service	Handles loan applications and status.
Notification Service	Sends SMS/Email notifications.
Discovery Service	Consul server for service registration and discovery.
🛠️ Tech Stack

Java 21

Spring Boot 3.3+

Spring Web, Spring Data JPA, Validation

Spring Cloud Gateway (API Gateway)

Spring Cloud Consul (Service Discovery)

MySQL 8

Lombok

Gradle (Build tool)

## 📁 Project Structure (Monorepo Example)

microservices/
│
├── api-gateway/
│   ├── src/main/java/com.microservice.gateway
│   └── build.gradle
│
├── auth-service/
│   ├── src/main/java/com.microservice.auth
│   └── build.gradle
│
├── customer-service/
│   ├── src/main/java/com.microservice.customer
│   └── build.gradle
│
├── loan-service/
│   ├── src/main/java/com.microservice.loan
│   └── build.gradle
│
├── notification-service/
│   ├── src/main/java/com.microservice.notification
│   └── build.gradle
│
├── discovery-service/
│   └── consul/ (Consul server files and configs)
│
└── README.md

## 🗂️ Ports Used (Recommended)

Service	Port
API Gateway	8080
Auth Service	8081
Customer Service	8082
Loan Service	8083
Notification Service	8084
Consul UI	8500

## 🔐 JWT Authentication Flow

Client logs in via Auth-Service → receives JWT Token.

Client sends all future requests via API Gateway with header:

Authorization: Bearer <token>


API Gateway validates the token & extracts customerId.

Microservices receive customerId using a custom annotation:

@CurrentCustomerId Long customerId

## 🌐 Consul Setup (Discovery Service)

Follow these steps to set up Consul for service discovery:

1. Download Consul

Go to the official site: https://www.consul.io/downloads

Select your OS (Windows/Linux/macOS) and download the zip file.

2. Extract and Setup

Unzip the downloaded file to a folder, e.g., C:\consul

Open Command Prompt / Terminal and navigate to the folder:

3. Run Consul in Development Mode
   consul agent -dev -ui 

This starts Consul in development mode with the UI enabled.

Access the UI at http://localhost:8500

4. Service Registration with Spring Boot

Add Spring Cloud Consul dependency to each service:

dependencies {
implementation 'org.springframework.cloud:spring-cloud-starter-consul-discovery'
}


Configure application.yml for each service:

spring:
application:
name: customer-service
cloud:
consul:
host: localhost
port: 8500
discovery:
enabled: true
register: true


Similarly, configure API Gateway to discover services via Consul:

spring:
application:
name: api-gateway
cloud:
gateway:
discovery:
locator:
enabled: true
consul:
host: localhost
port: 8500

5. Running Microservices

Start Consul server first.

Start microservices (auth-service, customer-service, loan-service, notification-service) → they will auto-register with Consul.

Start API Gateway → it will discover all services via Consul and route requests automatically.

## ✅ API Gateway + Consul Flow

Client → API Gateway → Consul → Service instance

Consul ensures load balancing and service discovery.

All services can dynamically discover each other without hardcoded URLs.

### 📌 Tips

Keep JWT secret same across all services for token validation.

Use @CurrentCustomerId annotation to automatically inject customerId in controllers.

Access Consul UI to check registered services and health status.