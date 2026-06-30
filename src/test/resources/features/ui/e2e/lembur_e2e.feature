@e2e @lembur @lemburE2E
Feature: E2E Lembur Submission and Admin Validation Flow


  @regression @e2eLemburan
  Scenario: Mobile user submits lembur and admin validates it appears in the report list
    Given a regular user submits a new lembur request on mobile
    When admin navigates to the lembur report page
    Then the lembur submission appears in the report list with status "Menunggu Approval V1"

  @regression
  Scenario: Lembur submission is visible as PENDING immediately after mobile submission
    Given a regular user submits a new lembur request on mobile
    Then the lembur submission appears in the list as PENDING before approval
