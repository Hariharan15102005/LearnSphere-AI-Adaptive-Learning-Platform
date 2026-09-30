# Topic 3: RESTful Web Services, Controllers & Microservices

Building RESTful Web Services with @RestController, request handling, HTTP methods, JSON serialization, Swagger/OpenAPI, and Microservices integration.

---

## Chapter 4: Controllers 18

Introduction 18
Examples 18
Spring boot rest controller. 18

---

## Chapter 10: REST Services 41

Parameters 41
Examples 41
Creating a REST-Service 41
Creating a Rest Service with JERSEY and Spring Boot 44
1.Project Setup 44
2.Creating a Controller 44
3.Wiring Jersey Configurations 45
4.Done 45
Consuming a REST API with RestTemplate (GET) 45

---

## Chapter 13: Spring Boot + JPA + REST 57

Remarks 57
Examples 57
Spring Boot Startup 57
Domain Object 57
Repository Interface 58
Maven Configuration 59

---

## Chapter 16: Spring Boot- Hibernate-REST Integration 77

Examples 77
Add Hibernate support 77
Add REST support 78

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

## Chapter 4: Controllers

Introduction
In this section i will add an example for the Spring boot rest controller with Get and post request.
Examples
Spring boot rest controller.
In this example i will show how to formulate an rest controller to get and post data to 
the database using JPA with the most ease and least code.
In this example we will refer to the data table named buyerRequirement .
BuyingRequirement.java
@Entity @Table(name = "BUYINGREQUIREMENTS") @NamedQueries({ @NamedQuery(name 
= "BuyingRequirement.findAll", query = "SELECT b FROM BuyingRequirement b")}) public class 
BuyingRequirement extends Domain implements Serializable { private static final long 
serialVersionUID = 1L;
@Column(name = "PRODUCT_NAME", nullable = false) 
private String productname; 
 
@Column(name = "NAME", nullable = false) 
private String name; 
 
@Column(name = "MOBILE", nullable = false) 
private String mobile; 
 
@Column(name = "EMAIL", nullable = false) 
private String email; 
 
@Column(name = "CITY") 
private String city; 
 
 
public BuyingRequirement() { 
} 
 
 
public String getProductname() { 
    return productname; 
} 
 
public void setProductname(String productname) { 
    this.productname = productname; 
} 
 
public String getName() { 
    return name; 
https://riptutorial.com/ 18

} 
 
public void setName(String name) { 
    this.name = name; 
} 
 
public String getMobile() { 
    return mobile; 
} 
 
public void setMobile(String mobile) { 
    this.mobile = mobile; 
} 
 
public String getEmail() { 
    return email; 
} 
 
public void setEmail(String email) { 
    this.email = email; 
} 
 
public String getCity() { 
    return city; 
} 
 
public void setCity(String city) { 
    this.city = city; 
}
}
This is the entity class which includes the parameter referring to columns in byuingRequirement 
table and their getters and setters.
IBuyingRequirementsRepository.java(JPA interface)
@Repository 
@RepositoryRestResource 
public interface IBuyingRequirementsRepository extends JpaRepository<BuyingRequirement, UUID> 
{ 
   // Page<BuyingRequirement> findAllByOrderByCreatedDesc(Pageable pageable); 
    Page<BuyingRequirement> findAllByOrderByCreatedDesc(Pageable pageable); 
    Page<BuyingRequirement> findByNameContainingIgnoreCase(@Param("name") String name, 
Pageable pageable); 
}
BuyingRequirementController.java
@RestController 
@RequestMapping("/api/v1") 
public class BuyingRequirementController { 
 
    @Autowired 
    IBuyingRequirementsRepository iBuyingRequirementsRepository; 
    Email email = new Email(); 
 
    BuyerRequirementTemplate buyerRequirementTemplate = new BuyerRequirementTemplate(); 
https://riptutorial.com/ 19

 
    private String To = "support@pharmerz.com"; 
    // private String To = "amigujarathi@gmail.com"; 
    private String Subject = "Buyer Request From Pharmerz "; 
 
