# Docker Fundamentals

A simple Dockerized Java Spring Boot application using MySQL in a separate container.

This project demonstrates the core Docker concepts required for backend development.

## Tech Stack

- Java 21
- Spring Boot
- MySQL 8
- Docker
- Docker Compose

## Project Structure

```
.
├── src/
├── Dockerfile
├── compose.yml
├── pom.xml
└── README.md
```

## Features

- Dockerized Spring Boot application
- MySQL running in a separate container
- Multi-stage Docker build
- Docker Compose for multi-container setup
- Persistent MySQL data using Docker Volumes
- Container networking

## Getting Started

Clone the repository

```bash
git clone https://github.com/Learner-Tirtha/docker-fundamentals.git
cd docker-fundamentals
```

Start the application

```bash
docker compose up --build
```

Run in background

```bash
docker compose up -d
```

Stop the application

```bash
docker compose down
```

Remove containers and volumes

```bash
docker compose down -v
```

## Architecture

```
+---------------------+
| Spring Boot         |
| Container           |
+----------+----------+
           |
           |
     Docker Network
           |
           |
+----------+----------+
| MySQL Container     |
+---------------------+
        |
 Docker Volume
        |
 Persistent Data
```

## Learning Outcomes

- Docker Images
- Docker Containers
- Dockerfile
- Multi-stage Build
- Docker Networking
- Docker Volumes
- Docker Compose

## Documentation

Detailed notes and explanations are available in my Notion Page.

👉 ([Notion Detailed Document](https://app.notion.com/p/Docker-Fundamentals-38b0bda3922680ffb589c1d5ec435688?source=copy_link))

