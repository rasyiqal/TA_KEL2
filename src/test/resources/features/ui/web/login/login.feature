@web @login
Feature: Web Login — HADIR

  Background:
    Given user opens web login page

  @smoke
  Scenario: Login successful with valid credentials
    When user enters valid credentials
    And user clicks Login button
    Then user successfully navigates to web dashboard
    And URL does not contain login page

  @regression
  Scenario: Login fails with incorrect password
    When user enters email "hadirsqa1@gmail.com" and password "PasswordSalah123"
    And user clicks Login button expecting failure
    Then error message is displayed
    And user remains on web login page

  @regression
  Scenario: Login fails with unregistered email
    When user enters email "tidakterdaftar@example.com" and password "SQA@Hadir12345"
    And user clicks Login button expecting failure
    Then error message is displayed
    And user remains on web login page

  @regression
  Scenario: Login fails with empty email
    When user enters email "" and password "SQA@Hadir12345"
    And user clicks Login button expecting failure
    Then user remains on web login page

  @regression
  Scenario: Login fails with empty password
    When user enters email "hadirsqa1@gmail.com" and password ""
    And user clicks Login button expecting failure
    Then user remains on web login page

  @regression
  Scenario Outline: Login fails with various invalid credentials combinations
    When user enters email "<email>" and password "<password>"
    And user clicks Login button expecting failure
    Then error message is displayed
    And user remains on web login page

    Examples:
      | email                    | password         |
      | salah@example.com        | SQA@Hadir12345   |
      | hadirsqa1@gmail.com      | passwordsalah    |
      | bukan-email              | SQA@Hadir12345   |
