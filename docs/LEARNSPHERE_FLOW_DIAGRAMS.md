# LearnSphere Platform — Complete System Flow & Sequence Diagrams

---

## 1. User Registration Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as New User / Student
    participant UI as React Frontend (Register.jsx)
    participant AuthCtrl as AuthController (/api/auth/register)
    participant AuthSvc as AuthService
    participant UserRepo as UserRepository
    participant StudentRepo as StudentRepository
    participant PasswordEncoder as BCryptPasswordEncoder
    participant JwtSvc as JwtService
    participant DB as Relational Database (H2/MySQL)

    User->>UI: Enter Name, Email, Password, Role (STUDENT)
    UI->>UI: Client-side validation (Email regex, Password length)
    UI->>AuthCtrl: POST /api/auth/register (RegisterRequest)
    AuthCtrl->>AuthSvc: register(request)
    AuthSvc->>UserRepo: findByEmail(request.getEmail())
    alt Email already exists
        UserRepo-->>AuthSvc: User entity present
        AuthSvc-->>AuthCtrl: Throw EmailAlreadyExistsException
        AuthCtrl-->>UI: 400 Bad Request ("Email already registered")
        UI-->>User: Display error toast notification
    else Email is unique
        AuthSvc->>PasswordEncoder: encode(request.getPassword())
        PasswordEncoder-->>AuthSvc: BCrypt Hashed Password
        AuthSvc->>UserRepo: save(UserEntity)
        UserRepo->>DB: INSERT INTO users
        DB-->>UserRepo: Persisted User
        alt Role == STUDENT
            AuthSvc->>StudentRepo: save(StudentEntity with User reference)
            StudentRepo->>DB: INSERT INTO students (total_points=0)
            DB-->>StudentRepo: Persisted Student
        end
        AuthSvc->>JwtSvc: generateToken(UserEntity)
        JwtSvc-->>AuthSvc: JWT Access Token (24h)
        AuthSvc->>JwtSvc: generateRefreshToken(UserEntity)
        JwtSvc-->>AuthSvc: JWT Refresh Token (7d)
        AuthSvc-->>AuthCtrl: AuthResponse (Tokens + UserDTO)
        AuthCtrl-->>UI: 200 OK (AuthResponse)
        UI->>UI: Save tokens to localStorage & Redux authSlice
        UI-->>User: Redirect to /dashboard
    end
```

---

## 2. User Login & Token Refresh Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as Registered User
    participant UI as React Frontend (Login.jsx / api.js)
    participant AuthCtrl as AuthController (/api/auth/login, /api/auth/refresh)
    participant AuthMgr as Spring AuthenticationManager
    participant UserDetailsSvc as UserDetailsServiceImpl
    participant JwtSvc as JwtService
    participant DB as Relational Database

    %% Login Sequence
    Note over User, DB: User Login Flow
    User->>UI: Enter Email & Password
    UI->>AuthCtrl: POST /api/auth/login (AuthRequest)
    AuthCtrl->>AuthMgr: authenticate(UsernamePasswordAuthenticationToken)
    AuthMgr->>UserDetailsSvc: loadUserByUsername(email)
    UserDetailsSvc->>DB: SELECT * FROM users WHERE email = ?
    DB-->>UserDetailsSvc: User Record + BCrypt Hash
    AuthMgr->>AuthMgr: Verify password against BCrypt hash
    alt Invalid Credentials
        AuthMgr-->>AuthCtrl: BadCredentialsException
        AuthCtrl-->>UI: 401 Unauthorized ("Invalid credentials")
        UI-->>User: Display error toast notification
    else Credentials Valid
        AuthMgr-->>AuthCtrl: Fully Authenticated Principal
        AuthCtrl->>JwtSvc: generateToken(UserDetails)
        JwtSvc-->>AuthCtrl: Access Token (JWT)
        AuthCtrl->>JwtSvc: generateRefreshToken(UserDetails)
        JwtSvc-->>AuthCtrl: Refresh Token (JWT)
        AuthCtrl-->>UI: 200 OK (AuthResponse)
        UI->>UI: Store tokens in Redux + localStorage
        UI-->>User: Redirect to Role-specific Dashboard
    end

    %% Token Refresh Sequence
    Note over User, DB: Silent Token Refresh Flow on HTTP 401
    UI->>AuthCtrl: GET /api/protected-resource (Expired Access Token)
    AuthCtrl-->>UI: 401 Unauthorized
    UI->>UI: Axios Interceptor pauses requests into failedQueue
    UI->>AuthCtrl: POST /api/auth/refresh (refreshToken)
    AuthCtrl->>JwtSvc: extractUsername(refreshToken)
    AuthCtrl->>JwtSvc: isTokenValid(refreshToken, userDetails)
    alt Refresh Token Valid
        JwtSvc-->>AuthCtrl: Token Valid
        AuthCtrl->>JwtSvc: generateToken(userDetails)
        JwtSvc-->>AuthCtrl: New Access Token
        AuthCtrl-->>UI: 200 OK (New Access Token)
        UI->>UI: Update localStorage & retry failedQueue requests
    else Refresh Token Expired
        AuthCtrl-->>UI: 400 Bad Request ("Invalid Refresh Token")
        UI->>UI: localStorage.clear() & Redirect to /login
    end
```

