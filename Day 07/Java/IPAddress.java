import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IPAddress {

    boolean tls;
    boolean ssl;
    private String address;
    private String unbracketed;

    IPAddress(String address) {

        this.address     = address;
        this.unbracketed = address.replaceAll("\\[[a-z]+\\]","_");
        this.tls         = false;
        this.ssl         = false;
        runChecks();

    }

    private void runChecks() {

        boolean inBracket=false;
        boolean outBracket=false;

        // Look for ABBA within the brackets:
        Pattern p = Pattern.compile("\\[([a-z]+)\\]");
        Matcher m = p.matcher(address);
        while (m.find()) {
            if (abbaCheck(m.group(1))) {
                inBracket = true;
            }
        }

        // Look for ABBA outside of brackets:
        outBracket = abbaCheck(unbracketed);

        // Valid if ABBA outside brackets AND NOT inside brackets
        this.tls = (outBracket && !inBracket);

        // SSL if ABA is found (unbracketed) AND BAB is found (bracketed)
        List<String> aba = getABA();
        int babCount = 0;
        if (aba.size() > 0) {
            String bab = "";
            for (int i=0;i<aba.size();i++) {
                String s = aba.get(i);
                char[] chars = s.toCharArray();
                bab += (String.valueOf(chars[1]) + String.valueOf(chars[0]) + String.valueOf(chars[1]));
                if (i+1<aba.size()) {bab += "|";}
            }
            String regex = "\\[[a-z]{0,}(" + bab + ")[a-z]{0,}\\]";
            Pattern pp = Pattern.compile(regex);
            Matcher mm = pp.matcher(address);
            while (mm.find()) {
                if (!mm.group(1).equals("")) {
                    babCount++;
                    this.ssl = true;
                }
            }
        }
        if (babCount > 0) {
            this.ssl = true;
        }

    }

    private List<String> getABA () {
        List<String> aba = new ArrayList<>();
        char[] chars = unbracketed.toCharArray();
        for (int i=0;i<chars.length-2; i++) {
            if (chars[i] == chars[i+2] && chars[i] != chars[i+1] && ((int) chars[i+1]) < 123 && ((int) chars[i+1]) > 96) {
                String s = String.valueOf(chars[i]) + String.valueOf(chars[i+1]) + String.valueOf(chars[i+2]);
                aba.add(s);
            }
        }
        return aba;
    }

    // Returns TRUE when ABBA is found
    private boolean abbaCheck(String line) {
        char[] chars = line.toCharArray();
        for (int i=0; i<chars.length-3; i++) {
            char[] firstPair  = new char[2];
            char[] secondPair = new char[2];
            firstPair[0] = chars[i];
            firstPair[1] = chars[i+1];
            secondPair[0] = chars[i+2];
            secondPair[1] = chars[i+3];
            if (firstPair[0] == secondPair[1] && firstPair[1] == secondPair[0] && firstPair[0] != firstPair[1]) {
                return true;
            }
        }
        return false;
    }
}

