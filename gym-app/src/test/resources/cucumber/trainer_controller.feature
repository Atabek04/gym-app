Feature: TrainerController API

  @create-trainer
  Scenario: Successfully create a new trainer
    Given a valid trainer request payload
    When the client sends a POST request to create trainer at "/trainers"
    Then the server responds with a 201 status
    And the trainer is created in the system

  @create-trainer
  Scenario: Fail to create a trainer with invalid payload
    Given an invalid trainer request payload
    When the client sends a POST request to create trainer at "/trainers"
    Then the server responds with a 400 status

  @get-trainer
  Scenario: Successfully retrieve a trainer by username
    Given an existing trainer with username "Peter.Parker"
    When the client sends a GET request to get trainer at "/trainers/"
    Then the server responds with a 200 status
    And the response contains the trainer's details and trainees

  @get-trainer
  Scenario: Fail to retrieve a non-existing trainer by username
    Given a non-existing trainer with username "No.Name"
    When the client sends a GET request to get trainer at "/trainers/"
    Then the server responds with a 404 status

  @update-trainer
  Scenario: Successfully update a trainer's details
    Given an existing trainer with username "Diana.Prince"
    And a valid trainer update request payload
    When the client sends a PUT request to update trainer at "/trainers/"
    Then the server responds with a 200 status
    And the trainer's details are updated in the system

  @update-trainer
  Scenario: Fail to update a trainer's details with invalid payload
    Given an existing trainer with username "Diana.Prince"
    And an invalid trainer update request payload
    When the client sends a PUT request to update trainer at "/trainers/"
    Then the server responds with a 400 status
    And no updates are made to the trainer in the system

  @update-trainer
  Scenario: Fail to update a non-existing trainer
    Given a non-existing trainer with username "No.Name"
    And a valid trainer update request payload
    When the client sends a PUT request to update trainer at "/trainers/"
    Then the server responds with a 404 status

  @delete-trainer
  Scenario: Successfully delete a trainer by username
    Given an existing trainer with username "Diana.Prince"
    When the client sends a DELETE request to "/api/v1/trainers/Diana.Prince"
    Then the server responds with a 204 status

  @delete-trainer
  Scenario: Fail to delete a non-existing trainer
    Given a non-existing trainer with username "No.Name"
    When the client sends a DELETE request to "/api/v1/trainers/No.Name"
    Then the server responds with a 404 status

  @get-trainings
  Scenario: Successfully retrieve trainer's trainings without filters
    Given an existing trainer with username "Peter.Parker"
    When the client sends a GET request to get trainer trainings "/trainers/Peter.Parker/trainings"
    Then the server responds with a 200 status
    And the response contains all trainings for the trainer
