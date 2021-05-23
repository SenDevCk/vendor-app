package org.nic.lmd.wenderapp.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> cer = new ArrayList<String>();
        cer.add("Apply New");
        cer.add("Renewal");
        cer.add("Alteration");
        expandableListDetail.put("Certificate", cer);
        return expandableListDetail;
    }
}