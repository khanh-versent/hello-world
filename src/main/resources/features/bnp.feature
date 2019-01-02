Feature: BNP system
  Scenario Outline: BNP check for nugget file and create F46 CSV file
    Given DMP reads nugget file from "brs" and forwards processed nugget file to "bnp" for BNP to read nugget data. BNP save matching trade data into "f46csv"
    When  DMP forwards a nugget file of <Trade amount> trades
    Then  BNP creates <CSV file amount> F46 CSV file in "f46csv"
    And   DMP sees same <Trade amount> trades from that new F46 CSV file

  Examples:
    | Trade amount  | CSV file amount |
    | 50            | 1               |
    | 60            | 1               |
    | 10            | 1               |