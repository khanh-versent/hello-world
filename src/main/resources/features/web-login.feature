Feature: Web login

  Scenario Outline: RESTful API getting trades information
    Given Web server runs at localhost and port 8081
    And   User accesses /login page
    When  User enters <username> and <password>
    Then  User receives <message>

    Examples:
      | username | password | message               |
      | Khanh    | Khanh    | Successful            |
      | Khanh    | Khanh1   | Incorrect combination |
      | Khanh1   | Khanh1   | Non existed user      |