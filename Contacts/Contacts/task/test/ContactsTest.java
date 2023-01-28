import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;
import java.util.function.Function;

class TestClue {

    Function<String, CheckResult> callback;

    TestClue(Function<String, CheckResult> callback) {
        this.callback = callback;
    }
}


public class ContactsTest extends StageTest<TestClue> {

    private TestCase<TestClue> testPhoneNumber(String num, boolean isCorrect, boolean giveHint) {
        return new TestCase<TestClue>()
            .setInput(
                "count\n" +
                    "add\n" +
                    "John1\n" +
                    "Smith2\n" +
                    num + "\n" +
                    "list\n" +
                    "exit")
            .setAttach(new TestClue(output -> {

                boolean containsNum = output.contains(num);
                boolean containsNoNumber = output.contains("[no number]");

                if (containsNum && containsNoNumber) {
                    return new CheckResult(false,
                        "On \'list\" action you seem to output both " +
                            "\"[no number]\" and an actual number - " +
                            "you should output only one of them."
                    );
                }

                if (!containsNum && !containsNoNumber) {
                    return new CheckResult(false,
                        "On \'list\" action you seem to output neither " +
                            "\"[no number]\" nor an actual number - " +
                            "you should output one of them."
                    );
                }


                if (isCorrect &&
                    !output.contains(num) &&
                    output.contains("[no number]")

                    ||

                    !isCorrect &&
                        output.contains(num) &&
                        !output.contains("[no number]")
                ) {
                    if (giveHint) {
                        if (isCorrect) {
                            return new CheckResult(false,
                                "Test contains a legal phone number \"" + num + "\", " +
                                    "you should print this number instead of \"[no number]\"");
                        } else {
                            return new CheckResult(false,
                                "Test contains an illegal phone number \"" + num + "\", " +
                                    "you should print \"[no number]\" instead of this number");
                        }
                    } else {
                        if (isCorrect) {
                            return new CheckResult(false,
                                "Test contains an legal phone number, " +
                                    "you should print this number instead of \"[no number]\"");
                        } else {
                            return new CheckResult(false,
                                "Test contains an illegal phone number, " +
                                    "you should print \"[no number]\" instead of this number");
                        }
                    }
                }
                return CheckResult.correct();
            }));
    }

