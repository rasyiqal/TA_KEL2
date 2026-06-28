@mobile @cuti
Feature: Mobile Menu Cuti

  Background:
    When user logs in using valid mobile credentials
    Then user successfully navigates to mobile dashboard

  Scenario: Berhasil mengajukan cuti
    When user clicks menu Cuti
    And user clicks button Ajukan Cuti
    And user selects tipe cuti
    And user clicks Input Area Cuti
    And user selects today as cuti start date
    And user selects cuti end date 2 days from today
    And user clicks button Simpan Cuti
    And user inputs random catatan
    And user clicks button Ajukan form Cuti

  Scenario: Reset form cuti
    When user clicks menu Cuti
    And user clicks button Ajukan Cuti
    And user selects tipe cuti
    And user clicks Input Area Cuti
    And user selects today as cuti start date
    And user selects cuti end date 2 days from today
    And user clicks button Simpan Cuti
    And user inputs random catatan
    And user clicks button Reset Cuti
