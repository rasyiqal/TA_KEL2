@mobile @sakit
Feature: Mobile Menu Sakit

  Background:
    When user logs in using valid mobile credentials
    Then user successfully navigates to mobile dashboard

  Scenario: Berhasil mengakses halaman Ajukan Sakit
    When user clicks menu Sakit
    And user clicks button Ajukan Sakit
    And user clicks Input Area
    And user selects today as start date
    And user selects end date 2 days from today
    And user clicks button Simpan
