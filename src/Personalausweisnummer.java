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
    private static final int DATE_LENGTH = 6;
    private static final int LINE2_ABLDAT_BEGIN = DATE_LENGTH + 2;
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
        serial = extractSerial();
        gebDat = extractGebDat();
        ablDat = extractAblDat();
    }

    public Personalausweisnummer(String[] lines) {
        if (lines.length != 3) throw new PersoFormatException();
        new Personalausweisnummer(lines[0], lines[1], lines[2]);
    }

    private String extractSerial() {
        String serialString = line1.substring(LINE1_BEGIN_LENGTH, LINE1_BEGIN_LENGTH + LINE1_SERIAL_LENGTH);
        ArrayList<String> serialValidChars = new ArrayList<>(Arrays.asList("C", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "T", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));

        // process serial string for checks
        String[] serialCharsArray = serialString.split("");
        ArrayList<String> serialCharsList = new ArrayList<>(Arrays.asList(serialCharsArray));

        if (!serialValidChars.containsAll(serialCharsList)) {
            throw new PersoCharacterException();
        }

        return serialString;
    }

    public String getSerial() {
        return serial;
    }

    private LocalDate extractGebDat() {
        String gebDatAsString = line2.substring(0, DATE_LENGTH);

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

    public LocalDate getGebDat() {
        return gebDat;
    }

    private LocalDate extractAblDat() {
        String gebAblAsString = line2.substring(LINE2_ABLDAT_BEGIN, LINE2_ABLDAT_BEGIN + DATE_LENGTH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd", Locale.GERMAN);
        LocalDate ablDat = LocalDate.parse(gebAblAsString, formatter);

        return ablDat;
    }

    public LocalDate getAblDat() {
        return ablDat;
    }
}
