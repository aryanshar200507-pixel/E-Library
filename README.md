# 📚 Stories E-Library

A web-based digital library built with **Java 21, Jakarta Servlet/JSP, JDBC, MySQL, Apache Tomcat, and Amazon S3**.

Stories E-Library lets users discover books, read PDFs online through a custom PDF.js reader, save reading progress, bookmark books and pages, highlight passages, rate and comment on books, request missing books, submit application suggestions, and receive recommendations. Administrators can manage books, categories, users, and suggestions through a dedicated admin dashboard.

> **Project type:** Eclipse Dynamic Web Project (not Maven/Gradle)  
> **Java:** 21  
> **Web platform:** Jakarta Servlet 6.0  
> **Server:** Apache Tomcat 10.1  
> **Database:** MySQL  
> **File storage:** Amazon S3  
> **Frontend:** JSP + HTML + CSS + JavaScript

---

## ✨ Features

### 👤 Authentication & Account Management

- User registration with email uniqueness validation.
- Login using email and password.
- Passwords are hashed with **BCrypt** before storage.
- Session-based authentication using `HttpSession`.
- Role-based access for **USER** and **ADMIN** accounts.
- Account states are represented by `ACTIVE`, `INACTIVE`, and `BLOCKED`.
- Secure **Remember Me** functionality:
  - Generates a cryptographically random 256-bit token.
  - Stores only the **SHA-256 hash** of the token in MySQL.
  - Stores the raw token in an HttpOnly browser cookie.
  - Token lifetime is 7 days.
- Logout removes the remember-me token, clears the cookie, and invalidates the session.
- Forgot-password flow uses a **6-digit OTP** delivered through email.
- OTP validity is **5 minutes**.
- Password reset requires successful OTP verification before the new password is saved.
- Logged-in users can change their password after verifying the current password.
- Users can update their name and email from the profile page.

### 📖 Book Discovery & Catalogue

- Browse the complete book catalogue.
- Browse books by category.
- Search books by keyword.
- Pagination for book lists and category results.
- View individual book details.
- Display book cover, author, description, category, views, ratings, and comments.
- Highest-rated book information is available through the book service layer.
- Book covers are generated as temporary S3 URLs for the frontend.

### 🧑‍💻 Online PDF Reader

The project includes a custom **PDF.js-based reading interface** rather than relying only on the browser's native PDF viewer.

Reader functionality includes:

- PDF rendering with PDF.js.
- Page navigation.
- Page thumbnails.
- Search/find inside the PDF.
- Zoom controls.
- Fit-to-width support.
- Scroll mode.
- Single-page and two-page reading layouts.
- Fullscreen mode.
- Rotation controls.
- Keyboard shortcuts.
- Mobile-friendly reader controls.
- Touch/swipe navigation.
- Persistent reading progress.
- Page-level bookmarks.
- Text highlighting with stored selection information.
- Multiple highlight colors.

When the reader is opened, the application increments the book's view counter and loads the user's saved page for that book.

### 🔖 Bookmarks, Highlights & History

- Save a complete book to the user's bookmarks.
- Create page-specific bookmarks inside the reader.
- Prevent duplicate bookmarks at the database/service level.
- Store and restore text highlights.
- Highlight colors supported by the service:
  - Yellow
  - Green
  - Blue
  - Pink
- Delete highlights for the current user.
- Change highlight color.
- Save reading progress separately for each user and book.
- Recently-read books are shown on the user dashboard.
- Full reading history is available with pagination.
- Dedicated bookmarked-books page.

### ⭐ Ratings & 💬 Comments

- Users can rate books from **1 to 5 stars**.
- A user can have only one rating per book; later submissions update the existing rating.
- Average rating and rating count are calculated for books.
- Users can add comments.
- Users can edit only their own comments.
- Users can delete only their own comments.
- Administrators can delete any comment.

### 🤝 Book Requests

Users can request books that are not currently available.

- Submit a book title and optional author.
- Requests are associated with the requesting user.
- Requests are limited to the project's weekly rules.
- The application prevents duplicate/current-week request conflicts.
- Users can vote for requested books.
- A user can vote only once per request.
- Votes are restricted to active/current-week requests.
- Administrators are not allowed to submit or vote on user book requests.

### 💡 Application Suggestions

Users can submit suggestions for improving the application.

- Suggestion length is validated between **10 and 1000 characters**.
- Suggestions are stored with a status.
- Administrators can review active suggestions.
- Administrators can accept or delete suggestions.

### 🛠️ Admin Dashboard

