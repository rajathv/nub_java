

package com.rvg;

import com.mashape.unirest.http.*;
import java.io.*;
public class YakeenLogin {
  public static void main(String []args) throws Exception{
    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest.get("https://yakeencore.api.elm.sa/api/v1/yakeen/login?grant_type=password&username=AMLAK_INTERNATIONAL_FINANCE_COMPANY_YAK&password=Uh5alPS5KC7a19bl5eVx")
      .header("app-id", "6a8020cb")
      .header("app-key", "4a5cf5aa0d113fbbe560ec714a043e67")
      .header("Cookie", "9fdd0c3c00e2cddb1a94180204e9dd86=1d91062e7edfe39593c42ad299750950; NSC_3Tdbmf-Pqfotijgu.w4=14b5a3d94456a1593f35e91f73179db4ec5cc88f5d93141396964eb279cec105ef88e639; TS01e2737f=015a42f27ef5e01b6f77627c41c67c4ed49b13f9b820663feb74b5dce7f891702f718a7d82e78af055344370cccc921d5aaeecc5554ac1f2f4eeb8f7e280caba8512ec4e4b3048c7d6e2e9889fb23f497829030eca")
      .asString();
    
    System.out.println(response.getBody());
  }
}
