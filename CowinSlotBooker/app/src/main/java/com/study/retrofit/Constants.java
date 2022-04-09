package com.study.retrofit;

import com.study.models.beneficiaries.Beneficiary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Constants {
    public static String SECRET_KEY = "U2FsdGVkX1/eshHyMOu7NPuPWX4ru1SNn0dn1XVlHhVQi7jGP81y3itG1b1UFfkMbdrmpq/n/Vy1RDepAaB0vQ==";
    public static String BEARER_TOKEN = "";
    public static String PIN_CODE = "302017";
    public static String FEE_TYPE = "Paid";
    public static long TIME_DELAY = 10000;
    public static long AGE_18 = 18L;
    public static long AGE_45 = 45L;
    public static final String COVISHIELD = "COVISHIELD";
    public static final String COVAXIN = "COVAXIN";
    public static final String BOTH_VACCINE = "BOTH";
    public static final String GENERAL_TOPIC = "GENERAL_TOPIC";
    public static final String NOTIFICATION_CHANNEL_ID = "2422AseA34asd35239jsa";

    @Nullable
    public static Beneficiary SELECTED_BENEFICARY;

}
