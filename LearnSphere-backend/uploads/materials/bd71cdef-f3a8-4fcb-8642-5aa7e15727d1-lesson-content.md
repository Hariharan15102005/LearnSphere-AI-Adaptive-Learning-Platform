# Topic 4: Data Persistence, Database Integration & Caching

Spring Data JPA, Hibernate, MySQL connectivity, JDBC template, transactions, and distributed caching with Redis.

---

## Chapter 2: Caching with Redis Using Spring Boot for MongoDB 6

Examples 6
Why Caching? 6
The Basic System 6

---

## Chapter 3: Connecting a spring-boot application to MySQL 13

Introduction 13
Remarks 13
Examples 13
Spring-boot sample using MySQL 13

---

## Chapter 12: Spring boot + JPA + mongoDB 52

Examples 52
CRUD operation in MongoDB using JPA 52
Customer Controller 53
Customer Repository 54
pom.xml 54
Insert data using rest client : POST method 54
Get Request URL 55
Get request result: 55

---

## Chapter 13: Spring Boot + JPA + REST 57

Remarks 57
Examples 57
Spring Boot Startup 57
Domain Object 57
Repository Interface 58
Maven Configuration 59

---

## Chapter 15: Spring boot + Spring Data JPA 69

Introduction 69
Remarks 69
Annotations 69
Official Documentation 69
Examples 70
Spring Boot and Spring Data JPA integration basic example 70
Main Class 70
Entity Class 70
Transient Properties 71
DAO Class 72
Service Class 72
Service Bean 72
Controller Class 73
Application properties file for MySQL database 75
SQL file 75
pom.xml file 75
Building an executable JAR 76

---

## Chapter 17: Spring-Boot + JDBC 80

Introduction 80
Remarks 80
Examples 81
schema.sql file 81
First JdbcTemplate Boot App 82
data.sql 82

---

## Chapter 18: Spring-Boot Microservice with JPA 83

Examples 83
Application Class 83
Book Model 83
Book Repository 84
Enabling validation 84
Loading some test data 85
Adding the Validator 85
Gradle Build File 86

---

## Chapter 20: ThreadPoolTaskExecutor: configuration and usage 93

Examples 93
application configuration 93
Credits 94
About
You can share this PDF with anyone you feel could benefit from it, downloaded the latest version 
from: spring-boot
It is an unofficial and free spring-boot ebook created for educational purposes. All the content is 
extracted from Stack Overflow Documentation, which is written by many hardworking individuals at 
Stack Overflow. It is neither affiliated with Stack Overflow nor official spring-boot.
The content is released under Creative Commons BY-SA, and the list of contributors to each 
chapter are provided in the credits section at the end of this book. Images may be copyright of 
their respective owners unless otherwise specified. All trademarks and registered trademarks are 
the property of their respective company owners.
Use the content presented in this book at your own risk; it is not guaranteed to be correct nor 
accurate, please send your feedback and corrections to info@zzzprojects.com
https://riptutorial.com/ 1

---

## Chapter 2: Caching with Redis Using Spring

Boot for MongoDB
Examples
Why Caching?
Today, performance is one of the most important metrics we need to evaluate when developing a 
web service/Application. Keeping customers engaged is critical to any product and for this reason, 
it is extremely important to improve the performances and reduce page load times.
When running a web server that interacts with a database, its operations may become a 
bottleneck. MongoDB is no exception here, and as our MongoDB database scales up, things can 
really slow down. This issue can even get worse if the database server is detached from the web 
server. In such systems, the communication with the database can cause a big overhead.
Luckily, we can use a method called caching to speed things up. In this example, we’ll introduce 
this method and see how we can use it to enhance the performance of our application using 
Spring Cache, Spring Data, and Redis.
The Basic System
As the first step, we’ll build a basic web server that stores data in MongoDB. For this 
demonstration, we’ll name it “fast Library”. The server will have two basic operations:
POST /book: This endpoint will receive the title, the author, and the content of the book, and create a 
book entry in the database.
GET /book/ {title}: This endpoint will get a title and return its content. We assume that titles 
uniquely identify books (thus, there won’t be two books with the same title). A better alternative 
would be, of course, to use an ID. However, to keep things simple, we’ll simply use the title.
This is a simple library system, but we’ll add more advanced abilities later.
Now, let’s create the project using Spring Tool Suite (build using eclipse) and spring starter Project
https://riptutorial.com/ 6

We are building our project using Java and to build we are using maven, select values and click on 
next
https://riptutorial.com/ 7

Select MongoDB, Redis from NOSQL and Web from the web module and click on finish. We are 
using Lombok for auto generation of Setters and getters of model values so we need to add the 
Lombok dependency to the POM
https://riptutorial.com/ 8

 
https://riptutorial.com/ 9