---

## 3. Course Creation & Multi-Modal Material Ingestion Flow

```mermaid
sequenceDiagram
    autonumber
    actor Instructor as Instructor
    participant UI as Course Creation Studio
    participant CourseCtrl as CourseController & MaterialController
    participant CourseSvc as CourseService & MaterialService
    participant RAGSvc as RagIngestionService (@Async)
    participant DB as H2 Database
    participant Flask as Python Service (/ingest)
    participant Extractor as Text Extractor (PyMuPDF / BeautifulSoup / Gemini)
    participant GeminiEmbed as Google Gemini Embedding
    participant ChromaDB as ChromaDB Vector Store

    Instructor->>UI: Create Course -> Add Topic -> Upload Material (PDF / Video / Link / Text)
    UI->>CourseCtrl: POST /api/materials (Multipart or JSON)
    CourseCtrl->>CourseSvc: createMaterial(request)
    CourseSvc->>DB: INSERT INTO materials (title, type, path, topic_id)
    DB-->>CourseSvc: Persisted Material Entity
    CourseSvc->>RAGSvc: ingestMaterial(materialEntity) (Asynchronous Trigger)
    CourseSvc-->>CourseCtrl: MaterialResponse
    CourseCtrl-->>UI: 201 Created (Material Saved)

    %% Background Ingestion
    Note over RAGSvc, ChromaDB: Asynchronous Background Vector Processing
    RAGSvc->>Flask: POST /ingest (materialId, courseId, topicId, materialType, filePath, text)
    Flask->>Extractor: extract_text_from_material(payload)
    alt MaterialType == PDF
        Extractor->>Extractor: PyMuPDF extracts raw text from PDF pages
    else MaterialType == LINK
        Extractor->>Extractor: BeautifulSoup scrapes semantic tags (p, h1-h3, li)
    else MaterialType == IMAGE / VIDEO
        Extractor->>Extractor: Upload to Gemini File API & generate transcript/summary
    end
    Extractor-->>Flask: Consolidated Text Payload
    Flask->>Flask: chunk_text(text, chunk_size=500, overlap=50)
    Flask->>GeminiEmbed: embed_texts(chunks) using 'models/gemini-embedding-2'
    GeminiEmbed-->>Flask: Vector Embeddings List (768-dim)
    Flask->>ChromaDB: collection.add(ids, documents, embeddings, metadatas)
    ChromaDB-->>Flask: Vectors Persisted
    Flask-->>RAGSvc: 200 OK ({"status": "success", "chunksIndexed": 6})
```

---

## 4. Student Learning Journey & Adaptive Progression

