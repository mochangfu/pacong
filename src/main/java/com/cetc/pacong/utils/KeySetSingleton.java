package com.cetc.pacong.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeySetSingleton {
    private static KeySetSingleton keySetSingleton = null;

    private Set<String> itemKey;
    private Set<String> itemUrlKey;

    private Map<String, String> urlDateMapOld;
    private Map<String, String> urlKeyMapNew;
    private Map<String, String> urlDateMapNew;

    private Map<String, String> lunwenKeyMap;


    private KeySetSingleton() {

    }

    public static KeySetSingleton getInstance() {
        if (keySetSingleton == null) {
            keySetSingleton = new KeySetSingleton();
            keySetSingleton.urlDateMapOld = new HashMap<>();
            keySetSingleton.urlKeyMapNew = new HashMap<>();
            keySetSingleton.urlDateMapNew = new HashMap<>();
            keySetSingleton.lunwenKeyMap = new HashMap<>();

        }
        return keySetSingleton;
    }

    public void clearBaikeItemKey() {

        keySetSingleton.urlKeyMapNew = new HashMap<>();
        keySetSingleton.urlDateMapNew = new HashMap<>();
    }

    public Map<String, String> getUrlDateMapOld() {
        return urlDateMapOld;
    }

    public void setUrlDateMapOld(Map<String, String> urlDateMapOld) {
        this.urlDateMapOld = urlDateMapOld;
    }

    public Map<String, String> getUrlKeyMapNew() {
        return urlKeyMapNew;
    }

    public void setUrlKeyMapNew(Map<String, String> urlKeyMapNew) {
        this.urlKeyMapNew = urlKeyMapNew;
    }

    public Map<String, String> getUrlDateMapNew() {
        return urlDateMapNew;
    }

    public void setUrlDateMapNew(Map<String, String> urlDateMapNew) {
        this.urlDateMapNew = urlDateMapNew;
    }

    public Map<String, String> getLunwenKeyMap() {
        return lunwenKeyMap;
    }

    public void setLunwenKeyMap(Map<String, String> lunwenKeyMap) {
        this.lunwenKeyMap = lunwenKeyMap;
    }
}