Administrators have a dedicated dashboard for application management.

- Dashboard statistics:
  - Total users
  - Total categories
  - Total books
- Search users.
- Manage user accounts.
- Recover/deactivate user accounts.
- Add categories.
- Edit categories.
- Delete categories.
- Add books.
- Edit books.
- Delete books.
- Manage application suggestions.
- Remove inappropriate comments.

---

## 🏗️ Architecture

The application follows a layered Java web-application structure:

```text
┌───────────────────────────────────────────────┐
│                  Browser / UI                 │
│          JSP + HTML + CSS + JavaScript        │
└───────────────────────┬───────────────────────┘
                        │ HTTP
                        ▼
┌───────────────────────────────────────────────┐
│             Controllers / Servlets            │
│ Login • Books • Reader • Profile • Admin ... │
└───────────────────────┬───────────────────────┘
                        ▼
┌───────────────────────────────────────────────┐
│                 Service Layer                 │
│ Business validation • Security • Rules        │
└───────────────────────┬───────────────────────┘
                        ▼
┌───────────────────────────────────────────────┐
│                    DAO Layer                  │
│             JDBC + Prepared Statements       │
└───────────────────┬───────────────┬───────────┘
                    │               │
                    ▼               ▼
              ┌──────────┐    ┌──────────────┐
              │  MySQL   │    │ Amazon S3    │
              │ Database │    │ PDFs/Covers  │
              └──────────┘    └──────────────┘

                         ┌────────────────────┐
                         │ SMTP / Jakarta Mail│
                         │ Password-reset OTP │
                         └────────────────────┘
```

### Request flow

1. A browser requests a JSP page or servlet endpoint.
2. Authorization filters validate access where required.
3. A servlet/controller receives the request and validates input.
4. The service layer applies business rules.
5. DAO classes perform MySQL operations through JDBC.
6. S3 services upload, delete, or generate temporary URLs for files.
7. The servlet forwards data to the appropriate JSP.

---

## 🔐 Authorization Model

The project uses servlet filters to keep authorization logic centralized.

### Admin protection

`AuthorizationFilter` is mapped to:

```text
/admin/*
```

It verifies:

1. A valid session exists.
2. `loggedInUser` exists in the session.
3. The authenticated user has the `ADMIN` role.

Unauthorized users are redirected to the login page or receive HTTP 403.

### Remember Me restoration

`RememberMeAuthenticationFilter` is mapped to all requests:

```text
/*
```

When no active session exists, it looks for the `rememberMe` cookie, validates the token hash and expiry, verifies that the account is still active, and recreates the authenticated session.

---

## ☁️ File Storage

Book PDFs and cover images are stored in **Amazon S3**.

The application does **not** store the binary files directly in MySQL. Instead, the database stores S3 storage keys such as:

```text
books/<uuid>-<file-name>.pdf
covers/<uuid>-<file-name>.jpg
```

When a file needs to be displayed, the storage service generates a **presigned GET URL**.

### Current storage behavior

- S3 region: `us-east-1`
- Bucket configured in `S3Config`
- PDF and cover uploads use unique UUID-based object keys.
- Uploaded files are deleted from S3 when corresponding book records are deleted.
- Presigned GET URLs are valid for **30 minutes**.
- Add/edit book upload handling limits each uploaded file to **50 MB**.

> The bucket name is currently configured in source code. Change `S3Config.java` when using a different bucket or environment.

---

## 🗄️ Database

The application initializes the MySQL database automatically when the web application starts.

`ApplicationStartup` calls:

```java
DatabaseIniti.initialize();
```

The initialization process:

1. Creates the database if it does not exist.
2. Runs database migrations.
3. Creates required tables.
4. Creates the configured default admin account if it does not already exist.

### Main tables

| Table | Purpose |
|---|---|
| `users` | User accounts, roles, account status |
| `category` | Book categories |
| `books` | Book metadata and S3 storage keys |
| `ratings` | User ratings for books |
| `comments` | User comments on books |
| `reading_progress` | Current reading page for each user/book |
| `bookmark` | Page-level and book-level bookmark data |
| `highlight` | Saved text selections and highlight metadata |
| `remember_me` | Hashed remember-me authentication tokens |
| `app_suggestion` | User suggestions and their status |
| `book_request` | User-submitted book requests |
| `book_request_vote` | Per-user votes on book requests |
| `database_migrations` | Applied schema migration versions |

### Important constraints

