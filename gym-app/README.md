# Gym CRM Application

Welcome to the **Gym CRM Application**! This is a Spring-based module that manages a Gym Customer Relationship
Management (CRM) system. It handles profiles for Trainees, Trainers, and Trainings.

## Features

**Trainee Management**: Create, update, delete, and select Trainee profiles.

- **Trainer Management**: Create, update, and select Trainer profiles.
- **Training Management**: Create and select Training profiles.
- **User Management**: Generate usernames and passwords for Trainees and Trainers.
- **In-Memory Storage**: Uses a common in-memory storage for all entities.
- **File-based Data Initialization**: Initializes storage with prepared data from external files during application
  startup.
- **Logging**: Comprehensive logging for all operations.
- **Unit Tests**: Fully covered with unit tests following best practices.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.6 or later

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/YourUsername/gym-crm-app.git
   cd gym-crm-app
    ```
2. **Build the application**:
    ```bash
    mvn clean install
    ```
3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

### Configuration

- The application uses **Spring framework** for dependency injection and configuration.
- It supports Java-based configuration with annotation-based wiring.
- Configuration properties are defined in the application.properties file located in the `src/main/resources` directory

### File-based Data Initialization

During the application startup, the data is loaded from JSON files into in-memory storage. The files are located in
the `src/main/resources/init_data` directory:

- trainee_data.json
- trainer_data.json
- training_data.json

These files can be customized as needed.

## Usage

After running the application, use the CLI to interact with the Gym CRM system:

- Trainee Management:
    - **Create**: Enter the details of a new trainee.
    - **Update**: Modify the details of an existing trainee.
    - **Delete**: Remove a trainee from the system.
    - **Select**: View the details of trainees.
- Trainer Management:
  - **Create**: Enter the details of a new trainer.
  - **Update**: Modify the details of an existing trainer.
  - **Select**: View the details of trainers.
- Training Management:
  - **Create**: Schedule a new training session.
  - **Select**: View the details of training sessions.

## Design and Implementation

The project adheres to the following principles:

- **SOLID Principles**: Ensures the code is easy to maintain, extend, and test.
- **KISS (Keep It Simple, Stupid)**: Avoids unnecessary complexity.
- **DRY (Don’t Repeat Yourself)**: Reuses code wherever possible to minimize redundancy.

### Key Components

- **Service Layer**: Provides business logic for managing trainees, trainers, and trainings.
- **DAO Layer**: Handles CRUD operations for each entity using in-memory storage.
- **Facade Layer**: Acts as an intermediary between the CLI and service layers.
- **Utility Classes**: Provides helper methods for generating usernames and passwords.

## Testing

The project includes unit tests that adhere to the F.I.R.S.T. principles:

- **Fast**: Tests should run quickly.
- **Independent**: Tests should not depend on each other.
- **Repeatable**: Tests should produce the same result every time.
- **Self-Validating**: Tests should have a boolean output.
- **Timely**: Tests should be written before the code.


To run the tests, use:

```bash
mvn test
```

## Logging

The application uses **SLF4J** with **Logback** for logging. Logs are configured to provide detailed information about
application flow, errors, and other important events. The configuration is in `src/main/resources/logback.xml`.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. **Fork** the repository.
2. **Create** a new branch.
3. **Commit** your changes.
4. **Push** to the branch.
5. Submit a **pull request**.

## License

This project is licensed under the MIT License - see the [LICENSE]() file for details.