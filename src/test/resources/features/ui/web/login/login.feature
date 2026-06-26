@web @login
Feature: Web Login — HADIR

  Background:
    Given user is on the web login page

  @smoke
  Scenario: Login successful with valid credentials
    When user logs in with valid credentials
    Then user is redirected to the web dashboard

  @regression
  Scenario: Login fails with incorrect password
    When user attempts web login with email "hadirsqa1@gmail.com" and password "PasswordSalah123"
    Then login fails and an error message is displayed
    And user remains on the web login page

  @regression
  Scenario: Login fails with unregistered email
    When user attempts web login with email "tidakterdaftar@example.com" and password "SQA@Hadir12345"
    Then login fails and an error message is displayed
    And user remains on the web login page

  @regression
  Scenario: Login fails with empty email
    When user attempts web login with email "" and password "SQA@Hadir12345"
    Then user remains on the web login page

  @regression
  Scenario: Login fails with empty password
    When user attempts web login with email "hadirsqa1@gmail.com" and password ""
    Then user remains on the web login page

  @regression
  Scenario Outline: Login fails with various invalid credentials combinations
    When user attempts web login with email "<email>" and password "<password>"
    Then login fails and an error message is displayed
    And user remains on the web login page

    Examples:
      | email               | password       |
      | salah@example.com   | SQA@Hadir12345 |
      | hadirsqa1@gmail.com | passwordsalah  |
      | bukan-email         | SQA@Hadir12345 |
