package com.rvg.utils;

import com.konylabs.middleware.api.processor.SessionHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

import java.net.HttpCookie;
import java.util.List;

public enum SessionManager {
    APPLICATION_ID("ApplicationId"),
    REQUEST_KEY("RequestKey"),
    JSESSION_ID("JSessionId"),
    SAVE_CHALLENGE_ANSWER("SaveChallengeAnswer"),
    ACCOUNT_IDS("AccountIds"),
    PHONE_NUMBER("PhoneNumber"),
    TEMPORARY_KEY("Temporary_Key"),
    JA_ACCESS_TOKEN("Ja_Access_Token"),
    CONTACT_DETAILS("ContactDetails"),
    FORM_CODE("FormCode"),
    CO_PARTY_ID("CoAppPartyId"),
    FORM_TRANSACTION_ID("formTransactionId"),
    FORM_SESSION_ID("formSessionId"),
    BUNDLE_NAME("Bundle_Name"),
    DIGITAL_PROFILE_IDS("DigitalProfileIds"),
    PRODUCT_GROUP_SELECTED("ProductGroupSelected"),
    FAILURE_COUNT("Failure_Count"),

    COOKIE("cookie"),
    URL("actionURL"),
    INPUT("jsonString");

    private String key;

    SessionManager(String key) {
        this.key = key;
    }

    public void insertIntoSession(String value, DataControllerRequest request) {
        request.getSession(true).setAttribute(this.key, value);
    }

    public String retreiveFromSession(DataControllerRequest request) {
        Object value = request.getSession().getAttribute(this.key);
        return (value != null) ? value.toString() : request.getParameter(this.key);
    }

    public String retreiveFromSession(FabricRequestManager request) {
        SessionHandler session = request.getSessionHandler();
        Object value = session.getAttribute(this.key);
        return (value != null) ? value.toString() : null;
    }

    public void removeFromSession(DataControllerRequest request) {
        request.getSession().removeAttribute(this.key);
    }

    public void insertIntoSessionCookie(List<HttpCookie> value, DataControllerRequest request) {
        // TODO Auto-generated method stub
        request.getSession(true).setAttribute(this.key, value);
    }

    @SuppressWarnings("unchecked")
    public List<HttpCookie> retreiveCookieFromSession(DataControllerRequest request) {
        Object value = request.getSession().getAttribute(this.key);
        return (value != null) ? (List<HttpCookie>) value : null;
    }

}
