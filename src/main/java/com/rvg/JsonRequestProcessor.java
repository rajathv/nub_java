package com.rvg;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class JsonRequestProcessor {

    public static void main(String[] args) {

        String jsonString = "{\n" +
                "  \"opstatus\": 0,\n" +
                "  \"header\": {\n" +
                "    \"idmsg\": \"000002032023\",\n" +
                "    \"mac\": \"20BD5FDFEC5DCC96\"\n" +
                "  },\n" +
                "  \"body\": {\n" +
                "    \"status\": {\n" +
                "      \"errordesc\": \"SUCCESS\",\n" +
                "      \"errorcode\": \"000\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"httpStatusCode\": 200\n" +
                "}";

        // Parse the JSON string
        JSONObject jsonObject = new JSONObject(jsonString);

        // Extract values using JSONObject methods
        int opstatus = jsonObject.getInt("opstatus");

        JSONObject header = jsonObject.getJSONObject("header");
        String idmsg = header.getString("idmsg");
        String mac = header.getString("mac");

        JSONObject body = jsonObject.getJSONObject("body");
        JSONObject status = body.getJSONObject("status");
        String errordesc = status.getString("errordesc");
        String errorcode = status.getString("errorcode");

        int httpStatusCode = jsonObject.getInt("httpStatusCode");

        // Print the extracted values
        System.out.println("opstatus: " + opstatus);
        System.out.println("idmsg: " + idmsg);
        System.out.println("mac: " + mac);
        System.out.println("errordesc: " + errordesc);
        System.out.println("errorcode: " + errorcode);
        System.out.println("httpStatusCode: " + httpStatusCode);

    }
}
