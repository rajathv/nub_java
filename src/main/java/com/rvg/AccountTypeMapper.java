package com.rvg;
import java.util.HashMap;
import java.util.Map;

public class AccountTypeMapper {
    private static final Map<String, String> accountsTypes = new HashMap<>();

    static {
        accountsTypes.put("Checking", "1");
        accountsTypes.put("Savings", "2");
        accountsTypes.put("CreditCard", "3");
        accountsTypes.put("Deposit", "4");
        accountsTypes.put("Mortgage", "5");
        accountsTypes.put("Loan", "6");
        accountsTypes.put("Current", "7");
        accountsTypes.put("Investment", "8");
        accountsTypes.put("mortgageFacility", "9");
    }

    public static String getAccountTypeId(String accountType) {
        String lowercaseAccountType = accountType.toLowerCase();

        // Check for exact match
        if (accountsTypes.containsKey(lowercaseAccountType)) {
            return accountsTypes.get(lowercaseAccountType);
        }

        // Check for partial match (e.g., "Term Deposit" matches "Deposit")
        for (Map.Entry<String, String> entry : accountsTypes.entrySet()) {
            if (lowercaseAccountType.contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }

        // No match found
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getAccountTypeId("Term Deposit")); // Output: 4
        System.out.println(getAccountTypeId("Savings")); // Output: 2
        System.out.println(getAccountTypeId("UnknownType")); // Output: null
    }
}