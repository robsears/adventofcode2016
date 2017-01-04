import java.math.BigInteger;
import java.security.MessageDigest;

public class Main {

    private static boolean debug = true;
    private static String input = "ffykfhsq";

    public static void main(String[] args) {

        String partOnePassword = "";
        String partTwoPassword = "........";

        int suffix = 0;
        String hash;

        while (!passwordsComplete(partOnePassword, partTwoPassword)) {
            hash = md5(input+suffix);
            if (hash.matches("^0{5}.*") && partOnePassword.length() < 8) {
                partOnePassword += String.valueOf(hash.charAt(5));
            }
            if (checkPartOne(hash)) {
                int pos = Integer.parseInt(String.valueOf(hash.charAt(5)));
                String c = String.valueOf(hash.charAt(6));
                partTwoPassword = updatePassword(partTwoPassword, pos, c);
            }

            // If debug is true, update every 3M trials
            if (debug && suffix % 3000000 == 0) {
                System.out.printf("Part 1 password: %s, Part 2 password: %s\n", partOnePassword, partTwoPassword);
            }
            suffix++;
        }

        System.out.printf("Part 1 answer: %s.\n", partOnePassword);
        System.out.printf("Part 2 answer: %s.\n", partTwoPassword);
    }


    private static String updatePassword(String password, int pos, String character) {
        String newString = "";
        char[] strChar = password.toCharArray();
        for (int i=0;i<strChar.length;i++) {
            String c = String.valueOf(strChar[i]);
            if (i == pos && c.equals(".")) {
                c = character;
            }
            newString += c;
        }
        return newString;
    }

    private static boolean passwordsComplete(String pass1, String pass2) {
        String[] passwords = {pass1, pass2};
        for (String pass : passwords) {
            for (char c : pass.toCharArray()) {
                if (c == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkPartOne(String hash) {
        int count = 5;
        String character = "0";
        char[] hashArray = hash.toCharArray();
        int matches = 0;
        for (int i=0; i<count; i++) {
            if (String.valueOf(hashArray[i]).equals(character)) {
                matches++;
            }
        }
        return (matches == count && hash.matches("^0{5}[0-7].*"));
    }

    private static String md5(String text) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(text.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            hash = bigInteger.toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
        } catch (Exception e) {
            // Sad trombone.
        }
        return hash;
    }
}

