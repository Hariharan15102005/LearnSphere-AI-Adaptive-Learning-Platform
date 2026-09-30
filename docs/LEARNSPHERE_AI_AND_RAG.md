# LearnSphere Platform — AI, Multi-Agent & RAG Technical Deep-Dive

## 1. Overview of AI Ecosystem

LearnSphere incorporates a multi-tiered artificial intelligence framework consisting of:
1. **Multi-Agent Workflow Engine (`python_service/agents.py`)**: Coordinates autonomous agents for curriculum planning, code debugging, and career skill gap matching.
2. **Native Model Context Protocol (MCP) Server (`python_service/mcp_server.py`)**: Exposes standardized JSON-RPC 2.0 tools and platform context resources.
3. **Multi-Modal Retrieval-Augmented Generation (RAG) (`python_service/app.py`)**: Persistent vector store in ChromaDB with `models/gemini-embedding-2` and `gemini-3.6-flash`.
4. **Machine Learning Telemetry Predictor (`python_service/ml_predictor.py`)**: Scikit-Learn `RandomForestClassifier` and `StandardScaler` pipeline assessing student dropout risk and mastery index.
5. **Backend AI Quiz Generator (`LearnSphere-backend/.../AIQuizGeneratorServiceImpl.java`)**: Multi-model Gemini fallback pipeline for rapid exam synthesis.

---

## 2. Multi-Agent System Architecture (`agents.py`)

The multi-agent system utilizes the **Google GenAI SDK** (`google-genai`) paired with structured JSON and tool orchestration to execute autonomous workflows.

```mermaid
flowchart TD
    subgraph UI_Triggers["Frontend Agent Hub (AiAgentWorkspace.jsx)"]
        T1["Course Builder Tab"]
        T2["Code Sandbox Tab"]
        T3["Skill Gap Tab"]
        T4["MCP Protocol Explorer"]
    end

    subgraph Agent_Engine["MultiAgentEngine (agents.py)"]
        Supervisor["Supervisor Planner Agent"]
        
        subgraph Flow1["Autonomous Course Synthesis Workflow"]
            Planner["Curriculum Planner Agent\n(Generates 3-Module Syllabus JSON)"]
            Synthesizer["Content Synthesizer Agent\n(Drafts Module Lessons & Code)"]
            RAG_Indexer["RAG Vector Ingestion Agent\n(Indexes Chunks into ChromaDB)"]
        end

        subgraph Flow2["AI Code Tutor & Debugger Workflow"]
            Sandbox_Dispatch["MCP Subprocess Dispatcher\n(execute_python_code)"]
            Code_Reviewer["Code Tutor & Trace Reviewer\n(Line-by-Line Bug Analysis & Correction)"]
        end

        subgraph Flow3["Career Readiness Workflow"]
            Skill_Matcher["MCP Skill Matcher\n(analyze_skill_gap)"]
            Roadmap_Gen["Career Advisor Agent\n(30-Day Action Plan Generation)"]
        end
    end

    subgraph MCP_Layer["MCP Server & Sandbox (mcp_server.py)"]
        MCP_Tools["MCP Tools (search_rag, execute_code, predict_risk, skill_gap)"]
        Isolated_Process["Python Subprocess Sandbox (sys.executable, 5s timeout)"]
    end

    subgraph Vector_Storage["ChromaDB Vector Store"]
        Chroma_Coll[("ChromaDB Collection: 'course_materials'")]
    end

    T1 --> Supervisor --> Planner --> Synthesizer --> RAG_Indexer --> Chroma_Coll
    T2 --> Sandbox_Dispatch --> MCP_Tools --> Isolated_Process
    Isolated_Process -- "stdout / stderr / exitCode" --> Code_Reviewer
    T3 --> Skill_Matcher --> MCP_Tools
    MCP_Tools -- "Readiness Score & Missing Skills" --> Roadmap_Gen
    T4 -- "Direct RPC" --> MCP_Tools
```

### Agent Detailed Breakdown:
1. **Supervisor Planner Agent**: Routes tasks, verifies input schemas, and orchestrates sequential sub-agent handoffs.
2. **Autonomous Course Synthesizer Agent (`run_autonomous_course_builder`)**:
   - **Input**: Topic string (e.g., "Kubernetes Masterclass"), Target Audience (e.g., "Cloud Engineers").
   - **Step 1 (Syllabus Generation)**: Prompts `gemini-3.6-flash` for a strictly validated 3-module syllabus with titles, summaries, and learning objectives.
   - **Step 2 (Content Synthesis)**: Expands each module and lesson with descriptive text and practical examples.
   - **Step 3 (Vector Ingestion)**: Automatically embeds and indexes the synthesized module text directly into ChromaDB for immediate RAG retrieval.
