package org.nic.lmd.wenderapp.prefrences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;
import org.nic.lmd.wenderapp.entities.UserData;


/**
 * Created by CKS on 15/06/2018.
 */
public class CommonPref {

	static Context context;

	CommonPref() {

	}

	CommonPref(Context context) {
		CommonPref.context = context;
	}


	public static void setCheckUpdate(Activity context, boolean first) {
		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		editor.putBoolean("first", first);

		editor.commit();

	}

	public static boolean getCheckUpdate(Context context) {
		String key = "_USER_DETAILS";
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		return prefs.getBoolean("first", false);
	}

    public static void setUserDetails(Activity activity,String res) {
		/*{"statusCode":1,"status":null,"remarks":null,"list":null,"data":{"applicantId":1127,"natureOfBusiness":11,"applicantName":"sdgsfdgsdgsdgsd","designation":18,"address1":"rgsre","address2":"gregte","country":"India","state":"Bihar","city":"gsfdgd","district":196,"landMark":"grete","pinCode":802215,"mobileNo":"9801800164","contactNo":"9801800164","emailId":"shasHI_@GMAIL.COM","contactPerson":"HSDGShadg","contactMobileNo":"9801800164","userId":"shashi","password":"123456","captcha":null,"confirm":null,"manufacturer":true,"repairer":true,"dealer":true,"packer":false,"importer":false,"trader":false}} */
		String key = "_USER_DETAILS";

		SharedPreferences prefs = activity.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		try {
			JSONObject jsonObject1=new JSONObject(res);
			JSONObject jsonObject=jsonObject1.getJSONObject("data");
			editor.putString("userid", jsonObject.getString("userId"));
			editor.putString("mobile", jsonObject.getString("mobileNo"));
			editor.putString("contact", jsonObject.getString("contactNo"));
			editor.putString("emailid", jsonObject.getString("emailId"));
			editor.putString("districtid", jsonObject.getString("district"));
			editor.putString("applicantname", jsonObject.getString("applicantName"));
			editor.putString("applicantId", jsonObject.getString("applicantId"));
			editor.putString("address1", jsonObject.getString("address1"));
			editor.putString("address2", jsonObject.getString("address2"));
			editor.putString("country", jsonObject.getString("country"));
			editor.putString("state", jsonObject.getString("state"));
			editor.putString("contactNo", jsonObject.getString("contactNo"));
			editor.putString("landMark", jsonObject.getString("landMark"));
			editor.putString("pinCode", jsonObject.getString("pinCode"));
			editor.putString("city", jsonObject.getString("city"));
		}catch (Exception e){
			e.printStackTrace();
		}
		editor.commit();
    }

    public static UserData getUserDetails(Activity activity){
		String key = "_USER_DETAILS";
		UserData userData=new UserData();
		SharedPreferences prefs = activity.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		userData.setUserid(prefs.getString("userid", ""));
		userData.setMobile(prefs.getString("mobile", ""));
		userData.setContact(prefs.getString("contact", ""));
		userData.setEmailid(prefs.getString("emailid", ""));
		userData.setDistrictid(prefs.getString("districtid", ""));
		userData.setApplicantname(prefs.getString("applicantname", ""));
		userData.setApplicantId(prefs.getString("applicantId", ""));
		userData.setAddress1(prefs.getString("address1", ""));
		userData.setAddress2(prefs.getString("address2", ""));
		userData.setCountry(prefs.getString("country", ""));
		userData.setState(prefs.getString("state", ""));
		userData.setContactNo(prefs.getString("contactNo", ""));
		userData.setLandMark(prefs.getString("landMark", ""));
		userData.setPinCode(prefs.getString("pinCode", ""));
		userData.setCity(prefs.getString("city", ""));

		return userData;
	}

	public static void setCheckUpdateForApply(Activity context, boolean first) {
		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		editor.putBoolean("apply", first);

		editor.commit();

	}

	public static boolean getCheckUpdateForApply(Context context) {
		String key = "_USER_DETAILS";
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		return prefs.getBoolean("apply", false);
	}
}
