@mobile @login
Feature: Mobile Login — HADIR

  Background:
    Given user opens mobile login page

  @smoke @loginUserSuccess
  Scenario: Mobile login successful with valid credentials
    When user enters valid mobile user credentials
    And user clicks Mobile Login button
    Then user successfully navigates to mobile dashboard
    And URL does not contain mobile login page

  @smoke
  Scenario: Forgot Password button is visible on mobile login page
    Then Forgot Password button is visible on mobile login page

  @regression
  Scenario: Mobile login fails with incorrect password
    When user enters mobile email "hadirsqa1@gmail.com" and password "PasswordSalah123"
    And user clicks Mobile Login button expecting failure
    Then mobile error message is displayed
    And user remains on mobile login page

  @regression
  Scenario: Mobile login fails with unregistered email
    When user enters mobile email "tidakterdaftar@example.com" and password "SQA@Hadir12345"
    And user clicks Mobile Login button expecting failure
    Then mobile error message is displayed
    And user remains on mobile login page

  @regression
  Scenario: Mobile login fails with empty email
    When user enters mobile email "" and password "SQA@Hadir12345"
    And user clicks Mobile Login button expecting failure
    Then user remains on mobile login page

  @regression
  Scenario: Mobile login fails with empty password
    When user enters mobile email "hadirsqa1@gmail.com" and password ""
    And user clicks Mobile Login button expecting failure
    Then user remains on mobile login page

  @regression
  Scenario Outline: Mobile login fails with various invalid credentials combinations
    When user enters mobile email "<email>" and password "<password>"
    And user clicks Mobile Login button expecting failure
    Then mobile error message is displayed
    And user remains on mobile login page

    Examples:
      | email               | password       |
      | salah@example.com   | SQA@Hadir12345 |
      | hadirsqa1@gmail.com | passwordsalah  |
      | bukan-email         | SQA@Hadir12345 |
