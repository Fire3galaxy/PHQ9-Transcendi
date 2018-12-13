package morningsignout.phq9transcendi.HelperClasses;

import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

// Test cases do not rely on Context class to get excel sheet from Assets folder.
// Instead, they use a local dummy version in the root of the project.
class QuestionDataTest {
    @org.junit.jupiter.api.Test
    void ensureDummySpreadsheetIsUsed() {
        QuestionData questionData = new QuestionData();

        assertEquals(questionData.getQuestionText("DUMMY"), "DUMMY QUESTION");
    }

    @org.junit.jupiter.api.Test
    void loadQuestionNames_countIsNonZero() {
        QuestionData questionData = new QuestionData();

        assertNotEquals(0, questionData.size(),
                "Number of questions in spreadsheet should never be 0");
    }

    @org.junit.jupiter.params.ParameterizedTest
    @CsvSource(
            {"anhedoniainterest, Example Question 1"
            })
    void loadQuestionNames_containsCorrectStrings(String questionName, String actualQuestionText) {
        QuestionData questionData = new QuestionData();

        assertEquals(actualQuestionText, questionData.getQuestionText(questionName));
    }
}