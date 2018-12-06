### Startup: ###
#### 1. run commands from src/main/resources/sql/orders-role-DDL.sql on your postgres server to create DB ####
      1.step - create role
      2.step - create db
#### 2. set 'ddl-auto: create' in src/main/resources/application.yml -> Hiberante will create tables and constraints ####
#### 3. in project root directory run: 'mvnw clean spring-boot:run' ####
#### 4. set 'ddl-auto: validate' in src/main/resources/application.yml -> Tables exist when you run app again, remove hiberante create ####

# Orders application #

This is backend application for creating simple orders with exposed REST API. 

### What is this repository for? ###

In this repository Java backend code is stored with REST API, business logic, entities and tests.

### How do I get set up? ###
1. run commands from src/main/resources/sql/orders-role-DDL.sql on your postgres server to create DB
      1.step - create role
      2.step - create db
2. set 'ddl-auto: create' in src/main/resources/application.yml -> Hiberante will create tables and constraints
3. in project root directory run: 'mvnw clean spring-boot:run'
4. set 'ddl-auto: validate' in src/main/resources/application.yml -> Tables exist when you run app again, remove hiberante create

Application REST API Swagger documentation should be on http://localhost:8085/swagger-ui.html#/.
Also, Postgres database is used and ordersdb database should be created on local machine (url: jdbc:postgresql://127.0.0.1:5432/ordersdb).

### About application

Application serves as backend part of system and provides various methods for managing products and orders. 
Visual interface is provided with Swagger tool, it is simple for usage. 
Test are covering many cases, and they are separated in multiple classes. H2 in memory database is used to run tests.

### Application usage
Application is used for adding new products to our DB. There are also services for updating product and getting the list of all products.
Also, it is possible to place an order. When order is placed, it's initial state is NEW. As long order is that status, changing the price of order products will aslo cause total order price change also (when calling service for calculating total price). But when order changes status then prices are taken which were there at the time of order creation.
There is aslo method which is used for getting all orders within some period of time.