    @Override
    public List<TestCase<TestClue>> generate() {
        return List.of (
            new TestCase<TestClue>()
                .setInput("exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("enter action")) {
                        return new CheckResult(false,
                            "No \"Enter action\" substring found in the output");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "count\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("0 records")) {
                        return new CheckResult(false,
                            "No \"0 records\" substring found in the output");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "edit\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("no records to edit")) {
                        return new CheckResult(false,
                            "No \"No records to edit\" substring found in the output");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "remove\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("no records to remove")) {
                        return new CheckResult(false,
                            "No \"No records to remove\" substring found in the output");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "John\n" +
                        "Smith\n" +
                        "123 456 789\n" +
                        "count\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (output.contains("0 records")) {
                        return new CheckResult(false,
                            "Can't add the person");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "John1\n" +
                        "Smith2\n" +
                        "123-456-78912\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. John1 Smith2, 123-456-78912") &&
                        !output.contains("1. John1 Smith2, [no number]")) {

                        return new CheckResult(false,
                            "No \"[id]. [name] [surname], [phone]\" part " +
                                "after \"list\" action");
                    }
                    return CheckResult.correct();
                })),

            testPhoneNumber("123", true, true),
            testPhoneNumber("123 abc", true, true),
            testPhoneNumber("123-ABC", true, true),
            testPhoneNumber("123 456 xyz", true, true),
            testPhoneNumber("123-456-XYZ", true, true),
            testPhoneNumber("123 456-789", true, true),
            testPhoneNumber("123-456 789", true, true),
            testPhoneNumber("123 45-up-89", true, true),

            testPhoneNumber("(123)", true, true),
            testPhoneNumber("(123) 456", true, true),
            testPhoneNumber("123-(456)", true, true),
            testPhoneNumber("123 (456) 789", true, true),
            testPhoneNumber("123-(456)-789", true, true),
            testPhoneNumber("(123) 456-789", true, true),
            testPhoneNumber("(123)-456 789", true, true),
            testPhoneNumber("123 (45)-67-89", true, true),
            testPhoneNumber("+(phone)", true, true),

            testPhoneNumber("123+456 78912", false, true),
            testPhoneNumber("(123)-456-(78912)", false, true),
            testPhoneNumber("9", true, true),
            testPhoneNumber("123 456 9", false, true),
            testPhoneNumber("123 9 9234", false, true),
            testPhoneNumber("123 4?5 678", false, true),
            testPhoneNumber("+(with space)", false, true),


            testPhoneNumber("193", true, false),
            testPhoneNumber("129 abf", true, false),
            testPhoneNumber("123-AFC", true, false),
            testPhoneNumber("154 456 xyz", true, false),
            testPhoneNumber("123-566-XYZ", true, false),
            testPhoneNumber("123 456-349", true, false),
            testPhoneNumber("134-456 789", true, false),
            testPhoneNumber("123 45-down-89", true, false),

            testPhoneNumber("(234)", true, false),
            testPhoneNumber("(123) 566", true, false),
            testPhoneNumber("873-(456)", true, false),
            testPhoneNumber("123 (786) 789", true, false),
            testPhoneNumber("163-(456)-789", true, false),
            testPhoneNumber("(123) 496-789", true, false),
            testPhoneNumber("(173)-456 789", true, false),
            testPhoneNumber("123 (95)-67-89", true, false),
            testPhoneNumber("+(another)", true, false),

            testPhoneNumber("132+456 78912", false, false),
            testPhoneNumber("(123)-456-(45912)", false, false),
            testPhoneNumber("8", true, false),
            testPhoneNumber("153 456 9", false, false),
            testPhoneNumber("823 9 9234", false, false),
            testPhoneNumber("123 4?5 654", false, false),
            testPhoneNumber("+(another space)", false, true),
            
            testPhoneNumber("+1 ()", false, false),
            testPhoneNumber("+1 11", true, false),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Jq Sr, 123") ||
                        !output.contains("2. Qw We, 234")) {
                        return new CheckResult(false,
                            "New persons should be added at the end of the list");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "edit\n" +
                        "2\n" +
                        "name\n" +
                        "Qe\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Jq Sr, 123") ||
                        !output.contains("2. Qe We, 234")) {
                        return new CheckResult(false,
                            "Something wrong with name editing");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "edit\n" +
                        "2\n" +
                        "surname\n" +
                        "QR\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Jq Sr, 123") ||
                        !output.contains("2. Qw QR, 234")) {
                        return new CheckResult(false,
                            "Something wrong with surname editing");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "edit\n" +
                        "2\n" +
                        "number\n" +
                        "+(123) (123)\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Jq Sr, 123") ||
                        !output.contains("2. Qw We, [no number]")) {
                        return new CheckResult(false,
                            "Something wrong with number editing");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "remove\n" +
                        "1\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Qw We, 234")) {
                        return new CheckResult(false,
                            "Something wrong with removing contacts");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "Jq\n" +
                        "Sr\n" +
                        "123\n" +
                        "add\n" +
                        "Qw\n" +
                        "We\n" +
                        "234\n" +
                        "remove\n" +
                        "2\n" +
                        "list\n" +
                        "exit")
                .setAttach(new TestClue(output -> {
                    if (!output.contains("1. Jq Sr, 123")) {
                        return new CheckResult(false,
                            "Something wrong with removing contacts");
                    }
                    return CheckResult.correct();
                }))
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        try {
            return clue.callback.apply(reply);
        }
        catch (Exception ex) {
            return new CheckResult(false, "Can't check the answer");
        }
    }
}