MongoDbRedisCacheApplication.java contains the main method which is used to run Spring Boot 
Application add
Create model class Book which contains id, book title, author, description and annotate with 
@Data to generate automatic setters and getters from jar project lombok
package com.example; 
import org.springframework.data.annotation.Id; 
import org.springframework.data.mongodb.core.index.Indexed; 
import lombok.Data; 
@Data 
public class Book { 
    @Id 
    private String id; 
    @Indexed 
    private String title; 
    private String author; 
    private String description; 
}
Spring Data creates all basic CRUD Operations for us automatically so let’s create 
BookRepository.Java which finds book by title and deletes book
package com.example; 
import org.springframework.data.mongodb.repository.MongoRepository; 
public interface BookRepository  extends MongoRepository<Book, String> 
{ 
    Book findByTitle(String title); 
    void delete(String title); 
}
https://riptutorial.com/ 10

Let’s create webservicesController which saves data to MongoDB and retrieve data by 
idTitle(@PathVariable String title).
package com.example; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.mongodb.core.MongoTemplate; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.RestController; 
@RestController 
public class WebServicesController { 
    @Autowired 
    BookRepository repository; 
    @Autowired 
    MongoTemplate mongoTemplate; 
    @RequestMapping(value = "/book", method = RequestMethod.POST) 
    public Book saveBook(Book book) 
    { 
        return repository.save(book); 
    } 
    @RequestMapping(va

---

## Chapter 3: Connecting a spring-boot

application to MySQL
Introduction
We know that spring-boot by default runs using H2 database. In this article, we will see how to 
tweak the default configuration to work with MySQL database.
Remarks
As a pre-requisite, make sure that MySQL is already running on port 3306 and has your database 
created.
Examples
Spring-boot sample using MySQL
We will follow the official guide for spring-boot and spring-data-jpa. We will be building the 
application using gradle.
Create the gradle build file
build.gradle
buildscript { 
    repositories { 
        mavenCentral() 
    } 
    dependencies { 
        classpath("org.springframework.boot:spring-boot-gradle-plugin:1.4.3.RELEASE") 
    } 
} 
 
apply plugin: 'java' 
apply plugin: 'eclipse' 
apply plugin: 'idea' 
apply plugin: 'org.springframework.boot' 
 
jar { 
    baseName = 'gs-accessing-data-jpa' 
    version =  '0.1.0' 
} 
 
repositories { 
    mavenCentral() 
    maven { url "https://repository.jboss.org/nexus/content/repositories/releases" } 
} 
 
sourceCompatibility = 1.8 
1. 
https://riptutorial.com/ 13

targetCompatibility = 1.8 
 
dependencies { 
    compile("org.springframework.boot:spring-boot-starter-data-jpa") 
    runtime('mysql:mysql-connector-java') 
    testCompile("junit:junit") 
}
Create the customer entity
src/main/java/hello/Customer.java
@Entity 
public class Customer { 
 
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO) 
    private Long id; 
    private String firstName; 
    private String lastName; 
 
    protected Customer() {} 
 
    public Customer(String firstName, String lastName) { 
        this.firstName = firstName; 
        this.lastName = lastName; 
    } 
 
    @Override 
    public String toString() { 
        return String.format( 
                "Customer[id=%d, firstName='%s', lastName='%s']", 
                id, firstName, lastName); 
    } 
}
2. 
Create Repositories
src/main/java/hello/CustomerRepository.java
import java.util.List; 
import org.springframework.data.repository.CrudRepository; 
 
public interface CustomerRepository extends CrudRepository<Customer, Long> { 
    List<Customer> findByLastName(String lastName); 
}
3. 
Create application.properties file
################### DataSource Configuration ########################## 
jdbc.driverClassName=com.mysql.jdbc.Driver 
jdbc.url=jdbc:mysql://localhost:3306/your_database_name 
jdbc.username=username 
jdbc.password=password 
 
init-db=false 
4. 
https://riptutorial.com/ 14

 
################### Hibernate Configuration ########################## 
 
hibernate.dialect=org.hibernate.dialect.MySQLDialect 
hibernate.show_sql=true 
hibernate.hbm2ddl.auto=update
Create the PersistenceConfig.java file5. 
In step 5, we will be defining how the datasource will be loaded and how our 
application connects to MySQL. The above snippet is the bare minimum configuration 
we need to connect to MySQL. Here we provide two beans:
@Configuration 
@EnableTransactionManagement 
@EnableJpaRepositories(basePackages="hello") 
public class PersistenceConfig 
{ 
    @Autowired 
    private Environment env; 
 
