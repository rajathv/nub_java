/**
 * @author Rajath V
 * @email [example@mail.com]
 * @create date 2024-03-25 22:00:52
 * @modify date 2024-03-25 22:00:52
 * @desc [Class for adding custoemr_id from user session data]
 */
package com.rvg.preprocessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.konylabs.middleware.api.ServicesManager;

import com.konylabs.middleware.api.processor.IdentityHandler;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * GetAccountsPreprocessor
 */
public class GetLoansPreprocessor implements DataPreProcessor2 {
    final Logger logger = LogManager.getLogger(GetLoansPreprocessor.class);

    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        ServicesManager servicesManager = request.getServicesManager();
        IdentityHandler identityHandler = servicesManager.getIdentityHandler();

        Map<String, Object> userAttributes = identityHandler.getUserAttributes();

        String reqString = request.getParameter("customerNumber");
        String loginUserId = (String) userAttributes.get("customer_id_cbs");
        logger.error("CustomerIDPreprocessor : " + reqString);
        logger.error("CustomerID From Session: " + loginUserId);

        // int customerIdReq = Integer.valueOf(reqString);
        // int customerIdSes = Integer.valueOf(loginUserId);
        if (reqString.trim().equalsIgnoreCase(loginUserId.trim())) {
            logger.error("Inside true customer");
            return true;
        } else {
            logger.error("Inside false customer");
            result.addErrMsgParam("Session violation error");
            return false;

        }
    }
}