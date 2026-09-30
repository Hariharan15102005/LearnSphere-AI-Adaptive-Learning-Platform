# LearnSphere Platform — Complete Database Design & Data Architecture

## 1. Database Overview & Technology Configuration

LearnSphere utilizes **Spring Data JPA (Hibernate)** with an **H2 Relational Database Engine** running in persistent local file mode (`./data/learnspheredb`) with `AUTO_SERVER=TRUE` enabled. The architecture is fully compatible with enterprise RDBMS engines (e.g., MySQL 8.0, PostgreSQL) via standard dialect switching.

### Database Configuration (`application.properties`):
```properties
spring.datasource.url=jdbc:h2:file:./data/learnspheredb;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 2. Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USERS ||--o| STUDENTS : "is-a (userId)"
    USERS ||--o| INSTRUCTORS : "is-a (userId)"
    USERS ||--o{ CERTIFICATES : "receives"
    USERS ||--o{ USER_ACTIVITIES : "logs"
    
    INSTRUCTORS ||--o{ COURSES : "creates (instructorId)"
    
    COURSES ||--o{ TOPICS : "contains (courseId)"
    COURSES ||--o{ ENROLLMENTS : "has (courseId)"
    COURSES ||--o{ QUIZZES : "has course-level (courseId)"
    COURSES ||--o{ CERTIFICATES : "issues (courseId)"
    
    TOPICS ||--o{ MATERIALS : "contains (topicId)"
    TOPICS ||--o{ QUIZZES : "contains topic-level (topicId)"
    
    MATERIALS ||--o{ MATERIAL_ATTACHMENTS : "has files (materialId)"
    
    STUDENTS ||--o{ ENROLLMENTS : "enrolls (studentId)"
    STUDENTS ||--o{ COURSE_PROGRESS : "tracks overall (studentId)"
    STUDENTS ||--o{ TOPIC_PROGRESS : "tracks module (studentId)"
    STUDENTS ||--o{ TOPIC_MATERIAL_PROGRESS : "tracks material (studentId)"
    STUDENTS ||--o{ TOPIC_QUIZ_PROGRESS : "tracks quiz (studentId)"
    STUDENTS ||--o{ QUIZ_ATTEMPTS : "takes (studentId)"
    
    QUIZZES ||--o{ QUESTIONS : "contains (quizId)"
    QUIZZES ||--o{ QUIZ_ATTEMPTS : "recorded in (quizId)"
    
    QUESTIONS ||--o{ ANSWERS : "has options (questionId)"
    QUESTIONS ||--o{ ATTEMPT_ANSWERS : "evaluated in (questionId)"
    
    QUIZ_ATTEMPTS ||--o{ ATTEMPT_ANSWERS : "contains (attemptId)"

    USERS {
        bigint id PK
        varchar name
        varchar email UK
        varchar password
        varchar role
        varchar avatar
        varchar bio
        varchar skills
        boolean is_active
        boolean verified
        timestamp created_at
        timestamp updated_at
    }

    STUDENTS {
        bigint id PK
        bigint user_id FK
        integer total_points
        varchar enrollment_number UK
        varchar grade
        timestamp created_at
    }

    INSTRUCTORS {
        bigint id PK
        bigint user_id FK
        varchar specialization
        varchar qualification
        integer total_students
    }

    COURSES {
        bigint id PK
        varchar title
        varchar slug UK
        text description
        varchar category
        varchar difficulty_level
        varchar thumbnail
        decimal price
        boolean is_published
        bigint instructor_id FK
        integer total_enrollments
        timestamp created_at
    }

    TOPICS {
        bigint id PK
        varchar name
        text description
        integer order_index
        varchar level
        bigint course_id FK
    }

    MATERIALS {
        bigint id PK
        varchar title
        text description
        varchar material_type
        text text_content
        varchar file_path
        varchar external_url
        integer duration_minutes
        integer order_index
        bigint topic_id FK
    }

    QUIZZES {
        bigint id PK
        varchar title
        text description
        integer time_limit
        integer passing_score
        integer total_marks
        integer reward_first_try
        integer reward_second_try
        integer reward_third_try
        integer reward_fourth_plus
        bigint course_id FK
        bigint topic_id FK
    }

    QUESTIONS {
        bigint id PK
        text question_text
        varchar question_type
        varchar difficulty
        integer points
        text explanation
        varchar correct_answer
        bigint quiz_id FK
    }

    QUIZ_ATTEMPTS {
        bigint id PK
        bigint quiz_id FK
        bigint student_id
        double score
        integer time_spent
        varchar status
        timestamp attempt_time
    }

    ATTEMPT_ANSWERS {
        bigint id PK
        bigint attempt_id FK
        bigint question_id FK
        text student_answer
        boolean is_correct
        integer points_earned
    }

    ENROLLMENTS {
        bigint id PK
        bigint student_id FK
        bigint course_id FK
        integer completion_percentage
        boolean is_completed
        timestamp enrolled_at
        timestamp completed_at
    }

    CERTIFICATES {
        bigint id PK
        varchar uid UK
        bigint student_id FK
        bigint course_id FK
        varchar student_name_snapshot
        varchar course_name_snapshot
        timestamp issued_at
    }

    COURSE_PROGRESS {
        bigint id PK
        bigint student_id
        bigint course_id
        double progress_percent
        bigint last_topic_id
        double total_time_minutes
        varchar current_recommendation_type
        text recommendation_reason
        timestamp last_updated
    }
```

