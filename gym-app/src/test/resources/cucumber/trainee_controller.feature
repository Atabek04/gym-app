Feature: Trainee Management

  @create-trainee
  Scenario: Successfully create a new trainee
    Given a valid trainee request
    When the client sends a POST request to "/api/v1/trainees"
    Then the server responds with a 201 status
    And the trainee credentials are returned

  @create-trainee
  Scenario: Fail to create a trainee with invalid data
    Given an invalid trainee request
    When the client sends a POST request to "/api/v1/trainees"
    Then the server responds with a 400 status

  @get-trainee
  Scenario: Successfully get trainee by username
    Given an existing trainee username "Tony.Stark"
    When the client sends a GET request to "/api/v1/trainees/"
    Then the server responds with a 200 status
    And the trainee details are returned

  @get-trainee
  Scenario: Fail to get trainee with non-existing username
    Given a non-existing trainee username "Non.Existent"
    When the client sends a GET request to "/api/v1/trainees/"
    Then the server responds with a 404 status

  @update-trainee
  Scenario: Successfully update trainee details
    Given a valid trainee update request
    When the client sends a PUT request to update the trainee at "/api/v1/trainees/Bruce.Wayne"
    Then the server responds with a 200 status
    And the updated trainee details are returned

  @update-trainee
  Scenario: Fail to update trainee with invalid data
    Given an invalid trainee update request
    When the client sends a PUT request to update the trainee at "/api/v1/trainees/Bruce.Wayne"
    Then the server responds with a 400 status

  @update-trainee-trainers
  Scenario: Successfully update trainers for a trainee
    Given an existing trainee username "Bruce.Wayne"
    And a list of trainer usernames ["Diana.Prince", "Peter.Parker"]
    When the client sends a PUT request to update trainers at "/api/v1/trainees/Bruce.Wayne/trainers"
    Then the server responds with a 200 status
    And the updated list of trainers is returned

  @update-trainee-trainers
  Scenario: Fail to update trainers with invalid data
    Given an existing trainee username "Bruce.Wayne"
    And an invalid list of trainer usernames ["Non.Existent"]
    When the client sends a PUT request to update trainers at "/api/v1/trainees/Bruce.Wayne/trainers"
    Then the server responds with a 400 status

  @get-not-assigned-trainers
  Scenario: Get not assigned trainers for a trainee
    Given an existing trainee username "Tony.Stark"
    When the client sends a GET request to get not assigned trainers at "/api/v1/trainees/Tony.Stark/trainers"
    Then the server responds with a 200 status
    And the list of not assigned trainers is returned

  @get-trainee-trainings
  Scenario: Get trainee trainings with no filters
    Given an existing trainee username "Bruce.Wayne"
    When the client sends a GET request to get trainee trainings at "/api/v1/trainees/Bruce.Wayne/trainings"
    Then the server responds with a 200 status
    And all trainee trainings are returned

  @delete-trainee
  Scenario: Successfully delete a trainee
    Given an existing trainee username for deletion "Bruce.Wayne"
    When the client sends a DELETE request to "/api/v1/trainees/Bruce.Wayne"
    Then the server responds with a 204 status
    And the trainee is removed from the system

  @delete-trainee
  Scenario: Fail to delete a non-existing trainee
    Given a non-existing trainee username "Non.Existent"
    When the client sends a DELETE request to "/api/v1/trainees/Non.Existent"
    Then the server responds with a 404 status