# Topic 2: Advanced Queries, Joins, Subqueries & CTEs

SQL joins (INNER, LEFT, RIGHT, FULL, CROSS), nested subqueries, aggregations, and Common Table Expressions (Questions 26 to 50).

---

### Q26. What is Subquery?



---

### Q27. What is Nested Query?



---

### Q28. What is Correlated Subquery?



---

### Q29. What is Group By in SQL?



---

### Q30. What is the difference between Group By and Order By?



---

### Q31. What is the use of LIMIT in SQL?



---

### Q32. How to find the Second Highest Salary in SQL?



---

### Q33. How to find Duplicate Records in a table?



---

### Q34. What is CTE (Common Table Expression)?



---

### Q35. What is Temporary Table in SQL?



---

### Q36. What is Window Function in SQL?



---

### Q37. What is the difference between ROW_NUMBER(), RANK(), and

DENSE_RANK()?

---

### Q38. What is CASE Statement in SQL?



---

### Q39. What is COALESCE in SQL?



---

### Q40. What is NVL Function in SQL?



---

### Q41. What is Indexing in SQL?



---

### Q42. What is Clustered Index?



---

### Q43. What is Non-Clustered Index?



---

### Q44. What is the difference between Clustered and Non-Clustered Index?



---

### Q45. What is View in SQL?



---

### Q46. What is the difference between View and Table?



---

### Q47. What is Stored Procedure?



---

### Q48. What is the difference between Function and Stored Procedure?



---

### Q49. What is Trigger in SQL?



---

### Q50. What is Cursor in SQL?

Intermediate Level

---

### Q26. What is Subquery?

A Subquery is a query nested inside another query in SQL.
It is used to fetch data that will be used by the main query as a condition to filter or
manipulate the result.
Key Points:
Also called Inner Query or Nested Query.
Always executes before the main query.
The result of the subquery is used by the Outer Query.
Can be used with SELECT, INSERT, UPDATE, or DELETE statements.
Syntax:
Types of Subqueries:
Explanation: It selects the employee with the highest salary.

---

### Q27. What is Nested Query?

A Nested Query is a query written inside another query to retrieve data based on the result of
the inner query.
It helps break down complex queries into smaller, more manageable parts.
Key Points:
Also known as Inner Query or Subquery.
The Inner Query executes first, and its result is passed to the Outer Query.
Used in SELECT, INSERT, UPDATE, or DELETE statements.
Can be used with WHERE, HAVING, and FROM clauses.
Syntax:
Example:
Find employees who work in the 'Sales' department.
Explanation:
The inner query finds the department_id for the 'Sales' department.
The outer query selects employees based on that department_id.

---

### Q28. What is Correlated Subquery?

A Correlated Subquery is a subquery that depends on the values from the outer query to
execute.
It is executed repeatedly for each row of the outer query, making it slower compared to
regular subqueries.
Key Points:
The Inner Query uses columns from the Outer Query.
Executes row by row for each result of the outer query.
Cannot be executed independently without the outer query.
Used for row-by-row comparisons.
Syntax:

---

### Q29. What is GROUP BY in SQL?

The GROUP BY clause in SQL is used to group rows that have the same values into summary
rows. It is typically used with aggregate functions to perform calculations on each group of data.
Key Points:
Groups data based on one or more columns.
It is used to summarize data.
Always used after the WHERE clause and before the ORDER BY clause.
Commonly used with aggregate functions like:
COUNT() – Counts the number of rows.
SUM() – Calculates the total sum.
AVG() – Calculates the average value.
MAX() – Returns the maximum value.
MIN() – Returns the minimum value.
Example:
Find employees whose salary is higher than the average salary of their department.
Explanation:
The inner query calculates the average salary of each department.
The outer query checks if the employee's salary is higher than their department's average
salary.
Syntax:
Example:
Find the total sales amount for each product category.
Conclusion:
The GROUP BY clause is essential for data summarization and helps in analyzing data patterns
efficiently.
GROUP BY ORDER BY
Used to group rows based on the same values
in one or more columns.
Used to sort the result set in ascending or
descending order.
Always works with aggregate functions like
COUNT(), SUM(), AVG(), etc. Does not require aggregate functions.
Syntax: Comes before ORDER BY. Syntax: Always comes after GROUP BY.
Groups the result into summary rows. Sorts the entire result set.
Example: Group sales by product category. Example: Sort sales by highest to lowest
amount.

---

### Q30. What is the Difference Between GROUP BY and ORDER

BY in SQL?
Both GROUP BY and ORDER BY are SQL clauses used to organize query results, but they
serve different purposes.
Example with GROUP BY:
Find the total sales for each product category.
Example with ORDER BY:
Sort products by price in descending order.
Conclusion:
GROUP BY is used to group data and perform aggregate calculations.
ORDER BY is used to sort the final result.

---

### Q31. What is the Use of LIMIT in SQL?

LIMIT is used to restrict the number of rows returned by a query.
Syntax:
Example:
Fetch top 3 highest salaries:
Conclusion:
LIMIT helps to fetch limited data and is commonly used for Top N records or
Pagination.

---

### Q32. How to Find the Second Highest Salary in SQL?

Using Subquery:
Explanation:
The inner query gets the highest salary.
The outer query finds the highest salary below the top salary.

---

### Q33. How to find Duplicate Records in a table?

You can find Duplicate Records using the GROUP BY clause with the HAVING keyword.
Syntax:
Explanation:
GROUP BY groups the same names.
HAVING COUNT(*) > 1 filters only duplicate names.

