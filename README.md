#  Academic Activity Reporting System (Terza Missione)

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue.svg)
![MariaDB](https://img.shields.io/badge/MariaDB-Database-lightgrey.svg)
![Maven](https://img.shields.io/badge/Build-Maven-red.svg)
![Docs](https://img.shields.io/badge/Docs-PDF_Report-success.svg)

##  About The Project
This project is a 3-tier desktop application designed to manage and report academic activities (Public Engagement, Cultural Heritage, etc.). 
Built with a strict **Model-Driven Design (MDD)** approach, the system translates rigorous UML models and **OCL (Object Constraint Language)** business rules into a robust Java implementation. 

##  Full Documentation
For a deep dive into the system's architecture, including detailed **UML Diagrams** (Use Cases, Class, Component, Deployment, Sequence, Activity) and the mathematical formalization of business rules (OCL/JML), please refer to the official project report:

-> [**Read the Full Architecture Report (PDF)**](./relazione.pdf)

##  Key Features
* **Role-Based Access Control (RBAC):** Strict separation of privileges among Professors, Managers, and Administrators.
* **Automated Privilege Revocation:** A native MariaDB **Event Scheduler** automatically cleans up expired user roles, decoupling time-based logic from the Java backend.
* **Formalized Business Logic:** Complex constraints (e.g., Conflict of Interest prevention during approvals) are mathematically formalized in OCL and implemented via JML (Java Modeling Language) and Java exceptions.
* **Reactive GUI:** The UI is built with **JavaFX**, leveraging the **Observer Pattern** (`ObservableList`) and Lambda expressions for real-time, decoupled data binding.
* **Automated Testing:** Data Access Layer (DAO) reliability is ensured through **JUnit 5** tests achieving high branch coverage, including specific tests for polymorphism and SQL failure handling.

## Architecture & Design Patterns
The application follows a rigid Layered Architecture to ensure maintainability and testability:
1. **View/Controller Layer (JavaFX):** Thin controllers handling UI events and bindings.
2. **Service Layer:** Centralized business logic, state machine transitions, and validation rules.
3. **Data Access Layer (DAO):** Complete abstraction of the MariaDB relational database.

**Applied Design Patterns:**
* **MVC (Model-View-Controller)** for structural separation.
* **DAO (Data Access Object)** for persistence abstraction.
* **Singleton** for centralized resource management (Database Connection, User Session).
* **Observer** for reactive UI updates.

##  Tech Stack
* **Language:** Java SE 21
* **GUI Framework:** JavaFX
* **Database:** MariaDB (JDBC)
* **Build Tool:** Maven
* **Testing:** JUnit 5
* **Modeling:** UML, OCL, JML

##  System Overview

### Architecture (Deployment)
![Deployment Diagram](link_to_your_deployment_diagram_image.png)

### Application Dashboard
![JavaFX Dashboard](link_to_your_dashboard_screenshot.png)

##  Getting Started

### Prerequisites
* Java Development Kit (JDK) 21 or higher
* MariaDB Server
* Maven

### Database & Environment Setup
1. Create a new MariaDB database named `terza_missione`.
2. Execute the `schema.sql` (and `events.sql` for the Scheduler) provided in the `/sql` directory to generate tables and triggers.
3. Update the database credentials in the `DatabaseManager` Singleton class (or environment variables).
4. Ensure the application has read/write permissions on the root directory to allow local file storage simulation (attachments are saved in the `/uploads` folder).

### Installation & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/academic-activity-reporting-system.git

    Navigate to the project directory:
    Install dependencies and build the project using Maven:
    Run the application.


***