    @PostMapping(value = "/buyingRequirement") 
    public ResponseEntity<BuyingRequirement> CreateBuyingRequirement(@RequestBody 
BuyingRequirement buyingRequirements) { 
 
        String productname = buyingRequirements.getProductname(); 
        String name = buyingRequirements.getName(); 
        String mobile = buyingRequirements.getMobile(); 
        String emails = buyingRequirements.getEmail(); 
        String city = buyingRequirements.getCity(); 
        if (city == null) { 
            city = "-"; 
        } 
 
        String HTMLBODY = buyerRequirementTemplate.template(productname, name, emails, mobile, 
city); 
 
        email.SendMail(To, Subject, HTMLBODY); 
 
 
        iBuyingRequirementsRepository.save(buyingRequirements); 
        return new ResponseEntity<BuyingRequirement>(buyingRequirements, HttpStatus.CREATED); 
    } 
 
 
    @GetMapping(value = "/buyingRequirements") 
    public Page<BuyingRequirement> getAllBuyingRequirements(Pageable pageable) { 
 
        Page requirements = 
iBuyingRequirementsRepository.findAllByOrderByCreatedDesc(pageable); 
        return requirements; 
    } 
 
    @GetMapping(value = "/buyingRequirmentByName/{name}"

---

## Chapter 10: REST Services

Parameters
Annotation Column
@Controller Indicates that an annotated class is a "Controller" (web 
controller).
@RequestMapping
Annotation for mapping web requests onto specific handler 
classes (if we used with class) and/or handler methods (if we 
used with methods).
method = 
RequestMethod.GET Type of HTTP request methods
ResponseBody Annotation that indicates a method return value should be bound 
to the web response body
@RestController @Controller + ResponseBody
@ResponseEntity Extension of HttpEntity that adds a HttpStatus status code, we 
can control the return http code
Examples
Creating a REST-Service
Create project using STS (Spring Starter Project) or Spring Initializr (at https://start.spring.io 
).
1. 
Add a Web Dependency in your pom.xml:2. 
    <dependency> 
        <groupId>org.springframework.boot</groupId> 
        <artifactId>spring-boot-starter-web</artifactId> 
    </dependency>
or type web in Search for dependencies search box, add web dependency and download zipped 
project.
Create a Domain Class (i.e. User)3. 
 public class User { 
 
        private Long id; 
 
https://riptutorial.com/ 41

        private String userName; 
 
        private String password; 
 
        private String email; 
 
        private String firstName; 
 
        private String lastName; 
 
        public Long getId() { 
            return id; 
        } 
 
        public void setId(Long id) { 
            this.id = id; 
        } 
 
        public String getUserName() { 
            return userName; 
        } 
 
        public void setUserName(String userName) { 
            this.userName = userName; 
        } 
 
        public String getPassword() { 
            return password; 
        } 
 
        public void setPassword(String password) { 
            this.password = password; 
        } 
 
        public String getEmail() { 
            return email; 
        } 
 
        public void setEmail(String email) { 
            this.email = email; 
        } 
 
        public String getFirstName() { 
            return firstName; 
        } 
 
        public void setFirstName(String firstName) { 
            this.firstName = firstName; 
        } 
 
        public String getLastName() { 
            return lastName; 
        } 
 
        public void setLastName(String lastName) { 
            this.lastName = lastName; 
        } 
 
        @Override 
        public String toString() { 
            return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", 
https://riptutorial.com/ 42

email=" + email 
                    + ", firstName=" + firstName + ", lastName=" + lastName + "]"; 
        } 
 
        public User(Long id, String userName, String password, String email, String firstName, 
String lastName) { 
            super(); 
            this.id = id; 
            this.userName = userName; 
            this.password = password; 
            this.email = email; 
            this.firstName = firstName; 
            this.lastName = lastName; 
        } 
 
        public User() {} 
    }
Create UserController class and add @Controller, @RequestMapping annotations4. 
    @Controller 
    @RequestMapping(value = "api") 
    public class UserController { 
    }
Define static List users variable to simulate database and add 2 users to the list5. 
    private static List<User> users = new ArrayList<User>(); 
 
    public UserController() { 
        User u1 = new User(1L, "shijazi", "password", "shijazi88@gmail.com", "Safwan", 
"Hijazi"); 
        User u2 = new User(2L, "test", "password", "test@gmail.com", "test", "test"); 
        users.add(u1); 
        users.add(u2); 
    }
Create new method to return all users in static list (getAllUsers)6. 
    @RequestMapping(value = "users", method = RequestMethod.GET) 
    public @ResponseBody List<User> getAllUsers() { 
         return users; 
    }
Run the application [by mvn clean install spring-boot:run] and call this URL 
http://localhost:8080/api/users
7. 
We

---

## Chapter 13: Spring Boot + JPA + REST

Remarks
This example uses Spring Boot, Spring Data JPA and Spring Data REST to expose a simple JPA-
managed domain object via REST. The example responds with the HAL JSON format and 
exposes a url accessible on /person. The maven configuration includes a H2 in-memory database 
to support a quick standup.
Examples
Spring Boot Startup
package com.example; 
 
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication; 
 
@SpringBootApplication 
public class Application { 
 
   //main entry point 
    public static void main(String[] args) { 
        SpringApplication.run(Application.class, args); 
    } 
 
}
Domain Object
package com.example.domain; 
 
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.Id; 
 
//simple person object with JPA annotations 
 
@Entity 
public class Person { 
 
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO) 
    private Long id; 
 
    @Column 
    private String firstName; 
 
    @Column 
    private String lastName; 
 
    public Long getId() { 
https://riptutorial.com/ 57

        return id; 
    } 
 
    public void setId(Long id) { 
        this.id = id; 
    } 
 
    public String getFirstName() { 
        return firstName; 
    } 
 
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    } 
 
