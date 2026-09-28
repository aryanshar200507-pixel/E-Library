<div align="center">

# 📚 Stories E-Library

### A digital library where you discover, read, and remember — all in the browser.

Read PDFs online with a custom **PDF.js** reader, save your progress, highlight passages, rate books, and get recommendations. Admins manage everything from a dedicated dashboard.

<br>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jakarta](https://img.shields.io/badge/Jakarta_Servlet-6.0-0A7BBB?style=for-the-badge&logo=eclipseide&logoColor=white)
![Tomcat](https://img.shields.io/badge/Tomcat-10.1-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![AWS S3](https://img.shields.io/badge/Amazon_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-HTML%20%7C%20CSS%20%7C%20JS-E34F26?style=for-the-badge&logo=html5&logoColor=white)

<br>

[✨ Features](#-features) · [🏗️ Architecture](#️-architecture) · [🔐 Security](#-authorization--security) · [🚀 Getting Started](#-getting-started) · [🐛 Troubleshooting](#-troubleshooting)

</div>

---

> [!NOTE]
> **Project type:** Eclipse Dynamic Web Project (no Maven/Gradle) &nbsp;•&nbsp; **Java 21** &nbsp;•&nbsp; **Jakarta Servlet 6.0** &nbsp;•&nbsp; **Tomcat 10.1**

## 📑 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [🔐 Authorization & Security](#-authorization--security)
- [☁️ File Storage](#️-file-storage)
- [🗄️ Database](#️-database)
- [🧠 Recommendation System](#-recommendation-system)
- [📁 Project Structure](#-project-structure)
- [🌐 Endpoints](#-endpoints)
- [🚀 Getting Started](#-getting-started)
- [🔄 Flows](#-flows)
- [⚠️ Implementation Notes](#️-implementation-notes)
- [🐛 Troubleshooting](#-troubleshooting)
- [🤝 Contributing](#-contributing)

---

## ✨ Features

<details open>
<summary><b>👤 Authentication & Account Management</b></summary>
<br>

- Registration with **email uniqueness** validation
- Login with email + password, hashed using **BCrypt**
- Session-based authentication via `HttpSession`
- Roles: `USER` and `ADMIN` &nbsp;|&nbsp; Account states: `ACTIVE`, `INACTIVE`, `BLOCKED`
- **Remember Me** (7-day lifetime):
  - Cryptographically random **256-bit** token
  - Only the **SHA-256 hash** is stored in MySQL
  - Raw token lives in an **HttpOnly** cookie
- Logout removes the token, clears the cookie, and invalidates the session
- **Forgot password:** 6-digit OTP by email, valid for **5 minutes**, verified before any reset
- Change password (after verifying the current one) and update name/email from the profile page

</details>

<details open>
<summary><b>📖 Book Discovery & Catalogue</b></summary>
<br>

- Browse the full catalogue, by category, or by keyword search
- Pagination for book lists and category results
- Book details: cover, author, description, category, views, ratings, and comments
- Highest-rated book info available through the service layer
- Covers served as temporary **S3 presigned URLs**

</details>

<details open>
<summary><b>🧑‍💻 Online PDF Reader</b></summary>
<br>

A custom **PDF.js-based** reading interface — not just the browser's native viewer.

| Navigation | Display | Experience |
|---|---|---|
| Page navigation | Zoom & fit-to-width | Persistent reading progress |
| Page thumbnails | Scroll mode | Page-level bookmarks |
| Search inside PDF | Single & two-page layouts | Text highlights (multiple colors) |
| Keyboard shortcuts | Fullscreen & rotation | Mobile controls + swipe navigation |

When the reader opens, the book's **view counter is incremented** and the user's **saved page is restored**.

</details>

<details>
<summary><b>🔖 Bookmarks, Highlights & History</b></summary>
<br>

- Bookmark a whole book, or bookmark specific pages in the reader
- Duplicate bookmarks are prevented at the database/service level
- Store, restore, recolor, and delete highlights — colors: 🟡 Yellow · 🟢 Green · 🔵 Blue · 🩷 Pink
- Reading progress saved **per user, per book**
- Recently-read books on the dashboard, plus full paginated history
- Dedicated bookmarked-books page

</details>

<details>
<summary><b>⭐ Ratings & 💬 Comments</b></summary>
<br>

- Rate books **1–5 stars** — one rating per user per book (resubmitting updates it)
- Average rating and rating count are calculated per book
- Users can add comments, and edit/delete **only their own**
- Admins can delete **any** comment

</details>

<details>
<summary><b>🤝 Book Requests & 💡 Suggestions</b></summary>
<br>

**Book requests**
- Request a missing book (title + optional author), tied to the requesting user
- Weekly limits apply, and duplicate/current-week conflicts are blocked
- Vote for requests — **once per request**, on active/current-week requests only
- Admins cannot submit or vote on user requests

**Application suggestions**
- Users submit suggestions (validated **10–1000 characters**)
- Admins review active suggestions and **accept** or **delete** them

</details>

<details>
<summary><b>🛠️ Admin Dashboard</b></summary>
<br>

| Area | Capabilities |
|---|---|
| 📊 Stats | Total users, categories, and books |
| 👥 Users | Search, manage, recover/deactivate accounts |
| 🗂️ Categories | Add, edit, delete |
| 📕 Books | Add, edit, delete (with S3 cleanup) |
| 💡 Suggestions | Review, accept, delete |
| 💬 Moderation | Remove inappropriate comments |

</details>

---

## 🏗️ Architecture

A clean, layered Java web-application structure:

```text
┌───────────────────────────────────────────────┐
│                  Browser / UI                 │
│          JSP + HTML + CSS + JavaScript        │
└───────────────────────┬───────────────────────┘
                        │ HTTP
                        ▼
┌───────────────────────────────────────────────┐
│             Controllers / Servlets            │
│ Login • Books • Reader • Profile • Admin ...  │
└───────────────────────┬───────────────────────┘
                        ▼
┌───────────────────────────────────────────────┐
│                 Service Layer                 │
│ Business validation • Security • Rules        │
└───────────────────────┬───────────────────────┘
                        ▼
┌───────────────────────────────────────────────┐
│                    DAO Layer                  │
│            JDBC + Prepared Statements         │
└───────────────────┬───────────────┬───────────┘
                    │               │
                    ▼               ▼
              ┌──────────┐    ┌──────────────┐
              │  MySQL   │    │  Amazon S3   │
              │ Database │    │ PDFs/Covers  │
              └──────────┘    └──────────────┘

                    ┌────────────────────┐
                    │ SMTP / Jakarta Mail│
                    │ Password-reset OTP │
                    └────────────────────┘
```

**Request flow**

1. Browser requests a JSP page or servlet endpoint
2. Authorization filters validate access where required
3. Servlet receives the request and validates input
4. Service layer applies business rules
5. DAO classes talk to MySQL via JDBC
6. S3 services upload, delete, or generate temporary URLs
7. Servlet forwards data to the right JSP

---

## 🔐 Authorization & Security

Authorization is centralized in servlet filters.

| Filter | Mapped to | What it does |
|---|---|---|
| `AuthorizationFilter` | `/admin/*` | Confirms a valid session, `loggedInUser` present, and `ADMIN` role. Otherwise redirects to login or returns **HTTP 403**. |
| `RememberMeAuthenticationFilter` | `/*` | With no active session, reads the `rememberMe` cookie, validates hash + expiry, checks the account is still active, and recreates the session. |

### 🛡️ Security practices

- ✅ **BCrypt** password hashing
- ✅ `SecureRandom` for OTPs and Remember-Me tokens
- ✅ **SHA-256** hashed Remember-Me tokens in MySQL
- ✅ **HttpOnly** Remember-Me cookie
- ✅ Centralized admin authorization filter
- ✅ Role checks on user-only actions (ratings, comments, voting)
- ✅ Ownership checks before editing/deleting comments and highlights
- ✅ Short-lived S3 presigned URLs (30 min)
- ✅ **Prepared statements** throughout the DAO layer
- ✅ File extension and size validation on uploads
- ✅ S3 cleanup if a DB save fails after a successful upload

---

## ☁️ File Storage

Book PDFs and covers live in **Amazon S3** — never as binaries in MySQL. The database stores only keys:

```text
books/<uuid>-<file-name>.pdf
covers/<uuid>-<file-name>.jpg
```

| Setting | Value |
|---|---|
| Region | `us-east-1` |
| Object keys | Unique, UUID-based |
| Presigned GET URL validity | **30 minutes** |
| Upload limit | **50 MB** per file |
| On book delete | Files removed from S3 |

> [!TIP]
> The bucket name is set in `S3Config.java`. Change it there when using a different bucket or environment.

---

## 🗄️ Database

The schema is created automatically at startup — no manual SQL script needed.

`ApplicationStartup` calls `DatabaseIniti.initialize()`, which:

1. Creates the database if missing
2. Runs migrations
3. Creates required tables
4. Creates the configured default admin (if absent)

<details>
<summary><b>📋 Main tables</b></summary>
<br>

| Table | Purpose |
|---|---|
| `users` | Accounts, roles, account status |
| `category` | Book categories |
| `books` | Book metadata and S3 storage keys |
| `ratings` | User ratings for books |
| `comments` | User comments on books |
| `reading_progress` | Current page per user/book |
| `bookmark` | Page-level and book-level bookmarks |
| `highlight` | Saved text selections and metadata |
| `remember_me` | Hashed remember-me tokens |
| `app_suggestion` | Suggestions and their status |
| `book_request` | User-submitted book requests |
| `book_request_vote` | Per-user votes on requests |
| `database_migrations` | Applied schema migration versions |

</details>

**Key constraints:** unique user email · rating limited to 1–5 · one rating and one progress record per user/book · no duplicate page bookmarks · unique remember-me hashes · one vote per user per request.

---

## 🧠 Recommendation System

A lightweight, **rule-based** recommender written in Java (not machine learning). It scores books using:

- 📖 Recently read books
- 🗂️ Categories of those books
- ⭐ Average rating
- 📊 Rating-count confidence
- 🔥 View count / popularity

New users with no history get existing books ranked by rating and popularity.

---

## 📁 Project Structure

```text
E-Library/
├── .classpath
├── .project
├── .settings/
└── src/
    └── main/
        ├── java/com/project/elibrary/
        │   ├── bean/
        │   ├── config/
        │   ├── controller/
        │   ├── dao/
        │   ├── filter/
        │   ├── service/
        │   └── util/
        └── webapp/
            ├── WEB-INF/
            │   ├── admin/
            │   ├── user/
            │   └── web.xml
            ├── css/
            ├── images/
            ├── javascript/
            ├── home.jsp
            ├── login.jsp
            ├── register.jsp
            ├── books.jsp
            ├── category-books.jsp
            ├── book-details.jsp
            ├── read-book.jsp
            ├── book-request.jsp
            ├── forgotPassword.jsp
            ├── verifyOtp.jsp
            ├── resetPassword.jsp
            └── changePassword.jsp
```

| Package | Responsibility |
|---|---|
| `bean` | Domain/data objects |
| `config` | DB, S3, and application initialization |
| `controller` | HTTP request handling |
| `dao` | Database access via JDBC |
| `filter` | Authentication/authorization interception |
| `service` | Business logic and validation |
| `util` | Security and helper utilities |

---

## 🌐 Endpoints

<details>
<summary><b>🔓 Public & Account</b></summary>
<br>

| Endpoint | Purpose |
|---|---|
| `/home` | Public home page |
| `/login` | User/admin authentication |
| `/register` | User registration |
| `/logout` | Logout |
| `/forgot-password` | Start password recovery |
| `/verify-otp` | Verify recovery OTP |
| `/reset-password` | Reset password |
| `/change-password` | Change password while authenticated |
| `/profile` | User profile |

</details>

<details>
<summary><b>📚 Books & Reader</b></summary>
<br>

| Endpoint | Purpose |
|---|---|
| `/books` | Book catalogue |
| `/books/category` | Category-specific books |
| `/books/details` | Book details |
| `/books/read` | Open the PDF reader |
| `/books/history` | Reading history |
| `/books/bookmark` | Page/bookmark actions |
| `/books/bookmark-book` | Whole-book bookmark actions |
| `/books/highlight` | Highlight actions |
| `/rating` | Book rating |
| `/comment` | Comment add/update/delete |

</details>

<details>
<summary><b>🤝 Community</b></summary>
<br>

| Endpoint | Purpose |
|---|---|
| `/book-request` | Book request page and submission |
| `/book-request/vote` | Vote for a requested book |
| `/book-request/delete` | Delete a book request |
| `/user/dashboard` | User dashboard |
| `/user/suggestion` | Submit application suggestion |

</details>

<details>
<summary><b>🛠️ Admin</b></summary>
<br>

| Endpoint | Purpose |
|---|---|
| `/admin/dashboard` | Admin dashboard |
| `/admin/books/add` | Add a book |
| `/admin/books/delete` | Delete a book |
| `/admin/suggestions` | Manage suggestions |

</details>

---

## 🚀 Getting Started

### 💻 Requirements

| Requirement | Notes |
|---|---|
| **JDK 21** | Project JRE: `JavaSE-21` |
| **Eclipse IDE for Enterprise Java and Web Developers** | Dynamic Web Project support |
| **Apache Tomcat 10.1** | Jakarta namespace (`jakarta.servlet.*`) |
| **MySQL Server** | User must be able to create DB and tables |
| **AWS account + S3 bucket** | Credentials via default provider chain |
| **SMTP provider** | For password-reset OTP emails |

### 1️⃣ Clone

```bash
git clone https://github.com/aryanshar200507-pixel/E-Library.git
```

### 2️⃣ Import into Eclipse

```text
File → Import → Existing Projects into Workspace → Select the cloned repository → Finish
```

### 3️⃣ Configure JDK & Tomcat

Confirm the project JRE is `JavaSE-21`, then add **Apache Tomcat 10.1** in Eclipse and associate it with the project.

### 4️⃣ Add local configuration

Create `src/main/resources/application.properties`:

```properties
db.server.url=jdbc:mysql://localhost:3306/
db.name=elibrary
db.username=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD

admin.email=YOUR_ADMIN_EMAIL
admin.password=YOUR_ADMIN_PASSWORD

mail.username=YOUR_SMTP_USERNAME
mail.password=YOUR_SMTP_PASSWORD
mail.smtp.host=YOUR_SMTP_HOST
mail.smtp.port=YOUR_SMTP_PORT
```

> [!CAUTION]
> **Never commit secrets.** Keep real database, admin, SMTP, and AWS credentials out of Git. The repository intentionally ships without them.

### 5️⃣ Set up MySQL

Start the server and make sure the configured user has privileges to create the database and tables. The schema is created on startup.

### 6️⃣ Set up AWS S3

1. Create a bucket in the region the app expects
2. Ensure the runtime AWS identity can **put**, **read**, and **delete** objects
3. Update `S3Config.java` if you use a different bucket
4. Supply credentials through a supported AWS mechanism (the app uses `DefaultCredentialsProvider`) — never hard-code them

### 7️⃣ Run

```text
Right-click project → Run As → Run on Server → Apache Tomcat 10.1
```

Then open the app at the context path Eclipse assigns.

---

## 🔄 Flows

<details>
<summary><b>⚡ Application startup</b></summary>

```text
Tomcat starts
     │
     ▼
ApplicationStartup
     │
     ▼
DatabaseIniti.initialize()
     ├── Create database
     ├── Run migrations
     ├── Create tables
     └── Create default admin
     │
     ▼
Application ready
```

</details>

<details>
<summary><b>🔑 Authentication</b></summary>

```text
Login form → LoginServlet → AuthService
                               ├── Find user by email
                               ├── Verify BCrypt password
                               └── Check ACTIVE status
                                        │
                                        ▼
                                   HttpSession
                                   ├── USER  ──> /user/dashboard
                                   └── ADMIN ──> /admin/dashboard
```

With **Remember Me**:

```text
Login → Generate 256-bit random token
          ├── Raw token → HttpOnly cookie
          └── SHA-256 hash → remember_me table
```

</details>

<details>
<summary><b>📚 Reader</b></summary>

```text
/books/read?id=<bookId>
          │
          ▼
Check authenticated session → Load book from MySQL
          │
          ▼
Read PDF storage key → Generate 30-minute S3 presigned URL
          ├── Load saved reading page
          └── Increment book views
          │
          ▼
read-book.jsp → PDF.js + read-book.js
          ├── Search        ├── Bookmarks
          ├── Zoom          ├── Highlights
          └── Navigation    └── Save progress
```

</details>

---

## ⚠️ Implementation Notes

- Eclipse Dynamic Web Project — **not** Maven/Gradle
- `application.properties` is required but not included in the repo
- The S3 bucket is defined in `S3Config.java`
- Uploads are capped at 50 MB per file by the upload servlet config
- PDFs are read through presigned S3 URLs
- OTPs are held temporarily in the HTTP session (5-minute expiry)
- Remember-Me tokens expire after 7 days
- Migration versions are tracked in `database_migrations`

---

## 🐛 Troubleshooting

<details>
<summary><b><code>application.properties not found</code></b></summary>
<br>

Create `src/main/resources/application.properties` and make sure it's on the application classpath.

</details>

<details>
<summary><b>MySQL connection errors</b></summary>
<br>

- MySQL server is running
- `db.server.url` is correct
- Username/password are correct
- The user can create the database and tables

</details>

<details>
<summary><b>S3 errors</b></summary>
<br>

- AWS credentials are available to the Tomcat runtime
- The bucket exists
- The bucket region matches `S3Config`
- The AWS identity has the required S3 permissions

</details>

<details>
<summary><b>Tomcat / Jakarta compatibility</b></summary>
<br>

Use **Tomcat 10.1+**. Older versions that use the legacy `javax.servlet.*` namespace are not supported.

</details>

<details>
<summary><b>Git / Eclipse project conflicts</b></summary>
<br>

Eclipse metadata (`.project`, `.classpath`, `.settings`) is tracked in Git. Coordinate branch changes carefully when several developers touch project configuration.

</details>

---

## 🤝 Contributing

1. Fork or clone the repository
2. Create a feature branch
3. Make your changes
4. Test with MySQL, Tomcat, and S3 configured
5. Commit with a clear message
6. Push the branch and open a pull request

> Avoid committing credentials or unrelated Eclipse metadata changes.

---

## 📄 License

No license file is currently included. Add a `LICENSE` file before distributing the project as open source.

---

<div align="center">

**📚 Stories E-Library**

*A Java/Jakarta EE project focused on practical web architecture, authentication, database design, cloud storage, and an interactive reading experience.*

[**GitHub Repository →**](https://github.com/aryanshar200507-pixel/E-Library)

</div>
