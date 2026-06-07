package com.example.hussain_progect_tp.classes;

public class User {
    private int user_id;
    private String username;
    private String email;
    private String nome;
    private String cognome;
    private String google_id;
    public User(int user_id, String username, String email, String nome, String cognome, String google_id) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.google_id = google_id;

    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getGoogle_id() {
        return google_id;
    }
    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }


}
