### Startup: ###
#### 1. run commands from src/main/resources/sql/orders-role-DDL.sql on your postgres server to create DB ####
      1.step - roles
      2.step - db
#### 2. set 'ddl-auto: create' in src/main/resources/application.yml -> Hiberante will create tables and constraints ####
#### 3. in project root directory run: 'mvnw clean spring-boot:run' ####
#### 4. set 'ddl-auto: validate' in src/main/resources/application.yml -> Tables exist when you run app again, remove hiberante create ####
