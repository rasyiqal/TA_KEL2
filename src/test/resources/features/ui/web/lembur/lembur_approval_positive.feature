@web @lembur @lemburApproval @lemburApprovalPositive
Feature: Web Admin Lembur Approval — Positive Scenarios

  Background:
    Given admin is on the lembur approval page for "Hadir SQA Testing 1"

  @smoke
  Scenario: Lembur approval page is successfully loaded
    Then the lembur approval page is loaded for "Hadir SQA Testing 1"

  @smoke
  Scenario: Lembur approval page shows PENDING items in the table
    Then the lembur table shows PENDING items
    And the lembur table shows 1 or more rows

  @smoke
  Scenario: Clicking PENDING badge opens the approval modal
    When admin clicks the PENDING badge for "Hadir SQA Testing 1"
    Then the approval modal is displayed

  @regression
  Scenario: Admin successfully approves a lembur submission
    When admin approves the lembur submission for "Hadir SQA Testing 1"
    Then the lembur status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario: Admin approves lembur using modal buttons step by step
    When admin clicks the PENDING badge for "Hadir SQA Testing 1"
    And admin clicks the Approve button in the modal
    Then the lembur status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario: Approved lembur status persists after page refresh
    When admin approves the lembur submission for "Hadir SQA Testing 1"
    And the lembur approval page is refreshed
    Then the lembur status for "Hadir SQA Testing 1" changes to Approved

  @regression
  Scenario Outline: Admin can approve lembur for multiple employees
    When admin approves the lembur submission for "<employee>"
    Then the lembur status for "<employee>" changes to Approved

    Examples:
      | employee            |
      | Hadir SQA Testing 1 |
