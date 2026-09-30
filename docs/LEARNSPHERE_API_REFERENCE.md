# LearnSphere Platform — Complete REST API Reference

This document provides the definitive, comprehensive REST API specification for the LearnSphere platform, covering the Spring Boot backend (`http://localhost:8080`) and the Python AI microservice (`http://localhost:5001`).

---

## 1. Authentication & Identity Management (`/api/auth`)

| Method | Endpoint | Controller Method | Required Role | Request Body | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | `AuthController.register` | Public | `RegisterRequest` (`name`, `email`, `password`, `role`) | `AuthResponse` (`accessToken`, `refreshToken`, `user`) | `200 OK`, `400 Bad Request` | `AuthService.register` |
| `POST` | `/api/auth/login` | `AuthController.login` | Public | `AuthRequest` (`email`, `password`) | `AuthResponse` (`accessToken`, `refreshToken`, `user`) | `200 OK`, `401 Unauthorized` | `AuthService.login` |
| `POST` | `/api/auth/refresh` | `AuthController.refresh` | Public | `Map<String, String>` (`refreshToken`) | `AuthResponse` (`accessToken`, `refreshToken`, `user`) | `200 OK`, `400 Bad Request` | `AuthService.refreshToken` |
| `POST` | `/api/auth/forgot-password` | `AuthController.forgotPassword` | Public | `Map<String, String>` (`email`) | `Map<String, Object>` (`success`, `message`) | `200 OK`, `404 Not Found` | `AuthService.sendResetPasswordOtp` |
| `POST` | `/api/auth/reset-password` | `AuthController.resetPassword` | Public | `ResetPasswordRequest` (`email`, `otp`, `newPassword`) | `Map<String, Object>` (`success`, `message`) | `200 OK`, `400 Bad Request` | `AuthService.resetPasswordWithOtp` |

---

## 2. Course Management (`/api/courses`)

| Method | Endpoint | Controller Method | Required Role | Request / Path Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/courses` | `CourseController.getAllCourses` | Public | None | `List<CourseResponse>` | `200 OK` | `CourseService.getAllCourses` |
| `GET` | `/api/courses/{id}` | `CourseController.getCourseById` | Public | `id` (Long) | `CourseDetailResponse` | `200 OK`, `404 Not Found` | `CourseService.getCourseById` |
| `POST` | `/api/courses` | `CourseController.createCourse` | `INSTRUCTOR`, `ADMIN` | `CourseRequest` (JSON or Multipart) | `CourseResponse` | `201 Created`, `400 Bad Request` | `CourseService.createCourse` |
| `PUT` | `/api/courses/{id}` | `CourseController.updateCourse` | `INSTRUCTOR`, `ADMIN` | `id` (Long), `CourseRequest` | `CourseResponse` | `200 OK`, `404 Not Found` | `CourseService.updateCourse` |
| `DELETE` | `/api/courses/{id}` | `CourseController.deleteCourse` | `INSTRUCTOR`, `ADMIN` | `id` (Long) | `Map<String, String>` | `200 OK`, `404 Not Found` | `CourseService.deleteCourse` |
| `GET` | `/api/courses/recommendations/dashboard` | `CourseController.getDashboardRecommendations` | `STUDENT`, `INSTRUCTOR`, `ADMIN` | Auth Token Context | `List<CourseResponse>` | `200 OK` | `CourseRecommendationService.recommendCoursesForDashboard` |
| `GET` | `/api/courses/{id}/recommend-next` | `CourseController.recommendNextCourse` | `STUDENT`, `INSTRUCTOR`, `ADMIN` | `id` (Long), Auth Token | `CourseResponse` | `200 OK` | `CourseRecommendationService.recommendNextCourse` |

---

## 3. Topics & Multi-Modal Materials (`/api/topics`, `/api/materials`)

| Method | Endpoint | Controller Method | Required Role | Request / Path Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/topics/course/{courseId}` | `TopicController.getTopicsByCourse` | Public | `courseId` (Long) | `List<TopicResponse>` | `200 OK` | `TopicService.getTopicsByCourse` |
| `POST` | `/api/topics` | `TopicController.createTopic` | `INSTRUCTOR`, `ADMIN` | `TopicRequest` | `TopicResponse` | `201 Created` | `TopicService.createTopic` |
| `PUT` | `/api/topics/{id}` | `TopicController.updateTopic` | `INSTRUCTOR`, `ADMIN` | `id` (Long), `TopicRequest` | `TopicResponse` | `200 OK` | `TopicService.updateTopic` |
| `DELETE` | `/api/topics/{id}` | `TopicController.deleteTopic` | `INSTRUCTOR`, `ADMIN` | `id` (Long) | `Map<String, String>` | `200 OK` | `TopicService.deleteTopic` |
| `GET` | `/api/materials/topic/{topicId}` | `MaterialController.getMaterialsByTopic` | Public | `topicId` (Long) | `List<MaterialResponse>` | `200 OK` | `MaterialService.getMaterialsByTopic` |
| `POST` | `/api/materials` | `MaterialController.createMaterial` | `INSTRUCTOR`, `ADMIN` | `MaterialRequest` / Multipart | `MaterialResponse` | `201 Created` | `MaterialService.createMaterial` + `RagIngestionService.ingestMaterial` |
| `PUT` | `/api/materials/{id}` | `MaterialController.updateMaterial` | `INSTRUCTOR`, `ADMIN` | `id` (Long), `MaterialRequest` | `MaterialResponse` | `200 OK` | `MaterialService.updateMaterial` + `RagIngestionService.ingestMaterial` |
| `DELETE` | `/api/materials/{id}` | `MaterialController.deleteMaterial` | `INSTRUCTOR`, `ADMIN` | `id` (Long) | `Map<String, String>` | `200 OK` | `MaterialService.deleteMaterial` + `RagIngestionService.deleteMaterial` |

