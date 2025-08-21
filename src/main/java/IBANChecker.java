import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Ein einfacher IBAN-Prüfer.
 * <p>
 * Implementiert die grundlegende IBAN-Prüfung:
 * <ul>
 *   <li>Länge je Land prüfen</li>
 *   <li>Erste 4 Zeichen nach hinten verschieben</li>
 *   <li>Buchstaben in Zahlen umwandeln</li>
 *   <li>Modulo-97-Prüfung</li>
 * </ul>
 */
public class IBANChecker {

    /** Zuordnung von Ländercode → erwartete IBAN-Länge. */
    private static final Map<String, Integer> chars = new HashMap<>();

    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    /**
     * Einstiegspunkt zum Testen des IBAN-Prüfers.
     *
     * @param args Kommandozeilen-Argumente (nicht genutzt)
     */
    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    /**
     * Validiert eine IBAN nach ISO-7064 (Modulo 97).
     *
     * @param iban IBAN-String
     * @return true, wenn die IBAN gültig ist, sonst false
     */
    public static boolean validate(String iban) {
        if (!checkLength(iban)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }

    /**
     * Führt die Modulo-97 Berechnung über die IBAN-Segmente aus.
     *
     * @param segments Liste der IBAN-Ziffern-Segmente
     * @return Rest der Division durch 97
     */
    private static int calculate(List<String> segments) {
        long n = 0;
        for (String segment : segments) {
            if (segment.length() == 9) {
                n = Long.parseLong(segment) % 97;
            } else {
                String s = n + segment;
                n = Long.parseLong(s) % 97;
            }
        }
        return (int) n;
    }

    /**
     * Prüft, ob die IBAN die richtige Länge für ihr Land hat.
     *
     * @param iban IBAN
     * @return true, wenn Länge stimmt und Land bekannt ist
     */
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) == iban.length();
    }

    /**
     * Wandelt Buchstaben in Zahlen um (A=10 ... Z=35).
     *
     * @param iban IBAN
     * @return IBAN nur aus Ziffern
     */
    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase();
        for (char c : upperIban.toCharArray()) {
            if (Character.isDigit(c)) {
                convertedIban.append(c);
            }
            if (Character.isLetter(c)) {
                convertedIban.append(c - 55);
            }
        }
        return convertedIban.toString();
    }

    /**
     * Teilt die IBAN in Segmente (erst 9 Zeichen, dann 7er-Blöcke, Rest).
     *
     * @param ibanDigits IBAN als reine Ziffernfolge
     * @return Liste der Segmente
     */
    private static List<String> createSegments(String ibanDigits) {
        List<String> segments = new ArrayList<>();
        String remaining = ibanDigits;

        if (remaining.length() <= 9) {
            segments.add(remaining);
            return segments;
        }

        segments.add(remaining.substring(0, 9));
        remaining = remaining.substring(9);

        while (remaining.length() >= 7) {
            segments.add(remaining.substring(0, 7));
            remaining = remaining.substring(7);
        }

        if (!remaining.isEmpty()) {
            segments.add(remaining);
        }

        return segments;
    }

    /**
     * Verschiebt die ersten 4 Zeichen ans Ende der IBAN.
     *
     * @param iban IBAN
     * @return verschobene IBAN
     */
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }
}