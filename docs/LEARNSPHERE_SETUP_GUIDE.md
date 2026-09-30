# LearnSphere Platform — Local Windows Setup & Execution Guide

This guide provides step-by-step instructions to configure, run, and verify the LearnSphere multi-tier platform locally on a Windows environment.

---

## 1. System Prerequisites & Environment Matrix

| Component | Verified Requirement | Command to Verify |
| :--- | :--- | :--- |
| **Operating System** | Windows 10 / 11 (64-bit) | `cmd.exe /c ver` |
| **Java JDK** | Java 17 (Eclipse Temurin / Oracle OpenJDK) | `java -version` |
| **Build Tool** | Apache Maven 3.8+ (or included `mvnw.cmd`) | `mvn -version` or `.\mvnw.cmd -v` |
| **Node.js & npm** | Node.js 18.x or 20.x, npm 9.x+ | `node -v` & `npm -v` |
| **Python** | Python 3.10 or 3.11 | `python --version` |
| **Google Gemini API Key** | Valid Gemini AI Studio API key | [Google AI Studio](https://aistudio.google.com/) |

---

## 2. Directory Layout & Ports

| Service | Root Directory | Default Port | Primary Function |
| :--- | :--- | :--- | :--- |
| **LearnSphere Frontend** | `C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\LearnSphere-frontend` | `5173` | React 19 UI, Dashboards, AI Agent Hub |
| **LearnSphere Backend** | `C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\LearnSphere-backend` | `8080` | Spring Boot REST API, Security, JPA DB |
| **Python AI Microservice** | `C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\python_service` | `5001` | ChromaDB, Flask, MCP Server, Multi-Agent Engine |

---

## 3. Step-by-Step Installation & Startup

### Step 1: Configure & Start Python AI Microservice (Port 5001)

Open a PowerShell terminal:
```powershell
cd "C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\python_service"

# Create virtual environment (if not already created)
python -m venv .venv

# Activate virtual environment
.\.venv\Scripts\Activate.ps1

# Upgrade pip and install dependencies
pip install --upgrade pip
pip install -r requirements.txt

# Create .env file with your Gemini API Key
# (Replace with your actual API key)
Set-Content -Path .env -Value "GEMINI_API_KEY=your_gemini_api_key_here"

# Start the Python Flask microservice
python app.py
```

**Verification**:
Open your browser or run:
```powershell
curl http://localhost:5001/rag/status
```
Expected response:
```json
{
  "collection": "course_materials",
  "embeddingModel": "models/gemini-embedding-2",
  "generationModel": "gemini-3.6-flash",
  "status": "ok",
  "totalChunks": 0,
  "vectorStore": "ChromaDB"
}
```

---

### Step 2: Configure & Start Spring Boot Backend (Port 8080)

Open a second PowerShell terminal:
```powershell
cd "C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\LearnSphere-backend"

# Set environment variable for Gemini API Key
$env:GEMINI_API_KEY="your_gemini_api_key_here"

# Build and package the project (skipping test suite for fast startup)
.\mvnw.cmd clean package -DskipTests

# Run the Spring Boot application
.\mvnw.cmd spring-boot:run
```

**Verification**:
The backend will automatically start and run the data seeder (`DataSeeder.java`), creating or updating the default administrator account:
- **Admin Email**: `khariharan.personal@gmail.com`
- **Admin Password**: `Admin@123`
- **Admin Role**: `ROLE_ADMIN`

Test backend health:
```powershell
curl http://localhost:8080/api/courses
```

Access the H2 Database Console:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/learnspheredb`
- User: `sa`
- Password: *(blank)*

---

### Step 3: Configure & Start React Frontend (Port 5173)

Open a third PowerShell terminal:
```powershell
cd "C:\Users\Hariharan K\OneDrive\Desktop\LearnSphere\LearnSphere-frontend"

# Install NPM dependencies
npm install

# Start the Vite development server
npm run dev
```

**Verification**:
Open your web browser and navigate to:
```
http://localhost:5173
```

---

## 4. Default Seeded Credentials

| Role | Email | Password | Pre-configured Access |
| :--- | :--- | :--- | :--- |
| **System Admin** | `khariharan.personal@gmail.com` | `Admin@123` | Full admin dashboard, user management, platform analytics, course creation |
| **Student** | *(Register via `/register`)* | *(User defined)* | Explore courses, enroll, track learning paths, play quizzes, AI Mentor chat |
| **Instructor** | *(Register via `/register` choosing INSTRUCTOR)* | *(User defined)* | Course Studio, manual & AI quiz generation, student progress telemetry |

---

## 5. Common Troubleshooting & Solutions

### 1. `Port 8080 or 5001 already in use`
- **Cause**: A previous server instance is still running in the background.
- **Solution**:
  ```powershell
  Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force
  Get-Process -Id (Get-NetTCPConnection -LocalPort 5001).OwningProcess | Stop-Process -Force
  ```

### 2. `ExecutionPolicy Error on .venv Activation`
- **Cause**: PowerShell script execution policy is restricted.
- **Solution**:
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```

### 3. `H2 Database Locked Error`
- **Cause**: Multiple processes trying to access `./data/learnspheredb.mv.db` without `AUTO_SERVER=TRUE`.
- **Solution**: Ensure only one backend instance is running or verify `AUTO_SERVER=TRUE` in `application.properties`.

### 4. `ChromaDB SQLite / C++ Build Error`
- **Cause**: Using older Python versions or missing pre-built wheels.
- **Solution**: Use standard Python 3.10 or 3.11 with `pip install --upgrade pip wheel setuptools` before installing `requirements.txt`.
