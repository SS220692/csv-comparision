package hellocucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;

public class CSVComparisonSteps {

    private String file1Content;
    private String file2Content;

    @Given("I have a CSV file named {string} and another CSV file named {string}")
    public void i_have_one_csv_files_named(String fileName1, String fileName2) throws Exception {
        file1Content = readCSVFile(fileName1);
        file2Content = readCSVFile(fileName2);
        System.out.println("File 1 content:\n" + file1Content);
    }

    private String readCSVFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        String absolutePath = "src/test/resources/hellocucumber/csv/" + fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.println("Successfully read file: " + fileName);
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            System.err.println("File not found: " + fileName);
            throw e; // Rethrow the exception to notify the caller
        } catch (IOException e) {
            // Handle other I/O exceptions
            System.err.println("Error reading file: " + fileName);
            throw e; // Rethrow the exception to notify the caller
        }
        return content.toString();
    }
    

    @When("I compare the content of {string} with {string}")
    public void i_compare_the_content_of_with(String fileName1, String fileName2) throws Throwable {
        if (!file1Content.equals(file2Content)) {
            throw new AssertionError("File contents do not match");
        }
    }
    @Then("the files should match")
    public void the_files_should_match() throws Throwable {
        // Split the content of both files into lines
        String[] file1Lines = file1Content.split("\n");
        String[] file2Lines = file2Content.split("\n");

        // Check if the number of lines in both files match
        if (file1Lines.length != file2Lines.length) {
            throw new AssertionError("Number of lines in files do not match");
        }

        // Iterate over each line and compare them
        for (int i = 0; i < file1Lines.length; i++) {
            // Remove leading and trailing whitespaces before comparison
            String trimmedFile1Line = file1Lines[i].trim();
            String trimmedFile2Line = file2Lines[i].trim();

            // Compare the lines
            if (!trimmedFile1Line.equals(trimmedFile2Line)) {
                throw new AssertionError("File contents do not match at line " + (i + 1));
            }
        }
    }

    @When("I compare the content of {string} with {string} for column {string}")
    public void i_compare_the_content_of_with_for_column(String fileName1, String fileName2, String columnName) throws Throwable {
        // Update file content variables with the content of the specified files
        file1Content = readCSVFile(fileName1);
        file2Content = readCSVFile(fileName2);
    }

    private int getColumnIndex(String columnName) {
        String[] headers = file1Content.split("\n")[0].split(",");
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equals(columnName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column '" + columnName + "' not found in header");
    }

    @Then("the {string} column in both files should match")
    public void the_column_in_both_files_should_match$(String columnName) {
        int bonusColumnIndex = getColumnIndex(columnName);
        
        String[] file1Rows = file1Content.split("\n");
        String[] file2Rows = file2Content.split("\n");

        if (file1Rows.length != file2Rows.length) {
            throw new AssertionError("Number of rows in files do not match");
        }
        
        for (int i = 0; i < file1Rows.length; i++) {
            String[] file1Columns = file1Rows[i].split(",");
            String[] file2Columns = file2Rows[i].split(",");
            
            if (!file1Columns[bonusColumnIndex].equals(file2Columns[bonusColumnIndex])) {
                throw new AssertionError("Bonus % column values do not match at row " + (i + 1));
            }
        }
    }

}