    public String getLastName() { 
        return lastName; 
    } 
 
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    } 
 
}
Repository Interface
package com.example.domain; 
 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.repository.query.Param; 
import org.springframework.data.rest.core.annotation.RepositoryRestResource; 
import org.springframework.data.rest.core.annotation.RestResource; 
 
//annotation exposes the 
@RepositoryRestResource(path="/person") 
public interface PersonRepository extends JpaRepository<Person,Long> { 
 
    //the method below allows us to expose a search query, customize the endpoint name, and 
specify a parameter name 
    //the exposed URL is GET /person/search/byLastName?lastname= 
    @RestResource(path="/byLastName") 
    Iterable<Person> findByLastName(@Param("lastName") String lastName); 
 
    //the methods below are examples on to prevent an operation from being exposed. 
    //For example DELETE; the methods are overridden and then annotated with 
RestResouce(exported=false) to make sure that no one can DELETE entities via REST 
    @Override 
    @RestResource(exported=false) 
    default void delete(Long id) { } 
 
    @Override 
    @RestResource(exported=false) 
    default void delete(Person entity) { } 
 
    @Override 
    @RestResource(exported=false) 
    default void delete(Iterable<? extends Person> entities) { } 
 
https://riptutorial.com/ 58

    @Override 
    @RestResource(exported=false) 
    default void deleteAll() { } 
 
    @Override 
    @RestResource(exported=false) 
    default void deleteAllInBatch() { } 
 
    @Override 
    @RestResource(exported=false) 
    default void deleteInBatch(Iterable<Person> arg0) { } 
 
 
}
Maven Configuration
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-
4.0.0.xsd"> 
    <modelVersion>4.0.0</modelVersion> 
    <parent> 
        <groupId>org.springframework.boot</groupId> 
        <artifactId>spring-boot-starter-parent</artifactId> 
        <version>1.4.0.RELEASE</version> 
    </parent> 
    <groupId>com.example</groupId> 
    <artifactId>spring-boot-data-jpa-rest</artifactId> 
    <version>0.0.1-SNAPSHOT</version> 
    <name>spring-boot-data-jpa-rest</name> 
    <build> 
        <plugins> 
            <plugin> 
                <artifactId>maven-compiler-plugin</artifactId> 
                <configuration> 
                    <sou

---

## Chapter 16: Spring Boot- Hibernate-REST

Integration
Examples
Add Hibernate support
Add spring-boot-starter-data-jpa dependency to pom.xml. You may skip version tag, if 
you are using spring-boot-starter-parent as the parent of your pom.xml. The dependency 
below brings Hibernate and everything related to JPA to your project (reference).
1. 
    <dependency> 
        <groupId>org.springframework.boot</groupId> 
        <artifactId>spring-boot-starter-data-jpa</artifactId> 
    </dependency>
Add database driver to pom.xml. This one below is for H2 database (reference).
<dependency> 
    <groupId>com.h2database</groupId> 
    <artifactId>h2</artifactId> 
</dependency>
2. 
Enable debug logging for Hibernate in application.properties
logging.level.org.hibernate.SQL = debug
3. 
or in application.yml
logging: 
  level: 
    org.hibernate.SQL: debug
Add entity class to desired package under ${project.home}/src/main/java/, for example 
under com.example.myproject.domain (reference):
package com.example.myproject.domain; 
 
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.Id; 
import java.io.Serializable; 
 
@Entity 
public class City implements Serializable { 
 
    @Id 
    @GeneratedValue 
4. 
https://riptutorial.com/ 77

    public Long id; 
 
    @Column(nullable = false) 
    public String name; 
}
Add import.sql to ${project.home}/src/main/resources/. Put INSERT statements into the 
file. This file will be used for database schema population on each start of the app (reference
):
insert into city(name) values ('Brisbane'); 
 
insert into city(name) values ('Melbourne');
5. 
Add Repository class to desired package under ${project.home}/src/main/java/, for 
example 

*(Full detailed section available in attached reference document)*