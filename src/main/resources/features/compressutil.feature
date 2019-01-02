Feature: CompressUtil
  Scenario: Compress file and decompress .tar.gz file
    Given I have list of files and their contents with following data
    | File path           | Content |
    | data/file1.txt      | File1   |
    | data/file2.txt      | File2   |
    | data/file3.txt      | File3   |
    | data/sub1/file4.txt | File4   |
    | data/sub2/file5.txt | File5   |
    When  CompressUtil compress all file to "data/test.tar.gz" file
    Then  "data" folder has "test.tar.gz" file
    And   CompressUtil is able to decompress the same structure to "temp" folder
