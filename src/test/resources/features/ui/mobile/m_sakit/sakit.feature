@mobile @sakit
Feature: Mobile Menu Sakit

  Background:
    When user logs in using valid mobile credentials
    Then user successfully navigates to mobile dashboard

  Scenario: Berhasil mengakses halaman Ajukan Sakit
    When user clicks menu Sakit
    And user clicks button Ajukan Sakit
    And user clicks Access Alarm icon

