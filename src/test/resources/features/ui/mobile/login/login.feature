@mobile @login
Feature: Mobile Login — HADIR

  Background:
    Given user is on the mobile login page

  @smoke @loginUserSuccess
  Scenario: Mobile login successful with valid credentials
    When user logs in with valid mobile credentials
    Then user is redirected to the mobile home page

  @smoke
  Scenario: Forgot Password button is visible on mobile login page
    Then the Forgot Password button is visible

