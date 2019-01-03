Feature: SIM system
  Scenario: SIM generate the CSV from trades and put into a folder
    Given that SIM save F365 CSV File into "data/sim" folder
    When SIM has 10 trades
    Then SIM creates 1 CSV file of 10 trades in "data/sim" folder