```mermaid
sequenceDiagram
    autonumber
    actor Student as Student Learner
    participant UI as Student Portal / Learning Path
    participant ProgressCtrl as ProgressController & AdaptiveLearningController
    participant AdaptSvc as AdaptiveLearningService
    participant QuizSvc as QuizAttemptService
    participant CompletionSvc as CompletionService
    participant DB as Database

    Student->>UI: Complete Material / Finish Topic
    UI->>ProgressCtrl: POST /api/progress/material/{id}/complete
    ProgressCtrl->>CompletionSvc: checkTopicCompletion(studentId, topicId)
    CompletionSvc->>DB: Update topic_material_progress & course_progress
    
    Student->>UI: Request Next Recommended Step
    UI->>ProgressCtrl: GET /api/adaptive/next-topic?studentId=1&courseId=5
    ProgressCtrl->>AdaptSvc: recommendNextTopic(studentId, courseId)
    AdaptSvc->>DB: Query last quiz score for previous topic
    DB-->>AdaptSvc: maxQuizScore
    alt Score < 30.0%
        AdaptSvc->>AdaptSvc: recType = "REVISION", stay on current topic
    else Score 30.0% - 70.0%
        AdaptSvc->>AdaptSvc: recType = "PRACTICE", suggest practice exercises
    else Score > 70.0% or No Quiz
        AdaptSvc->>AdaptSvc: recType = "NEXT_TOPIC", advance to next topic in order
    end
    AdaptSvc->>DB: Save recommendation in course_progress
    AdaptSvc-->>ProgressCtrl: TopicRecommendationResponse
    ProgressCtrl-->>UI: Render Adaptive Card (Revision / Practice / Next)
```

---

## 5. Quiz Play, Submission, Scoring & Gamification Flow

```mermaid
sequenceDiagram
    autonumber
    actor Student as Student Learner
    participant UI as QuizPlayPage.jsx
    participant QuizCtrl as QuizController (/api/quizzes/{id}/submit)
    participant AttemptSvc as QuizAttemptService
    participant DB as Relational Database
    participant ActivitySvc as UserActivityService

    Student->>UI: Answer Questions & Click Submit Exam (or Timer Expires)
    UI->>QuizCtrl: POST /api/quizzes/{quizId}/submit (answers, timeSpentSeconds, topicId)
    QuizCtrl->>AttemptSvc: evaluateAndSaveAttempt(studentId, quizId, answers, timeSpent, topicId)
    AttemptSvc->>DB: SELECT * FROM questions WHERE quiz_id = ?
    DB-->>AttemptSvc: Questions with correct answers & points
    AttemptSvc->>AttemptSvc: Compare submitted answers vs correct answers
    AttemptSvc->>AttemptSvc: Calculate totalScorePercentage = (earnedPoints / totalPoints) * 100
    AttemptSvc->>DB: INSERT INTO quiz_attempts (score, status, time_spent)
    DB-->>AttemptSvc: Persisted QuizAttempt
    AttemptSvc->>DB: INSERT INTO attempt_answers (question_id, student_answer, is_correct)
    
    alt Status == "PASSED" (Score >= 60%)
        AttemptSvc->>DB: Check previous passed attempts for this quiz
        alt First Successful Pass
            AttemptSvc->>AttemptSvc: Calculate reward points (e.g. 50 pts for first try, 30 for second)
            AttemptSvc->>DB: UPDATE students SET total_points = total_points + rewardPoints
        end
        AttemptSvc->>DB: UPDATE topic_quiz_progress SET completed = true
    end

    AttemptSvc->>ActivitySvc: logTime(studentUserId, timeSpentMinutes)
    ActivitySvc->>DB: INSERT / UPDATE user_activities
    AttemptSvc-->>QuizCtrl: QuizAttempt Entity
    QuizCtrl-->>UI: 200 OK (Score, Result Breakdown, Points Awarded)
    UI-->>Student: Display Scorecard, Celebration Modal & Points Badge
```

---

## 6. Multi-Modal RAG Chat & Grounded Retrieval Flow

