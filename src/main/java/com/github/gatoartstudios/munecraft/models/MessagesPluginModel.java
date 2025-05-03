package com.github.gatoartstudios.munecraft.models;

public class MessagesPluginModel {
    public static String HELLO;
    public static String GOODBYE;
    public static String WELCOME;
    public static String PLAYER_JOIN;
    public static String FAREWELL;
    public static String ERROR;
    public static String SUCCESS;
    public static String INFO;
    public static String WARNING;
    public static String DEBUG;

    public MessagesPluginModel() {
    }

    public MessagesPluginModel(String HELLO, String GOODBYE, String WELCOME, String PLAYER_JOIN, String FAREWELL, String ERROR, String SUCCESS, String INFO, String WARNING, String DEBUG) {
        MessagesPluginModel.HELLO = HELLO;
        MessagesPluginModel.GOODBYE = GOODBYE;
        MessagesPluginModel.WELCOME = WELCOME;
        MessagesPluginModel.PLAYER_JOIN = PLAYER_JOIN;
        MessagesPluginModel.FAREWELL = FAREWELL;
        MessagesPluginModel.ERROR = ERROR;
        MessagesPluginModel.SUCCESS = SUCCESS;
        MessagesPluginModel.INFO = INFO;
        MessagesPluginModel.WARNING = WARNING;
        MessagesPluginModel.DEBUG = DEBUG;
    }

    public static String getHELLO() {
        return HELLO;
    }

    public static void setHELLO(String HELLO) {
        MessagesPluginModel.HELLO = HELLO;
    }

    public static String getGOODBYE() {
        return GOODBYE;
    }

    public static void setGOODBYE(String GOODBYE) {
        MessagesPluginModel.GOODBYE = GOODBYE;
    }

    public static String getWELCOME() {
        return WELCOME;
    }

    public static void setWELCOME(String WELCOME) {
        MessagesPluginModel.WELCOME = WELCOME;
    }

    public static String getPlayerJoin() {
        return PLAYER_JOIN;
    }

    public static void setPlayerJoin(String playerJoin) {
        PLAYER_JOIN = playerJoin;
    }

    public static String getFAREWELL() {
        return FAREWELL;
    }

    public static void setFAREWELL(String FAREWELL) {
        MessagesPluginModel.FAREWELL = FAREWELL;
    }

    public static String getERROR() {
        return ERROR;
    }

    public static void setERROR(String ERROR) {
        MessagesPluginModel.ERROR = ERROR;
    }

    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static void setSUCCESS(String SUCCESS) {
        MessagesPluginModel.SUCCESS = SUCCESS;
    }

    public static String getINFO() {
        return INFO;
    }

    public static void setINFO(String INFO) {
        MessagesPluginModel.INFO = INFO;
    }

    public static String getWARNING() {
        return WARNING;
    }

    public static void setWARNING(String WARNING) {
        MessagesPluginModel.WARNING = WARNING;
    }

    public static String getDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(String DEBUG) {
        MessagesPluginModel.DEBUG = DEBUG;
    }
}
