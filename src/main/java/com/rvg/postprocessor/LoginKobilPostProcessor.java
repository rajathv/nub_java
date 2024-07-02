package com.rvg.postprocessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.rvg.utils.HTTPOperations;

public class LoginKobilPostProcessor implements DataPostProcessor2{

    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

//     @Override
//     public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
//             throws Exception {
//         throw new UnsupportedOperationException("Unimplemented method 'execute'");
//     }
    

  

// private Boolean checkCustomerCommunicatio(String customer_id){
//     return false;

// }

//       private static void createCommunication(String userId, String type, String value, DataControllerRequest dcRequest)
//             {
//         Map<String, String> input = null;

//         input = new HashMap<>();
//         SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
//         input.put("id", "CUS_" + type + "_" + idformatter.format(new Date()));
//         input.put("Customer_id", userId);
//         input.put("Type_id", type.equalsIgnoreCase("Email") ? "COMM_TYPE_EMAIL" :"COMM_TYPE_PHONE");
//         input.put("isPrimary", "1");
//         input.put("Value", value);
//         input.put("Extension","Mobile");
//         input.put("Description", type);
//         if ("Phone".equals(type)) {
//             input.put("countryType", "domestic");
//         }
//         input.put("IsPreferredContactMethod", "1");
//         String resp = DBPServiceExecutorBuilder.builder().withServiceId(ServiceIDConstants.)
//                     .withOperationId(OperationIDConstants.CUSTOMERCOMMUNICATION_CREATE).withRequestParameters(inpGet)
//                     .build().getResponse();
//         //  String endPointResponse = HTTPOperations
//         //                         .hitPOSTServiceAndGetResponse(url, requestMapJsonObject,
//         //                                 request.getHeader("X-Kony-Authorization"), headersMap);
//         // HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
//         //         URLConstants.CUSTOMERCOMMUNICATION_CREATE);
//     }
}
