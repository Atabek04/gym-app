Feature: TrainerWorkloadController API

  @create-trainer-workload
  Scenario: Successfully create trainer workload
    Given a valid trainer workload request
    When the client sends a POST request to "/report"
    Then the server responds with a 201 status
    And the trainer workload is successfully created

  @create-trainer-workload
  Scenario: Fail to create trainer workload with invalid request
    Given an invalid trainer workload request
    When the client sends a POST request to "/report"
    Then the server responds with a 400 status

  @get-trainer-workload
  Scenario: Successfully get trainer workload summary
    Given an existing trainer username "Peter.Parker"
    When the client sends a GET request to "/Peter.Parker"
    Then the server responds with a 200 status
    And the response contains the trainer's workload summary

  @get-trainer-workload
  Scenario: Fail to get workload for a non-existing trainer
    Given a non-existing trainer username "No.Trainer"
    When the client sends a GET request to "/No.Trainer"
    Then the server responds with a 404 status