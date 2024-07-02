
package com.rvg;

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
import com.rvg.constants.OperationName;
import com.rvg.constants.ServiceId;

public class ApplyDebitCardRaw implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        final Logger logger = LogManager.getLogger(ApplyDebitCardRaw.class);
        Result result = new Result();
        String res = "";

        Map<String, String> inputParams = (Map<String, String>) inputArray[1];

        logger.error("Customer::::::::" + request.getParameter("customer_id"));
        String customer_id = request.getParameter("customer_id");
        String account_id = request.getParameter("account_id");
        String cardprogramcode = request.getParameter("cardprogramcode");
        String nameoncard = request.getParameter("nameoncard");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phonenumber = request.getParameter("phonenumber");
        String backaccounttype = request.getParameter("backaccounttype");
        String pickupbranchcode = request.getParameter("pickupbranchcode");
        String branchcode = request.getParameter("branchcode");
        String requestor = request.getParameter("requestor");
        String getBranchCode = "";
        getBranchCode = getBranchCodeDataLake(branchcode);
        Map<String, Object> paramsDB = new HashMap<>();

        paramsDB.put("uuid", UUID.randomUUID());
        paramsDB.put("requestor", requestor);
        paramsDB.put("bankaccounttype", backaccounttype);
        paramsDB.put("pickupbranchcode", pickupbranchcode);
        paramsDB.put("customer_id", customer_id);
        paramsDB.put("account_id", account_id);
        paramsDB.put("cardprogramcode", cardprogramcode);
        paramsDB.put("nameoncard", nameoncard);
        paramsDB.put("firstname", firstname);
        paramsDB.put("lastname", lastname);
        paramsDB.put("email", email);
        paramsDB.put("phonenumber", phonenumber);
        paramsDB.put("branchcode", getBranchCode);

        Map<String, Object> paramsDebitCard = new HashMap<>();
        paramsDebitCard.put("bankaccounttype", backaccounttype);
        paramsDebitCard.put("customerid", customer_id);
        paramsDebitCard.put("accountnumber", account_id);
        paramsDebitCard.put("cardprogramcode", cardprogramcode);
        paramsDebitCard.put("nameoncard", nameoncard);
        paramsDebitCard.put("firstname", firstname);
        paramsDebitCard.put("lastname", lastname);
        paramsDebitCard.put("email", email);
        paramsDebitCard.put("phonenumber", phonenumber);
        paramsDebitCard.put("branchcode", getBranchCode);

        res = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.RAWBANK_CARD_DETAILS_DB)
                .withOperationId(OperationName.RAW_BANK_DEBITCARD_APPLICATION_CREATE).withRequestParameters(paramsDB)
                .build()
                .getResponse();

        JSONObject jsonObjectDB = new JSONObject(res);

        int opstatusDB = jsonObjectDB.getInt("opstatus");
        if (opstatusDB == 0 && jsonObjectDB != null) {

            String resp = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.RAWBANK_CARD_API)
                    .withOperationId(OperationName.RAW_BANK_DEBIT_APPICATION).withRequestParameters(paramsDebitCard)
                    .build()
                    .getResponse();

            JSONObject jsonObject = new JSONObject(resp);

            int opstatus = jsonObject.getInt("opstatus");

            JSONObject header = jsonObject.getJSONObject("header");
            String idmsg = header.getString("idmsg");
            String mac = header.getString("mac");

            JSONObject body = jsonObject.getJSONObject("body");
            JSONObject status = body.getJSONObject("status");
            JSONObject statusError = jsonObject.getJSONObject("status");

            JSONObject additionalInfo = body.getJSONObject("additionaldata");

            String errordesc = status.optString("errordesc");
            String errorcode = status.optString("errorcode");

            String msgerror = statusError.optString("msgerror");
            String descstatus = statusError.optString("descstatus");
            String appId = additionalInfo.optString("appappid");
            Long appcode = additionalInfo.optLong("appappcode");

            if (errorcode != null && errordesc.equalsIgnoreCase("SUCCESS")) {

                result.addParam(new Param("idmsg", idmsg));
                result.addParam(new Param("mac", mac));
                result.addParam(new Param("errordesc", errordesc));
                result.addParam(new Param("errorcode", errorcode));
                result.addParam(new Param("appappid", appId));
                result.addParam(new Param("msgerror", msgerror));
                result.addParam(new Param("descstatus", descstatus));
                return result;
            } else {
                result.addParam(new Param("errordesc", errordesc));

                result.addParam(new Param("errorcode", errorcode));
                result.addParam(new Param("msgerror", msgerror));
                result.addParam(new Param("descstatus", descstatus));
                return result;
            }

        } else {
            result.addParam(new Param("errordesc", "Failed to add in db"));
            return result;
        }
    }

    private String getBranchCodeDataLake(String branch) {
        Map<String, Object> datalakeDb = new HashMap<>();
        datalakeDb.put("lib2", branch);
        String respo = null;
        String newBranch = "";
        try {
            respo = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.RAWBANKDATALAKEDBSERVICES)
                    .withOperationId(OperationName.RAW_BANK_DATA_LAKE_CRM_BRANCH_MAPPING)
                    .withRequestParameters(datalakeDb)
                    .build()
                    .getResponse();
        } catch (DBPApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject jsonObjectDL = new JSONObject(respo);
        int opstatusDL = jsonObjectDL.getInt("opstatus");

        if (opstatusDL == 0 && jsonObjectDL != null) {
            if (!jsonObjectDL.getJSONArray("records").isEmpty()) {
                newBranch = jsonObjectDL.getJSONArray("records").getJSONObject(0)
                        .optString("lib3");
            } else {
                newBranch = "001";
            }

        }

        return newBranch;

    }
}