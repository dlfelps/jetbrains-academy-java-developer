import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.Arrays;
import java.util.List;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;

public class LearningProgressTrackerTest extends StageTest<String> {

    @DynamicTest
    CheckResult testStartAndExit() {
        TestedProgram main = new TestedProgram();

        String output = main.start().toLowerCase();

        expect(output).toContain(1).lines();
        if (incorrectString(output, "learning progress tracker")) {
            return CheckResult.wrong("When started, your program " +
                    "should print \"Learning Progress Tracker\"");
        }

        if (!main.isWaitingInput()) {
            return CheckResult.wrong("After the start, your program should " +
                    "be ready to accept commands from the user");
        }

        output = main.execute("exit");
        expect(output).toContain(1).lines();
        if (anyMissingKeywords(output, "bye")) {
            return CheckResult.wrong("When the 'exit' command is entered, " +
                    "your program should say bye to the user");
        }

        if (!main.isFinished()) {
            return CheckResult.wrong("After the 'exit' command has been entered, " +
                    "your program should stop working");
        }

        return CheckResult.correct();

    }

    @DynamicTest(data = "getBlankInput")
    CheckResult testBlankInput(String input) {
        TestedProgram main = new TestedProgram();
        main.start();

        String output = main.execute(input);
        expect(output).toContain(1).lines();
        if (incorrectString(output, "no input")) {
            return CheckResult.wrong("When the user enters an empty or blank " +
                    "string, your program should print \"no input\"");
        }

        return CheckResult.correct();
    }

    @DynamicTest(data = "getUnknownCommands")
    CheckResult testUnknownCommands(String input) {
        TestedProgram main = new TestedProgram();
        main.start();

        String output = main.execute(input);
        expect(output).toContain(1).lines();
        if (anyMissingKeywords(output, "unknown", "command")) {
            return CheckResult.wrong("When an unknown command is entered, your " +
                    "program should display an error message: \"Unknown command!\"");
        }

        return CheckResult.correct();
    }

    private boolean anyMissingKeywords(String output, String... keywords) {
        List<String> tokens = Arrays.asList(
                output.toLowerCase().split("\\W+")
        );

        return !tokens.containsAll(Arrays.asList(keywords.clone()));
    }

    private boolean incorrectString(String output, String model) {
        String normalizedOutput = output.replaceAll("\\W+", "").toLowerCase();
        String normalizedModel = model.replaceAll("\\W+", "").toLowerCase();

        return !normalizedOutput.contains(normalizedModel);
    }

    private String[] getBlankInput() {
        return new String[]{"", "  ", "\t", " \t"};
    }

    private String[] getUnknownCommands() {
        return new String[]{"abc", "quit", "  brexit ", "exi  t", "help", "break",
                "-help", "Ctrl+C", "exit please", ":q"};
    }
}
