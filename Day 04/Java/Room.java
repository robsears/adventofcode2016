import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Room {

    boolean real;
    boolean northPoleRoom;
    Integer sector;
    private HashMap<String, Integer> charCount = new HashMap<>();
    private String  encrypted;
    private String  checksum;
    private String  encryptedName;


    Room(String encrypted) {
        this.encrypted     = encrypted;
        this.encryptedName = getEncryptedName();
        this.checksum      = getChecksum();
        this.sector        = getSector();
        this.charCount     = getCharCounts();
        this.real          = getRealityCheck();
        this.northPoleRoom = isNorthPoleRoom();
    }

    private boolean isNorthPoleRoom() {
        String unencrypted = "";
        for (char c : encryptedName.toCharArray()) {
            if (c == '-') {
                unencrypted += ' ';
            }
            else {
                int mod = sector % 26;
                int cCode = (int) c + mod;
                if (cCode > 122) {
                    cCode = 97 + (cCode - 123);
                }
                char newC = (char) cCode;
                unencrypted += String.valueOf(newC);
            }
            if (unencrypted.equals("north")) {
                return true;
            }
        }
        return false;
    }

    private boolean getRealityCheck() {
        String thisChecksum = "";
        List<String> q = new ArrayList<>();
        for (String z : charCount.keySet()) {
            q.add(z);
        }
        Collections.sort(q);
        while(!q.isEmpty()) {
            Integer max = 0;
            String maxS = "";
            for (String s : q) {
                if (charCount.get(s) > max) {
                    max  = charCount.get(s);
                    maxS = s;
                }
            }
            thisChecksum += maxS;
            q.remove(maxS);
            if (thisChecksum.length() == 5) {
                break;
            }
        }
        String testChecksum = thisChecksum;
        return (testChecksum.equals(checksum));
    }

    private HashMap<String, Integer> getCharCounts() {
        String clean = encryptedName.replaceAll("[-_0-9]+","");
        for (char c : clean.toCharArray()) {
            String s = String.valueOf(c);
            Integer count = 0;
            if (charCount.containsKey(s)) {
                count = charCount.get(s);
            }
            count++;
            charCount.put(s, count);
        }
        return charCount;
    }

    private String getChecksum() throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(".*?\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(encrypted);
        if(matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException(
                String.format("Could not find a checksum for %s.", encrypted)
        );
    }

    private String getEncryptedName() {
        return encrypted.replaceAll("\\[(.*?)\\]","");
    }

    private Integer getSector() {
        String digits = encryptedName.replaceAll("^[-_a-z]+","");
        return Integer.parseInt(digits);
    }
}

