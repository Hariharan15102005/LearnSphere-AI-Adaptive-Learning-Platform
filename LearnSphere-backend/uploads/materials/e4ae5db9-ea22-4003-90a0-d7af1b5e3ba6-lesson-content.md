# Topic 3: Transactions, ACID Properties, Indexing & Optimization

Database transactions, isolation levels, ACID compliance, B-Trees, Clustered/Non-Clustered indexing, and query performance (Questions 51 to 70).

---

### Q51. What is the ACID Property in SQL?



---

### Q52. What is a Transaction in SQL?



---

### Q53. What is the difference between COMMIT and ROLLBACK?



---

### Q54. What is Savepoint in SQL?



---

### Q55. What is the difference between IN and EXISTS?



---

### Q56. What is the difference between DELETE and TRUNCATE?



---

### Q57. What is Index Fragmentation?



---

### Q58. What is the difference between RANK() and DENSE_RANK()?



---

### Q59. How to fetch common records from two tables?



---

### Q60. What is the difference between UNION and JOIN?



---

### Q61. What is Pivot Table in SQL?



---

### Q62. What is Case Sensitivity in SQL?



---

### Q63. How to find the Nth Highest Salary?



---

### Q64. How to get First 3 Maximum Salaries?



---

### Q65. What is the difference between Drop, Delete, and Truncate?



---

### Q66. How to calculate Age from Date of Birth in SQL?



---

### Q67. What is Recursive Query in SQL?



---

### Q68. What is the difference between Temporary Table and CTE?



---

### Q69. How to find Odd and Even records in SQL?



---

### Q70. What is JSON in SQL?



---

### Q51. What is the ACID Property in SQL?

The ACID properties in SQL define the key principles to ensure that database transactions
are processed reliably without affecting data integrity.
ACID Stands for:
Example:
Explanation:
Atomicity: If one of the two queries fails, both updates will be rolled back.1.
Consistency: The total amount in both accounts will remain the same.2.
Isolation: If another transaction is trying to access the same account, it will
wait until this transaction completes.

---

### Q52. What is a Transaction in SQL?

A Transaction in SQL is a group of SQL operations that are executed as a single unit to
perform a specific task on the database.
It follows ACID Properties to maintain data integrity.
Key ACID Properties:
Transaction Commands:
Example:
COMMIT ROLLBACK
Saves the changes made by the transaction
permanently into the database. Undo all changes made by the transaction.
Once executed, changes cannot be undone. Restores the database to its previous state.
Syntax: COMMIT; Syntax: ROLLBACK;
Used when all operations are successful. Used when any error occurs during the
transaction.
Improves data durability. Helps to maintain data consistency.
Transactions in SQL

---

### Q53. What is the difference between COMMIT and ROLLBACK?

Example:
IN EXISTS
Compares values from the main query with a
list of values. Checks if subquery returns any rows.
Works with static values or subqueries. Only works with subqueries.
Slower with large datasets. Faster for large datasets.
Returns all matching rows. Stops checking after finding the first
match.
Key Points:
Allows setting multiple points in a transaction.
Helps in partial rollback.
Improves error handling.
Used with ROLLBACK.

---

### Q54. What is Savepoint in SQL?

Savepoint in SQL is used to temporarily save a transaction at a specific point, allowing you
to rollback only part of the transaction without affecting the entire transaction.
Syntax:
Explanation:
The first update will be saved.
The second update will be rolled back.
Remaining changes will be committed.

---

### Q55. What is the difference between IN and EXISTS?

DELETE TRUNCATE
Removes specific rows based on a
condition using the WHERE clause.
Removes all rows from the table without
any condition.
Can be rolled back using ROLLBACK if
inside a transaction. Cannot be rolled back once executed.
Slower because it logs each row deletion. Faster because it does not log individual
row deletions.
Maintains table structure and identity
column values.
Resets identity column values to the initial
seed.
Index Fragmentation occurs when the logical order of index pages in the database does not
match the physical order of data on disk, making data retrieval slower.

---

### Q56. What is the difference between DELETE and TRUNCATE?

Conclusion:
Index fragmentation slows down query performance and should be fixed regularly to
maintain database efficiency.

---

### Q57. What is Index Fragmentation?

Types of Index Fragmentation:
Internal Fragmentation – Unused space inside index pages due to data
deletion or updates.

---

### Q58. What is the difference between RANK() and

DENSE_RANK()?

---

### Q59. How to fetch common records from two tables?

To fetch common records from two tables, you can use the INNER JOIN clause in SQL.
Syntax:

---

### Q60. What is the difference between UNION and JOIN?

Conclusion:
Pivot Tables help to summarize large datasets and present them in a structured format.

---

### Q61. What is Pivot Table in SQL?

A Pivot Table in SQL is used to transform row data into column data to provide a summary
report of the dataset.
It is commonly used to perform data aggregation and present data in a more readable
format.
Key Points:
Converts rows into columns.
Used to generate summary reports.
Helps in data analysis.
Commonly used with aggregate functions like SUM(), AVG(), COUNT(), etc.
Example: Pivot Table Query
Key Points:
Column Names: Most databases are not case-sensitive (MySQL, SQL Server).
Table Names: Case sensitivity depends on the database and operating system.
String Values: By default, MySQL is case-insensitive for string comparisons.

