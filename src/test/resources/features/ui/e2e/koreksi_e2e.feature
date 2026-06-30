@e2e @koreksiabsen @koreksiE2E
Feature: E2E Koreksi Absen Submission and Admin Approval Flow


  @regression @e2eKoreksiApprove
  Scenario: Mobile user submits koreksi and admin approves it
    Given a regular user submits a new koreksi absen request on mobile
    When admin approves the submitted koreksi for "Hadir SQA Testing 1"
    And the admin sees the koreksi approval success message
    Then the regular user verifies the koreksi request status changed to "Approved" on mobile

  @regression @e2eKoreksiReject
  Scenario: Mobile user submits koreksi and admin rejects it
    Given a regular user submits a new koreksi absen request on mobile
    When admin rejects the submitted koreksi for "Hadir SQA Testing 1" with reason "E2E automated rejection"
    And the admin sees the koreksi rejection success message
    Then the regular user verifies the koreksi request status changed to "Rejected" on mobile

