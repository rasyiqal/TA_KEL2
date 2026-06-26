@mobile @koreksiabsen
Feature: Mobile Koreksi Absen — HADIR

  Background:
    Given user is logged in and on the koreksi absen page

  @smoke
  Scenario: Koreksi absen page is successfully loaded
    Then koreksi absen page displays the correction list with total count

  @smoke
  Scenario: Successfully submit koreksi with only Jam masuk
    When user submits a koreksi with Jam masuk "26 06 2026, 08:00"
    Then the koreksi is successfully submitted
    And the latest koreksi item appears in the list

  @smoke
  Scenario: Successfully submit koreksi with only Jam keluar
    When user submits a koreksi with Jam keluar "26 06 2026, 17:00"
    Then the koreksi is successfully submitted

  @regression
  Scenario: Koreksi absen submission form appears when Ajukan Koreksi button is clicked
    When user opens the koreksi submission form
    Then koreksi absen submission form is displayed

  @regression
  Scenario: Successfully submit koreksi with Jam masuk and Jam keluar
    When user submits a koreksi with Jam masuk "25 06 2026, 08:00" and Jam keluar "25 06 2026, 17:00"
    Then the koreksi is successfully submitted
    And the latest koreksi item appears in the list

  @regression
  Scenario: Total koreksi increases by 1 after successful submission
    Given user notes the current total koreksi
    When user submits a koreksi with Jam masuk "24 06 2026, 09:00"
    Then the koreksi is successfully submitted
    And total koreksi increases by 1

  @regression
  Scenario: Koreksi list displays complete information for each item
    Then the koreksi list displays complete item information



  @regression
  Scenario Outline: Successfully submit koreksi with various jam combinations
    When user submits a koreksi with Jam masuk "<jam_masuk>" and Jam keluar "<jam_keluar>"
    Then the koreksi is successfully submitted

    Examples:
      | jam_masuk         | jam_keluar        |
      | 20 06 2026, 08:00 | 20 06 2026, 17:00 |
      | 19 06 2026, 09:30 |                   |
      |                   | 18 06 2026, 18:00 |
