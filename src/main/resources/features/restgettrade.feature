Feature: REST GetTrade

  Scenario Outline: RESTful API getting trades information
    Given RESTful server runs at localhost, port 8080
    And   System has <trade amount> trades that id is starting from <start id>
    When  API request to /trades/list?pageSize=<page size>&pageIndex=<page index>
    Then  RESTful server returns <receiving trade amount> trades

    Examples:
      | trade amount | start id | page size | page index | receiving trade amount |
      | 100          | 1        | 50        | 1          | 50                     |
      | 100          | 1        | 150       | 2          | 100                    |