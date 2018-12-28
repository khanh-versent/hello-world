Feature: BRS system
  Scenario: BRS read the CSV file and create nugget file
    Given that BRS read F365 CSV in folder "sim" and then save nugget file into folder "brs"
    When SIM upload csv of 5 trades
    Then BRS create 1 nugget file ends with ".tar.gz" in folder "brs"