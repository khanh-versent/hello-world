Feature: Web add trade

  Scenario Outline: Add trade to the system
    Given Web server runs at localhost and port 8082. User accesses add trade page at /addtrade
    When  User enters <trade>, <volume>, <price>, <type> and <note> in add trade form
    Then  User receives add trade response <response>

    Examples:
      | trade   | volume | price | type | note         | response       |
      | Trade 1 | 1      | 10000 | buy  | buy Trade 1  | Successful     |
      | Trade 2 | 1.0    | 100.7 | sell | sell Trade 2 | Successful     |
      |         | 2.0    | 150.7 | sell | sell         | Invalid trade  |
      | Trade 3 | 3.0    | 700.7 |      |              | Invalid type   |
      | Trade 2 | 0.0    | 700.7 | buy  |              | Invalid volume |
      | Trade 2 | 2.0    | 0.0   | buy  |              | Invalid price  |
      | Trade 2 | -2     | 20000 | buy  |              | Invalid volume |
      | Trade 2 | 2.0    | -2000 | buy  |              | Invalid price  |