@mobile @lembur
Feature: Mobile Lembur — HADIR

  Background:
    Given user is logged in and on the lembur page

  @smoke @lemburPositive
  Scenario: Lembur page is successfully loaded
    Then lembur page displays the overtime list with total count

  @smoke @lemburPositive
  Scenario: Successfully submit lembur with valid data
    When user submits lembur with jam masuk "26 06 2026, 08:00", jam keluar "26 06 2026, 17:00", and catatan "Testing lembur valid"
    Then the lembur is successfully submitted
    And the latest lembur item appears in the list

  @smoke @lemburPositive
  Scenario: Ajukan Lembur form is displayed when button clicked
    When user opens the lembur submission form
    Then lembur submission form is displayed

  @regression @lemburPositive
  Scenario: Total lembur count increases by 1 after successful submission
    Given user notes the current total lembur
    When user submits lembur with jam masuk "25 06 2026, 09:00", jam keluar "25 06 2026, 21:00", and catatan "Pengerjaan proyek mendesak"
    Then the lembur is successfully submitted
    And total lembur increases by 1

  @regression @lemburPositive
  Scenario: Latest submitted lembur item appears in the overtime list
    When user submits lembur with jam masuk "24 06 2026, 08:00", jam keluar "24 06 2026, 20:00", and catatan "Lembur persiapan demo"
    Then the lembur is successfully submitted
    And the latest lembur item appears in the list

  @regression @lemburPositive
  Scenario: Lembur list displays complete information for each item
    Then the lembur list displays complete item information

  @regression @lemburPositive
  Scenario: Reset clears all lembur form fields
    When user fills and resets the lembur form
    Then all lembur form fields are empty

  @regression @lemburPositive
  Scenario: Kembali button navigates away from lembur page
    When user navigates back from the lembur page
    Then user is no longer on the lembur page

  @regression @lemburPositive
  Scenario Outline: Successfully submit lembur with various time combinations
    When user submits lembur with jam masuk "<jam_masuk>", jam keluar "<jam_keluar>", and catatan "<catatan>"
    Then the lembur is successfully submitted

    Examples:
      | jam_masuk         | jam_keluar        | catatan                   |
      | 20 06 2026, 08:00 | 20 06 2026, 22:00 | Testing automation lembur |
      | 19 06 2026, 09:00 | 19 06 2026, 21:00 | Finishing sprint tasks    |
      | 18 06 2026, 10:00 | 18 06 2026, 23:00 | Deployment preparation    |
