Feature: Xml Utility
  Scenario: Xml Utility serialize and de-serialize the data
    Given TradeDetails and TradeMetaData hold a list of Trade and system save xml data file into "data" folder
    When system has a list of 100 trades and save as "trade-details.xml" and "trade-metadata.xml"
    Then system reads back same identical list of 100 trades