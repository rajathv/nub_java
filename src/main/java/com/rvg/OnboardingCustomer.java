package com.rvg;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// Collections in Kony Java
////////////////////////////////
//Result
//Dataset
//Record
//Param

public class OnboardingCustomer implements JavaService2 {
    private static final Logger logger = LogManager.getLogger(OnboardingCustomer.class);

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        Dataset data = new Dataset();
        Record rec = new Record();
        Param par = new Param();

        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String firstNameString = request.getParameter("firstName");
        String lastNameString = request.getParameter("lastName");
        String passwordString = request.getParameter("password");

        if (preprocessor(request, result)) {
            // if preprocessor is successful then continue processing
            if (checkCustomerExisist(phoneNumber, result, request)) {
                result.addErrMsgParam("Customer already exists");
                return result;
            } else {

                HashMap<String, Object> newCustomerInput = new HashMap<>();
                newCustomerInput.put("PhoneNumber", phoneNumber);
                newCustomerInput.put("FirstName", firstNameString);
                newCustomerInput.put("LastName", lastNameString);
                newCustomerInput.put("EmailAddress", email);
                newCustomerInput.put("Password", passwordString);
                String respon = DBPServiceExecutorBuilder.builder().withServiceId("Customer_Rajath")
                        .withOperationId("tutorial_customer_rajath_create").withRequestParameters(newCustomerInput)
                        .build()
                        .getResponse();

                logger.debug("Response from the creat customer service::::>>>" + respon);

                result.addStringParam("StatusMessage", "Customer created Successfully");
            }

        } else {
            // if preprocessor is false exit the code and return result object with error
            // message
            return result;
        }

        return result;

    }

    private boolean preprocessor(DataControllerRequest request, Result result) throws Exception {
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String firstNameString = request.getParameter("firstName");
        String lastNameString = request.getParameter("lastName");
        String passwordString = request.getParameter("password");

        if (email.isEmpty()) {
            result.addErrMsgParam("Email is empty");
            return false;
        } else if (phoneNumber.isEmpty()) {
            result.addErrMsgParam("Phone number is empty");
            return false;
        } else if (firstNameString.isEmpty()) {
            result.addErrMsgParam("firstNameString is empty ");
            return false;
        } else if (lastNameString.isEmpty()) {
            result.addErrMsgParam("lastNameString is empty");
            return false;
        } else if (passwordString.isEmpty()) {
            result.addErrMsgParam("password is empty");
            return false;
        }

        return true;
    }

    private boolean checkCustomerExisist(String phoneNumber, Result result, DataControllerRequest request) {
        HashMap<String, Object> userInputs = new HashMap<>();
        String res = "";
        userInputs.put("$filter", "PhoneNumber eq " + phoneNumber);

        boolean flag = false;

        try {
            res = DBPServiceExecutorBuilder.builder().withServiceId("Customer_Rajath")
                    .withOperationId("tutorial_customer_rajath_get").withRequestParameters(userInputs).build()
                    .getResponse();
            JSONObject JsonResponse = new JSONObject(res);
            if (JsonResponse.getJSONArray("customer_rajath").length()>0) {
                logger.error("Customer Record from Get::::>>>>>>> " + res);
                flag = true;
            } else {
                logger.error("Customer Record from::::>>>>>>> " + res);

                flag = false;
            }
        } catch (Exception exception) {
            logger.error("Exception in checkCustomerExisist:::", exception);
        }
        return flag;

    }
}