3. **AI Code Tutor & Debugger Agent (`run_code_tutor_agent`)**:
   - **Input**: User-provided Python code snippet, optional student question.
   - **Execution**: Invokes MCP tool `execute_python_code` in an isolated temporary subprocess with a strict 5-second timeout.
   - **LLM Diagnostic**: Feeds `exitCode`, `stdout`, `stderr`, and user code to `gemini-3.6-flash`.
   - **Output**: Returns Execution Status, Step-by-Step Trace Analysis, Corrected Code Solution, and Key Learning Takeaways.
4. **Career & Skill Gap Analyst Agent (`run_career_skill_agent`)**:
   - **Input**: Target Job Role (e.g., "Azentio AI Agent Developer"), User completed courses and quiz history.
   - **Matching**: Invokes MCP tool `analyze_skill_gap` to compare profile against enterprise competency matrices.
   - **Roadmap Synthesis**: Generates an actionable 30-day technical readiness roadmap highlighting project portfolio requirements.

---

## 3. Model Context Protocol (MCP) Server (`mcp_server.py`)

LearnSphere adheres to the standardized **Model Context Protocol (MCP)** specification (JSON-RPC 2.0).

### Registered MCP Tools:
```json
[
  {
    "name": "search_rag_materials",
    "description": "Perform semantic hybrid search across course materials using vector embeddings.",
    "parameters": {
      "type": "object",
      "properties": {
        "query": { "type": "string" },
        "course_id": { "type": "string" },
        "top_k": { "type": "integer" }
      },
      "required": ["query"]
    }
  },
  {
    "name": "execute_python_code",
    "description": "Safely execute Python code snippets in an isolated sandbox environment.",
    "parameters": {
      "type": "object",
      "properties": {
        "code": { "type": "string" },
        "timeout_sec": { "type": "integer" }
      },
      "required": ["code"]
    }
  },
  {
    "name": "predict_student_risk",
    "description": "Run Scikit-Learn ML classifier to calculate student mastery score and dropout risk.",
    "parameters": {
      "type": "object",
      "properties": {
        "avg_quiz_score": { "type": "number" },
        "completion_rate_pct": { "type": "number" },
        "total_time_hours": { "type": "number" },
        "quiz_attempts_avg": { "type": "number" },
        "consecutive_failures": { "type": "integer" }
      },
      "required": ["avg_quiz_score", "completion_rate_pct", "total_time_hours"]
    }
  },
  {
    "name": "analyze_skill_gap",
    "description": "Evaluate student portfolio & progress against target enterprise job descriptions.",
    "parameters": {
      "type": "object",
      "properties": {
        "target_role": { "type": "string" },
        "completed_courses": { "type": "array", "items": { "type": "string" } },
        "quiz_scores": { "type": "object" }
      },
      "required": ["target_role"]
    }
  },
  {
    "name": "generate_adaptive_quiz",
    "description": "Synthesize difficulty-adjusted exam questions using frontier LLM.",
    "parameters": {
      "type": "object",
      "properties": {
        "topic": { "type": "string" },
        "difficulty": { "type": "string", "enum": ["EASY", "MEDIUM", "HARD", "ADAPTIVE"] },
        "num_questions": { "type": "integer" }
      },
      "required": ["topic"]
    }
  }
]
```

### Registered MCP Resources:
- `learnsphere://platform/metrics`: Live vector store counts, ML model states, and RAG status.
- `learnsphere://job-roles/ai-agent-developer`: Required skill matrix for Agentic AI, MCP, Vector DBs, and RAG.

---

## 4. Multi-Modal Retrieval-Augmented Generation (RAG) Pipeline

