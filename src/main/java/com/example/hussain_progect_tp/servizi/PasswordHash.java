package com.example.hussain_progect_tp.servizi;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public static boolean controllo_pass(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
