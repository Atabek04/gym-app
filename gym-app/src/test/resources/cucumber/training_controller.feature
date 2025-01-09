Feature: TrainingController API

  @create-training
  Scenario: Successfully create a new training
    Given a valid training request
    When the client sends a POST request to create training at "/api/v1/trainings"
    Then the server responds with a 201 status
    And the training is successfully created

  @create-training
  Scenario: Fail to create a training with invalid request
    Given an invalid training request
    When the client sends a POST request to create training at "/api/v1/trainings"
    Then the server responds with a 400 status

  @list-training-types
  Scenario: Successfully list all training types
    Given the system contains predefined training types
    When the client sends a GET request to get training types at "/api/v1/trainings"
    Then the server responds with a 200 status
    And the response contains the list of training types