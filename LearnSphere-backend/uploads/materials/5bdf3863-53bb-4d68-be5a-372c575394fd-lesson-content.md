# Topic 5: Cloud Deployment, Security & Production Actuator

Spring Boot Actuator for health monitoring, AWS Elastic Beanstalk / Docker cloud deployment, testing with JUnit/Mockito, and Spring Security.

---

## Chapter 6: Deploying Sample application using Spring-boot on Amazon Elastic Beanstalk 24

Examples 24
Deploying sample application using Spring-boot in Jar format on AWS 24

---

## Chapter 7: Fully-Responsive Spring Boot Web Application with JHipster 31

Examples 31
Create Spring Boot App using jHipster on Mac OS 31

---

## Chapter 11: Spring boot + Hibernate + Web UI (Thymeleaf) 48

Introduction 48
Remarks 48
Examples 48
Maven dependencies 48
Hibernate Configuration 49
Entities and Repositories 50
Thymeleaf Resources and Spring Controller 50

---

## Chapter 19: Testing in Spring Boot 88

Examples 88
How to Test a Simple Spring Boot Application 88
Loading different yaml [or properties] file or override some properties 91
Loading different yml file 91
Alternatively options 91

---

## Chapter 6: Deploying Sample application

using Spring-boot on Amazon Elastic 
Beanstalk
Examples
Deploying sample application using Spring-boot in Jar format on AWS
Create a sample application using spring-boot from spring-boot initializer site.1. 
Import the code in your local IDE and run the goal as clean install spring-boot:run -e2. 
Go to target folder and check for the jar file.3. 
Open your Amazon account or create a new Amazon Account and select for the Elastic 
Beanstalk as shown in the below 
4. 
https://riptutorial.com/ 24

.
Create a new web server environment as shown in below 5. 
https://riptutorial.com/ 25

.
Select the Environment type as Java for JAR file deployment for Spring-boot, if you are 
planning to deploy as a WAR file, it should be selected as tomcat as shown in below 
6. 
https://riptutorial.com/ 26

. 
https://riptutorial.com/ 27

Select with Default configuration's upon clicking next next ...7. 
Once you complete the default configuration's, in the overview screen the JAR file can be 
uploaded and deployed as shown in the figures. 
 
8. 
https://riptutorial.com/ 28

Once the Deployment is successful (5 -10 minutes for the first time) you can hit the context 
url as shown in the figure below. 
9. 
Result is as shown below,it should work as same as with your local env.10. 
https://riptutorial.com/ 29

Please find my Github URL11. 
Read Deploying Sample application using Spring-boot on Amazon Elastic Beanstalk online: 
https://riptutorial.com/spring-boot/topic/6117/deploying-sample-application-using-spring-boot-on-
amazon-elastic-beanstalk
https://riptutorial.com/ 30

---

## Chapter 7: Fully-Responsive Spring Boot

Web Application with JHipster
Examples
Create Spring Boot App using jHipster on Mac OS
jHipster allows you to bootstrap a Spring Boot web application with a REST API back-end and a 
AngularJS and Twitter Bootstrap front-end.
More on jHipster here: jHipster Documentation
Install brew:
 /usr/bin/ruby -e "$(curl -fsSL 
https://raw.githubusercontent.com/Homebrew/install/master/install)"
View additional info on how to install brew here: Install Brew
Install Gradle
Gradle is a dependency management and build system.
 brew install gradle
Install Git
Git is a version control tool
 brew install git
Install NodeJS
NodeJS gives you access to npm, the node package manager which is needed to install other 
tools.
 brew install node
Install Yeoman
Yeoman is a generator
 npm install -g yo
Install Bower
https://riptutorial.com/ 31

Bower is a dependency management tool
 npm install -g bower
Install Gulp
Gulp is a task runner
 npm install -g gulp
Install jHipster Yeoman Generator
This is the jHipster generator
 npm install -g generator-jhipster
Create an Application
Open a Terminal window.
Navigate to the root directory where you will keep your projects. Create an empty directory in 
which you will create your application
 mkdir myapplication
Go to that directory
 cd myapplication/
To generate your application, type
 yo jhipster
You will be prompted with the following questions
Which type of application would you like to create?
Your type of application depends on whether you wish to use a microservices architecture or not. 
A full explanation on microservices is available here, if unsure use the default “Monolithic 
application”.
Choose Monolithic application by default if you are not sure
What is your default Java package name?
Your Java application will use this as its root package.
Which type of authentication would you like to use?
https://riptutorial.com/ 32

