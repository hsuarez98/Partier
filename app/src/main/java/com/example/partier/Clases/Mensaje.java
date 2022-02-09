package com.example.partier.Clases;

public class Mensaje {
    private String chatId,mensaje,user,userCreator;

    public Mensaje(){}

    public Mensaje(String chatId, String mensaje, String user, String userCreator) {
        this.chatId = chatId;
        this.mensaje = mensaje;
        this.user = user;
        this.userCreator = userCreator;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }
}
