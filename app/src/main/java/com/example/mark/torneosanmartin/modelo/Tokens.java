package com.example.mark.torneosanmartin.modelo;

public class Tokens {

    String idToken;
    String version;

    public Tokens(String idToken, String version) {
        this.idToken = idToken;
        this.version = version;
    }

    public Tokens() {

    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String toString() {
        return "Tokens{" +
                "idToken='" + idToken + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
