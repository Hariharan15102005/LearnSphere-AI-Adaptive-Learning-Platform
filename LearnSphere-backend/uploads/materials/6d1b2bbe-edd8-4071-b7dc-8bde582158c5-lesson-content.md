# Topic 4: Multithreading, Concurrency & Synchronization

Thread lifecycle, Runnable vs Callable, synchronized blocks, volatile keyword, locks, deadlock prevention, and java.util.concurrent executors.

---

Java Interview Questions i
Java Interview Questions
Java Interview Questions ii
Contents
1 Object Oriented Programming (OOP) 1
1.1 Encapsulation . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1
1.2 Polymorphism . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1
1.3 Inheritance . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1
1.4 Abstraction . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 2
1.5 Differences between Abstraction and Encapsulation . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 2
2 General Questions about Java 3
2.1 What is JVM ? Why is Java called the Platform Independent Programming Language? . . . . . . . . . . . . . . 3
2.2 What is the Difference between JDK and JRE ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 3
2.3 What does the “static” keyword mean ? Can you override private or static method in Java ? . . . . . . . . . . . . 3
2.4 Can you access non static variable in static context ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 3
2.5 What are the Data Types supported by Java ? What is Autoboxing and Unboxing ? . . . . . . . . . . . . . . . . 4
2.6 What is Function Overriding and Overloading in Java ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 4
2.7 What is a Constructor, Constructor Overloading in Java and Copy-Constructor . . . . . . . . . . . . . . . . . . . 4
2.8 Does Java support multiple inheritance ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 4
2.9 What is the difference between an Interface and an Abstract class ? . . . . . . . . . . . . . . . . . . . . . . . . . 4
2.10 What are pass by reference and pass by value ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 5
3 Java Threads 6
3.1 What is the difference between processes and threads ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 6
3.2 Explain different ways of creating a thread. Which one would you prefer and why ? . . . . . . . . . . . . . . . . 6
3.3 Explain the available thread states in a high-level. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 6
3.4 What is the difference between a synchronized method and a synchronized block ? . . . . . . . . . . . . . . . . 7
3.5 How does thread synchronization occurs inside a monitor ? What levels of synchronization can you apply ? . . . 7
3.6 What’s a deadlock ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 7
3.7 How do you ensure that N threads can access N resources without deadlock ? . . . . . . . . . . . . . . . . . . . 7
Java Interview Questions iii
4 Java Collections 8
4.1 What are the basic interfaces of Java Collections Framework ? . . . . . . . . . . . . . . . . . . . . . . . . . . . 8
4.2 Why Collection doesn’t extend Cloneable and Serializable interfaces ? . . . . . . . . . . . . . . . . . . . . . . . 8
4.3 What is an Iterator ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 8
4.4 What differences exist between Iterator and ListIterator ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 8
4.5 What is difference between fail-fast and fail-safe ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 9
4.6 How HashMap works in Java ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 9
4.7 What is the importance of hashCode() and equals() methods ? . . . . . . . . . . . . . . . . . . . . . . . . . . . 9
4.8 What differences exist between HashMap and Hashtable ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 9
4.9 What is difference between Array and ArrayList ? When will you use Array over ArrayList ? . . . . . . . . . . . 9
4.10 What is difference between ArrayList and LinkedList ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 10
4.11 What is Comparable and Comparator interface ? List their differences. . . . . . . . . . . . . . . . . . . . . . . . 10
4.12 What is Java Priority Queue ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 10
4.13 What do you know about the big-O notation and can you give some examples with respect to different data
structures ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 10
4.14 What is the tradeoff between using an unordered array versus an ordered array ? . . . . . . . . . . . . . . . . . . 10
4.15 What are some of the best practices relating to the Java Collection framework ? . . . . . . . . . . . . . . . . . . 11
4.16 What’s the difference between Enumeration and Iterator interfaces ? . . . . . . . . . . . . . . . . . . . . . . . . 11
4.17 What is the difference between HashSet and TreeSet ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 11
5 Garbage Collectors 12
5.1 What is the purpose of garbage collection in Java, and when is it used ? . . . . . . . . . . . . . . . . . . . . . . 12
5.2 What does System.gc() and Runtime.gc() methods do ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 12
5.3 When is the ﬁnalize() called ? What is the purpose of ﬁnalization ? . . . . . . . . . . . . . . . . . . . . . . . . . 12
5.4 If an object reference is set to null, will the Garbage Collector immediately free the memory held by that object ? 12
5.5 What is structure of Java Heap ? What is Perm Gen space in Heap ? . . . . . . . . . . . . . . . . . . . . . . . . 12
5.6 What is the difference between Serial and Throughput Garbage collector ? . . . . . . . . . . . . . . . . . . . . . 13
5.7 When does an Object becomes eligible for Garbage collection in Java ? . . . . . . . . . . . . . . . . . . . . . . 13
5.8 Does Garbage collection occur in permanent generation space in JVM ? . . . . . . . . . . . . . . . . . . . . . . 13
6 Exception Handling 14
6.1 What are the two types of Exceptions in Java ? Which are the differences between them ? . . . . . . . . . . . . . 14
6.2 What is the difference between Exception and Error in java ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . 14
6.3 What is the difference between throw and throws ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 14
6.4 What is the importance of ﬁnally block in exception handling ? . . . . . . . . . . . . . . . . . . . . . . . . . . . 14
6.5 What will happen to the Exception object after exception handling ? . . . . . . . . . . . . . . . . . . . . . . . . 14
6.6 How does ﬁnally block differ from ﬁnalize() method ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 15
Java Interview Questions iv
7 Java Applets 16
7.1 What is an Applet ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 16
7.2 Explain the life cycle of an Applet. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 16
7.3 What happens when an applet is loaded ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 16
7.4 What is the difference between an Applet and a Java Application ? . . . . . . . . . . . . . . . . . . . . . . . . . 16
7.5 What are the restrictions imposed on Java applets ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 16
7.6 What are untrusted applets ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 17
7.7 What is the difference between applets loaded over the internet and applets loaded via the ﬁle system ? . . . . . . 17
7.8 What is the applet class loader, and what does it provide ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 17
7.9 What is the applet security manager, and what does it provide ? . . . . . . . . . . . . . . . . . . . . . . . . . . . 17
8 Swing 18
8.1 What is the difference between a Choice and a List ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.2 What is a layout manager ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.3 What is the difference between a Scrollbar and a JScrollPane ? . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.4 Which Swing methods are thread-safe ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.5 Name three Component subclasses that support painting. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.6 What is clipping ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 18
8.7 What is the difference between a MenuItem and a CheckboxMenuItem ? . . . . . . . . . . . . . . . . . . . . . . 18
8.8 How are the elements of a BorderLayout organized ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 19
8.9 How are the elements of a GridBagLayout organized ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 19
8.10 What is the difference between a Window and a Frame ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 19
8.11 What is the relationship between clipping and repainting ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 19
8.12 What is the relationship between an event-listener interface and an event-adapter class ? . . . . . . . . . . . . . . 19
8.13 How can a GUI component handle its own events ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 19
8.14 What advantage do Java’s layout managers provide over traditional windowing systems ? . . . . . . . . . . . . . 19
8.15 What is the design pattern that Java uses for all Swing components ? . . . . . . . . . . . . . . . . . . . . . . . . 19
9 JDBC 20
9.1 What is JDBC ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 20
9.2 Explain the role of Driver in JDBC. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 20
9.3 What is the purpose Class.forName method ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 20
9.4 What is the advantage of PreparedStatement over Statement ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . 20
9.5 What is the use of CallableStatement ? Name the method, which is used to prepare a CallableStatement. . . . . . 20
9.6 What does Connection pooling mean ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 21
Java Interview Questions v
10 Remote Method Invocation (RMI) 22
10.1 What is RMI ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 22
10.2 What is the basic principle of RMI architecture ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 22
10.3 What are the layers of RMI Architecture ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 22
10.4 What is the role of Remote Interface in RMI ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 22
10.5 What is the role of the java.rmi.Naming Class ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 23
10.6 What is meant by binding in RMI ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 23
10.7 What is the difference between using bind() and rebind() methods of Naming Class ? . . . . . . . . . . . . . . . 23
10.8 What are the steps involved to make work a RMI program ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 23
10.9 What is the role of stub in RMI ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 23
10.10What is DGC ? And how does it work ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 23
10.11What is the purpose of using RMISecurityManager in RMI ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . 24
10.12Explain Marshalling and demarshalling. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 24
10.13Explain Serialization and Deserialization. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 24
11 Servlets 25
11.1 What is a Servlet ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 25
11.2 Explain the architechure of a Servlet. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 25
11.3 What is the difference between an Applet and a Servlet ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 25
11.4 What is the difference between GenericServlet and HttpServlet ? . . . . . . . . . . . . . . . . . . . . . . . . . . 25
11.5 Explain the life cycle of a Servlet. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 25
11.6 What is the difference between doGet() and doPost() ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 26
11.7 What is meant by a Web Application ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 26
11.8 What is a Server Side Include (SSI) ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 26
11.9 What is Servlet Chaining ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 26
11.10How do you ﬁnd out what client machine is making a request to your servlet ? . . . . . . . . . . . . . . . . . . . 26
11.11What is the structure of the HTTP response ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 26
11.12What is a cookie ? What is the difference between session and cookie ? . . . . . . . . . . . . . . . . . . . . . . 27
11.13Which protocol will be used by browser and servlet to communicate ? . . . . . . . . . . . . . . . . . . . . . . . 27
11.14What is HTTP Tunneling ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 27
11.15What’s the difference between sendRedirect and forward methods ? . . . . . . . . . . . . . . . . . . . . . . . . 27
11.16What is URL Encoding and URL Decoding ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 27
12 JSP 28
12.1 What is a JSP Page ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 28
12.2 How are the JSP requests handled ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 28
12.3 What are the advantages of JSP ? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 28
12.4 What are Directives ? What are the d

*(Full detailed section available in attached reference document)*