@web @lembur @lemburApproval @lemburApprovalNegative
Feature: Web Admin Lembur Approval — Negative Scenarios

  Background:
    Given admin is on the lembur approval page for "Hadir SQA Testing 1"

  @regression
  Scenario: Admin rejects a lembur submission with reason
    When admin rejects the lembur submission for "Hadir SQA Testing 1" with notes "Data lembur tidak sesuai kebijakan perusahaan"
    Then the lembur status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario: Admin opens rejection modal and provides reject notes
    When admin clicks the PENDING badge for "Hadir SQA Testing 1"
    And admin clicks the Reject button in the modal
    And admin enters reject notes "Jam lembur tidak valid"
    Then the lembur status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario: Rejected lembur item no longer shows as PENDING
    When admin rejects the lembur submission for "Hadir SQA Testing 1" with notes "Tidak memenuhi syarat lembur"
    Then the lembur status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario Outline: Admin rejects lembur with various rejection reasons
    When admin rejects the lembur submission for "Hadir SQA Testing 1" with notes "<reject_notes>"
    Then the lembur status for "Hadir SQA Testing 1" changes to Rejected

    Examples:
      | reject_notes                              |
      | Lembur tidak disetujui atasan             |
      | Dokumen pendukung tidak lengkap           |
      | Jam lembur melebihi batas yang diizinkan  |
