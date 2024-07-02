package com.rvg.constants;

public class OperationName {
        public static final String RAW_BANK_DATA_LAKE_CRM_CLIENT_INFO = "CRM_ClientInfo";
        public static final String RAW_BANK_DATA_LAKE_CRM_BRANCH_MAPPING = "CRM_cbs_cms_bracnhmapping";
        public static final String RAW_BANK_SCHEMA_NAME = "rawbank_db"; // EnvironmentConfigurationsHandler.getValue("RAW_BANK_SCHEMA_NAME");
        public static final String DB_SP_FETCH_RAW_BANK_CONFIG_VALUE = RAW_BANK_SCHEMA_NAME + "_"
                        + "GetRawbankConfigValue";
        public static final String DB_SP_UPDATE_RAW_BANK_CONFIG_VALUE = RAW_BANK_SCHEMA_NAME + "_"
                        + "UpdateRawbankConfigValue";
        public static final String DB_SP_FETCH_KOBIL_TOKEN_CONFIG_VALUE = RAW_BANK_SCHEMA_NAME + "_"
                        + "FetchTokenConfigByModule";

        public static final String RAW_BANK_DEBITCARD_APPLICATION_CREATE = "rawbank_db_debitcard_application_create";

        public static final String RAW_BANK_DEBIT_APPICATION = "debitapplication";

        public static final String RAW_BANK_CREATE_PRODUCT_SUBSCRIPTION = "createProductSubscription"; // authenticate
                                                                                                       // service

        public static final String RAW_BANK_CARD_PROGRAM_TYPE = "CRM_cardprgm_acctype"; // authenticate

        public static final String CUSTOMERCOMMUNICATION_CREATE = ""; // authenticate


}
