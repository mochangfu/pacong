package com.cetc.pacong.utils.message;

import org.apache.commons.lang3.StringUtils;

public class ErrorMessage {

    public static final ErrorMessage UNDEFINED_MSG_TEMPLATE = new ErrorMessage(
            "000001", "{}",
            MessageLanguage.CN);
    public static final ErrorMessage SYSTEM_ERROR_TEMPLATE = new ErrorMessage(
            "000002", "系统错误({})，请联系系统管理员!",
            MessageLanguage.CN);
    private String id;
    /**
     * 错误描述信息，如Patient 'Steven Lee' isn't found.
     */
    private String content;
    /**
     * 错误描述信息所采用的语言
     */
    private String language;

    public ErrorMessage() {
        id = SYSTEM_ERROR_TEMPLATE.getId();
        language = MessageLanguage.CN.toString();
    }

    public ErrorMessage(String id, String content, MessageLanguage language) {
        this.id = id;
        this.content = content;
        this.language = language.toString();
    }

    public ErrorMessage(String content, MessageLanguage language) {
        id = SYSTEM_ERROR_TEMPLATE.getId();
        this.content = content;
        this.language = language.toString();
    }

    public String getId() {
        return id;
    }

    public ErrorMessage setId(String id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ErrorMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public ErrorMessage setLanguage(String language) {
        this.language = language;
        return this;
    }

    public ErrorMessage copy() {
        ErrorMessage def = new ErrorMessage();
        def.setId(getId());
        def.setContent(getContent());
        def.setLanguage(getLanguage());
        return def;
    }

    public String getKey() {
        // 为了兼容单语言的实现，如果未指定语言则key为id
        if (StringUtils.isEmpty(getLanguage())) {
            return String.valueOf(getId());
        }
        return getLanguage() + getId();
    }

    public String formatMessage() {
        StringBuilder sbr = new StringBuilder();
        if (!StringUtils.isEmpty(id)) {
            sbr.append("[").append(id).append("]");
        }
        return sbr.append(content).toString();
    }
}