---

## 4. Enrollments & Progress Tracking (`/api/enrollments`, `/api/progress`, `/api/adaptive`)

| Method | Endpoint | Controller Method | Required Role | Request / Path Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/enrollments/course/{courseId}` | `EnrollmentController.enrollInCourse` | `STUDENT`, `INSTRUCTOR`, `ADMIN` | `courseId` (Long) | `EnrollmentResponse` | `200 OK` | `EnrollmentService.enrollStudent` |
| `GET` | `/api/enrollments/my-courses` | `EnrollmentController.getMyEnrolledCourses` | `STUDENT` | Auth Context | `List<EnrollmentResponse>` | `200 OK` | `EnrollmentService.getStudentEnrollments` |
| `DELETE` | `/api/enrollments/course/{courseId}` | `EnrollmentController.unenrollCourse` | `STUDENT` | `courseId` (Long) | `Map<String, String>` | `200 OK` | `EnrollmentService.unenrollStudent` |
| `POST` | `/api/progress/material/{materialId}/complete` | `ProgressController.markMaterialComplete` | `STUDENT` | `materialId` (Long), `TimeSpentDTO` | `ProgressResponse` | `200 OK` | `MaterialProgressService.markComplete` + `CompletionService` |
| `GET` | `/api/progress/course/{courseId}` | `ProgressController.getCourseProgress` | `STUDENT` | `courseId` (Long) | `CourseProgressResponse` | `200 OK` | `ProgressService.getCourseProgress` |
| `GET` | `/api/adaptive/next-topic` | `AdaptiveLearningController.recommendNextTopic` | Public / Authenticated | `studentId` (Long), `courseId` (Long) | `TopicRecommendationResponse` | `200 OK` | `AdaptiveLearningService.recommendNextTopic` |

---

## 5. Quizzes & Exam Evaluation (`/api/quizzes`)

| Method | Endpoint | Controller Method | Required Role | Request / Path Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/quizzes/{id}` | `QuizController.getQuizById` | Authenticated | `id` (Long) | `QuizDetailResponse` | `200 OK`, `404 Not Found` | `QuizService.getQuizById` |
| `POST` | `/api/quizzes/generate` | `QuizController.generateAIQuiz` | `INSTRUCTOR`, `ADMIN` | `AIQuizGenerationRequest` (`topicName`, `difficulty`, `numberOfQuestions`) | `AIQuizResponse` (Questions List) | `200 OK`, `500 Server Error` | `AIQuizGeneratorServiceImpl.generateQuiz` |
| `POST` | `/api/quizzes/save-from-ai` | `QuizController.saveAIQuiz` | `INSTRUCTOR`, `ADMIN` | Query Params: `instructorId`, `courseId`, `topicId`, `title`, `duration` + Body: `AIQuizResponse` | `QuizResponse` | `201 Created` | `QuizService.saveAIQuiz` |
| `POST` | `/api/quizzes/{id}/submit` | `QuizController.submitQuiz` | `STUDENT` | `id` (Long), Body: `QuizSubmissionRequest` (`answers`, `timeSpentSeconds`, `topicId`) | `QuizAttemptResponse` (Score, Passed Status, Earned Points) | `200 OK` | `QuizAttemptService.evaluateAndSaveAttempt` |
| `GET` | `/api/quizzes/{id}/attempts` | `QuizController.getQuizAttempts` | `STUDENT`, `INSTRUCTOR`, `ADMIN` | `id` (Long) | `List<QuizAttemptResponse>` | `200 OK` | `QuizAttemptService.getAttempts` |