---

## 3. Entity Dictionary & Schema Details

### 3.1 Identity & User Tables
1. **`users` (`User.java`)**:
   - `id` (BIGINT, PK, Auto-increment)
   - `name` (VARCHAR(100), NOT NULL)
   - `email` (VARCHAR(150), UNIQUE, NOT NULL)
   - `password` (VARCHAR(255), NOT NULL, BCrypt hashed)
   - `role` (VARCHAR(20), Enum: `STUDENT`, `INSTRUCTOR`, `ADMIN`)
   - `avatar`, `bio`, `phone`, `skills` (TEXT / VARCHAR)
   - `is_active` (BOOLEAN, default TRUE)
   - `verified` (BOOLEAN, default FALSE)
   - `created_at`, `updated_at` (TIMESTAMP)

2. **`students` (`Student.java`)**:
   - `id` (BIGINT, PK)
   - `user_id` (BIGINT, FK -> `users.id`, UNIQUE)
   - `total_points` (INTEGER, default 0) — Gamification points earned from quizzes
   - `enrollment_number` (VARCHAR, UNIQUE)
   - `grade` (VARCHAR)

3. **`instructors` (`Instructor.java`)**:
   - `id` (BIGINT, PK)
   - `user_id` (BIGINT, FK -> `users.id`, UNIQUE)
   - `specialization`, `qualification` (VARCHAR)
   - `total_students` (INTEGER, default 0)

---

### 3.2 Curriculum & Multi-Modal Content Tables
4. **`courses` (`Course.java`)**:
   - `id` (BIGINT, PK)
   - `title` (VARCHAR(255), NOT NULL)
   - `slug` (VARCHAR(255), UNIQUE)
   - `description` (TEXT)
   - `category` (VARCHAR(100))
   - `difficulty_level` (VARCHAR(20), Enum: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`)
   - `instructor_id` (BIGINT, FK -> `instructors.id`)
   - `thumbnail` (VARCHAR(255))
   - `price` (DECIMAL(10,2))
   - `is_published` (BOOLEAN, default FALSE)
   - `total_enrollments` (INTEGER, default 0)

5. **`topics` (`Topic.java`)**:
   - `id` (BIGINT, PK)
   - `name` (VARCHAR(255), NOT NULL)
   - `description` (TEXT)
   - `order_index` (INTEGER)
   - `level` (VARCHAR(20), Enum: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`)
   - `course_id` (BIGINT, FK -> `courses.id`)

6. **`materials` (`Material.java`)**:
   - `id` (BIGINT, PK)
   - `title` (VARCHAR(255), NOT NULL)
   - `description` (TEXT)
   - `material_type` (VARCHAR(20), Enum: `TEXT`, `PDF`, `VIDEO`, `IMAGE`, `LINK`)
   - `text_content` (LONGTEXT / TEXT)
   - `file_path` (VARCHAR(500))
   - `external_url` (VARCHAR(500))
   - `duration_minutes` (INTEGER)
   - `order_index` (INTEGER)
   - `topic_id` (BIGINT, FK -> `topics.id`)

---

