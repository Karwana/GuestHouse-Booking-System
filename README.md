# GuestHouse Booking System

A web application for managing guest house reservations, built with Spring Boot, Thymeleaf, and Docker.

## Microservices
This application is part of an interconnected microservices architecture designed to run alongside **Customer Service** and **Review Service** via Docker Compose. It communicates directly with Customer Service via REST to fetch customer details and validate bookings.

## Live Deployments
* **Production:** [https://booking-prod-production.up.railway.app](https://booking-prod-production.up.railway.app)
* **Production Health:** [https://booking-prod-production.up.railway.app/actuator/health](https://booking-prod-production.up.railway.app/actuator/health)
* **Staging:** [https://booking-stage-staging.up.railway.app](https://booking-stage-staging.up.railway.app)
* **Staging Health:** [https://booking-stage-staging.up.railway.app/actuator/health](https://booking-stage-staging.up.railway.app/actuator/health)

## Features
* **Customers:** Register, update, and delete customers.
* **Rooms:** Manage single and double rooms with optional extra beds.
* **Bookings:** Create and manage reservations with automatic double-booking prevention.
* **Search:** Find available rooms by date range and guest capacity.

## Tech Stack
* Java 21
* Spring Boot 4.0.6
* Spring Data JPA & Hibernate
* Spring Boot Actuator
* MySQL 8.0
* Thymeleaf & Bootstrap 5
* Docker & Docker Hub
* GitHub Actions (CI/CD)
* Railway (Staging and Production)

## Repository Structure Note for Docker Compose
For `docker compose up --build` to locate all service directories correctly using the relative build contexts, ensure that all three repositories (`GuestHouse-Booking-System`, `GuestHouse-Customer-Service`, `GuestHouse-Review-Service`) and your infrastructure repository (`GuestHouse-Infrastructure`) are placed within the same parent folder like this:

```text
📁 parent-folder/
├── 📁 GuestHouse-Infrastructure/  (contains docker-compose.yml)
├── 📁 GuestHouse-Booking-System/
├── 📁 GuestHouse-Customer-Service/
└── 📁 GuestHouse-Review-Service/
```

## Branch Strategy & Workflow

### Chosen Strategy: Trunk-Based Development
The team applies **Trunk-Based Development** utilizing short-lived feature branches. The trunk is `master`.

**Rationale for Trunk-Based Development:**
* **Minimized Merge Friction:** Long-lived feature branches frequently lead to complex merge conflicts ("merge hell"). By integrating small, frequent changes into trunk (`master`), we ensure all contributors continually work against the latest shared codebase.
* **Short Feedback Loop:** Code changes are tested, packaged, and verified immediately through our GitHub Actions CI pipeline, catching regression errors within minutes rather than weeks.
* **Simplicity and Traceability:** Trunk-based reduces administrative overhead compared to GitFlow and maintains a clear, linear history where every commit on `master` represents a deployable release candidate.
* **Fits our team:** We are a small team working on one service with a short deadline. Frequent small merges keep everyone on the latest code, and the manual production step gives us a safety gate without the overhead of a separate `develop` branch.

### Branch Protection
`master` is protected:
* No direct pushes. All changes go through a pull request from a feature branch.
* At least one approval from another team member is required.
* The CI check must be green before a pull request can be merged.

### Workflow: From Branch to Production
1. **Local Development:** The developer creates a short-lived feature branch from `master` (`feature/<feature-name>`).
2. **Pull Request (PR) & Code Review:**
   * Merging into `master` requires an approved Pull Request and peer code review with concrete comments.
   * GitHub Actions triggers automatically on PR events (`on: pull_request: branches: [master]`).
   * The pipeline provisions an isolated MySQL service container and runs the tests with `mvn -B package`. Branch protection rules block merging if the checks fail.
3. **Merge to Master:** Once approved and verified green, changes are merged into `master`.
4. **Automated Image Build & Docker Hub Push:**
   * GitHub Actions builds the application (`mvn -B package`, which also runs the tests).
   * The Docker image is tagged and pushed to Docker Hub under two tags:
     * `:latest` (tracked by Staging)
     * `:<commit-sha>` (unique, immutable tag used for all deployments and rollbacks)
5. **Deployment:**
   * **Staging (automatic):** After the image is pushed, the pipeline triggers a redeploy of the Staging service in Railway through Railway's API. Staging tracks the `:latest` tag, so every merge to `master` ends up in staging.
   * **Production (manual):** After verifying staging and its `/actuator/health`, we run the **Production Release** workflow (GitHub Actions → Run workflow). It sets the Production service to the image `booking-system:<commit-sha>` and deploys it. Leaving the SHA field empty deploys the newest `master` commit.
   * `:latest` and `:<commit-sha>` are pushed from the same build and have the same digest on Docker Hub, so Staging and Production run the exact same image. Only environment variables (database host/port, database name and service URLs) differ.
   * Each environment has its own MySQL database.

## Observability
* **Logging:** We use standardized logging levels (INFO, WARN, ERROR) via SLF4J. INFO logs normal business events (startup, booking creation), WARN flags recoverable anomalies (e.g. slow responses or non-critical dependencies), and ERROR captures unhandled exceptions. Passwords, tokens, and personal data are never logged.
* **Health:** `/actuator/health` exposes the runtime status of the application, the database, and our custom `DatabaseHealthIndicator`. Railway's deployment health check probe monitors `/actuator/health` to verify that the service is operational before routing live traffic to the deployment.

## Resolved Merge Conflict
During development, a conflict occurred in `src/main/resources/application.properties` when integrating Actuator and logging:

* **How the Conflict Occurred:**
  * Fahim created branch `feature/custom-health` (PR #65) and added Actuator endpoint properties (`management.endpoints.web.exposure.include=health,info` and `management.endpoint.health.show-details=always`).
  * Karwan simultaneously worked on `feature/actuator-and-logging` (PR #66) adding logging levels (`logging.level...`) and the same Actuator settings.
  * When merging PR #66 into `master` after PR #65 was already merged, identical configuration lines collided in `application.properties`.

* **How It Was Resolved:**
  1. We inspected the duplicate blocks in `application.properties`.
  2. Removed the redundant `management.endpoints.*` duplicate entries.
  3. Kept the clean combination: Fahim's health configuration alongside Karwan's logging levels (`INFO`).
  4. Verified that the CI build and tests passed before completing the merge.

## Image Tagging & Rollback Routine
Every build pushed to Docker Hub is tagged with its immutable Git commit SHA alongside `:latest`:
* `<DOCKER_USERNAME>/booking-system:latest`
* `<DOCKER_USERNAME>/booking-system:<commit-sha>`

While Staging tracks the `:latest` tag for immediate continuous delivery, Production deployments strictly use the immutable commit-SHA tag, ensuring every release in production corresponds to an exact, verified commit.

### How to Roll Back via Commit-SHA
If a deployment fails or introduces a breaking bug:

1. **Get the stable SHA:** Go to GitHub Actions or the commit history and copy the commit SHA of the last working build.
2. **Redeploy production:** Go to GitHub Actions → **Production Release** → **Run workflow**, enter the stable SHA and run it. Production is set to `<DOCKER_USERNAME>/booking-system:<stable-commit-sha>`.
3. **Verify:** Check the health endpoint, which should contain `"status":"UP"`:
[https://booking-prod-production.up.railway.app/actuator/health](https://booking-prod-production.up.railway.app/actuator/health)
