package org.nic.lmd.wenderapp.prefrences;

import org.nic.lmd.wenderapp.entities.Block;
import org.nic.lmd.wenderapp.entities.NatureOfBusiness;

public class GlobalVariable {

    //Registaration Form1
    public static long count_data=0;
    public static NatureOfBusiness nature_of_business=null;
    public static String  userID = "", password = "";
    public static boolean isManufacturere, isRepeirer, isDeler;
    public static boolean isManufacturere_ra, isRepeirer_ra;
    public static boolean isConsumer;



    public static void clearGlobalData() {
        isManufacturere = false;
        isDeler = false;
        isRepeirer = false;
        isRepeirer_ra = false;
        isManufacturere_ra = false;
        isConsumer = false;
        userID = "";
        password = "";
    }

    public static String Pid = "0";
    public static String id = "0";
    public static String area = "";

    public static boolean isOfflineGPS = false;


    public static final String PIN_PATTERN = "^[1-9][0-9]{5}$";
    public static final String MOB_PATTERN = "[6-9]{1}[0-9]{9}";
    public static final String LANDLINE_PATTERN = "\\d{5}([- ]*)\\d{6}";
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String NAME_PATTERN="^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)";
    public static final String CITY_PATTERN="^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
}
