# Topic 5: Practical SQL Scenarios & Query Tuning

Real-world database administration, query tuning with EXPLAIN, window functions, and advanced interview questions (Questions 86 to 100).

---

### Q86. How to display the Duplicate Records with their Count?



---

### Q87. How to find the Highest Salary without using MAX()?



---

### Q88. How to fetch common records from two tables without JOIN?



---

### Q89. How to delete Duplicate Records from a table?



---

### Q90. How to find the Department with the highest Employee Count?

Real-Time Scenarios

---

### Q91. How to Optimize SQL Queries?



---

### Q92. What is Query Execution Plan?



---

### Q93. How to Improve Query Performance?



---

### Q94. What is Indexing?



---

### Q95. What is Table Partitioning?



---

### Q96. How to Avoid Deadlocks in SQL?



---

### Q97. What is the use of EXISTS in SQL?



---

### Q98. What is Query Optimization?



---

### Q99. What is the Difference Between Stored Procedure and Function in

SQL?

---

### Q100. What is the difference between OLTP and OLAP in SQL?

Optimization Techniques
Basic Level (Beginner)

---

### Q86. How to display the Duplicate Records with their Count?

To display duplicate records along with their occurrence count, use the GROUP BY clause
with the HAVING clause.
Syntax:
Conclusion:
Use ORDER BY with LIMIT for simple queries.
Use Subqueries or ALL for advanced scenarios.
This method works in all RDBMS.

---

### Q87. How to find the Highest Salary without using MAX()?

You can find the Highest Salary without using the MAX() function by using the ORDER
BY clause with the LIMIT or TOP keyword.
Method 1: Using ORDER BY with LIMIT (MySQL)
Method 2: Using Subquery (MySQL, SQL Server)
Method 3: Using NOT IN (MySQL, SQL Server)

---

### Q88. How to fetch common records from two tables without

JOIN?
You can fetch common records from two tables without using JOIN by using the IN or
INTERSECT operators.
Method 1: Using IN (MySQL, SQL Server)
Method 2: Using INTERSECT (SQL Server, PostgreSQL)

---

### Q89. How to delete Duplicate Records from a table?

You can delete duplicate records using CTE, ROW_NUMBER(), or GROUP BY methods.
Method 1: Using ROW_NUMBER() (SQL Server, PostgreSQL)
Method 2: Using GROUP BY with MIN() (MySQL)
Method 3: Using DISTINCT INTO Temporary Table (MySQL)
Conclusion:
Use ROW_NUMBER() for advanced databases.
Use GROUP BY for simple queries.
Always backup data before deleting duplicates.

---

### Q90. How to find the Department with the highest Employee

Count?
You can find the Department with the Highest Employee Count using the GROUP BY
clause along with the ORDER BY and LIMIT clauses.
Method 1: Using GROUP BY with ORDER BY (MySQL)
Method 2: Using Subquery (MySQL, SQL Server)
Conclusion:
Use GROUP BY with ORDER BY for a simple approach.
Use Subqueries for better performance with large datasets.

---

### Q91. How to Optimize SQL Queries?

Optimizing SQL queries improves performance and execution speed while handling large
datasets.
Best Practices to Optimize SQL Queries:
Optimization Techniques
Use Indexes: Create indexes on columns used in WHERE, JOIN, and
ORDER BY clauses.

---

### Q92. What is Query Execution Plan?

A Query Execution Plan is a detailed roadmap used by the database engine to execute
SQL queries efficiently.
Key Points:
Shows how the database will retrieve data.
Helps to analyze performance and optimize queries.
Displays the order of execution for each query operation like joins, scans, and
sorting.
Provides information about indexes, table scans, and filtering methods.
How to View Execution Plan:
Why Use Execution Plan?
Identify performance bottlenecks.
Check if the query is using Indexes or Table Scans.
Understand how Joins and Filters are applied.
MySQL:
SQL Server:

---

### Q93. How to Improve Query Performance?

Improving Query Performance ensures faster data retrieval and better database efficiency,
especially for large datasets.
Best Practices to Improve Query Performance:
Use Indexes: Create indexes on columns used in WHERE, JOIN, and
ORDER BY clauses.

---

### Q94. What is Indexing?

