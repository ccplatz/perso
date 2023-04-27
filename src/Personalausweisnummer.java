import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Personalausweisnummer {
    private static final String LINE1_BEGIN_PATTERN = "IDD<<";
    private static final int LINE1_BEGIN_LENGTH = LINE1_BEGIN_PATTERN.length();
    private static final int LINE1_SERIAL_LENGTH = 9;
    private static final int LINE1_CHECKSUM_START = LINE1_BEGIN_LENGTH + LINE1_SERIAL_LENGTH;
    private static final int DATE_LENGTH = 6;
    private static final int LINE2_ABLDAT_BEGIN = DATE_LENGTH + 2;
    private static final int LINE2_ABLDAT_CHECKSUM_START = LINE2_ABLDAT_BEGIN + DATE_LENGTH;
    private String line1;
    private String line2;
    private String line3;
    private String serial;
    private LocalDate gebDat;
    private LocalDate ablDat;

    public Personalausweisnummer(String line1, String line2, String line3) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;

        checkFormat(this.line1);
        checkFormat(this.line2);
        checkFormat(this.line3);
        serial = extractSerial();
        gebDat = extractGebDat();
        ablDat = extractAblDat();
        checkSuperChecksum(extractSuperChecksum(), extractSuperChecksumBase());
    }

    public Personalausweisnummer(String[] lines) {
        if (lines.length != 3) throw new PersoFormatException();
        new Personalausweisnummer(lines[0], lines[1], lines[2]);
    }

    private void checkFormat(String line) {
        if (line.length() != 30)
            throw new PersoFormatException();
    }

    private String extractSerial() {
        String serialString = line1.substring(LINE1_BEGIN_LENGTH, LINE1_BEGIN_LENGTH + LINE1_SERIAL_LENGTH);
        ArrayList<String> serialValidChars = new ArrayList<>(Arrays.asList("C", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "T", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        char checksumChar = line1.charAt(LINE1_CHECKSUM_START);

        if (!Character.isDigit(checksumChar))
            throw new PersoCharacterException();
        int checksum = Integer.parseInt(Character.toString(checksumChar));

        // process serial string for checks
        String[] serialCharsArray = serialString.split("");
        ArrayList<String> serialCharsList = new ArrayList<>(Arrays.asList(serialCharsArray));

        if (!serialValidChars.containsAll(serialCharsList))
            throw new PersoCharacterException();

        if (checksum != calculateChecksum(serialString))
            throw new PersoChecksumException();

        return serialString;
    }

    public String getSerial() {
        return serial;
    }

    private LocalDate extractGebDat() {
        String gebDatAsString = line2.substring(0, DATE_LENGTH);
        char checksumChar = line2.charAt(DATE_LENGTH);

        if (!Character.isDigit(checksumChar))
            throw new PersoCharacterException();
        int checksum = Integer.parseInt(Character.toString(checksumChar));

        String yearString = gebDatAsString.substring(0, 2);
        String monthString = gebDatAsString.substring(2, 4);
        String dayString = gebDatAsString.substring(4, 6);

        checkYear(yearString);
        checkMonth(monthString);

        int year = Integer.parseInt(yearString);
        year = year > 23 ? 1900 + year : 2000 + year;
        int month = Integer.parseInt(monthString);

        checkDay(dayString, month, isLeapYear(year));
        int day = Integer.parseInt(dayString);

        if (checksum != calculateChecksum(gebDatAsString))
            throw new PersoChecksumException();

        LocalDate gebDat = LocalDate.of(year, month, day);

        return gebDat;
    }

    private void checkYear(String yearString) {
        // check for wrong symbols
        char[] chars = yearString.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                throw new PersoCharacterException();
            }
        }
        int year = Integer.parseInt(yearString);
        if (year < 0 || year > 99)
            throw new PersoDateException();
    }

    private void checkMonth(String monthString) {
        // check for wrong symbols
        char[] chars = monthString.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                throw new PersoCharacterException();
            }
        }
        int month = Integer.parseInt(monthString);
        if (month < 1 || month > 12)
            throw new PersoDateException();
    }

    private void checkDay(String dayString, int month, boolean isLeapYear) {
        // check for wrong symbols
        char[] chars = dayString.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                throw new PersoCharacterException();
            }
        }

        int day = Integer.parseInt(dayString);
        if (day < 1 || day > 31)
            throw new PersoDateException();

        if (day > Month.of(month).length(isLeapYear))
            throw new PersoDateException();
    }

    private boolean isLeapYear(int year) {
        boolean leap = false;

        // if the year is divided by 4
        if (year % 4 == 0) {

            // if the year is century
            if (year % 100 == 0) {

                // if year is divided by 400
                // then it is a leap year
                if (year % 400 == 0)
                    leap = true;
                else
                    leap = false;
            }

            // if the year is not century
            else
                leap = true;
        } else
            leap = false;
        return leap;
    }

    private static int calculateChecksum(String base) {
        int checksum = 0;
        int sum = 0;
        int product = 0;
        char[] alphabetArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        ArrayList<String> alphabetList = new ArrayList<>();
        for (char character : alphabetArray) {
            alphabetList.add(String.valueOf(character));
        }

        int j = 1;
        for (int i = 0; i < base.length(); i++) {
            String currentValueAsString = String.valueOf(base.charAt(i));
            int currentValue;
            if (alphabetList.contains(currentValueAsString)) {
                currentValue = alphabetList.indexOf(currentValueAsString) + 10;
            } else {
                currentValue = Integer.parseInt(currentValueAsString);
            }

            int multiplicator = 0;
            switch (j) {
                case 1 -> {
                    multiplicator = 7;
                    j++;
                }
                case 2 -> {
                    multiplicator = 3;
                    j++;
                }
                case 3 -> {
                    multiplicator = 1;
                    j = 1;
                }
            }

            product = currentValue * multiplicator;

            sum += product;
        }
        checksum = sum % 10;

        return checksum;
    }

    public LocalDate getGebDat() {
        return gebDat;
    }

    private LocalDate extractAblDat() {
        String gebAblAsString = line2.substring(LINE2_ABLDAT_BEGIN, LINE2_ABLDAT_BEGIN + DATE_LENGTH);
        char checksumChar = line2.charAt(LINE2_ABLDAT_CHECKSUM_START);

        if (!Character.isDigit(checksumChar))
            throw new PersoCharacterException();
        int checksum = Integer.parseInt(Character.toString(checksumChar));

        if (checksum != calculateChecksum(gebAblAsString))
            throw new PersoChecksumException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd", Locale.GERMAN);
        LocalDate ablDat = LocalDate.parse(gebAblAsString, formatter);

        return ablDat;
    }

    public LocalDate getAblDat() {
        return ablDat;
    }

    private int extractSuperChecksum() {
        char checksumChar = line2.charAt(line2.length() - 1);

        if (!Character.isDigit(checksumChar))
            throw new PersoCharacterException();
        int checksum = Integer.parseInt(Character.toString(checksumChar));

        return checksum;
    }

    private String extractSuperChecksumBase() {
        String serialAndChecksumString = line1.substring(LINE1_BEGIN_LENGTH, LINE1_BEGIN_LENGTH + LINE1_SERIAL_LENGTH + 1);
        String gebDatAndChecksumString = line2.substring(0, DATE_LENGTH + 1);
        String ablDatAndChecksumString = line2.substring(LINE2_ABLDAT_BEGIN, LINE2_ABLDAT_BEGIN + DATE_LENGTH + 1);

        return serialAndChecksumString + gebDatAndChecksumString + ablDatAndChecksumString;
    }

    private void checkSuperChecksum(int superChecksum, String base) {
        if (superChecksum != calculateChecksum(base))
            throw new PersoChecksumException();
    }
}
