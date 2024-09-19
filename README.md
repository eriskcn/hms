### HMS Setup Guide

#### Step 1: Clone the Repository

```bash
git clone https://github.com/eriskcn/hms.git
cd hms
```

#### Step 2: Build the Project

Make sure you have [Maven](https://maven.apache.org/install.html) installed on your system.

Run the following commands to build the project and resolve dependencies:

```bash
mvn install
```

#### Step 3: Run the Application

Once the build is complete, you can run the application using:

```bash
mvn spring-boot:run
```

#### Step 4: Set up the MySQL Database

Make sure you have [MySQL](https://www.mysql.com/downloads/) installed on your system.

1. Log in to your MySQL database:

```bash
sudo mysql -u root -p
```

2. Create the database and user:

```sql
CREATE
DATABASE hms_db;
CREATE
USER '<username>'@'localhost' IDENTIFIED BY '<password>';
GRANT ALL PRIVILEGES ON hms_db.* TO
'<username>'@'localhost';
FLUSH
PRIVILEGES;
```

> **Note**: Replace `<username>` and `<password>` with your actual database username and password.

#### Step 5: Configure Application Properties

Modify the `src/main/resources/application.properties` file to include your database credentials:

```properties
spring.application.name=hms
spring.datasource.url=jdbc:mysql://localhost:3306/hms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Make sure to replace `<username>` and `<password>` with the values you set in the previous step.

#### Step 6: Access the Application

After running the application, the HMS system should be accessible at:

```
http://localhost:8080
```
### Something maybe useful
#### Search and Filter for getAll**()
- Booking
  - search: guest name, room number
  - filterCriteria: prebooking, checked-out, active
- Guest
  - search: name, idCard, phone
  - filterCriteria: vip, normal
- Service
  - search: name
  - filterCriteria: spa, fnb, housekeeping, other
- Room
  - search: number
  - filterCriteria: standard, suite, vip

> Note: Maybe you never use BookingServiceController

### Dependencies
1. **Spring Boot Data JPA**
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-data-jpa

2. **Spring Boot Data REST**
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-data-rest

3. **Spring Boot Validation**
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-validation

4. **Spring Boot Web**
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-web

5. **Spring Boot DevTools** (runtime and optional)
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-devtools
  - `scope`: runtime
  - `optional`: true

6. **MySQL Connector**
  - `groupId`: com.mysql
  - `artifactId`: mysql-connector-j
  - `scope`: runtime

7. **Lombok** (optional)
  - `groupId`: org.projectlombok
  - `artifactId`: lombok
  - `optional`: true

8. **Spring Boot Tomcat** (provided scope)
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-tomcat
  - `scope`: provided

9. **Spring Boot Test** (test scope)
  - `groupId`: org.springframework.boot
  - `artifactId`: spring-boot-starter-test
  - `scope`: test


### API URL Dashboard

GET /hms/dashboard/occupied-rooms

POST /hms/dashboard/check-in (body: Long guestId, Long roomId)

GET /hms/dashboard/stats 

POST /hms/dashboard/room-service (body: Long bookingId, Long serviceId, int quantity)

PUT /hms/dashboard/room-service/:id (body: int quantity)

PUT /check-out/:id