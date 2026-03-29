# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Tmall (e-commerce) platform built with Spring Boot. It consists of:
- **Frontend store** (`/fore*` endpoints) - Customer-facing pages
- **Admin panel** (`/admin*` endpoints) - Back-office management
- **Search** - Powered by Elasticsearch

## Tech Stack

- Spring Boot 1.5.9, Spring Data JPA, Spring Data Redis, Spring Data Elasticsearch
- Apache Shiro for authentication with MD5 password hashing
- Thymeleaf with LEGACYHTML5 mode for templates
- MySQL database, Redis caching, Elasticsearch for product search
- Maven build system

## Build & Run

```bash
# Build
mvn clean package

# Run (requires MySQL, Redis, and Elasticsearch running)
mvn spring-boot:run
```

Application runs at `http://localhost:8080/tmall_springboot`

## Required Services

| Service       | Port  |
|---------------|-------|
| MySQL         | 3306  |
| Redis         | 6379  |
| Elasticsearch | 9300  |

Database: `tmall_springboot` (schema auto-import from `SQL/tmall_springboot.sql`)

## Architecture

```
src/main/java/com/qqlin/tmall/
├── Application.java          # Main entry, enables JPA/ES repos and caching
├── config/                    # Shiro, Redis, CORS, WebMvc configurations
├── comparator/               # Product sorting comparators (date, price, review, saleCount)
├── dao/
│   ├── entity/               # JPA entities (Category, Product, User, Order, OrderItem, etc.)
│   ├── repository/           # JPA repositories
│   └── elasticsearch/        # Elasticsearch DAO for product search
├── service/                  # Service interfaces
│   └── impl/                 # Service implementations
├── web/
│   ├── fore/                 # Frontend REST controllers (customer-facing)
│   └── admin/               # Admin REST controllers (back-office)
├── interceptor/              # LoginInterceptor, OrderInterceptor
├── realm/                   # JpaRealm for Shiro authentication
└── util/                    # Page4Navigator, ImageUtil, Result, PortUtil, SpringContextUtil
```

## Key Design Patterns

- **Service layer** uses interface + implementation pattern (`ProductService` interface, `ProductServiceImpl`)
- **Product images** are stored as files on disk (not in DB), organized by type: `single` and `detail`
- **Elasticsearch sync** - `ProductService.initDatabase2ES()` syncs MySQL data to Elasticsearch for search
- **Shiro auth** - passwords hashed with MD5 + salt (2 iterations), stored in `User.salt` and `User.password`

## API Routing

- `/fore*` - Frontend APIs (login, register, cart, orders, search)
- `/admin*` or `/categories`, `/products`, etc. - Admin CRUD APIs
- Interceptors enforce login requirements on frontend routes