    @Bean 
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() 
    { 
        LocalContainerEntityManagerFactoryBean factory = new 
LocalContainerEntityManagerFactoryBean(); 
 
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter(); 
        vendorAdapter.setGenerateDdl(Boolean.TRUE); 
        vendorAdapter.setShowSql(Boolean.TRUE); 
 
        factory.setDataSource(dataSource()); 
        factory.setJpaVendorAdapter(vendorAdapter); 
        factory.setPackagesToScan("hello"); 
 
        Properties jpaProperties = new Properties(); 
        jpaProperties.put("hibernate.hbm2ddl.auto", 
env.getProperty("hibernate.hbm2ddl.auto")); 
        factory.setJpaProperties(jpaProperties); 
 
        factory.afterPropertiesSet(); 
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver()); 
        return factory; 
    } 
 
    @Bean 
    public DataSo

---

## Chapter 12: Spring boot + JPA + mongoDB

Examples
CRUD operation in MongoDB using JPA
Customer Model
package org.bookmytickets.model; 
 
import org.springframework.data.annotation.Id; 
 
public class Customer { 
 
    @Id 
    private String id; 
    private String firstName; 
    private String lastName; 
 
    public Customer() {} 
 
    public Customer(String firstName, String lastName) { 
        this.firstName = firstName; 
        this.lastName = lastName; 
    } 
 
    public Customer(String id,String firstName, String lastName) { 
        this.id = id; 
        this.firstName = firstName; 
        this.lastName = lastName; 
    } 
 
    @Override 
    public String toString() { 
        return String.format( 
                "Customer[id=%s, firstName='%s', lastName='%s']", 
                id, firstName, lastName); 
    } 
 
    public String getId() { 
        return id; 
    } 
 
    public void setId(String id) { 
        this.id = id; 
    } 
 
    public String getFirstName() { 
        return firstName; 
    } 
 
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    } 
 
    public String getLastName() { 
https://riptutorial.com/ 52

        return lastName; 
    } 
 
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    } 
 
}
Customer Controller
package org.bookmytickets.controller; 
 
import java.util.List; 
 
import org.bookmytickets.model.Customer; 
import org.bookmytickets.repository.CustomerRepository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController; 
 
@RestController 
@RequestMapping(value = "/customer") 
public class CustomerController { 
 
    @Autowired 
    private CustomerRepository repository; 
 
    @GetMapping("") 
    public List<Customer> selectAll(){ 
        List<Customer> customerList = repository.findAll(); 
        return customerList; 
    } 
 
    @GetMapping("/{id}") 
    public List<Customer> getSpecificCustomer(@PathVariable String id){ 
        return repository.findById(id); 
    } 
 
    @GetMapping("/search/lastName/{lastName}") 
    public List<Customer> searchByLastName(@PathVariable String lastName){ 
        return repository.findByLasttName(lastName); 
    } 
 
    @GetMapping("/search/firstname/{firstname}") 
    public List<Customer> searchByFirstName(@PathVariable String firstName){ 
        return repository.findByFirstName(firstName); 
    } 
 
    @PostMapping("") 
    public void insert(@RequestBody Customer customer) { 
        repository.save(customer); 
    } 
 
    @PatchMapping("/{id}") 
    public void update(@RequestParam String id, @RequestBody Customer customer) { 
https://riptutorial.com/ 53

        Customer oldCustomer = repository.finedById(id); 
        if(customer.getFirstName() != null) { 
            oldCustomer.setFristName(customer.getFirstName()); 
        } 
        if(customer.getLastName() != null) { 
            oldCustomer.setLastName(customer.getLastName()); 
        } 
        repository.save(oldCustomer); 
    } 
 
    @DeleteMapping("/{id}") 
    public void delete(@RequestParam String id) { 
        Customer deleteCustomer = repository.findById(id); 
        repository.delete(deleteCustomer); 
    } 
}
Customer Repository
package org.bookmytickets.repository; 
 
import java.util.List; 
 
import org.bookmytickets.model.Customer; 
import org.springframework.data.mongodb.repository.MongoRepository; 
 
public interface CustomerRepository extends MongoRepository<Customer, String> { 
    public Customer findByFirstName(String firstName); 
    public List<Customer> findByLastName(String lastName); 
}
pom.xml
Please add below dependencies in pom.xml file:
<dependencies> 
 
        <dependency> 

---

## Chapter 13: Spring Boot + JPA + REST

Remarks
This example uses Spring Boot, Spring Data JPA and Spring Data REST to expose a simple JPA-


*(Full detailed section available in attached reference document)*