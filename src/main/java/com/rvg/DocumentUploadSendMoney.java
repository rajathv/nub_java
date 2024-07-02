package com.rvg;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

// {"uuid": uuid,"invoice_number": "12345","invoice_amount": "100","account_number": "3242322","customer_id": "CIF1244","document_base64": document};

public class DocumentUploadSendMoney implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        String res = "";
        Result result = new Result();
        String uuid = request.getParameter("uuid");
        String invoice_number = request.getParameter("invoice_number");
        String invoice_amount = request.getParameter("invoice_amount");
        String account_number = request.getParameter("account_number");
        String customer_id = request.getParameter("customer_id");
        String document_base64 = request.getParameter("document_base64");

        Map<String, Object> requestInputs = new HashMap<>();
        requestInputs.put("document_base64", document_base64);
        requestInputs.put("invoice_number", invoice_number);
        requestInputs.put("invoice_amount", invoice_amount);
        requestInputs.put("account_number", account_number);
        requestInputs.put("customer_id", customer_id);
        requestInputs.put("uuid", uuid);

        res = DBPServiceExecutorBuilder.builder().withServiceId("RAWBANKDBSERVICES")
                .withOperationId("dbxdb_documentstorage_create").withRequestParameters(requestInputs).build()
                .getResponse();
        JSONObject jsonObject = new JSONObject(res);

        int opStat = jsonObject.getInt("opstatus");
        if (opStat == 0) {
            result.addParam("Success", "True");
        } else {
            result.addParam("Success", "False");

        }

        return result;

    }

}