Use basic session-based Spring Security by default if you are not sure
Which type of database would you like to use?
Which development database would you like to use?
This is the database you will use with your “development” profile. You can either use:
Use H2 by default if you are not sure
H2, running in-memory. This is the easiest way to use JHipster, but your data will be lost when you 
restart your server.
Do you want to use Hibernate 2nd level cache?
Hibernate is the JPA provider used by JHipster. For performance reasons, we highly recommend 
you to use a cache, and to tune it according to your application’s needs. If you choose to do so, 
you can use either ehcache (local cache) or Hazelcast (distributed cache, for use in a clustered 
environnement)
Do you want to use a search engine in your application? Elasticsearch will be configured using 
Spring Data Elasticsearch. You can find more information on our Elasticsearch guide.
Choose no if you are not sure
Do you want to use clustered HTTP sessions?
By default, JHipster uses a HTTP session only for storing Spring Security’s authentication and 
autorisations information. Of course, you can choose to put more data in your HTTP sessions. 
Using HTTP sessions will cause issues if you are running in a cluster, especially if you don’t use a 
load balancer with “sticky sessions”. If you want to replicate your sessions inside your cluster, 
choose this option to have Hazelcast configured.
Choose no if you are not sure
Do you want to use WebSockets? Websockets can be enabled using Spring Websocket. We also 
provide a complete sample to show you how to use the framework efficiently.
Choose no if you are not sure
Would you like to use Maven or Gradle? You can build your generated Java application either with 
Maven or Gradle. Maven is more stable and more mature. Gradle is more flexible, easier to 
extend, and more hype.
Choose Gradle if you are not sure
Would you like to use the LibSass stylesheet preprocessor for your CSS? Node-sass a great 
solution to simplify designing CSS. To be used efficiently, you will need to run a Gulp server,

---

## Chapter 11: Spring boot + Hibernate + Web UI

(Thymeleaf)
Introduction
This thread is focused on how to create a spring boot application with hibernate and thymyleaf 
template engine.
Remarks
Also check the Thymeleaf documentation
Examples
Maven dependencies
This example is based on spring boot 1.5.1.RELEASE. with the following dependencies:
<!-- Spring --> 
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-data-jpa</artifactId> 
</dependency> 
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-thymeleaf</artifactId> 
</dependency> 
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-web</artifactId> 
</dependency> 
<!-- Lombok --> 
<dependency> 
    <groupId>org.projectlombok</groupId> 
    <artifactId>lombok</artifactId> 
    <optional>true</optional> 
</dependency> 
<!-- H2 --> 
<dependency> 
    <groupId>com.h2database</groupId> 
    <artifactId>h2</artifactId> 
</dependency> 
<!-- Test --> 
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-test</artifactId> 
    <scope>test</scope> 
</dependency>
In this example we are going to use Spring Boot JPA, Thymeleaf and web starters. I am using 
https://riptutorial.com/ 48

Lombok to generate getters and setters easier but it is not mandatory. H2 will be used as an in-
memory easy to configure database.
Hibernate Configuration
First, lets overview what we need in order to setup Hibernate correctly.
@EnableTransactionManagement and @EnableJpaRepositories - we want transactional 
management and to use spring data repositories.
1. 
DataSource - main datasource for the application. using in-memory h2 for this example.2. 
LocalContainerEntityManagerFactoryBean - spring entity manager factory using 
HibernateJpaVendorAdapter.
3. 
PlatformTransactionManager - main transaction manager for @Transactional annotated 
components.
4. 
Configuration file:
@Configuration 
@EnableTransactionManagement 
@EnableJpaRepositories(basePackages = "com.example.repositories") 
public class PersistanceJpaConfig { 
 
    @Bean 
    public DataSource dataSource() { 
        DriverManagerDataSource dataSource = new DriverManagerDataSource(); 
        dataSource.setDriverClassName("org.h2.Driver"); 
        dataSource.setUrl("jdbc:h2:mem:testdb;mode=MySQL;DB_CLOSE_DELAY=-
1;DB_CLOSE_ON_EXIT=FALSE"); 
        dataSource.setUsername("sa"); 
        dataSource.setPassword(""); 
        return dataSource; 
    } 
 
    @Bean 
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) 
{ 
        LocalContainerEntityManagerFactoryBean em = new 
LocalContainerEntityManagerFactoryBean(); 
        em.setDataSource(dataSource); 
        em.setPackagesToScan(new String[] { "com.example.models" }); 
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter(); 
        em.setJpaVendorAdapter(vendorAdapter); 
        em.setJpaProperties(additionalProperties()); 
        return em; 
    } 
 
