# 🔐 Checkpoint – Full Stack Authentication System

Checkpoint is a **full-stack authentication application** built using **React, Spring Boot, Spring Security, and MySQL**. The project provides a secure authentication system with user registration, login, logout, JWT-based authentication, email OTP password reset, and protected APIs.

## 🚀 Live Demo

**Frontend:**
https://checkpoint-yu76-iqhjjwgs3-mallesh3.vercel.app

**GitHub:**
https://github.com/malleswararaopalepogu/Checkpoint

## ✨ Features

* 👤 User Registration
* 🔐 User Login & Logout
* 🎫 JWT-based Authentication
* 🍪 Secure Cookie-based Authentication
* 🛡️ Spring Security Integration
* 🔑 BCrypt Password Encryption
* 📧 Email OTP for Password Reset
* 🔎 Authentication Status Verification
* 🔒 Protected REST APIs
* 🗄️ MySQL Database Integration
* 🌐 CORS Configuration
* ☁️ Full-stack Deployment

## 🛠️ Technologies Used

### Frontend

* React.js
* JavaScript
* Axios
* HTML5
* CSS3
* Vite

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* REST APIs
* Java Mail

### Database & Tools

* MySQL
* Git & GitHub
* Maven
* Postman
* VS Code
* Eclipse

## 🔐 Authentication Flow

```text
User
 ↓
React Frontend
 ↓
REST API
 ↓
Spring Security
 ↓
AuthenticationManager
 ↓
UserDetailsService
 ↓
MySQL
 ↓
JWT Token
 ↓
Secure Cookie
```

After successful login, the application generates an authentication token and stores it securely in a cookie. Protected APIs verify the authentication before allowing access.

## 📧 Password Reset

Checkpoint provides an OTP-based password recovery system:

```text
Enter Email
     ↓
Generate OTP
     ↓
Send OTP via Email
     ↓
Verify OTP
     ↓
Set New Password
```

The new password is encrypted using **BCrypt** before being stored in the database.

## 📂 Project Structure

```text
Checkpoint/
│
├── backend/
│   └── Spring Boot Application
│
├── frontend/
│   └── React Application
│
└── README.md
```

## ⚙️ Running the Project Locally

### Clone the repository

```bash
git clone https://github.com/malleswararaopalepogu/Checkpoint.git
cd Checkpoint
```

### Start Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

### Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

Configure the required **MySQL, JWT, and email credentials** using environment variables.

## ☁️ Deployment

* **Frontend:** Vercel
* **Backend:** Render
* **Database:** MySQL

## 🎯 Key Learning Outcomes

Through this project, I gained practical experience in:

* Building REST APIs using Spring Boot
* Implementing authentication with Spring Security
* Working with JWT and cookies
* Password encryption using BCrypt
* Integrating Java Mail for OTP verification
* Connecting Spring Boot with MySQL using JPA
* Connecting React with Spring Boot APIs using Axios
* Testing APIs using Postman
* Deploying a full-stack application

## 👨‍💻 Author

**Palepogu Nagamalleswararao**

Computer Science & Engineering Graduate

GitHub:
https://github.com/malleswararaopalepogu
