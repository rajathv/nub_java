package com.rvg.utils;

import com.konylabs.middleware.controller.DataControllerRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

public class KobilHelperMethods {
    private static final Logger LOG = LogManager.getLogger(KobilHelperMethods.class);

    public static boolean isValidJsonObject(String jsonString) {
        LOG.error(" @@ jsonString" + jsonString);
        try {
            new JSONObject(jsonString);
            LOG.error(" @@ Error while validating json inside try:");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            LOG.error(" @@ Error while validating json inside catch:" + e.toString());
            return false;
        }
    }

    public static String getNextDay(String date) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new Exception("Invalid date format. Expected format: dd-MM-yyyy");
        }

        // Add one day
        localDate = localDate.plusDays(1);
        return localDate.format(formatter);
    }

    public static String GetUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
        try {
            if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
                Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
                if (userMap.get(attribute) != null) {
                    String attributeValue = userMap.get(attribute) + "";
                    return attributeValue;
                }
            }
        } catch (Exception var4) {
            LOG.error("The string has returned null in the getUserAttributeFromIdentity method " + var4.toString());
        }
        return "";
    }

    public static String convertDateForKobil(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy");
        return targetFormat.format(date);
    }

    public static String createPayload(Map<String, Object> payloadData) {
        StringJoiner payloadJoiner = new StringJoiner(",", "{", "}");

        for (Map.Entry<String, Object> entry : payloadData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                String nestedJson = createPayload((Map<String, Object>) value);
                payloadJoiner.add("\"" + key + "\":" + nestedJson);
            } else {
                payloadJoiner.add("\"" + key + "\":\"" + value.toString() + "\"");
            }
        }

        return payloadJoiner.toString();
    }
}