Indexing in SQL is a technique used to improve the speed of data retrieval from a
database by creating a lookup table for faster access.
Key Points:
Works like a book index to find data quickly.
Reduces the time required for SELECT queries.
Automatically updated when data is inserted, updated, or deleted.
Indexes are created on columns frequently used in WHERE, JOIN, and
ORDER BY clauses.
Types of Indexes:
Primary Index – Automatically created on Primary Key columns.1.
Unique Index – Ensures that values in a column are unique.2.
Clustered Index – Sorts data rows physically based on key values (Only
one per table).

---

### Q95. What is Table Partitioning?

Table Partitioning is a technique used to divide large tables into smaller, more
manageable pieces without changing the table structure.
Key Points:
Improves query performance on large datasets.
Simplifies data management.
Helps in faster data retrieval.
Each partition is stored separately.
Data can be partitioned by range, list, hash, or composite methods.
Types of Partitioning:
Range Partitioning – Divides data based on value ranges.1.
List Partitioning – Divides data based on specific column values.2.
Hash Partitioning – Distributes data evenly using a hash function.3.
Composite Partitioning – Combination of Range and Hash partitioning.4.
Conclusion: Table Partitioning improves performance and data
management by splitting large datasets into smaller, more manageable
sections.
Syntax (MySQL Range Partitioning):
Example Query:

---

### Q96. How to Avoid Deadlocks in SQL?

A Deadlock occurs when two or more transactions block each other by
holding locks on resources that the other transactions need.
Ways to Avoid Deadlocks:
Access Tables in the Same Order:1.
Always access tables in a consistent sequence across transactions.
Minimize Lock Time:2.
Keep transactions short and fast to reduce lock holding time.
Use Lower Isolation Levels:3.
Use READ COMMITTED instead of SERIALIZABLE isolation
level if possible.
Avoid User Interaction Inside Transactions:4.
Do not wait for user input during a transaction.
Use NOLOCK or Read Uncommitted:5.
Allow non-blocking reads for read-only operations.
Conclusion:
Follow consistent table access patterns, minimize lock times, and use
proper indexing to avoid deadlocks in SQL transactions.

---

### Q97. What is the use of EXISTS in SQL?

EXISTS in SQL is used to check whether a subquery returns any rows. It returns:
TRUE if the subquery returns at least one row.
FALSE if the subquery returns no rows.
Syntax:
Conclusion: EXISTS is faster than IN for large datasets and is commonly
used in subqueries to check data existence.
Stored Procedure Function
Can return multiple values. Returns only one value (scalar or table).
Can perform DML operations like
INSERT, UPDATE, DELETE. Cannot perform DML operations.
Supports input and output parameters. Only supports input parameters.
Can call Functions inside it. Cannot call Stored Procedures inside it.
Used for business logic and complex
operations. Used for calculations and returning values.
Query Optimization is the process of improving the efficiency and performance of
SQL queries to retrieve data faster while using minimal system resources.
Key Points:
Helps in reducing query execution time.
Improves database performance.
Minimizes CPU usage, memory usage, and I/O operations.
Automatically performed by the Query Optimizer in most RDBMS.
Techniques for Query Optimization:
Use Indexes to speed up searches.1.
Avoid SELECT and select only necessary columns.2.
Use JOINs instead of subqueries when possible.3.
Use EXISTS instead of IN for large datasets.4.
Avoid Functions in WHERE clause.5.
Use LIMIT or TOP to fetch only required rows.6.
Analyze the Execution Plan using tools like EXPLAIN.7.
Conclusion:
Query Optimization improves execution speed, reduces resource usage, and
enhances overall database performance.

---

### Q98. What is Query Optimization?



---

### Q99. What is the Difference Between Stored Procedure and

Function in SQL?
OLTP (Online Transaction
Processing)
OLAP (Online Analytical
Processing)
Used for day-to-day transactional
operations. Used for data analysis and reporting.
Focuses on data consistency and speed. Focuses on data aggregation and analysis.
Stores detailed transactional data. Stores historical and summarized data.
Supports INSERT, UPDATE, DELETE
operations.
Supports SELECT operations with complex
queries.
Small amounts of data processed per
transaction. Large amounts of data processed at once.
Example: Banking systems, E-commerce
websites.
Example: Data Warehouses, Business
Intelligence Tools.

---

### Q100. What is Query Optimization?



---