---

## 6. RAG Chat & AI Agent Gateway (`/api/chat`, `/api/agents`)

| Method | Endpoint | Controller Method | Required Role | Request Payload | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/chat` | `ChatController.chat` | Authenticated | `ChatRequest` (`message`) | `ChatResponse` (`response`, `sources`, `ragUsed`) | `200 OK` | `ChatService.processChat` → `Python /chat` |
| `POST` | `/api/chat` (Multipart) | `ChatController.chatMultipart` | Authenticated | `message` (String), `files` (Multipart) | `ChatResponse` (`response`, `sources`, `ragUsed`) | `200 OK` | `ChatService.processChatMultipart` → `Python /chat` |
| `GET` | `/api/chat/rag/status` | `ChatController.ragStatus` | Public / Authenticated | None | `Map<String, Object>` (Total chunks, vector model) | `200 OK` | Proxies `http://localhost:5001/rag/status` |
| `POST` | `/api/agents/course-builder` | `AiAgentController.buildAutonomousCourse` | Public / Authenticated | `Map` (`topic`, `targetAudience`) | `Map` (`courseTitle`, `modules`, `workflowTrace`) | `200 OK` | Proxies `http://localhost:5001/api/agents/course-builder` |
| `POST` | `/api/agents/code-sandbox` | `AiAgentController.executeCodeSandbox` | Public / Authenticated | `Map` (`code`, `question`) | `Map` (`sandboxResult`, `agentAnalysis`, `workflowTrace`) | `200 OK` | Proxies `http://localhost:5001/api/agents/code-sandbox` |
| `POST` | `/api/agents/skill-gap` | `AiAgentController.analyzeSkillGap` | Public / Authenticated | `Map` (`targetRole`, `userProfile`) | `Map` (`skillGapData`, `careerRoadmap`, `workflowTrace`) | `200 OK` | Proxies `http://localhost:5001/api/agents/skill-gap` |
| `GET` | `/api/agents/mcp/tools` | `AiAgentController.getMcpTools` | Public / Authenticated | None | `Map` (`tools`, `resources`, `protocol`) | `200 OK` | Proxies `http://localhost:5001/api/agents/mcp/tools` |
| `POST` | `/api/agents/mcp/call` | `AiAgentController.callMcpTool` | Public / Authenticated | `Map` (`name`, `arguments`) | `Map` (`tool`, `output`) | `200 OK` | Proxies `http://localhost:5001/api/agents/mcp/call` |
| `POST` | `/api/agents/predict-risk` | `AiAgentController.predictStudentRisk` | Public / Authenticated | `Map` (`avgQuizScore`, `completionRatePct`, `totalTimeHours`) | `Map` (`masteryIndex`, `riskTier`, `probabilities`) | `200 OK` | Proxies `http://localhost:5001/api/ml/predict-risk` |

---

## 7. Certificates & Verification (`/api/certificates`)

