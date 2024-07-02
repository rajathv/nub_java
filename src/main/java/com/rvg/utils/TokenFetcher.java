package com.rvg.utils;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rvg.constants.OperationName;
import com.rvg.constants.ServiceId;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TokenFetcher {

    public static String fetchAccessToken(String module_name) {

        // Map<String, String> inputParamsFetchAccessToken =
        // FetchTokenConfigByModule(module_name);
        // String username = inputParamsFetchAccessToken.get("username");
        // String clientId = inputParamsFetchAccessToken.get("clientId");
        // String grantType = inputParamsFetchAccessToken.get("grantType");
        // String clientSecret = inputParamsFetchAccessToken.get("clientSecret");
        // String password = inputParamsFetchAccessToken.get("password");
        // {
        // "clientId": "direct-grant-with-secret",
        // "kobilPassword": "fpQWZDFf2U4D",
        // "scope": "openid",
        // "module": "CHANGE_PASSWORD",
        // "clientSecret": "A6hUhNr3utAJvQoySp5M0mXsDyht0qta",
        // "id": "4",
        // "grantType": "password",
        // "username": "api"
        // },

        String username = "api";
        String clientId = "RawBankChangePassword";
        String grantType = "password";
        String clientSecret = "A6hUhNr3utAJvQoySp5M0mXsDyht0qta";
        String password = "fpQWZDFf2U4D";

        String baseUrl = "https://idp.monaco-rawbank-jqky0.midentity.dev/auth/realms/RAWBank/";
        String endpointUrl = "protocol/openid-connect/token";

        // Construct the body data as key-value pairs
        String urlParameters = String.format(
                "username=%s&client_id=%s&grant_type=%s&client_secret=%s&password=%s",
                username, clientId, grantType, clientSecret, password);

        try {
            URL url = new URL(baseUrl + endpointUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Set request properties
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(urlParameters.getBytes().length));

            // To send a POST request, we need this to be true
            httpURLConnection.setDoOutput(true);

            // Write data to the connection
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                os.write(urlParameters.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close the connections
            in.close();
            httpURLConnection.disconnect();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            return jsonResponse.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, String> FetchTokenConfigByModule(String module_name) {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("module_name", module_name);
            String serviceName = ServiceId.RAWBANKKOBILDBSERVICES;
            String operationName = OperationName.DB_SP_FETCH_KOBIL_TOKEN_CONFIG_VALUE;
            String kobilTokenConfig = null;
            try {
                kobilTokenConfig = DBPServiceExecutorBuilder.builder()
                        .withServiceId(serviceName)
                        .withObjectId(null)
                        .withOperationId(operationName)
                        .withRequestParameters(inputMap)
                        .build().getResponse();
                return createInputParamsForFetchAccessToken(kobilTokenConfig);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> createInputParamsForFetchAccessToken(String kobilTokenConfig) {
        Map<String, String> inputParamsFetchAccessToken = new HashMap<>();

        try {
            JsonObject kobilTokenConfigObject = JsonParser.parseString(kobilTokenConfig).getAsJsonObject();

            int opStatus = kobilTokenConfigObject.get("opstatus").getAsInt();

            if (opStatus != 0) {
                throw new IllegalArgumentException("Invalid operation status or HTTP status code");
            }

            JsonArray recordsArray = kobilTokenConfigObject.getAsJsonArray("records");

            if (recordsArray == null || recordsArray.size() == 0) {
                throw new IllegalArgumentException("No records found");
            }

            JsonObject firstRecord = recordsArray.get(0).getAsJsonObject();

            inputParamsFetchAccessToken.put("username", getValueSafely(firstRecord, "username"));
            inputParamsFetchAccessToken.put("clientId", getValueSafely(firstRecord, "clientId"));
            inputParamsFetchAccessToken.put("grantType", getValueSafely(firstRecord, "grantType"));
            inputParamsFetchAccessToken.put("clientSecret", getValueSafely(firstRecord, "clientSecret"));
            inputParamsFetchAccessToken.put("password", getValueSafely(firstRecord, "kobilPassword"));
            inputParamsFetchAccessToken.put("scope", getValueSafely(firstRecord, "scope"));

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return inputParamsFetchAccessToken;
    }

    private static String getValueSafely(JsonObject jsonObject, String key) {
        JsonElement value = jsonObject.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Missing or invalid value for key: " + key);
        }
        return value.getAsString();
    }
}