- User email is unique.
- Rating is constrained to 1–5.
- One rating per user/book.
- One reading-progress record per user/book.
- Duplicate page bookmarks are prevented.
- Remember-me token hashes are unique.
- A user can vote only once per book request.

---

## 🧠 Recommendation System

The user dashboard includes a lightweight recommendation service.

The current implementation uses:

- Recently read books.
- Categories from recently read books.
- Average book rating.
- Rating count confidence.
- Book view count/popularity.

For users with no reading history, the service falls back to existing books and ranks them using rating and popularity signals.

This is a rule-based recommendation system implemented in Java rather than a machine-learning model.

---

## 📁 Project Structure

```text
E-Library/
├── .classpath
├── .project
├── .settings/
│
└── src/
    └── main/
        ├── java/
        │   └── com/project/elibrary/
        │       ├── bean/
        │       ├── config/
        │       ├── controller/
        │       ├── dao/
        │       ├── filter/
        │       ├── service/
        │       └── util/
        │
        └── webapp/
            ├── WEB-INF/
            │   ├── admin/
            │   ├── user/
            │   └── web.xml
            │
            ├── css/
            ├── images/
            ├── javascript/
            │
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

### Java package responsibilities

| Package | Responsibility |
|---|---|
| `bean` | Domain/data objects |
| `config` | DB, S3 and application initialization |
| `controller` | HTTP request handling |
| `dao` | Database access via JDBC |
| `filter` | Authentication/authorization interception |
| `service` | Business logic and validation |
| `util` | Security and helper utilities |

---

## 🌐 Important Endpoints

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
| `/book-request` | Book request page and submission |
| `/book-request/vote` | Vote for a requested book |
| `/book-request/delete` | Delete a book request |
| `/profile` | User profile |
| `/user/dashboard` | User dashboard |
| `/user/suggestion` | Submit application suggestion |
| `/admin/dashboard` | Admin dashboard |
| `/admin/books/add` | Add a book |
| `/admin/books/delete` | Delete a book |
| `/admin/suggestions` | Manage suggestions |

---

## 💻 Requirements

Install the following before running the project:

- **JDK 21**
- **Eclipse IDE for Enterprise Java and Web Developers**
- **Apache Tomcat 10.1**
- **MySQL Server**
- An **AWS account** with an S3 bucket configured for the application
- An SMTP account/provider for password-reset emails

The Eclipse project is configured for **Java 21** and **Tomcat 10.1**.

---

## ⚙️ Configuration

The application expects an `application.properties` file on the application classpath.

The source currently reads these keys:

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

Place the file under:

```text
src/main/resources/application.properties
```

### Do not commit secrets

Keep real database, administrator, SMTP, or AWS credentials out of Git.

A local configuration file is required because the repository does not provide application secrets.

---

## 🪣 AWS S3 Setup

1. Create an S3 bucket in the region expected by the application.
2. Ensure the runtime AWS identity can:
   - Put objects.
   - Read objects needed for presigned URLs.
   - Delete objects.
3. Update `S3Config.java` when using a bucket other than the configured one.
4. Configure AWS credentials using the AWS SDK's default credential provider chain.

The application uses the AWS SDK's `DefaultCredentialsProvider`, so credentials should be supplied through a supported AWS credential mechanism rather than hard-coded into the project.

---

## 🚀 Running the Project in Eclipse

Because this is an **Eclipse Dynamic Web Project**, there is no Maven or Gradle build file in the repository.

### 1. Clone the repository

```bash
git clone https://github.com/aryanshar200507-pixel/E-Library.git
```

### 2. Import into Eclipse

In Eclipse:

```text
File
→ Import
→ Existing Projects into Workspace
→ Select the cloned repository
→ Finish
```

### 3. Configure JDK

Use **JDK 21** for the project.

Confirm the project JRE is:

```text
JavaSE-21
```

### 4. Configure Tomcat

Add **Apache Tomcat 10.1** in Eclipse and associate the project with that runtime.

### 5. Add local configuration

Create:

```text
src/main/resources/application.properties
```

using the configuration template shown above.

### 6. Configure MySQL

Start the MySQL server and make sure the configured database user has enough privileges for the application to create the database and tables.

The application creates the database and schema during startup, so a separate manual schema script is normally not required.

### 7. Configure AWS

Set up AWS credentials and make sure the configured S3 bucket is available.

### 8. Run on Tomcat

Right-click the project:

```text
Run As
→ Run on Server
→ Apache Tomcat 10.1
```

Open the application at the Tomcat context path assigned by Eclipse.

---

## 🔄 Application Startup Flow

```text
Tomcat starts
     │
     ▼