### 3.3 Assessment & Evaluation Tables
7. **`quizzes` (`Quiz.java`)**:
   - `id` (BIGINT, PK)
   - `title` (VARCHAR(255), NOT NULL)
   - `description` (TEXT)
   - `time_limit` (INTEGER, minutes)
   - `passing_score` (INTEGER, default 50/60)
   - `total_marks` (INTEGER)
   - `reward_first_try` (INTEGER, default 50 points)
   - `reward_second_try` (INTEGER, default 30 points)
   - `reward_third_try` (INTEGER, default 15 points)
   - `reward_fourth_plus` (INTEGER, default 5 points)
   - `course_id` (BIGINT, FK -> `courses.id`, Nullable)
   - `topic_id` (BIGINT, FK -> `topics.id`, Nullable)

8. **`questions` (`Question.java`)**:
   - `id` (BIGINT, PK)
   - `question_text` (TEXT, NOT NULL)
   - `question_type` (VARCHAR(20), Enum: `MCQ`, `TRUE_FALSE`, `SHORT_ANSWER`, `LONG_ANSWER`)
   - `difficulty` (VARCHAR(20))
   - `points` (INTEGER, default 1)
   - `explanation` (TEXT)
   - `correct_answer` (VARCHAR(500))
   - `quiz_id` (BIGINT, FK -> `quizzes.id`)

9. **`answers` (`Answer.java`)**:
   - `id` (BIGINT, PK)
   - `answer_text` (TEXT)
   - `is_correct` (BOOLEAN)
   - `question_id` (BIGINT, FK -> `questions.id`)

10. **`quiz_attempts` (`QuizAttempt.java`)**:
    - `id` (BIGINT, PK)
    - `quiz_id` (BIGINT, FK -> `quizzes.id`)
    - `student_id` (BIGINT, student reference)
    - `score` (DOUBLE, percentage 0-100)
    - `time_spent` (INTEGER, seconds)
    - `status` (VARCHAR(20), `PASSED` / `FAILED` / `COMPLETED`)
    - `attempt_time` (TIMESTAMP)

11. **`attempt_answers` (`AttemptAnswer.java`)**:
    - `id` (BIGINT, PK)
    - `attempt_id` (BIGINT, FK -> `quiz_attempts.id`)
    - `question_id` (BIGINT, FK -> `questions.id`)
    - `student_answer` (TEXT)
    - `is_correct` (BOOLEAN)
    - `points_earned` (INTEGER)

---

### 3.4 Progress, Certificates & Telemetry Tables
12. **`enrollments` (`Enrollment.java`)**:
    - `id` (BIGINT, PK)
    - `student_id` (BIGINT, FK -> `students.id`)
    - `course_id` (BIGINT, FK -> `courses.id`)
    - `completion_percentage` (INTEGER, 0-100)
    - `is_completed` (BOOLEAN, default FALSE)
    - `enrolled_at` (TIMESTAMP)
    - `completed_at` (TIMESTAMP, Nullable)

13. **`course_progress` (`CourseProgress.java`)**:
    - `id` (BIGINT, PK)
    - `student_id` (BIGINT)
    - `course_id` (BIGINT)
    - `progress_percent` (DOUBLE)
    - `last_topic_id` (BIGINT)
    - `total_time_minutes` (DOUBLE)
    - `current_recommendation_type` (VARCHAR: `NEXT_TOPIC`, `REVISION`, `PRACTICE`)
    - `recommendation_reason` (TEXT)
    - `last_updated` (TIMESTAMP)

14. **`topic_material_progress` (`TopicMaterialProgress.java`)**:
    - `id` (BIGINT, PK)
    - `student_id` (BIGINT)
    - `topic_id` (BIGINT)
    - `material_id` (BIGINT)
    - `completed` (BOOLEAN)
    - `completed_at` (TIMESTAMP)

15. **`topic_quiz_progress` (`TopicQuizProgress.java`)**:
    - `id` (BIGINT, PK)
    - `student_id` (BIGINT)
    - `topic_id` (BIGINT)
    - `completed` (BOOLEAN)
    - `score` (DOUBLE)
    - `completed_at` (TIMESTAMP)

16. **`certificates` (`Certificate.java`)**:
    - `id` (BIGINT, PK)
    - `uid` (VARCHAR(100), UNIQUE, UUID format)
    - `student_id` (BIGINT, FK -> `users.id`)
    - `course_id` (BIGINT, FK -> `courses.id`)
    - `student_name_snapshot` (VARCHAR(150))
    - `course_name_snapshot` (VARCHAR(255))
    - `issued_at` (TIMESTAMP)

17. **`user_activities` (`UserActivity.java`)**:
    - `id` (BIGINT, PK)
    - `user_id` (BIGINT)
    - `date` (DATE)
    - `minutes_spent` (INTEGER)
