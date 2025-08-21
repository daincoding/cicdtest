import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IBANCheckerTest {

    @Test
    @DisplayName("Valide IBAN: DE22790200760027913168")
    void validIban() {
        String iban = "DE22790200760027913168";
        assertTrue(IBANChecker.validate(iban),
                "Erwartet: gültig für " + iban);
    }

    @Test
    @DisplayName("Nicht-valide IBAN (falsche Prüfsumme): DE21790200760027913173")
    void invalidIbanChecksum() {
        String iban = "DE21790200760027913173";
        assertFalse(IBANChecker.validate(iban),
                "Erwartet: ungültig (Prüfsumme) für " + iban);
    }

    @Test
    @DisplayName("Falsche Länge: DE227902007600279131")
    void invalidIbanLength() {
        String iban = "DE227902007600279131"; // zu kurz (DE erwartet 22)
        assertFalse(IBANChecker.validate(iban),
                "Erwartet: ungültig (Länge) für " + iban);
    }

    @Test
    @DisplayName("Unbekannter Ländercode: XX22790200760027913168")
    void unknownCountryCode() {
        String iban = "XX22790200760027913168";
        assertFalse(IBANChecker.validate(iban),
                "Erwartet: ungültig (unbekanntes Land) für " + iban);
    }
}