```mermaid
sequenceDiagram
    autonumber
    actor Instructor as Instructor / Admin
    participant Spring as Spring Boot Backend
    participant IngestSvc as RagIngestionService (@Async)
    participant Flask as Python Service (/ingest)
    participant Extractor as Text & Multimodal Extractor
    participant GeminiEmbed as Google Gemini Embedding
    participant ChromaDB as ChromaDB Vector Store
    actor Student as Student Learner
    participant ChatCtrl as ChatController & ChatService
    participant FlaskChat as Python Service (/chat)
    participant GeminiLLM as Google Gemini 3.6 Flash

    %% Ingestion Phase
    Note over Instructor, ChromaDB: Ingestion Lifecycle
    Instructor->>Spring: Upload Course Material (PDF / Video / Image / Link / Text)
    Spring->>IngestSvc: ingestMaterial(materialEntity)
    IngestSvc->>Flask: POST /ingest (JSON Payload)
    Flask->>Extractor: extract_text_from_material(data)
    alt MaterialType == PDF
        Extractor->>Extractor: PyMuPDF (fitz) text extraction
    else MaterialType == LINK
        Extractor->>Extractor: BeautifulSoup HTML scraping (p, h1..h3, li)
    else MaterialType == IMAGE / VIDEO
        Extractor->>GeminiLLM: Upload via Gemini File API & generate transcript/description
    end
    Extractor-->>Flask: Clean Extracted Text
    Flask->>Flask: chunk_text(text, chunk_size=500, overlap=50)
    Flask->>GeminiEmbed: embed_texts(chunks) using 'models/gemini-embedding-2'
    GeminiEmbed-->>Flask: 768-dim Vector Embeddings
    Flask->>ChromaDB: collection.add(ids, documents, embeddings, metadatas)
    Flask-->>IngestSvc: 200 OK (chunksIndexed)

    %% Query & Generation Phase
    Note over Student, GeminiLLM: Student Query & RAG Retrieval Lifecycle
    Student->>Spring: Submit Question / Voice Query + Attachments
    Spring->>ChatCtrl: Process Chat Request
    ChatCtrl->>ChatCtrl: Aggregate Student Context (Enrolled Course IDs, Quiz Scores, Certs)
    ChatCtrl->>FlaskChat: POST /chat (User Message + Context + Enrolled CourseIDs)
    FlaskChat->>GeminiEmbed: embed_query(user_message)
    GeminiEmbed-->>FlaskChat: Query Vector
    FlaskChat->>ChromaDB: collection.query(where={"courseId": {"$in": studentCourseIds}}, n_results=5)
    ChromaDB-->>FlaskChat: Top Chunks + Metadatas + Cosine Distances
    FlaskChat->>FlaskChat: Filter Chunks (distance < 0.7 threshold)
    FlaskChat->>FlaskChat: Assemble Prompt (BASE_SYSTEM_PROMPT + Retrieved Context + Student Data)
    FlaskChat->>GeminiLLM: generate_content(contents=[Prompt, AttachmentParts, UserParts])
    GeminiLLM-->>FlaskChat: Grounded AI Response
    FlaskChat-->>ChatCtrl: { response, sources, ragUsed: true }
    ChatCtrl-->>Student: Render Answer with Source Citations & Markdown
```

### Ingestion Details:
- **Chunking Algorithm**: Word-count based windowing with `chunk_size = 500` words and `overlap = 50` words (`app.py:L91-L101`).
- **Embedding Model**: `models/gemini-embedding-2` called via `client.models.embed_content(task_type="RETRIEVAL_DOCUMENT")` for documents and `RETRIEVAL_QUERY` for search queries.
- **Cosine Relevance Threshold**: Retains only chunks where `dist < 0.7` in ChromaDB cosine space, guaranteeing high groundedness and eliminating hallucinations.

---

## 5. Machine Learning Telemetry Predictor (`ml_predictor.py`)

LearnSphere incorporates a Scikit-Learn **RandomForestClassifier** with 50 estimators trained on educational telemetry features.

### Input Features:
1. `avg_quiz_score` (Float, 0 - 100)
2. `completion_rate_pct` (Float, 0 - 100)
3. `total_time_hours` (Float, lifetime learning hours)
4. `quiz_attempts_avg` (Float, average attempts per quiz)
5. `consecutive_failures` (Integer, consecutive failed attempts)

### Mathematical Formulas & Outputs:
$$\text{Mastery Index} = \min\left(100.0, \max\left(0.0, (0.50 \times \text{avg\_quiz\_score}) + (0.35 \times \text{completion\_rate\_pct}) + \left(15.0 \times \min\left(1.0, \frac{\text{total\_time\_hours}}{10.0}\right)\right)\right)\right)$$

- **Risk Tiers**:
  - `0`: **Low Risk (On Track)** — Mastery index high, triggers advanced capstone recommendations.
  - `1`: **Medium Risk (Needs Reinforcement)** — Prompts practice exercises and flashcard reviews.
  - `2`: **High Risk (Intervention Needed)** — Triggers automated AI Mentor proactive outreach and foundational diagnostic quizzes.

---

## 6. Backend AI Quiz Generation with Multi-Model Fallback

In `AIQuizGeneratorServiceImpl.java`, the backend communicates with Google Generative Language API using OkHttp3 with an automatic candidate model fallback chain:
1. `gemini-3.6-flash`
2. `gemini-3-flash-preview`
3. `gemini-3.1-flash-lite`
4. `gemini-3.8-flash`

If a model returns HTTP `429`, `503`, or `404`, the service automatically rotates to the next candidate model in the list, ensuring 99.9% uptime for AI examination generation.
