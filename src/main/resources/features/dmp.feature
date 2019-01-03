Feature: DMP system
  Scenario: DMP check new nugget file and read data from nugget file
    Given that SIM creates CSV file to "data/sim" folder, BRS use that CSV to create nugget to "data/brs" folder, DMP read nugget file and forward to "data/forwarded" and "data/archived" folder
    When SIM creates a CSV of 10 trades
    Then DMP sees 1 new file ends with ".tar.gz" containing those 10 trades in "data/brs", then send copy to "data/forwarded", and "data/archived" folders