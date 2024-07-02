package com.rvg;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetForexRate implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        String res = "";
        Result result = new Result();
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = currentDate.format(formatter);
        String branchCode = request.getParameter("branchCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String initialCurrencyCode = request.getParameter("initialCurrencyCode");
        String searchNature = request.getParameter("searchNature");
        Map<String, Object> requestInputs = new HashMap<>();
        requestInputs.put("branchCode", branchCode);
        requestInputs.put("targetCurrencyCode", targetCurrencyCode);
        requestInputs.put("initialCurrencyCode", initialCurrencyCode);
        requestInputs.put("applicationDate", formattedDate);
        requestInputs.put("searchNature", searchNature);

        res = DBPServiceExecutorBuilder.builder().withServiceId("RawBankForexExchange")
                .withOperationId("getExchangeRateList").withRequestParameters(requestInputs).build()
                .getResponse();
        JSONObject jsonObject = new JSONObject(res);

        int opStat = jsonObject.getInt("opstatus");
        if (opStat == 0) {
            JSONObject exchangeRateObject = jsonObject.getJSONObject("fjs1:exchangeRate");

            // Get the value of "fjs1:rate"
            String rateValue = exchangeRateObject.optString("fjs1:rate");

            result.addParam("exchangeRate", rateValue);

        } else {
            result.addParam("Success", "False");
        }

        return result;

    }

}
