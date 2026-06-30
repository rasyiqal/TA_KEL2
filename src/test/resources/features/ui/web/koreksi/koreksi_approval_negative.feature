@web @koreksiabsen @koreksiApproval @koreksiApprovalNegative
Feature: Web Admin Koreksi Absen Approval — Negative Scenarios

  Background:
    Given admin is on the koreksi approval page for "Hadir SQA Testing 1"

  @regression
  Scenario: Admin rejects a koreksi absen submission with reason
    When admin rejects the koreksi submission for "Hadir SQA Testing 1" with notes "Data koreksi tidak valid"
    Then the koreksi status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario: Admin opens rejection modal and provides reject notes
    When admin clicks the PENDING koreksi badge for "Hadir SQA Testing 1"
    And admin rejects the koreksi in the modal with notes "Jam absen tidak sesuai"
    Then the koreksi status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario: Rejected koreksi item no longer shows as PENDING
    When admin rejects the koreksi submission for "Hadir SQA Testing 1" with notes "Tidak ada bukti pendukung"
    Then the koreksi status for "Hadir SQA Testing 1" changes to Rejected

  @regression
  Scenario Outline: Admin rejects koreksi with various rejection reasons
    When admin rejects the koreksi submission for "Hadir SQA Testing 1" with notes "<reject_notes>"
    Then the koreksi status for "Hadir SQA Testing 1" changes to Rejected

    Examples:
      | reject_notes                              |
      | Koreksi tidak sesuai jadwal kerja         |
      | Data absen sudah tercatat dengan benar    |
      | Permintaan duplikat dari hari sebelumnya  |
