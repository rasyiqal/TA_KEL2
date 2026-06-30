@web @koreksiabsen @koreksiApproval @koreksiApprovalPositive
Feature: Web Admin Koreksi Absen Approval — Positive Scenarios

  Background:
    Given admin is on the koreksi approval page for "Hadir SQA Testing 1"

  @smoke
  Scenario: Koreksi approval page is successfully loaded
    Then the koreksi approval page is loaded for "Hadir SQA Testing 1"

  @smoke
  Scenario: Koreksi approval page shows PENDING items in the table
    Then the koreksi table shows PENDING items
    And the koreksi table shows 1 or more rows

  @smoke
  Scenario: Clicking PENDING badge opens the koreksi approval modal
    When admin clicks the PENDING koreksi badge for "Hadir SQA Testing 1"
    Then the koreksi approval modal is displayed

  @regression
  Scenario: Admin successfully approves a koreksi absen submission
    When admin approves the koreksi submission for "Hadir SQA Testing 1"
    Then the koreksi status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario: Admin approves koreksi using modal buttons step by step
    When admin clicks the PENDING koreksi badge for "Hadir SQA Testing 1"
    And admin approves the koreksi in the modal
    Then the koreksi status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario: Approved koreksi status persists after page refresh
    When admin approves the koreksi submission for "Hadir SQA Testing 1"
    And the koreksi approval page is refreshed
    Then the koreksi status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario Outline: Admin can approve koreksi for multiple employees
    When admin approves the koreksi submission for "<employee>"
    Then the koreksi status for "<employee>" changes to Approved

    Examples:
      | employee            |
      | Hadir SQA Testing 1 |
