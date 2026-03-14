# Project Genesis: Bare-Metal Java Web Server

Genesis is an educational, high-performance web server and framework built entirely from scratch in Java. 

The primary objective of this project is to strip away the abstractions of modern commercial frameworks (like Spring Boot or Jakarta EE) and demonstrate the underlying mechanics of web infrastructure. By manipulating raw POSIX sockets, managing byte streams, and parsing the HTTP protocol natively, this project proves that a deep understanding of standard libraries and network protocols is far more valuable than simply memorizing framework annotations.

## Architecture & Core Features

* **Raw TCP/IP Socket Management:** Direct handling of `java.net.ServerSocket` and byte streams.
* **High-Concurrency Model:** Implementation of Java 21 Virtual Threads (Project Loom) to solve the C10K problem without memory exhaustion.
* **Native HTTP/1.1 Parser:** Custom parsing of HTTP methods, headers, URIs, and payloads directly from the input stream.
* **Static File Server:** Native disk I/O manipulation for serving static assets with dynamic MIME type resolution.
* **Custom Dependency Injection Container:** A reflection-based IoC (Inversion of Control) container built from the ground up.
* **Annotation-Driven Routing:** Custom `@RestController`, `@GET`, and `@POST` annotations mimicking standard enterprise frameworks.
* **JSON Serialization:** Integration for dynamic Java Object to JSON mapping.

## Evolutionary Branches

This repository is structured in sequential branches, representing the architectural evolution of the server. To understand the progression, review the branches in the following order:

1. `step-01-raw-sockets` - Bare minimum TCP connection and raw byte response.
2. `step-02-virtual-threads` - Thread pool extraction and JVM Loom implementation.
3. `step-03-static-file-server` - Disk I/O reading and MIME type handling.
4. `step-04-http-parser` - Request wrapping and URI routing logic.
5. `step-05-manual-crud-api` - In-memory database and manual endpoint routing.
6. `step-06-custom-annotations` - Implementation of Java Reflection for dynamic routing.
7. `step-07-json-serialization` - Automated payload parsing and response formatting.
8. `step-08-global-exception-handler` - Global error interception and HTTP status code management.
9. `step-09-dependency-injection` - Classpath scanning and automated bean instantiation.
10. `step-10-framework-modularization` - Final decoupling between the underlying Framework and the Application logic.

## Reference Documentation

The architecture of this server is based on the direct implementation of internet standards and JVM low-level APIs.

* [RFC 2616 (HTTP/1.1 Specification)](https://datatracker.ietf.org/doc/html/rfc2616)
* [Java NIO (New I/O) Official Documentation](https://docs.oracle.com/en/java/javase/21/core/java-nio.html)
* POSIX Socket API implementation concepts
* JEP 444: Virtual Threads (Java 21)

## Getting Started

To run the final version of the framework (Step 10):

1. Clone the repository:
   ```bash
   git clone git@github.com:NullPointer-Labs/genesis.git
   ```
2. Checkout the final architectural branch:
   ```bash
   git checkout step-10-framework-modularization
   ```
3. Build and run the application via Gradle:
   ```bash
   ./gradlew build
   ./gradlew run
   ```
The server will start listening for TCP connections on port 8080.
