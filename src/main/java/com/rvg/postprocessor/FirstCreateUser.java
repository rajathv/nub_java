package com.rvg.postprocessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.rvg.utils.*;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FirstCreateUser implements DataPostProcessor2 {
    private static final Logger logger = LogManager.getLogger(FirstCreateUser.class);


    // public static void main(String[] args) {
    //   System.out.println(getAccountTypeId("Saving account"));
    // }
    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {

        String res = ResultToJSON.convert(result);
        JSONObject resObj = new JSONObject(res);
        if (resObj.getJSONArray("Accounts").length() > 0) {
            String cif = resObj.getJSONArray("Accounts").getJSONObject(0).optString("customerID");
            if (customerExist(cif)) {
                logger.error("::CustomerExist is true::");
                return result;
            } else {
                logger.error("::CustomerExist is False::");
                JSONArray accounts = resObj.getJSONArray("Accounts");
                JSONArray resultObj = new JSONArray();

                for (int i = 0; i < accounts.length(); i++) {
                    JSONObject account = accounts.getJSONObject(i);
                    String accountId = account.getString("account_id");
                    String accountName = account.getString("accountType");
                    if (!accountName.isBlank()) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("accountId", accountId);
                        jsonObject.put("accountName", accountName);
                        jsonObject.put("isEnabled", "true");
                        jsonObject.put("typeId", getAccountTypeId(accountName));
                        jsonObject.put("ownerType", "Owner");

                        resultObj.put(jsonObject);
                    }
                }

                // GET DATA FROM ENVIORNMENT PROPERTIES
                String roleId = EnvironmentConfigurationsRaw.CONTRACT_ROLE_ID.getValue();
                String serviceDef = EnvironmentConfigurationsRaw.CONTRACT_SERVICE_DEFENITION.getValue();
                String leagalId = EnvironmentConfigurationsRaw.CONTRACT_LEGAL_ENTITY_ID.getValue();
                String url = EnvironmentConfigurationsRaw.CONTRACT_CREATION_URL.getValue();
                if (!url.isBlank()) {

                    JSONObject requestMapJsonObject = new JSONObject();
                    requestMapJsonObject.put("roleId", roleId);
                    requestMapJsonObject.put("accounts", resultObj.toString());
                    requestMapJsonObject.put("serviceDefinitionId", serviceDef);
                    requestMapJsonObject.put("legalEntityId", leagalId);
                    requestMapJsonObject.put("cif", cif);
                    requestMapJsonObject.put("isDigitalProfileNeeded", "true");
                    logger.error("@@ CREATE CUSTOMER PAYLOAD PLY:CC"+requestMapJsonObject);
                    try {
                        logger.error("@@@X-Kony-Auth:::" + request.getHeader("X-Kony-Authorization"));
                        HashMap<String, String> headersMap = new HashMap<String, String>();
                        String endPointResponse = HTTPOperations
                                .hitPOSTServiceAndGetResponse(url, requestMapJsonObject,
                                        request.getHeader("X-Kony-Authorization"), headersMap);
                        logger.error("Response From ContractCreation" + endPointResponse);
                    } catch (Exception e) {
                        logger.error("Exception occur while customerExist : " + e.getMessage());
                    }

                }else{
                    logger.error("@@@ ADD ENVIORNMENT URL FOR CONTRACT CREATION @@@@");
                }
            }
        }
        return result;

    }
    private static final Map<String, String> accountsTypes = new HashMap<>();

    static {
        accountsTypes.put("Checking", "1");
        accountsTypes.put("Saving", "2");
        accountsTypes.put("CreditCard", "3");
        accountsTypes.put("Deposit", "4");
        accountsTypes.put("Mortgage", "5");
        accountsTypes.put("Loan", "6");
        accountsTypes.put("Current", "7");
        accountsTypes.put("Investment", "8");
        accountsTypes.put("mortgageFacility", "9");
    }

    public static String getAccountTypeId(String accountType) {
        String lowercaseAccountType = accountType.toLowerCase();

        // Check for exact match
        if (accountsTypes.containsKey(lowercaseAccountType)) {
            return accountsTypes.get(lowercaseAccountType);
        }

        // Check for partial match (e.g., "Term Deposit" matches "Deposit")
        for (Map.Entry<String, String> entry : accountsTypes.entrySet()) {
            if (lowercaseAccountType.contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }

        // No match found
        return null;
    }
    

    // This function searches the customer table if the customer record is created
    // by using CIF in the Ssn column of dbxdb.customer
    private Boolean customerExist(String cif) {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("$filter", "Ssn eq " + cif);
        String resp = "";
        String serviceName = "dbpRbLocalServicesdb";
        String operationName = "dbxdb_customer_get";
        try {
            resp = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceName)
                    .withObjectId(null)
                    .withOperationId(operationName)
                    .withRequestParameters(inputMap)
                    .build()
                    .getResponse();

        } catch (Exception e) {
            logger.error("Exception occur while customerExist : " + e.getMessage());
            return false;
        }
        JSONObject customerObj = new JSONObject(resp);
        if (customerObj.getJSONArray("customer").length() > 0) {
            return true;
        }
        return false;
    }

}
