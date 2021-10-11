package com.cetc.pacong.utils.message;

public enum MessageLanguage {
    CN("cn"),
    EN("en");

    private final String val;

    MessageLanguage(final String text) {
        val = text;
    }

    public static MessageLanguage create(String str) {
        for (MessageLanguage type : MessageLanguage.values()) {
            if (type.toString().equals(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("MessageLanguage " + str + " is invalid!");
    }

    @Override
    public String toString() {
        return val;
    }
}