    @Bean 
    public PlatformTransactionManager 
transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory, DataSource 
dataSource) { 
        JpaTransactionManager tm = new JpaTransactionManager(); 
        tm.setEntityManagerFactory(entityManagerFactory.getObject()); 
        tm.setDataSource(dataSource); 
        return tm; 
    } 
 
https://riptutorial.com/ 49

    Properties additionalProperties() { 
        Properties properties = new Properties(); 
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); 
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect"); 
        return properties; 
    } 
 
}
Entities and Repositories
A simple entity: Using Lombok @Getter and @Setter annotations to generate getters and setters for 
us
@Entity 
@Getter @Setter 
public class Message { 
 
    @Id 
    @GeneratedValue(generator = "system-uuid") 
    @GenericGenerator(name = "system-uuid"

---

## Chapter 19: Testing in Spring Boot

Examples
How to Test a Simple Spring Boot Application
We have a sample Spring boot application which stores user data in MongoDB and we are using 
Rest services to retrieve data
First there is a domain class i.e. POJO
@Document 
public class User{ 
    @Id 
    private String id; 
 
    private String name; 
 
}
A corresponding repository based on Spring Data MongoDB
public interface UserRepository extends MongoRepository<User, String> { 
}
Then Our User Controller
@RestController 
class UserController { 
 
    @Autowired 
    private UserRepository repository; 
 
    @RequestMapping("/users") 
    List<User> users() { 
        return repository.findAll(); 
    } 
 
    @RequestMapping(value = "/Users/{id}", method = RequestMethod.DELETE) 
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    void delete(@PathVariable("id") String id) { 
        repository.delete(id); 
    } 
 
    // more controller methods 
}
And finally our Spring Boot Application
@SpringBootApplication 
public class Application { 
    public static void main(String args[]){ 
https://riptutorial.com/ 88

     SpringApplication.run(Application.class, args); 
    } 
}
If, let’s say that John Cena, The Rock and TripleHHH were the only three users in the database, a 
request to /users would give the following response:
$ curl localhost:8080/users 
[{"name":"John Cena","id":"1"},{"name":"The Rock","id":"2"},{"name":"TripleHHH","id":"3"}]
Now to test the code we will verify that the application work
@RunWith(SpringJUnit4ClassRunner.class)   // 1 
@SpringApplicationConfiguration(classes = Application.class)   // 2 
@WebAppConfiguration   // 3 
@IntegrationTest("server.port:0")   // 4 
public class UserControllerTest { 
 
    @Autowired   // 5 
    UserRepository repository; 
 
    User cena; 
    User rock; 
    User tripleHHH; 
 
    @Value("${local.server.port}")   // 6 
    int port; 
 
    @Before 
    public void setUp() { 
        // 7 
        cena = new User("John Cena"); 
        rock = new User("The Rock"); 
        tripleHHH = new User("TripleHH"); 
 
        // 8 
        repository.deleteAll(); 
        repository.save(Arrays.asList(cena, rock, tripleHHH)); 
 
        // 9 
        RestAssured.port = port; 
    } 
 
    // 10 
    @Test 
    public void testFetchCena() { 
        String cenaId = cena.getId(); 
 
        when(). 
                get("/Users/{id}", cenaId). 
        then(). 
                statusCode(HttpStatus.SC_OK). 
                body("name", Matchers.is("John Cena")). 
                body("id", Matchers.is(cenaId)); 
    } 
 
    @Test 
    public void testFetchAll() { 
https://riptutorial.com/ 89

        when(). 
                get("/users"). 
        then(). 
                statusCode(HttpStatus.SC_OK). 
                body("name", Matchers.hasItems("John Cena", "The Rock", "TripleHHH")); 
    } 
 
    @Test 
    public void testDeletetripleHHH() { 
        String tripleHHHId = tripleHHH.getId(); 
 
        when() 
                .delete("/Users/{id}", tripleHHHId). 
        then(). 
                statusCode(HttpStatus.SC_NO_CONTENT); 
    } 
}
Explanation
Like any other Spring based test, we need the SpringJUnit4ClassRunner so that an application 
context is created.
1. 
The @SpringApplicationConfiguration annotation is similar to the @ContextConfiguration 
annotation in that it is used to specify which application context(s) that should be used in the 
test. Additionally, it will trigger logic for reading Spring Boot specific configurations, 
properties, and so on.
2. 
@WebAppConfiguration must be present in order to tell Spring that a WebApplicationContext 
should be loaded for the test. It also provides an attribute for specifying the path to the root 
of the web application.
3. 
@IntegrationTest is used to tell Spring Boot that the embedded web server should be started. 
By providing colon- or equals-separated name-value pair(s), any environment variable can 
be overridden. In this example, the "server.port:0" will over

---

