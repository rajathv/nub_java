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

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CustomerEnroll implements JavaService2 {
    final Logger logger = LogManager.getLogger(CustomerEnroll.class);

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        Map<String, String> inputParams = (Map<String, String>) inputArray[1];
        logger.error("Customer::::::::" + inputParams.get("customer_id"));

        String customer_id = (String) inputParams.get("customer_id");
        String loginId = (String) inputParams.get("loginId");
        String full_name = (String) inputParams.get("full_name");
        String first_name = (String) inputParams.get("first_name");
        String last_name = (String) inputParams.get("last_name");
        String email = (String) inputParams.get("email");
        String dob = (String) inputParams.get("dob");
        String phone_number_country_code = (String) inputParams.get("phone_number_country_code");
        String phoneNumber = (String) inputParams.get("phone_number");



    

        return result;
    }

    private JSONObject getAccountList(){

        return null;
    }

}

// "loginId": "yDcMMeE9",
// "phone_number_region": "cd",
// "last_name": "deposits",
// "phone_number_country_code": "+91",
// "full_name": "mounika deposits",
// "phoneNumber": "+918308253257",
// "content_type": "application/json",
// "user_id": "yDcMMeE9",
// "dob": "15/12/1990",
// "_provider_profile":
// "{\"userId\":\"yDcMMeE9\",\"email\":\"mc0c118128@techmahindra.com\",\"firstName\":\"mounika\",\"lastName\":\"deposits\",\"profileAttributes\":{\"firstname\":\"mounika\",\"userid\":\"yDcMMeE9\",\"email\":\"mc0c118128@techmahindra.com\",\"lastname\":\"deposits\"}}",
// "raw_response":
// "{\"phone_number_country_code\":\"+91\",\"full_name\":\"mounika
// deposits\",\"loginId\":\"yDcMMeE9\",\"phoneNumber\":\"+918308253257\",\"phone_number_region\":\"cd\",\"user_id\":\"yDcMMeE9\",\"dob\":\"15/12/1990\",\"last_name\":\"deposits\",\"customer_id\":\"00327302\",\"first_name\":\"mounika\",\"email\":\"mc0c118128@techmahindra.com\"}",
// "customer_id": "00327302",
// "first_name": "mounika",
// "email": "mc0c118128@techmahindra.com"