ApplicationStartup
     │
     ▼
DatabaseIniti.initialize()
     │
     ├── Create database
     ├── Run migrations
     ├── Create tables
     └── Create default admin
     │
     ▼
Application ready
```

---

## 🔑 Authentication Flow

```text
Login form
   │
   ▼
LoginServlet
   │
   ▼
AuthService
   │
   ├── Find user by email
   ├── Verify BCrypt password
   └── Check ACTIVE status
   │
   ▼
HttpSession
   │
   ├── USER  ──> /user/dashboard
   └── ADMIN ──> /admin/dashboard
```

When Remember Me is selected:

```text
Login
  │
  ▼
Generate 256-bit random token
  │
  ├── Raw token → HttpOnly cookie
  └── SHA-256 hash → remember_me table
```

---

## 📚 Reader Flow

```text
/books/read?id=<bookId>
          │
          ▼
Check authenticated session
          │
          ▼
Load book from MySQL
          │
          ▼
Read PDF storage key
          │
          ▼
Generate 30-minute S3 presigned URL
          │
          ├── Load saved reading page
          └── Increment book views
          │
          ▼
read-book.jsp
          │
          ▼
PDF.js + read-book.js
          │
          ├── Search
          ├── Zoom
          ├── Navigation
          ├── Bookmarks
          ├── Highlights
          └── Save progress
```

---

## 🛡️ Security Practices Used

The project includes several security-focused implementation choices:

- BCrypt password hashing.
- SecureRandom for OTP generation.
- SecureRandom for Remember-Me token generation.
- SHA-256 hashing for Remember-Me tokens stored in MySQL.
- HttpOnly Remember-Me cookie.
- Session-based authentication.
- Centralized admin authorization filter.
- Role checks for user-only actions such as ratings, comments, and voting.
- User ownership checks before editing/deleting personal comments and highlights.
- Short-lived S3 presigned URLs.
- Prepared statements in the DAO layer.
- File extension and size validation for uploaded book files.
- S3 cleanup when a database save fails after successful file upload.

---

## ⚠️ Implementation Notes

- The repository is an **Eclipse Dynamic Web Project**, not a Maven/Gradle project.
- The project expects an `application.properties` file but does not expose application secrets in the repository.
- AWS S3 bucket configuration is currently defined in `S3Config.java`.
- PDF and cover uploads are limited to 50 MB per file by the upload servlet configuration.
- Reader file access uses presigned S3 URLs rather than storing PDFs in MySQL.
- OTPs are stored temporarily in the HTTP session and expire after 5 minutes.
- Remember-Me tokens expire after 7 days.
- The recommendation engine is rule-based and uses reading history, category overlap, ratings, rating confidence, and popularity.
- The database migration mechanism currently tracks migration versions in the `database_migrations` table.

---

## 🐛 Troubleshooting

### `application.properties not found`

Create:

```text
src/main/resources/application.properties
```

and ensure the file is available on the application classpath.

### MySQL connection errors

Check:

- MySQL server is running.
- `db.server.url` is correct.
- Database username/password are correct.
- The configured user can create the database and tables.

### S3 errors

Check:

- AWS credentials are available to the Tomcat runtime.
- The S3 bucket exists.
- The bucket region matches `S3Config`.
- The AWS identity has the required S3 permissions.

### Tomcat/Jakarta compatibility problems

Use **Tomcat 10.1+** with the Jakarta Servlet API used by this project. Older Tomcat versions using the legacy `javax.servlet.*` namespace are not the target runtime.

### Git/Eclipse project conflicts

Because Eclipse metadata such as `.project`, `.classpath`, and `.settings` is tracked, coordinate branch changes carefully when multiple developers are modifying Eclipse project configuration.

---

## 🤝 Contributing

1. Fork or clone the repository.
2. Create a feature branch.
3. Make your changes.
4. Test the application with MySQL, Tomcat, and S3 configured.
5. Commit with a clear message.
6. Push the branch and open a pull request.

For team development, avoid committing credentials and avoid unrelated Eclipse metadata changes unless they are intentionally part of the update.

---

## 📌 Repository

**GitHub:** https://github.com/aryanshar200507-pixel/E-Library.git

---

## 📄 License

No license file is currently included in the repository. Add an appropriate `LICENSE` file before distributing the project under an open-source license.

---

## 👨‍💻 Project

**Stories E-Library**  
A Java/Jakarta EE digital library project focused on practical web application architecture, authentication, database design, cloud object storage, and an interactive online reading experience.
