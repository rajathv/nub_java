package com.rvg;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class Login implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(Login.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();

		if (preProcess(request, response)) {
			if (validatePassword(request, response)) {
				// String sessionToken =
				// BCrypt.hashpw(Encryption.decrypt(request.getParameter("UserName"), request),
				// BCrypt.gensalt());
				Record securityAttrRecord = new Record();
				securityAttrRecord.setId("security_attributes");

				Record userAttrRecord = new Record();
				userAttrRecord.setId("user_attributes");

				HashMap<String, Object> userInputs = new HashMap<>();
				String res = "";
				userInputs.put("$filter", "PhoneNumber eq " + request.getParameter("UserName"));

				try {
					res = DBPServiceExecutorBuilder.builder().withServiceId("Customer_Rajath")
							.withOperationId("tutorial_customer_rajath_get").withRequestParameters(userInputs).build()
							.getResponse();
					JSONObject customerResonse = new JSONObject(res);
					String firstName = customerResonse.getJSONArray("customer_rajath").getJSONObject(0)
							.optString("FirstName");
					String lastName = customerResonse.getJSONArray("customer_rajath").getJSONObject(0)
							.optString("LastName");
					String email = customerResonse.getJSONArray("customer_rajath").getJSONObject(0)
							.optString("EmailAddress");
					userAttrRecord.addParam(new Param("user_id", firstName));
					userAttrRecord.addParam(new Param("LastName", lastName));
					userAttrRecord.addParam(new Param("Email", email));
					userAttrRecord.addParam(new Param("PhoneNumber", request.getParameter("UserName")));

					securityAttrRecord.addParam(new Param("session_token", request.getParameter("UserName")));

				} catch (Exception exception) {
					logger.error("Exception in checkCustomerExisist:::", exception);
				}

				result.addRecord(securityAttrRecord);
				result.addRecord(userAttrRecord);
				result.addParam(new Param("httpStatusCode", "200", "int"));

			} else {
				result.addErrMsgParam("Password entered is incorrect");
				return result;
			}

		} else {
			result.addErrMsgParam("UsernName or Password empty");
			return result;
		}

		return result;
	}

	private boolean validatePassword(DataControllerRequest request, DataControllerResponse response) {
		HashMap<String, Object> userInputs = new HashMap<>();
		String res = "";
		userInputs.put("$filter", "PhoneNumber eq " + request.getParameter("UserName"));

		try {
			res = DBPServiceExecutorBuilder.builder().withServiceId("Customer_Rajath")
					.withOperationId("tutorial_customer_rajath_get").withRequestParameters(userInputs).build()
					.getResponse();
			JSONObject JsonResponse = new JSONObject(res);
			if (JsonResponse.getJSONArray("customer_rajath").length() > 0) {
				String password = JsonResponse.getJSONArray("customer_rajath").getJSONObject(0).getString("Password");

				if (password.equals(request.getParameter("Password"))) {
					return true;
				} else {
					return false;
				}

			} else {
				logger.error("Customer Record from::::>>>>>>> " + res);

				return false;
			}
		} catch (Exception exception) {
			logger.error("Exception in checkCustomerExisist:::", exception);
		}

		return false;
	}

	private boolean preProcess(DataControllerRequest request, DataControllerResponse response) {

		if (request.getParameter("UserName") != null && request.getParameter("Password") != null) {
			return true;
		}

		return false;

	}

}
