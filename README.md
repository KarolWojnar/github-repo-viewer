# GitHub Repository Info API

The application exposes a REST API that allows retrieving information about a GitHub user's public repositories, excluding forks.

---

## ✅ Features

- Fetch all **non-fork** repositories of a given GitHub user.
- For each repository, return:
  - Repository name
  - Owner login
  - All branches with:
    - Branch name
    - Last commit SHA
- Return a structured error (`404`) when the GitHub user does not exist.

---

## 📑 Example Usage

### Endpoint
`GET /api/users/{username}/repositories`

### Sample Response – 200 OK

```json
[
  {
    "repositoryName": "hello-world",
    "ownerLogin": "testuser",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
      },
      {
        "name": "feature-branch",
        "lastCommitSha": "a8f9b12c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a"
      }
    ]
  }
]
```

### Sample Response – 404 Not Found
```json
{
  "status": 404,
  "message": "User not found"
}
```

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web (MVC)
- Maven
- WireMock (for integration tests)
- JUnit 5

---

## 🚀 Getting Started
## Run locally
```bash
git clone https://github.com/your-username/github-repo-api.git
cd github-repo-api
mvn spring-boot:run
```
### The API will be available at:
```bash
http://localhost:8080/api/users/{username}/repositories
```
---

## 👤 Author

**Name:** Karol 
**GitHub:** [https://github.com/KarolWojnar](https://github.com/KarolWojnar)
