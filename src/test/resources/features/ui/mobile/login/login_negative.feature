@mobile @login
Feature: Mobile Login Negative — HADIR

  Background:
    Given user is on the mobile login page

  @regression @loginNegative
  Scenario: Mobile login fails with incorrect password
    When user attempts mobile login with email "hadirsqa1@gmail.com" and password "PasswordSalah123"
    Then mobile login fails and an error message is displayed
    And user remains on the mobile login page

  @regression @loginNegative
  Scenario: Mobile login fails with unregistered email
    When user attempts mobile login with email "tidakterdaftar@example.com" and password "SQA@Hadir12345"
    Then mobile login fails and an error message is displayed
    And user remains on the mobile login page

  @regression @loginNegative
  Scenario: Mobile login fails with empty email
    When user attempts mobile login with email "" and password "SQA@Hadir12345"
    Then user remains on the mobile login page

  @regression @loginNegative
  Scenario: Mobile login fails with empty password
    When user attempts mobile login with email "hadirsqa1@gmail.com" and password ""
    Then user remains on the mobile login page

  @regression @loginNegative
  Scenario Outline: Mobile login fails with various invalid credentials combinations
    When user attempts mobile login with email "<email>" and password "<password>"
    Then mobile login fails and an error message is displayed
    And user remains on the mobile login page

    Examples:
      | email               | password       |
      | salah@example.com   | SQA@Hadir12345 |
      | hadirsqa1@gmail.com | passwordsalah  |
      | bukan-email         | SQA@Hadir12345 |