```mermaid
sequenceDiagram
    autonumber
    actor Student as Student Learner
    participant UI as AI Mentor Chat Component
    participant Spring as Spring Boot Backend (ChatService)
    participant Flask as Python Flask Microservice (/chat)
    participant GeminiEmbed as Google Gemini Embedding
    participant ChromaDB as ChromaDB Vector Store
    participant GeminiLLM as Google Gemini 3.6 Flash

    Student->>UI: Submit Message / Upload File / Record Voice Note
    UI->>Spring: POST /api/chat (JSON or Multipart with Audio/Image)
    Spring->>Spring: Lookup Student Enrolled Courses (courseIds), Quiz History, Certificates
    Spring->>Flask: POST http://localhost:5001/chat (message, context: {studentName, courseIds, ...}, files)
    
    %% Vector Search
    Flask->>GeminiEmbed: embed_query(message) using 'models/gemini-embedding-2'
    GeminiEmbed-->>Flask: 768-dim Query Vector
    Flask->>ChromaDB: query(query_embeddings, where={"courseId": {"$in": courseIds}}, n_results=5)
    ChromaDB-->>Flask: Candidate Chunks + Metadata + Cosine Distances
    Flask->>Flask: Filter Chunks where cosine distance < 0.7
    
    %% Prompt Assembly & Inference
    Flask->>Flask: Build Augmented Prompt (BASE_SYSTEM_PROMPT + COURSE MATERIAL CONTEXT + Student Data)
    Flask->>GeminiLLM: generate_content(SystemPrompt, UserMessage, Attachments)
    GeminiLLM-->>Flask: Grounded Pedagogical Response
    Flask-->>Spring: { response: "...", sources: ["Module 1: Polymorphism"], ragUsed: true }
    Spring-->>UI: ChatResponse DTO
    UI-->>Student: Render Response with Markdown formatting and source tags
```

---

## 7. Multi-Agent Autonomous Course Builder & Code Debugger Flow

```mermaid
sequenceDiagram
    autonumber
    actor User as Instructor / Student
    participant UI as AiAgentWorkspace.jsx
    participant SpringProxy as AiAgentController (Proxy)
    participant FlaskEngine as MultiAgentEngine (agents.py)
    participant MCP as LearnSphereMCPServer (mcp_server.py)
    participant GeminiLLM as Google Gemini 3.6 Flash
    participant Subprocess as Python Subprocess Sandbox
    participant ChromaDB as ChromaDB Vector Store

    %% Workflow A: Course Builder
    Note over User, ChromaDB: Workflow A: Autonomous Course Creation Agent
    User->>UI: Enter Topic ("Rust Programming") & Click Generate
    UI->>SpringProxy: POST /api/agents/course-builder
    SpringProxy->>FlaskEngine: run_autonomous_course_builder(topic, targetAudience)
    FlaskEngine->>GeminiLLM: Prompt Curriculum Planner for 3-module syllabus JSON
    GeminiLLM-->>FlaskEngine: Valid Syllabus JSON
    FlaskEngine->>FlaskEngine: Synthesize module text & code samples
    FlaskEngine->>ChromaDB: collection.add(auto_generated_chunks)
    FlaskEngine-->>SpringProxy: { courseTitle, modules, workflowTrace }
    SpringProxy-->>UI: Render Structured Course Tree & Vector Index Confirmation

    %% Workflow B: Code Tutor & Sandbox
    Note over User, Subprocess: Workflow B: AI Code Tutor & Debugger Agent
    User->>UI: Submit Python Code Snippet & Question in Sandbox
    UI->>SpringProxy: POST /api/agents/code-sandbox
    SpringProxy->>FlaskEngine: run_code_tutor_agent(code, question)
    FlaskEngine->>MCP: call_tool("execute_python_code", {code, timeout: 5})
    MCP->>Subprocess: subprocess.run([sys.executable, temp_file])
    Subprocess-->>MCP: { exitCode: 0/1, stdout: "...", stderr: "..." }
    MCP-->>FlaskEngine: Sandbox Execution Metrics
    FlaskEngine->>GeminiLLM: Prompt Code Tutor Agent with code + stdout/stderr + user question
    GeminiLLM-->>FlaskEngine: Line-by-line Trace Analysis & Fixed Code Solution
    FlaskEngine-->>SpringProxy: { sandboxResult, agentAnalysis, workflowTrace }
    SpringProxy-->>UI: Render Console Output & AI Debugging Guide
```