---

### Q62. What is Case Sensitivity in SQL?

Case Sensitivity in SQL refers to whether the database treats uppercase and lowercase letters
as different or same when performing queries.
Example in MySQL:
EmpID Name
101 Vinay
102 VINAY
Output:
How to Make MySQL Case-Sensitive:
This query will only return exact case-sensitive matches.
Conclusion:
Case Insensitivity is default in MySQL for string comparisons.
Use the BINARY keyword to perform case-sensitive searches.
Explanation:
ORDER BY Salary DESC  Sorts the salaries in descending order.
OFFSET 2  Skips the top 2 salaries.
LIMIT 1  Selects the next salary as the 3rd highest.

---

### Q63. How to find the Nth Highest Salary?

To find the Nth Highest Salary in SQL, you can use the LIMIT with OFFSET method or
Subquery with ORDER BY.
Method 1: Using LIMIT with OFFSET (MySQL)
Example: Example (3rd Highest Salary):
Method 2: Using Subquery with LIMIT
Conclusion:
Use LIMIT with OFFSET for faster results in MySQL.
This method is commonly asked in interviews.
Always use DISTINCT to remove duplicate salaries.
Explanation:
DISTINCT  Removes duplicate salaries.
ORDER BY Salary DESC  Sorts salaries in descending order.
LIMIT 3  Fetches the top 3 salaries.

---

### Q64. How to get First 3 Maximum Salaries?

To fetch the First 3 Maximum Salaries from a table, you can use the DISTINCT, ORDER
BY, and LIMIT clauses.
Method : Using LIMIT (MySQL)
Comman
d Purpose Can
Rollback Speed Structure
Affected
Condition
-Based
DROP
Deletes the
entire table with
its structure
No Fastest
Yes (Removes
table
structure)
No
DELETE
Removes
specific rows
based on
condition
Yes (if
within
Transaction
)
Slow No
Yes (With
WHERE
Clause)
TRUNCAT
E
Removes all
rows from the
table
No Fast No (Keeps
structure) No
Conclusion:
Use DELETE for removing specific rows with conditions.
Use TRUNCATE for removing all rows quickly without rollback.
Use DROP to delete both data and table structure completely.

---

### Q65. What is the difference between Drop, Delete, and

Truncate?

---

### Q66. How to calculate Age from Date of Birth in SQL?

You can calculate the Age from the Date of Birth using the DATEDIFF() or YEAR()
functions depending on the database.
Method 1: Using DATEDIFF() (MySQL)
Explanation:
CURDATE()  Returns the current date.
DATEDIFF()  Calculates the difference between the current date and date
of birth in days.
FLOOR()  Converts the result into whole years.
EmpID Name ManagerID
101 Vinay NULL
102 Awadhesh 101
103 Nevendra 102
Conclusion:
Use DATEDIFF() for accurate age calculation.
Use YEAR() for simple year-based age calculation.
Method 2: Using YEAR() (MySQL)
Explanation:
This method calculates the difference between the current year and the birth year.

---

### Q67. What is Recursive Query in SQL?

A Recursive Query in SQL is a query that refers to itself to perform repetitive operations
until a specific condition is met. It is commonly used to process hierarchical data such as
employee-manager relationships or organizational structures.
Key Points:
Used to handle hierarchical data.
Implemented using Common Table Expressions (CTE).
The recursion continues until the termination condition is satisfied.
Example:
Employee Table:
Recursive Query:
Temporary Table CTE (Common Table Expression)
Stores data physically in temporary
memory. Stores data logically without physical storage.
Needs to be explicitly created and dropped. Automatically disappears after query
execution.
Can be used multiple times within a session. Can be used only once in the same query.
Supports Indexing and DDL operations. Does not support Indexing or DDL
operations.
Slower for small datasets. Faster for small datasets.
Conclusion:
Use Temporary Tables when data needs to be reused multiple times.
Use CTE for short-term data manipulation and improved readability.

---

### Q68. What is the difference between Temporary Table and CTE?



---

### Q69. How to find Odd and Even records in SQL?

You can find Odd and Even records in SQL using the MOD() or
ROW_NUMBER() functions.
Method 1: Using MOD() Function (MySQL)
Explanation:
MOD(EmpID, 2)  Returns the remainder when EmpID is divided by 2.
If the remainder is 0, the record is Even.
If the remainder is 1, the record is Odd.
Method 2: Using ROW_NUMBER() (SQL Server, PostgreSQL)
Insert JSON Data:

---

### Q70. What is JSON in SQL?

JSON (JavaScript Object Notation) in SQL is used to store, retrieve, and manipulate data in
a structured, text-based format within relational databases.
Key Points:
Stores data in key-value pairs.
Lightweight and easy to read.
Commonly used for semi-structured data.
Supported in MySQL, SQL Server, and PostgreSQL.
MySQL Example:
Create Table with JSON Column:
Conclusion:
Use MOD() for databases like MySQL.
Use ROW_NUMBER() for databases that support Window Functions.
Retrieve JSON Data:
Conclusion:
JSON helps to handle semi-structured data within relational databases without the need for
separate NoSQL databases.

---