---

### Q34. What is CTE (Common Table Expression)?

CTE (Common Table Expression) is a temporary result set that is defined within the
execution of a single SQL statement.
Syntax:
Example:
Find employees who work in the IT department and have a salary greater than 50000.
Explanation:
The CTE IT_Employees selects all employees from the IT department.
The SELECT query fetches employees with salary above 50000.

---

### Q35. What is Temporary Table in SQL?

A Temporary Table in SQL is used to store temporary data during the session.
Key Points:
Automatically deleted when the session ends.
Used to store intermediate results.
Prefixed with # in SQL Server or TEMP in MySQL.
Syntax:
MySQL:
Example:
Create a temporary table to store employees with salary above 50000.
Explanation:
The table holds filtered data temporarily.
It is automatically deleted after the session ends.
SQL Server:

---

### Q36. What is Window Function in SQL?

A Window Function performs calculations across a set of table rows related to the current
row without collapsing the result into a single value.
Key Points:
Works with OVER() clause.
Does not group rows like aggregate functions.
Commonly used for ranking, running totals, and moving averages.
Syntax:
Types of Window Functions:

---

### Q37. What is the Difference Between ROW_NUMBER(),

RANK(), and DENSE_RANK()?
These three Window Functions are used to assign numbers to rows based on their order.
Key Differences:

---

### Q38. What is CASE Statement in SQL?

The CASE Statement is used to apply conditional logic in SQL queries, similar to IF-ELSE
statements.
Syntax:
Example:

---

### Q39. What is COALESCE in SQL?

COALESCE returns the first non-null value from a list of expressions.
Syntax:
Example:
Key Points:
Returns the first non-null value.
Used to handle NULL values.
Can accept multiple expressions.

---

### Q40. What is NVL Function in SQL?

NVL function replaces NULL values with a specified value.
Syntax:
Example:

---

### Q41. What is Indexing in SQL?

Indexing improves the speed of data retrieval from a table by creating a lookup structure.
Syntax:
Example:
Key Points:
Speeds up SELECT queries.
Automatically maintained by the database.
Can be created on one or more columns.
Slows down INSERT, UPDATE, DELETE operations.
Clustered Index Non-Clustered Index
Stores data physically sorted. Stores pointers to data.
Only one per table. Multiple indexes allowed.
Faster for data retrieval. Slower than Clustered Index.
Automatically created on Primary Key. Manually created on any column.
Affects physical order of table. Does not affect physical order.

---

### Q42. What is Clustered Index in SQL?

A Clustered Index sorts and stores the data physically in the table based on the indexed
column.
Syntax:
Example:
Key Points:
Only one clustered index is allowed per table.
Faster for data retrieval.
Automatically created on Primary Key by default.
Rearranges table rows physically.

---

### Q43. What is Non-Clustered Index in SQL?

A Non-Clustered Index creates a separate structure from the table data, storing pointers to
the actual rows.
Syntax:
Example:
Key Points:
Multiple Non-Clustered Indexes can be created on a table.
Improves search performance.
Does not affect the physical order of data.
Stores pointers to the actual data.

---

### Q44. Difference between Clustered and Non-Clustered Index

View Table
Virtual table. Physical table.
Does not store data physically. Stores data physically.
Based on SQL queries. Contains raw data.
Provides data security by restricting
access to certain columns. No data restriction unless applied.
Automatically updates when base table
data changes. Needs manual updates.

---

### Q45. What is View in SQL?

A View is a virtual table based on the result of a SQL query.
Syntax:
Example:
Key Points:
Does not store data physically.
Simplifies complex queries.
Provides data security.
Can be used like a table in SELECT queries.

---

### Q46. Difference between View and Table

Function Stored Procedure
Returns a single value or table. May or may not return a value.
Can be used in SELECT statements. Cannot be used in SELECT statements.
Allows only input parameters. Allows input and output parameters.
Cannot modify database state. Can modify database state (INSERT,
UPDATE, DELETE).
Always returns a value. Does not always return a value.
Key Points:
Improves code reusability.
Increases performance.
Supports input and output parameters.
Provides security by hiding SQL code.

---

### Q47. What is Stored Procedure ?

A Stored Procedure is a group of predefined SQL statements stored in the database that can
be executed multiple times.
Syntax:
Example:

---

### Q48. What is the difference between Function and Stored

Procedure?
Key Points:
Automatically executes on INSERT, UPDATE, or DELETE.
Used for data validation and logging.
Cannot be called manually.
Improves data integrity.

---

### Q49. What is Trigger in SQL?

A Trigger is an automatic action executed when a specified event occurs in a table.
Syntax:
Example:

---

### Q50. What is Cursor in SQL?

A Cursor is a database object used to retrieve, manipulate, and navigate row-by-row through
the result set.
Syntax:
Key Points:
Used to process row-by-row results.
Slower than set-based operations.
Helps in complex data manipulation.
Not recommended for large datasets.
Example:
Property Description
A - Atomicity Transaction should be all or nothing. If one part of the transaction
fails, the entire transaction fails, and the database remains unchanged.
C - Consistency The database must be in a consistent state before and after the
transaction. It ensures that data remains correct and valid.
I - Isolation Transactions should be executed independently, without interfering
with each other.
D - Durability Once a transaction is committed, the changes must be permanent in the
database, even if the system crashes.
Advanced Level

---

