package handlingPassword;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {
    
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        boolean match = BCrypt.checkpw(plainPassword, hashedPassword);
        System.out.println("🔍 Checking password:");
        System.out.println("🔹 Entered: " + plainPassword);
        System.out.println("🔹 Stored Hash (DB): " + hashedPassword);
        System.out.println("✅ Match: " + match);
        return match;
    }
}
