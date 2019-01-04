Feature: Web login

  Scenario Outline: Login to the system with a combination of username and password
    Given Web server runs at localhost and port 8081. User accesses login page at /login
    When  User enters <username> and <password> in login form
    Then  User receives login page response <message>

    Examples:
      | username | password | message               |
      | Khanh    | Khanh    | Successful            |
      | Khanh    | Khanh1   | Incorrect combination |
      | Khanh1   | Khanh1   | Non existed user      |