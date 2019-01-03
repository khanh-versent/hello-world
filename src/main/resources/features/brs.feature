Feature: BRS system
  Scenario: BRS read the CSV file and create nugget file with 5 trades
    Given that BRS read F365 CSV in folder "data/sim" and then save nugget file into folder "data/brs"
    When SIM upload csv of 5 trades
    Then BRS create 1 nugget file ends with ".tar.gz" in folder "data/brs"

  Scenario: BRS read the CSV file and create nugget file with 10 trades
    Given that BRS read F365 CSV in folder "data/sim" and then save nugget file into folder "data/brs"
    When SIM upload csv of 10 trades
    Then BRS create 1 nugget file ends with ".tar.gz" in folder "data/brs"