import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PersonalausweisnummerTest {

    @Test
    public void testKonstruktor() {
        // a valid Example
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertNotNull(p);
    }

    @Test
    public void testGetSerial() {
        // a valid Example
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "0208127<2610313D<<<<<<<<<<<<<0";
        String line3 = "SCHMIDT<<KEVIN<<<<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertEquals("T22000129", p.getSerial());
    }

    @Test
    public void testGebDat() {
        // a valid Example
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "0208127<2610313D<<<<<<<<<<<<<0";
        String line3 = "SCHMIDT<<KEVIN<<<<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertEquals(LocalDate.of(2002, 8, 12), p.getGebDat());
    }

    @Test
    public void testAblDat() {
        // a valid Example
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "0208127<2610313D<<<<<<<<<<<<<0";
        String line3 = "SCHMIDT<<KEVIN<<<<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertEquals(LocalDate.of(2026, 10, 31), p.getAblDat());
    }




    @Test
    public void testKonstruktor2() {
        // a valid Example
        String[] lines = {"IDD<<T220001293<<<<<<<<<<<<<<<",
                "6408125<2010315D<<<<<<<<<<<<<4",
                "MUSTERMANN<<ERIKA<<<<<<<<<<<<<"};
        Personalausweisnummer p = new Personalausweisnummer(lines);
        assertNotNull(p);
    }

    @Test
    public void testFormat2() {
        String[] lines = {"IDD<<T220001293<<<<<<<<<<<<<<<",
                "6408125<2010315D<<<<<<<<<<<<<4"};
        try {
            Personalausweisnummer p = new Personalausweisnummer(lines);
            fail("PersoFormatException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoFormatException.class, e.getClass());
        }
    }

    @Test
    public void testFormat3() {
        String line1 = "IDD<<L7HOL22F18<<<<<<<<<<<<<<<";
        String line2 = "6402156<2010315D<<<<<<<<<<<<<0";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("Expected PersoCharacterException");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoCharacterException.class, e.getClass());
        }
    }
    @Test
    public void testChecksum1() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408123<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoChecksumException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoChecksumException.class, e.getClass());
        }
    }

    @Test
    public void testChecksum2() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010312D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoChecksumException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoChecksumException.class, e.getClass());
        }
    }

    @Test
    public void testChecksum3() {
        String line1 = "IDD<<T220001291<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoChecksumException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoChecksumException.class, e.getClass());
        }
    }

    @Test
    public void testChecksum4() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<3";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoChecksumException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoChecksumException.class, e.getClass());
        }
    }

    @Test
    public void testChecksum5() {
        String line1 = "IDD<<T22000129O<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<3";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoCharacterException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoCharacterException.class, e.getClass());
        }
    }

    @Test
    public void testChecksum6() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<O";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoCharacterException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoCharacterException.class, e.getClass());
        }
    }

    @Test
    public void testFormat1() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("Expected PersoFormatException");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoFormatException.class, e.getClass());
        }
    }

    @Test
    public void testGebDat2() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6408125<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertEquals(LocalDate.of(1964, 8, 12), p.getGebDat());
    }


    @Test
    public void testGebDat3() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "640B125<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoCharacterException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoCharacterException.class, e.getClass());
        }
    }

    @Test
    public void testGebDat4() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6413121<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoDateException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoDateException.class, e.getClass());
        }
    }
    @Test
    public void testGebDat4a() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6403326<2010315D<<<<<<<<<<<<<4";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoDateException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoDateException.class, e.getClass());
        }
    }

    @Test
    public void testGebDat5() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6404312<2010315D<<<<<<<<<<<<<8";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoDateException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoDateException.class, e.getClass());
        }
    }

    @Test
    public void testGebDat6() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6402293<2010315D<<<<<<<<<<<<<0";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
        assertEquals(LocalDate.of(1964, 2, 29), p.getGebDat());
    }

    @Test
    public void testGebDat7() {
        String line1 = "IDD<<T220001293<<<<<<<<<<<<<<<";
        String line2 = "6302290<2010315D<<<<<<<<<<<<<0";
        String line3 = "MUSTERMANN<<ERIKA<<<<<<<<<<<<<";
        try {
            Personalausweisnummer p = new Personalausweisnummer(line1, line2, line3);
            fail("PersoDateException expected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(PersoDateException.class, e.getClass());
        }
    }
}

