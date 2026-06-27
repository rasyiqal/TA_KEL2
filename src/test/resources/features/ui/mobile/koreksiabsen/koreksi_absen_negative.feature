@mobile @koreksiabsen @negative
Feature: Mobile Koreksi Absen Negative — HADIR

  Background:
    Given user is logged in and on the koreksi absen page

  @regression @koreksiNegative
  Scenario: Koreksi submission fails when no fields are filled
    When user submits a koreksi without filling any fields
    Then koreksi submission fails with error message "Salah satu harus diisi!"
    And the submission form remains open

  @regression @koreksiNegative
  Scenario: Reset clears all koreksi form fields
    When user fills and resets the koreksi form
    Then all koreksi form fields are empty

  @regression @koreksiNegative
  Scenario: Kembali button navigates away from koreksi absen page
    When user navigates back from the koreksi absen page
    Then user is no longer on the koreksi absen page
