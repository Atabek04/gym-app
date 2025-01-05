Feature: AuthController API

  @create-user
  Scenario: Successfully create a new user
    Given a valid user payload
    When the client sends a POST request to "/api/v1/auth/users" with the user payload
    Then the server responds with a 201 status
    And the user is created in the system

  @create-user
  Scenario: Fail to create a user with invalid payload
    Given an invalid user payload
    When the client sends a POST request to "/api/v1/auth/users" with the user payload
    Then the server responds with a 400 status

  @delete-user
  Scenario: Successfully delete a user by username
    Given an existing username "James.Bond"
    When the client sends a DELETE request to "/api/v1/auth/users/James.Bond"
    Then the server responds with a 204 status
    And the user is removed from the system

  @delete-user
  Scenario: Fail to delete a user by non-existing username
    Given a non-existing username "Harry.Potter"
    When the client sends a DELETE request to "/api/v1/auth/users/Harry.Potter"
    Then the server responds with a 404 status

  @username-availability
  Scenario: Check when a username is taken
    Given a username "Bruce.Wayne"
    When the client sends a GET request to "/api/v1/auth/users/exists"
    Then the server responds with a 200 status
    And the response body indicates the username is_taken as true

  @username-availability
  Scenario: Check when a username is available
    Given a username "Super.Man"
    When the client sends a GET request to "/api/v1/auth/users/exists"
    Then the server responds with a 200 status
    And the response body indicates the username is_taken as false

  @login
  Scenario: Successfully log in with valid credentials
    Given valid credentials
    When the client sends a POST request to "/api/v1/auth/login" with the login payload
    Then the server responds with a 200 status
    And the response body contains a valid access token and refresh token

  @login
  Scenario: Fail to log in with invalid credentials
    Given invalid credentials
    When the client sends a POST request to "/api/v1/auth/login" with the login payload
    Then the server responds with a 401 status

  @refresh-token
  Scenario: Successfully refresh a token
    Given a valid refresh token
    When the client sends a POST request to "/api/v1/auth/refresh-token"
    Then the server responds with a 200 status
    And the response body contains a new access token and refresh token

  @refresh-token
  Scenario: Fail to refresh a token with invalid token
    Given an invalid refresh token
    When the client sends a POST request to "/api/v1/auth/refresh-token"
    Then the server responds with a 401 status

  @password-change
  Scenario: Successfully change password
    Given a valid password change request
    When the client sends a PUT request to "/api/v1/auth/password"
    Then the server responds with a 200 status
    And the password is updated successfully

  @password-change
  Scenario: Fail to change password with invalid request
    Given an invalid password change request
    When the client sends a PUT request to "/api/v1/auth/password"
    Then the server responds with a 401 status