@mobile @lembur
Feature: Mobile Lembur Negative — HADIR

  Background:
    Given user is logged in and on the lembur page

  @regression @lemburNegative
  Scenario: Lembur submission fails when jam masuk is empty
    When user submits lembur without jam masuk
    Then lembur submission fails with field error "Jam masuk harus di isi!"
    And the lembur submission form remains open

  @regression @lemburNegative
  Scenario: Lembur submission fails when jam keluar is empty
    When user submits lembur without jam keluar
    Then lembur submission fails with field error "Jam Keluar harus di isi!"
    And the lembur submission form remains open

  @regression @lemburNegative
  Scenario: Lembur submission fails when catatan is empty
    When user submits lembur without catatan
    Then lembur submission fails with field error "Masukan minimal 5 karakter"
    And the lembur submission form remains open

  @regression @lemburNegative
  Scenario: Lembur submission fails when catatan is shorter than 5 characters
    When user submits lembur with catatan shorter than 5 characters
    Then lembur submission fails with field error "Masukan minimal 5 karakter"
    And the lembur submission form remains open
