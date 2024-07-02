
package com.rvg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import java.util.UUID;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.rvg.constants.GenericConstants;
import com.rvg.constants.OperationName;
import com.rvg.constants.ServiceId;

public class CreateCardSubscription implements JavaService2 {
    final Logger logger = LogManager.getLogger(CreateCardSubscription.class);

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        // TODO: implement Elgiblity check SP for getting the product code "cdprdcbs":
        // "822",
        // RawBankDataLakeDB
        // CRM_cardprgm_acctype

        // Input: 05101-00149892502-USD

        Result result = new Result();

        // Map<String, String> inputParams = (Map<String, String>) inputArray[1];
        // logger.debug("======> Customer Id " + inputParams.get("customerCode"));
        // String customer_id = request.getParameter("customerCode");
        String customer_id = request.getParameter("customerCode");
        logger.error("Customer::::::::" + customer_id);
        String productCode = request.getParameter("productCode");
        String branch = request.getParameter("branch");
        String currency = request.getParameter("currency");
        String account_id = request.getParameter("account");
        String requestId = generateUniqueRequestID();
        String timestamp = generateTimestamp();

        Map<String, Object> paramsCreateSub = new HashMap<>();

        paramsCreateSub.put("customerCode", customer_id);
        paramsCreateSub.put("productCode", productCode);
        paramsCreateSub.put("branch", branch);
        paramsCreateSub.put("currency", currency);
        paramsCreateSub.put("account", account_id);
        paramsCreateSub.put("requestId", requestId);
        paramsCreateSub.put("timestamp", timestamp);
        String resp = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CARD_PROGRAM_SERVICE)
                .withOperationId(OperationName.RAW_BANK_CREATE_PRODUCT_SUBSCRIPTION)
                .withRequestParameters(paramsCreateSub)
                .build()
                .getResponse();
        JSONObject jsonObject = new JSONObject(resp);

        int opstatus = jsonObject.optInt("opstatus");
        int httpstatus = jsonObject.optInt("httpStatusCode");

        if (opstatus == GenericConstants.OPSTATUS_SUCCESS && httpstatus == GenericConstants.HTTP_SUCCESS) {
            String serviceStatus = jsonObject.optString("fjs1:statusCode");
            if (serviceStatus.equals("0")) {
                result.addParam("status", "success");
                result.addParam("subscriptionReference", jsonObject.optString("fjs1:subscriptionReference"));

            } else {
                result.addParam("status", "failure");
                result.addParam("errorDesc", "Service failed due to error in data");
            }

        } else {
            result.addParam("status", "failure");
            result.addParam("errorDesc", "Server internal error");
        }

        return result;

    }

    public static String generateUniqueRequestID() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        String id = String.format("%010d", mostSignificantBits);
        return id;
    }

    public static String generateTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);
        return formattedTimestamp;
    }
}