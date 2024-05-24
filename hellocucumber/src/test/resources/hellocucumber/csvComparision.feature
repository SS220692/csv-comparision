Feature: CSV Comparison

  Scenario: Compare two CSV files
    Given I have a CSV file named "EmployeeData.csv" and another CSV file named "EmployeeDataOne.csv"
    When I compare the content of "EmployeeData.csv" with "EmployeeDataOne.csv"
    Then the files should match
  
  Scenario Outline: Compare two CSV file Column Bonus %
    When I compare the content of "EmployeeData.csv" with "EmployeeDataOne.csv" for column "Bonus %"
    Then the "Bonus %" column in both files should match


  