| Method | Endpoint | Controller Method | Required Role | Request / Path Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/certificates/generate` | `CertificateController.generateCertificate` | `STUDENT`, `INSTRUCTOR`, `ADMIN` | Query: `courseId` | `CertificateDTO` (`uid`, `studentName`, `courseTitle`, `issuedAt`) | `200 OK` | `CertificateService.generateCertificate` |
| `GET` | `/api/certificates/download/{uid}` | `CertificateController.downloadCertificatePdf` | Authenticated | `uid` (String UUID) | Binary PDF (`application/pdf`) | `200 OK` | `CertificateService.generateCertificatePdf` |
| `GET` | `/api/certificates/public/{uid}` | `CertificateController.verifyPublicCertificate` | Public | `uid` (String UUID) | `CertificateVerificationDTO` | `200 OK`, `404 Not Found` | `CertificateService.getCertificateByUid` |

---

## 8. Analytics & Dashboards (`/api/analytics`)

| Method | Endpoint | Controller Method | Required Role | Request Params | Response Body | Status Codes | Main Service Called |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/analytics/student` | `AnalyticsController.getStudentAnalytics` | `STUDENT` | Auth Context | `AnalyticsDTO.StudentAnalytics` | `200 OK` | `AnalyticsService.getStudentAnalytics` |
| `GET` | `/api/analytics/instructor` | `AnalyticsController.getInstructorAnalytics` | `INSTRUCTOR` | Auth Context | `AnalyticsDTO.InstructorAnalytics` | `200 OK` | `AnalyticsService.getInstructorAnalytics` |
| `GET` | `/api/analytics/instructor/student-progress` | `AnalyticsController.getInstructorStudentProgress` | `INSTRUCTOR` | Auth Context | `List<StudentCourseProgressDTO>` | `200 OK` | `AnalyticsService.getInstructorStudentProgress` |
| `GET` | `/api/analytics/admin` | `AnalyticsController.getAdminAnalytics` | `ADMIN` | Auth Context | `AnalyticsDTO.AdminAnalytics` | `200 OK` | `AnalyticsService.getAdminAnalytics` |

---

## 9. Python Microservice Native Endpoints (`http://localhost:5001`)

| Method | Endpoint | Handler Function | Purpose | Input Payload | Output Schema |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/ingest` | `ingest()` | Chunks, embeds, and indexes material into ChromaDB | `{"materialId": 1, "courseId": 2, "topicId": 3, "title": "...", "materialType": "PDF", "filePath": "..."}` | `{"status": "success", "materialId": "1", "chunksIndexed": 4}` |
| `POST` | `/ingest/delete` | `ingest_delete()` | Purges vector chunks associated with material ID | `{"materialId": 1}` | `{"status": "deleted", "chunksRemoved": 4}` |
| `GET` | `/rag/status` | `rag_status()` | Health check & chunk count of ChromaDB | None | `{"status": "ok", "totalChunks": 142, "embeddingModel": "models/gemini-embedding-2"}` |
| `POST` | `/chat` | `chat()` | RAG vector retrieval + augmented Gemini generation | Multipart or JSON `{"message": "...", "context": {"courseIds": [1, 2], ...}}` | `{"response": "...", "sources": ["..."], "ragUsed": true}` |
| `POST` | `/api/agents/course-builder` | `agent_course_builder()` | Autonomous multi-module course synthesis | `{"topic": "Kubernetes", "targetAudience": "DevOps"}` | `{"courseTitle": "...", "modules": [...], "workflowTrace": [...]}` |
| `POST` | `/api/agents/code-sandbox` | `agent_code_sandbox()` | Python subprocess execution + trace debugging | `{"code": "print('hello')", "question": "Explain"}` | `{"sandboxResult": {...}, "agentAnalysis": "...", "workflowTrace": [...]}` |
| `POST` | `/api/agents/skill-gap` | `agent_skill_gap()` | Career skill gap matching against target roles | `{"targetRole": "AI Agent Developer", "userProfile": {...}}` | `{"skillGapData": {...}, "careerRoadmap": "...", "workflowTrace": [...]}` |
| `GET` | `/api/agents/mcp/tools` | `mcp_list_tools()` | Returns JSON-RPC 2.0 MCP registered tools & resources | None | `{"protocol": "Model Context Protocol (MCP) v1.0", "tools": [...], "resources": [...]}` |
| `POST` | `/api/agents/mcp/call` | `mcp_call_tool()` | Executes specified MCP tool with parameter arguments | `{"name": "execute_python_code", "arguments": {"code": "2+2"}}` | `{"tool": "execute_python_code", "output": {...}}` |
| `POST` | `/api/ml/predict-risk` | `ml_predict_student_risk()` | Scikit-Learn RandomForest student telemetry predictor | `{"avgQuizScore": 85, "completionRatePct": 90, "totalTimeHours": 12}` | `{"prediction": {"masteryIndex": 89.5, "riskTier": "Low Risk (On Track)", ...}}` |
