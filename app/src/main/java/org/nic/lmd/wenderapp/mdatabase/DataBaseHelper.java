package org.nic.lmd.wenderapp.mdatabase;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.asynctask.VenderDataSingle;
import org.nic.lmd.wenderapp.entities.Block;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.Designation;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.entities.FirmType;
import org.nic.lmd.wenderapp.entities.InsCapacityEntity;
import org.nic.lmd.wenderapp.entities.InsCategoryEntity;
import org.nic.lmd.wenderapp.entities.InsProduct;
import org.nic.lmd.wenderapp.entities.InsProposalEntity;
import org.nic.lmd.wenderapp.entities.InstrumentEntity;
import org.nic.lmd.wenderapp.entities.NatureOfBusiness;
import org.nic.lmd.wenderapp.entities.Nozzle;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.entities.PremisesTypeEntity;
import org.nic.lmd.wenderapp.entities.ProposalTypeEntity;
import org.nic.lmd.wenderapp.entities.SubDivision;
import org.nic.lmd.wenderapp.entities.ThanaEntity;
import org.nic.lmd.wenderapp.entities.TypeOfPump;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.entities.WeightCategoriesEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Created by NIC2 on 1/6/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    // The Android's default system path of your application database.
    private static String DB_PATH = "";
    private static String DB_NAME = "crms.db";
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 3);
        if (Build.VERSION.SDK_INT >= 29) {
            DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        } else if (Build.VERSION.SDK_INT >= 21) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            Log.d("DataBase", "exist");
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            Log.d("DataBase", "exist");
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        String myPath = null;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                myPath = DB_PATH;
            } else {
                myPath = DB_PATH + DB_NAME;
            }
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS
                            | SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    public boolean databaseExist() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = null;
        if (Build.VERSION.SDK_INT >= 29) {
            outFileName = DB_PATH;
        } else {
            outFileName = DB_PATH + DB_NAME;
        }

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = null;
        if (Build.VERSION.SDK_INT >= 29) {
            myPath = DB_PATH;
        } else {
            myPath = DB_PATH + DB_NAME;
        }
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

      /*  String CREATE_NEFT_TABLE = "CREATE TABLE NEFT (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,WALLET_ID TEXT,AMOUNT TEXT,UTR_NO TEXT,TOPUP_TIME TEXT,u_id TEXT);";
        String CREATE_STATEMENT_TABLE = "CREATE TABLE Statement (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT   ,CON_ID TEXT,RCPT_NO TEXT,PAY_AMT TEXT,WALLET_BALANCE TEXT,WALLET_ID TEXT,RRFContactNo TEXT,ConsumerContactNo TEXT,transStatus TEXT,MESSAGE_STRING TEXT,Authenticated TEXT,payDate\tTEXT,BILL_NO TEXT,payMode TEXT,CNAME TEXT,IS_PRINTED TEXT,TRANS_ID TEXT,USER_ID TEXT);";
        String CREATE_BookNo_TABLE = "CREATE TABLE BookNo (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,BookNo TEXT,MessageString TEXT,USER_ID TEXT);";
        String CREATE_MRU_TABLE = "CREATE TABLE MRU (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,CON_ID TEXT,ACT_NO TEXT,OLD_CON_ID TEXT,CNAME TEXT,METER_NO TEXT,BOOK_NO TEXT,MOBILE_NO TEXT,PAYBLE_AMOUNT TEXT,BILL_NO TEXT,TARIFF_ID TEXT,MESSAGE_STRING TEXT,DATE_TIME TEXT,FA_HU_NAME TEXT,BILL_ADDR1 TEXT,USER_ID TEXT);";
        db.execSQL(CREATE_NEFT_TABLE);
        db.execSQL(CREATE_STATEMENT_TABLE);
        db.execSQL(CREATE_BookNo_TABLE);
        db.execSQL(CREATE_MRU_TABLE);*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("version-- " + "older= " + oldVersion + "newer= " + newVersion);
        if (oldVersion >= newVersion) return;
        //ClearAllTable(db);
        //onCreate(db);
        if (oldVersion == 1) {
            Log.d("New Version", "Data can be upgraded");
        }
        if (newVersion == 1) {
            String CREATE_TANK_TABLE = "CREATE TABLE VEHICLE_TANK (v_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,reg_no TEXT,eng_no TEXT,chechis_no TEXT,firm_or_owner_name TEXT,country TEXT,denom_id NUMERIC);";
            db.execSQL(CREATE_TANK_TABLE);
        }
        Log.d("Sample Data", "onUpgrade	: " + newVersion);
    }


    public void ClearAllTable(SQLiteDatabase db) {
        /*db.execSQL("DROP TABLE IF EXISTS DISTRICT");
        db.execSQL("DROP TABLE IF EXISTS NATURE_OF_BUSINESS");*/
    }

    public long saveDistrict(ArrayList<District> activityGroups) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (District activityGroup : activityGroups) {
                ContentValues values = new ContentValues();
                values.put("_id", activityGroup.getId().trim());
                values.put("name", activityGroup.getName().trim());
                String[] whereArgs = new String[]{activityGroup.getId().trim()};
                c = db.update("DISTRICT", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("DISTRICT", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for ActivityGroup");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }


    public ArrayList<District> getDistrict() {
        ArrayList<District> mruEntities = new ArrayList<>();
        mruEntities.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from DISTRICT";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                District district = new District();
                district.setId(cursor.getString(cursor.getColumnIndex("_id")));
                district.setName(cursor.getString(cursor.getColumnIndex("name")));
                mruEntities.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mruEntities;
    }

    public District getDistrictByID(String district_id) {
        District district = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from DISTRICT where _id='" + district_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                district = new District();
                district.setId(cursor.getString(cursor.getColumnIndex("_id")));
                district.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return district;
    }

    public long saveNatureOfRequest(ArrayList<NatureOfBusiness> natureOfRequests) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (NatureOfBusiness natureOfRequest : natureOfRequests) {
                ContentValues values = new ContentValues();
                values.put("_id", natureOfRequest.getId().trim());
                values.put("name", natureOfRequest.getValue());
                String[] whereArgs = new String[]{natureOfRequest.getId().trim()};
                c = db.update("NATURE_OF_BUSINESS", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("NATURE_OF_BUSINESS", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for ActivityGroup");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<NatureOfBusiness> getNatureofBusiness() {
        ArrayList<NatureOfBusiness> natureOfRequests = new ArrayList<>();
        natureOfRequests.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from NATURE_OF_BUSINESS";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                NatureOfBusiness natureOfRequest = new NatureOfBusiness();
                natureOfRequest.setId(cursor.getString(cursor.getColumnIndex("_id")));
                natureOfRequest.setValue(cursor.getString(cursor.getColumnIndex("name")));
                natureOfRequests.add(natureOfRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return natureOfRequests;
    }

    public NatureOfBusiness getNatureofBusinessByID(String nOfBusinessid) {
        NatureOfBusiness nature = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from NATURE_OF_BUSINESS where _id='" + nOfBusinessid + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                nature = new NatureOfBusiness();
                nature.setId(cursor.getString(cursor.getColumnIndex("_id")));
                nature.setValue(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return nature;
    }

    public long saveDesignation(ArrayList<Designation> designations) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (Designation designation : designations) {
                ContentValues values = new ContentValues();
                values.put("_id", designation.getId().trim());
                values.put("name", designation.getName());
                String[] whereArgs = new String[]{designation.getId().trim()};
                c = db.update("DESIGNATION", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("DESIGNATION", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for ActivityGroup");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<Designation> getDesignation() {
        ArrayList<Designation> natureOfRequests = new ArrayList<>();
        natureOfRequests.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from DESIGNATION";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                Designation designation = new Designation();
                designation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                designation.setName(cursor.getString(cursor.getColumnIndex("name")));
                natureOfRequests.add(designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return natureOfRequests;
    }

    public long saveFirmType(ArrayList<FirmType> firmTypes) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (FirmType designation : firmTypes) {
                ContentValues values = new ContentValues();
                values.put("_id", designation.getId().trim());
                values.put("name", designation.getName());
                String[] whereArgs = new String[]{designation.getId().trim()};
                c = db.update("FIRM_TYPE", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("FIRM_TYPE", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for FIRM_TYPE");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<FirmType> getFirmType() {
        ArrayList<FirmType> firmTypeArrayList = new ArrayList<>();
        firmTypeArrayList.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from FIRM_TYPE";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                FirmType designation = new FirmType();
                designation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                designation.setName(cursor.getString(cursor.getColumnIndex("name")));
                firmTypeArrayList.add(designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmTypeArrayList;
    }

    public long saveTypeOfPump(ArrayList<TypeOfPump> firmTypes) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (TypeOfPump typeOfPump : firmTypes) {
                ContentValues values = new ContentValues();
                values.put("_id", typeOfPump.getId().trim());
                values.put("name", typeOfPump.getName().trim());
                String[] whereArgs = new String[]{typeOfPump.getId().trim()};
                c = db.update("TYPE_PUMP", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("TYPE_PUMP", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for TYPE_PUMP");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;

    }

    public ArrayList<TypeOfPump> getPumpType() {
        ArrayList<TypeOfPump> pumpArrayList = new ArrayList<>();
        pumpArrayList.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from TYPE_PUMP";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                TypeOfPump designation = new TypeOfPump();
                designation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                designation.setName(cursor.getString(cursor.getColumnIndex("name")));
                pumpArrayList.add(designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pumpArrayList;
    }

    public long saveProposals(ArrayList<ProposalTypeEntity> proposalTypeEntities) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (ProposalTypeEntity proposalTypeEntity : proposalTypeEntities) {
                ContentValues values = new ContentValues();
                values.put("_id", proposalTypeEntity.getId().trim());
                values.put("name", proposalTypeEntity.getValue().trim());
                String[] whereArgs = new String[]{proposalTypeEntity.getId().trim()};
                c = db.update("PROPOSAL_TYPE", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("PROPOSAL_TYPE", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for PROPOSAL_TYPE");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<ProposalTypeEntity> getProposalType() {
        ArrayList<ProposalTypeEntity> pumpArrayList = new ArrayList<>();
        pumpArrayList.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from PROPOSAL_TYPE";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                ProposalTypeEntity designation = new ProposalTypeEntity();
                designation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                designation.setValue(cursor.getString(cursor.getColumnIndex("name")));
                pumpArrayList.add(designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pumpArrayList;
    }

    public long saveWeightCategories(ArrayList<WeightCategoriesEntity> weightCategoriesEntities) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (WeightCategoriesEntity weightCategoriesEntity : weightCategoriesEntities) {
                ContentValues values = new ContentValues();
                values.put("_id", weightCategoriesEntity.getId().trim());
                values.put("name", weightCategoriesEntity.getValue().trim());
                values.put("pro_id", weightCategoriesEntity.getPro_id().trim());
                values.put("categoryOrder", weightCategoriesEntity.getCategoryOrder().trim());
                values.put("validity", weightCategoriesEntity.getValidity().trim());
                String[] whereArgs = new String[]{weightCategoriesEntity.getId().trim()};
                c = db.update("WEIGHT_CAT", values, "_id=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("WEIGHT_CAT", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for WEIGHT_CAT");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<WeightCategoriesEntity> getWeightCategories(String proposalId) {
        ArrayList<WeightCategoriesEntity> pumpArrayList = new ArrayList<>();
        pumpArrayList.clear();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from WEIGHT_CAT where pro_id='" + proposalId + "' order by categoryOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                WeightCategoriesEntity designation = new WeightCategoriesEntity();
                designation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                designation.setValue(cursor.getString(cursor.getColumnIndex("name")));
                designation.setPro_id(cursor.getString(cursor.getColumnIndex("pro_id")));
                designation.setCategoryOrder(cursor.getString(cursor.getColumnIndex("categoryOrder")));
                designation.setValidity(cursor.getString(cursor.getColumnIndex("validity")));
                pumpArrayList.add(designation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pumpArrayList;
    }

    public long saveWei_Denomination(ArrayList<DenomintionEntity> denomintionEntities) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (DenomintionEntity denomintionEntity : denomintionEntities) {
                ContentValues values = new ContentValues();
                values.put("value", Integer.parseInt(denomintionEntity.getValue().trim()));
                values.put("name", denomintionEntity.getName().trim());
                if (denomintionEntity.getVendorId() != null)
                    values.put("vendorId", denomintionEntity.getVendorId().trim());
                if (denomintionEntity.getVcId() != null)
                    values.put("vcId", denomintionEntity.getVcId().trim());
                values.put("categoryId", Integer.parseInt(denomintionEntity.getCategoryId().trim()));
                values.put("denominationDesc", denomintionEntity.getDenominationDesc().trim());
                values.put("denominationOrder", Integer.parseInt(denomintionEntity.getDenominationOrder().trim()));
                //values.put("remarks", denomintionEntity.getRemarks().trim());
                values.put("set_m", denomintionEntity.getSet_m().trim());
                values.put("fee", denomintionEntity.getFee().trim());
                values.put("pro_id", "0");
                String[] whereArgs = new String[]{denomintionEntity.getValue().trim()};
                c = db.update("W_DENOMINATION", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("W_DENOMINATION", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for WEIGHT_CAT");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }


    public ArrayList<DenomintionEntity> getWeightDenomination(String cat_id, String flag) {
        ArrayList<DenomintionEntity> denomintionEntities = new ArrayList<>();
        denomintionEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            if (Integer.parseInt(flag) == 1)
                quary = "select * from W_DENOMINATION where categoryId='" + cat_id + "' order by  categoryId ASC,denominationOrder ASC";
            else
                quary = "select * from W_DENOMINATION where checked='Y' order by categoryId ASC,denominationOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                DenomintionEntity denomintionEntity = new DenomintionEntity();
                denomintionEntity.setValue(String.valueOf(cursor.getInt(cursor.getColumnIndex("value"))));
                denomintionEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                denomintionEntity.setVendorId(cursor.isNull(cursor.getColumnIndex("vendorId")) ? null : cursor.getString(cursor.getColumnIndex("vendorId")));
                denomintionEntity.setVcId(cursor.isNull(cursor.getColumnIndex("vcId")) ? null : cursor.getString(cursor.getColumnIndex("vcId")));
                denomintionEntity.setCategoryId(String.valueOf(cursor.getInt(cursor.getColumnIndex("categoryId"))));
                denomintionEntity.setDenominationDesc(cursor.getString(cursor.getColumnIndex("denominationDesc")));
                denomintionEntity.setDenominationOrder(String.valueOf(cursor.getInt(cursor.getColumnIndex("denominationOrder"))));
                //denomintionEntity.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                denomintionEntity.setChecked(cursor.getString(cursor.getColumnIndex("checked")));
                denomintionEntity.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                denomintionEntity.setSet_m(cursor.getString(cursor.getColumnIndex("set_m")));
                denomintionEntity.setFee(cursor.getString(cursor.getColumnIndex("fee")));
                denomintionEntity.setVal_year(cursor.getString(cursor.getColumnIndex("val_year")));
                denomintionEntity.setPro_id(cursor.getString(cursor.getColumnIndex("pro_id")));
                denomintionEntity.setIs_set(cursor.getString(cursor.getColumnIndex("is_set")));
                denomintionEntities.add(denomintionEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return denomintionEntities;
    }

    public DenomintionEntity getWeightDenominationByID(String den_id) {
        DenomintionEntity denomintionEntity = null;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from W_DENOMINATION where value='" + den_id + "' order by  categoryId ASC,denominationOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                denomintionEntity = new DenomintionEntity();
                denomintionEntity.setValue(String.valueOf(cursor.getInt(cursor.getColumnIndex("value"))));
                denomintionEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                denomintionEntity.setVendorId(cursor.isNull(cursor.getColumnIndex("vendorId")) ? null : cursor.getString(cursor.getColumnIndex("vendorId")));
                denomintionEntity.setVcId(cursor.isNull(cursor.getColumnIndex("vcId")) ? null : cursor.getString(cursor.getColumnIndex("vcId")));
                denomintionEntity.setCategoryId(String.valueOf(cursor.getInt(cursor.getColumnIndex("categoryId"))));
                denomintionEntity.setDenominationDesc(cursor.getString(cursor.getColumnIndex("denominationDesc")));
                denomintionEntity.setDenominationOrder(String.valueOf(cursor.getInt(cursor.getColumnIndex("denominationOrder"))));
                //denomintionEntity.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                denomintionEntity.setChecked(cursor.getString(cursor.getColumnIndex("checked")));
                denomintionEntity.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                denomintionEntity.setSet_m(cursor.getString(cursor.getColumnIndex("set_m")));
                denomintionEntity.setFee(cursor.getString(cursor.getColumnIndex("fee")));
                denomintionEntity.setVal_year(cursor.getString(cursor.getColumnIndex("val_year")));
                denomintionEntity.setPro_id(cursor.getString(cursor.getColumnIndex("pro_id")));
                denomintionEntity.setIs_set(cursor.getString(cursor.getColumnIndex("is_set")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return denomintionEntity;
    }

    public long updateWeightDenominationBetween(String cat_id, String max, String min, int no_of_set, String val_year, String pro_id) {
        long c = 0;
        boolean is_lst_mem = false;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select value,set_m,quantity from W_DENOMINATION  where value BETWEEN '" + min + "' and '" + max + "' and categoryId='" + cat_id + "' order by denominationOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                c++;
                if (cursor.isLast()) {
                    is_lst_mem = true;
                } else {
                    is_lst_mem = false;
                }
                updateDenomination("", "", String.valueOf(cursor.getInt(cursor.getColumnIndex("value"))), "Y", cursor.getString(cursor.getColumnIndex("set_m")), no_of_set, cursor.getString(cursor.getColumnIndex("quantity")), val_year, pro_id, is_lst_mem);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public long updateDenomination(String vanderId, String vcId, String den_id, String flag, String set_quantity, int no_of_set, String qty_total, String val_year, String pro_id, boolean is_set_last_mem) {
        long c = -1;
        int qt_now = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (no_of_set > 0) {
                if (is_set_last_mem) {
                    qt_now = 1;
                } else if (Integer.parseInt(qty_total) > 0) {
                    qt_now = (Integer.parseInt(set_quantity) * no_of_set) + Integer.parseInt(qty_total);
                } else {
                    qt_now = Integer.parseInt(set_quantity) * no_of_set;
                }
            } else {
                qt_now = Integer.parseInt(set_quantity);
            }
            ContentValues values = new ContentValues();
            if (!vanderId.equals("")) values.put("vendorId", vanderId);
            if (!vcId.equals("")) values.put("vcId", vcId);
            values.put("checked", flag);
            values.put("quantity", String.valueOf(qt_now));
            values.put("val_year", val_year);
            values.put("pro_id", pro_id.trim());
            values.put("is_set", String.valueOf(no_of_set));
            String[] whereArgs = new String[]{den_id.trim()};
            c = db.update("W_DENOMINATION", values, "value=?", whereArgs);
        } catch (Exception e) {
            Log.e("ERROR 3", " Updating W_DENOMINATION");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;

    }

    public long getAddedWeightCount() {
        long c = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from W_DENOMINATION where checked='Y'";
            Cursor cursor = db.rawQuery(quary, null);
            c = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public void deleteAllPatner() {

        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            if (getAllPatnersCount() > 0) {
                sqLiteDatabase.execSQL("delete from PATNER_ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }

    }

    public long saveInsProposal(ArrayList<InsProposalEntity> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (InsProposalEntity insProposalEntity : res) {
                ContentValues values = new ContentValues();
                values.put("value", insProposalEntity.getValue().trim());
                values.put("name", insProposalEntity.getName().trim());
                String[] whereArgs = new String[]{insProposalEntity.getValue().trim()};
                c = db.update("INS_PROPOSAL", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("INS_PROPOSAL", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for INS_PROPOSAL");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<InsProposalEntity> getInsProposal() {
        ArrayList<InsProposalEntity> denomintionEntities = new ArrayList<>();
        denomintionEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from INS_PROPOSAL order by name ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                InsProposalEntity insProposalEntity = new InsProposalEntity();
                insProposalEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                insProposalEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                denomintionEntities.add(insProposalEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return denomintionEntities;
    }

    public InsProposalEntity getINSProByID(String pro) {
        InsProposalEntity proposalTypeEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from INS_PROPOSAL where value='" + pro + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                proposalTypeEntity = new InsProposalEntity();
                proposalTypeEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                proposalTypeEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return proposalTypeEntity;
        }
        return proposalTypeEntity;
    }

    public long saveInsCategory(ArrayList<InsCategoryEntity> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (InsCategoryEntity insCategoryEntity : res) {
                ContentValues values = new ContentValues();
                values.put("value", insCategoryEntity.getValue().trim());
                values.put("name", insCategoryEntity.getName().trim());
                values.put("proposalId", insCategoryEntity.getProposalId().trim());
                values.put("categoryOrder", insCategoryEntity.getCategoryOrder().trim());
                values.put("validity", insCategoryEntity.getValidity().trim());
                String[] whereArgs = new String[]{insCategoryEntity.getValue().trim()};
                c = db.update("INS_CATEGORY", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("INS_CATEGORY", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for INS_CATEGORY");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<InsCategoryEntity> getInsCategory(String pro_id) {
        ArrayList<InsCategoryEntity> insCategoryEntities = new ArrayList<>();
        insCategoryEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from INS_CATEGORY where proposalId='" + pro_id + "'order by categoryOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                InsCategoryEntity insCategoryEntity = new InsCategoryEntity();
                insCategoryEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                insCategoryEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                insCategoryEntity.setProposalId(cursor.getString(cursor.getColumnIndex("proposalId")));
                insCategoryEntity.setCategoryOrder(cursor.getString(cursor.getColumnIndex("categoryOrder")));
                insCategoryEntity.setValidity(cursor.getString(cursor.getColumnIndex("validity")));
                insCategoryEntities.add(insCategoryEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insCategoryEntities;
    }

    public InsCategoryEntity getINSCatByID(String cat_id) {
        InsCategoryEntity insCategoryEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from INS_CATEGORY where value='" + cat_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                insCategoryEntity = new InsCategoryEntity();
                insCategoryEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                insCategoryEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return insCategoryEntity;
        }
        return insCategoryEntity;
    }

    public long saveInsCapacity(ArrayList<InsCapacityEntity> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (InsCapacityEntity capacityEntity : res) {
                ContentValues values = new ContentValues();
                values.put("capacityId", capacityEntity.getCapacityId().trim());
                values.put("categoryId", capacityEntity.getCategoryId().trim());
                values.put("capacityValue", capacityEntity.getCapacityValue().trim());
                values.put("capacityDesc", capacityEntity.getCapacityDesc().trim());
                values.put("capacityOrder", capacityEntity.getCapacityOrder().trim());
                String[] whereArgs = new String[]{capacityEntity.getCapacityId().trim()};
                c = db.update("INS_CAPACITY", values, "capacityId=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("INS_CAPACITY", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for INS_CAPACITY");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }


    public ArrayList<InsCapacityEntity> getInsCapacity(String cat_id, String flag) {
        ArrayList<InsCapacityEntity> insCapacityEntities = new ArrayList<>();
        insCapacityEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            if (Integer.parseInt(flag) == 1)
                quary = "select * from INS_CAPACITY where categoryId='" + cat_id + "'order by categoryId ASC,capacityOrder ASC";
            else
                quary = "select * from INS_CAPACITY where categoryId='" + cat_id + "'order by categoryId ASC,capacityOrder ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                InsCapacityEntity insCapacityEntity = new InsCapacityEntity();
                insCapacityEntity.setCapacityId(cursor.getString(cursor.getColumnIndex("capacityId")));
                insCapacityEntity.setCategoryId(cursor.getString(cursor.getColumnIndex("categoryId")));
                insCapacityEntity.setCapacityValue(cursor.getString(cursor.getColumnIndex("capacityValue")));
                insCapacityEntity.setCapacityDesc(cursor.getString(cursor.getColumnIndex("capacityDesc")));
                insCapacityEntity.setCapacityOrder(cursor.getString(cursor.getColumnIndex("capacityOrder")));
                insCapacityEntity.setChecked(cursor.getString(cursor.getColumnIndex("checked")));
                insCapacityEntities.add(insCapacityEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insCapacityEntities;
    }

    public InsCapacityEntity getINSCapByID(String cap_id) {
        InsCapacityEntity insCapacityEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from INS_CAPACITY where capacityId='" + cap_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                insCapacityEntity = new InsCapacityEntity();
                insCapacityEntity.setCapacityId(cursor.getString(cursor.getColumnIndex("capacityId")));
                insCapacityEntity.setCategoryId(cursor.getString(cursor.getColumnIndex("categoryId")));
                insCapacityEntity.setCapacityValue(cursor.getString(cursor.getColumnIndex("capacityValue")));
                insCapacityEntity.setCapacityDesc(cursor.getString(cursor.getColumnIndex("capacityDesc")));
                insCapacityEntity.setCapacityOrder(cursor.getString(cursor.getColumnIndex("capacityOrder")));
                insCapacityEntity.setChecked(cursor.getString(cursor.getColumnIndex("checked")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return insCapacityEntity;
        }
        return insCapacityEntity;
    }


    public long updateCapacity(String value, String flag) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("checked", flag);
            String[] whereArgs = new String[]{value.trim()};
            c = db.update("INS_CAPACITY", values, "value=?", whereArgs);
        } catch (Exception e) {
            Log.e("ERROR 3", " Updating INS_CAPACITY");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;

    }

    public long getAddedCapacityCount() {
        long c = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from INS_CAPACITY where checked='Y'";
            Cursor cursor = db.rawQuery(quary, null);
            c = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public long addPartner(PatnerEntity patnerEntity) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            if (patnerEntity.getPartnerId() != null)
                values.put("partnerId", patnerEntity.getPartnerId().trim());
            if (patnerEntity.getVendorId() != null)
                values.put("vendorId", patnerEntity.getVendorId().trim());
            values.put("firm_type", patnerEntity.getFirm_type().trim());
            values.put("date_of_com", patnerEntity.getDate_of_com().trim());
            values.put("place_of_varifi", patnerEntity.getPlace_of_var().trim());
            values.put("designation_id", patnerEntity.getDesignation_id().trim());
            values.put("name", patnerEntity.getName().trim());
            values.put("father_name", patnerEntity.getFather_name().trim());
            values.put("adhar_vid", patnerEntity.getAdhar_vid().trim());
            values.put("add1", patnerEntity.getAdd1().trim());
            values.put("add2", patnerEntity.getAdd2().trim());
            values.put("landmark", patnerEntity.getLandmark().trim());
            values.put("city", patnerEntity.getCity().trim());
            values.put("district", patnerEntity.getDistrict().trim());
            values.put("block", patnerEntity.getBlock().trim());
            values.put("pincode", patnerEntity.getPinCode().trim());
            values.put("mobile", patnerEntity.getMobile().trim());
            values.put("landline", patnerEntity.getLandline().trim());
            values.put("email", patnerEntity.getEmail().trim());
            values.put("is_nom_under49", patnerEntity.getIs_nom_under49().trim());
            c = db.insert("PATNER_ADD", null, values);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            c = 0;
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public long updatePartner(PatnerEntity patnerEntity) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("firm_type", patnerEntity.getFirm_type().trim());
            values.put("date_of_com", patnerEntity.getDate_of_com().trim());
            values.put("place_of_varifi", patnerEntity.getPlace_of_var().trim());
            values.put("designation_id", patnerEntity.getDesignation_id().trim());
            values.put("name", patnerEntity.getName().trim());
            values.put("father_name", patnerEntity.getFather_name().trim());
            values.put("adhar_vid", patnerEntity.getAdhar_vid().trim());
            values.put("add1", patnerEntity.getAdd1().trim());
            values.put("add2", patnerEntity.getAdd2().trim());
            values.put("landmark", patnerEntity.getLandmark().trim());
            values.put("city", patnerEntity.getCity().trim());
            values.put("district", patnerEntity.getDistrict().trim());
            values.put("block", patnerEntity.getBlock().trim());
            values.put("pincode", patnerEntity.getPinCode().trim());
            values.put("mobile", patnerEntity.getMobile().trim());
            values.put("landline", patnerEntity.getLandline().trim());
            values.put("email", patnerEntity.getEmail().trim());
            values.put("is_nom_under49", patnerEntity.getIs_nom_under49().trim());
            String[] whereArgs = new String[]{patnerEntity.getId().trim()};
            c = db.update("PATNER_ADD", values, "_id=?", whereArgs);

        } catch (Exception e) {
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for PATNER");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
            c = 0;
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<PatnerEntity> getAllPatners() {
        ArrayList<PatnerEntity> patnerEntities = new ArrayList<>();
        patnerEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from PATNER_ADD order by _id ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                PatnerEntity patnerEntity = new PatnerEntity();
                patnerEntity.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                patnerEntity.setVendorId(cursor.isNull(cursor.getColumnIndex("vendorId")) ? null : cursor.getString(cursor.getColumnIndex("vendorId")));
                patnerEntity.setPartnerId(cursor.isNull(cursor.getColumnIndex("partnerId")) ? null : cursor.getString(cursor.getColumnIndex("partnerId")));
                patnerEntity.setFirm_type(cursor.getString(cursor.getColumnIndex("firm_type")));
                patnerEntity.setFirm_type(cursor.getString(cursor.getColumnIndex("firm_type")));
                patnerEntity.setDate_of_com(cursor.getString(cursor.getColumnIndex("date_of_com")));
                patnerEntity.setPlace_of_var(cursor.getString(cursor.getColumnIndex("place_of_varifi")));
                patnerEntity.setDesignation_id(cursor.getString(cursor.getColumnIndex("designation_id")));
                patnerEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                patnerEntity.setFather_name(cursor.getString(cursor.getColumnIndex("father_name")));
                patnerEntity.setAdhar_vid(cursor.getString(cursor.getColumnIndex("adhar_vid")));
                patnerEntity.setAdd1(cursor.getString(cursor.getColumnIndex("add1")));
                patnerEntity.setAdd2(cursor.getString(cursor.getColumnIndex("add2")));
                patnerEntity.setLandmark(cursor.getString(cursor.getColumnIndex("landmark")));
                patnerEntity.setCity(cursor.getString(cursor.getColumnIndex("city")));
                patnerEntity.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                patnerEntity.setBlock(cursor.getString(cursor.getColumnIndex("block")));
                patnerEntity.setPinCode(cursor.getString(cursor.getColumnIndex("pincode")));
                patnerEntity.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                patnerEntity.setLandline(cursor.getString(cursor.getColumnIndex("landline")));
                patnerEntity.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                patnerEntity.setIs_nom_under49(cursor.getString(cursor.getColumnIndex("is_nom_under49")));
                patnerEntities.add(patnerEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patnerEntities;
    }

    public long getAllPatnersCount() {

        long c = -1;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            quary = "select _id from PATNER_ADD";
            Cursor cursor = db.rawQuery(quary, null);
            c = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            c = 0;
        }
        return c;
    }

    public long deletePartner(String id) {
        long c = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            c = db.delete("PATNER_ADD", "_id=?", new String[]{id.trim()});
        } catch (Exception e) {
            e.printStackTrace();
            c = 0;
        }
        return c;
    }

    /* public int deleteAllPatner() {
         int c = -1;
         try {
             SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
             if (getAllPatnersCount() > 0) {
                 sqLiteDatabase.execSQL("delete from PATNER_ADD");
             }
         } catch (Exception e) {
             e.printStackTrace();
             return c;
         }
         return c;
     }*/

    public String getFirmType(String firm_type_id) {
        String firm = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from FIRM_TYPE where _id='" + firm_type_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                firm = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firm;
    }

    public String getFirmTypeByID(String firm_type_id) {
        String firm = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from FIRM_TYPE where _id='" + firm_type_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                firm = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firm;
    }

    public String getDesignationName(String designation_id) {
        String desig = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from DESIGNATION where _id='" + designation_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                desig = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desig;
    }

    public WeightCategoriesEntity getCategoryById(String cai_id) {
        WeightCategoriesEntity waEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from WEIGHT_CAT where _id='" + cai_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                waEntity = new WeightCategoriesEntity();
                waEntity.setId(cursor.getString(cursor.getColumnIndex("_id")));
                waEntity.setValue(cursor.getString(cursor.getColumnIndex("name")));
                waEntity.setPro_id(cursor.getString(cursor.getColumnIndex("pro_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return waEntity;
        }
        return waEntity;
    }

    public ProposalTypeEntity getProByID(String pro) {
        ProposalTypeEntity proposalTypeEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from PROPOSAL_TYPE where _id='" + pro + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                proposalTypeEntity = new ProposalTypeEntity();
                proposalTypeEntity.setId(cursor.getString(cursor.getColumnIndex("_id")));
                proposalTypeEntity.setValue(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return proposalTypeEntity;
        }
        return proposalTypeEntity;
    }

    public long addInstrument(InstrumentEntity instrumentEntity) {
        long c = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (instrumentEntity.getVendorId() != null)
                contentValues.put("vendorId", instrumentEntity.getVendorId());
            if (instrumentEntity.getVcId() != null)
                contentValues.put("vcId", instrumentEntity.getVcId());
            contentValues.put("pro_id", "" + instrumentEntity.getPro_id());
            contentValues.put("cat_id", "" + instrumentEntity.getCat_id());
            contentValues.put("cap_id", "" + instrumentEntity.getCap_id());
            contentValues.put("quantity", "" + instrumentEntity.getQuantity());
            contentValues.put("m_class", "" + instrumentEntity.getIns_class());
            contentValues.put("m_or_brand", "" + instrumentEntity.getM_or_brand());
            contentValues.put("val_year", "" + instrumentEntity.getVal_year());
            contentValues.put("cap_max", "" + instrumentEntity.getCap_max());
            contentValues.put("cap_min", "" + instrumentEntity.getCap_min());
            contentValues.put("model_no", "" + instrumentEntity.getModel_no());
            contentValues.put("ser_no", "" + instrumentEntity.getSer_no());
            contentValues.put("e_val", "" + instrumentEntity.getE_val());
            //c = sqLiteDatabase.update("INSTRUMENT_ADD", contentValues, "pro_id=? AND cat_id=? AND cap_id=?", new String[]{instrumentEntity.getPro_id(), instrumentEntity.getCat_id(), instrumentEntity.getCap_id()});
            //if (c <= 0)
            c = sqLiteDatabase.insert("INSTRUMENT_ADD", null, contentValues);
            if (c > 0) {
               /* if (getSelectedInsClass(String.valueOf(c)).size() > 0) {
                    deleteSelectedClassByID(String.valueOf(c));
                    this.saveSelectedClassINS(instrumentEntity.getIns_class(), String.valueOf(c));
                }*/
                //if (getAddedNozzle(String.valueOf(c)).size()>0){
                //deleteAllNozzleofINS(String.valueOf(c));
                //this.addNozzle(instrumentEntity.getNozzles(), String.valueOf(c));
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
            return c;
        }
        return c;
    }

    public ArrayList<InstrumentEntity> getInstrumentAdded() {
        ArrayList<InstrumentEntity> instrumentEntities = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "Select * from INSTRUMENT_ADD order by _id";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                InstrumentEntity instrumentEntity = new InstrumentEntity();
                instrumentEntity.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                instrumentEntity.setVendorId(cursor.isNull(cursor.getColumnIndex("vendorId")) ? null : cursor.getString(cursor.getColumnIndex("vendorId")));
                instrumentEntity.setVcId(cursor.isNull(cursor.getColumnIndex("vcId")) ? null : cursor.getString(cursor.getColumnIndex("vcId")));
                instrumentEntity.setPro_id(cursor.getString(cursor.getColumnIndex("pro_id")));
                instrumentEntity.setCat_id(cursor.getString(cursor.getColumnIndex("cat_id")));
                instrumentEntity.setCap_id(cursor.getString(cursor.getColumnIndex("cap_id")));
                instrumentEntity.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                //instrumentEntity.setIns_class(getSelectedInsClass(instrumentEntity.getId()));
                instrumentEntity.setIns_class(cursor.getString(cursor.getColumnIndex("m_class")));
                instrumentEntity.setM_or_brand(cursor.getString(cursor.getColumnIndex("m_or_brand")));
                instrumentEntity.setVal_year(cursor.getString(cursor.getColumnIndex("val_year")));
                instrumentEntity.setCap_max(cursor.getString(cursor.getColumnIndex("cap_max")));
                instrumentEntity.setCap_min(cursor.getString(cursor.getColumnIndex("cap_min")));
                instrumentEntity.setModel_no(cursor.getString(cursor.getColumnIndex("model_no")));
                instrumentEntity.setSer_no(cursor.getString(cursor.getColumnIndex("ser_no")));
                instrumentEntity.setE_val(cursor.getString(cursor.getColumnIndex("e_val")));
                instrumentEntity.setNozzles(getAddedNozzle(instrumentEntity.getId()));
                instrumentEntities.add(instrumentEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instrumentEntities;
    }

    public long getInstrumentAddedCount() {
        long c = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "Select _id from INSTRUMENT_ADD order by _id";
            Cursor cursor = db.rawQuery(quary, null);
            c = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return c;
    }

    public void deleteAllInstruments() {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            if (getInstrumentAddedCount() > 0) {
                sqLiteDatabase.execSQL("delete from INSTRUMENT_ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long deleteSingleInstrument(String id) {
        long c = -1, c1 = -1;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            if (getInstrumentAddedCount() > 0) {
                c = db.delete("INSTRUMENT_ADD", "_id=?", new String[]{id.trim()});
                if (getNozzleCountBySL_ID(id.trim()) > 0) {
                    c1 = db.delete("NOZZLE", "sl_id=?", new String[]{id.trim()});
                    String msg = (c1 > 0) ? "Nozzle deleted" : "Nozzle not deleted";
                    Log.e("database-1359", "***** " + msg + " *******");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return c;
        } finally {
            db.close();
        }
        return c;
    }


    public void updateweightDenomination() {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("update W_DENOMINATION SET checked='N',quantity='0'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long saveClass(ArrayList<Class_ins> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (Class_ins class_ins : res) {
                ContentValues values = new ContentValues();
                values.put("value", class_ins.getValue().trim());
                values.put("name", class_ins.getName().trim());
                String[] whereArgs = new String[]{class_ins.getValue().trim()};
                c = db.update("CLASS_INS", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("CLASS_INS", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for CLASS_INS");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<Class_ins> getInsClass(int flag) {
        ArrayList<Class_ins> insArrayList = new ArrayList<>();
        insArrayList.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            if (flag == 0) quary = "select * from CLASS_INS order by name ASC";
            else quary = "select * from CLASS_INS where is_selected='Y' order by name ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                Class_ins ins_class = new Class_ins();
                ins_class.setValue(cursor.getString(cursor.getColumnIndex("value")));
                ins_class.setName(cursor.getString(cursor.getColumnIndex("name")));
                ins_class.setIs_selected(cursor.getString(cursor.getColumnIndex("is_selected")));
                insArrayList.add(ins_class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insArrayList;
    }

    public long updateInsClassParticular(String is_selected, String classid) {
        long return_is = -1;
        SQLiteDatabase sqLiteOpenHelper = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("is_selected", is_selected);
            return_is = sqLiteOpenHelper.update("CLASS_INS", values, "value=?", new String[]{classid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return return_is;
    }

    public void updateAllInsClass() {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("update CLASS_INS SET is_selected='N'");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Class_ins getINSClsByID(String ins_classs) {
        Class_ins class_ins = null;
        String quary = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            quary = "select * from CLASS_INS where value='" + ins_classs + "'order by name ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                class_ins = new Class_ins();
                class_ins.setValue(cursor.getString(cursor.getColumnIndex("value")));
                class_ins.setName(cursor.getString(cursor.getColumnIndex("name")));
                class_ins.setIs_selected(cursor.getString(cursor.getColumnIndex("is_selected")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return class_ins;
    }

    public long saveSelectedClassINS(ArrayList<Class_ins> class_ins_list, String _id) {
        long c = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            for (Class_ins class_ins : class_ins_list) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", class_ins.getName());
                contentValues.put("value", class_ins.getValue());
                contentValues.put("_id", _id);
                c = db.update("CLASS_INS_SELECTED", contentValues, "_id=? and value=?", new String[]{_id, class_ins.getValue()});
                if (c <= 0) {
                    c = db.insert("CLASS_INS_SELECTED", null, contentValues);
                }
                updateAllInsClass();
            }
        } catch (Exception e) {
            e.printStackTrace();
            c = -1;
        } finally {
            db.close();
        }
        return c;
    }

    public ArrayList<Class_ins> getSelectedInsClass(String id) {
        ArrayList<Class_ins> classInsArrayList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("select * from CLASS_INS_SELECTED where _id=?", new String[]{id});
            while (cursor.moveToNext()) {
                Class_ins class_ins = new Class_ins();
                class_ins.setValue(cursor.getString(cursor.getColumnIndex("value")));
                class_ins.setName(cursor.getString(cursor.getColumnIndex("name")));
                class_ins.set_id(cursor.getString(cursor.getColumnIndex("_id")));
                classInsArrayList.add(class_ins);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return classInsArrayList;
    }

    public void deleteAllSelectedClass() {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from CLASS_INS_SELECTED");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    public void deleteSelectedClassByID(String _id) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from CLASS_INS_SELECTED where _id='" + _id + "'");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    public long saveBlock(ArrayList<Block> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (Block block : res) {
                ContentValues values = new ContentValues();
                values.put("value", block.getValue().trim());
                values.put("name", block.getName().trim());
                values.put("districtId", block.getDistrictId().trim());
                if (block.getEstbSubdivId() != null) {
                    if (!block.getEstbSubdivId().equals("null")) {
                        values.put("estbSubdivId", block.getEstbSubdivId().trim());
                    } else {
                        values.put("estbSubdivId", "145");
                    }
                } else {
                    values.put("estbSubdivId", "145");
                }
                String[] whereArgs = new String[]{block.getValue().trim()};
                c = db.update("BLOCK", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("BLOCK", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for BLOCK");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<Block> getBlock(String districtid) {
        ArrayList<Block> blockArrayList = new ArrayList<>();
        blockArrayList.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from BLOCK where districtId='" + districtid + "' order by name ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                Block block = new Block();
                block.setValue(cursor.getString(cursor.getColumnIndex("value")).trim());
                block.setName(cursor.getString(cursor.getColumnIndex("name")).trim());
                block.setDistrictId(cursor.getString(cursor.getColumnIndex("districtId")));
                block.setEstbSubdivId(cursor.getString(cursor.getColumnIndex("estbSubdivId")));
                blockArrayList.add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockArrayList;
    }

    public Block getBlockByID(String block_id) {
        Block block = null;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from BLOCK where value='" + block_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                block = new Block();
                block.setValue(cursor.getString(cursor.getColumnIndex("value")));
                block.setName(cursor.getString(cursor.getColumnIndex("name")));
                block.setDistrictId(cursor.getString(cursor.getColumnIndex("districtId")));
                block.setEstbSubdivId(cursor.getString(cursor.getColumnIndex("estbSubdivId")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return block;
    }

    public long savePremises(ArrayList<PremisesTypeEntity> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (PremisesTypeEntity typeEntity : res) {
                ContentValues values = new ContentValues();
                values.put("value", typeEntity.getValue().trim());
                values.put("name", typeEntity.getName().trim());
                String[] whereArgs = new String[]{typeEntity.getValue().trim()};
                c = db.update("PREMISES", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("PREMISES", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for PremisesTypeEntity");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<PremisesTypeEntity> getPremises() {
        ArrayList<PremisesTypeEntity> blockArrayList = new ArrayList<>();
        blockArrayList.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from PREMISES order by name ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                PremisesTypeEntity typeEntity = new PremisesTypeEntity();
                typeEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                typeEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                blockArrayList.add(typeEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockArrayList;
    }

    public PremisesTypeEntity getPremissesByID(String p_id) {
        PremisesTypeEntity typeEntity = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "select * from PREMISES where value='" + p_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                typeEntity = new PremisesTypeEntity();
                typeEntity.setValue(cursor.getString(cursor.getColumnIndex("value")));
                typeEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeEntity;
    }

    public long saveSubDivision(ArrayList<SubDivision> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (SubDivision typeEntity : res) {
                ContentValues values = new ContentValues();
                values.put("subDivId", typeEntity.getSubDivId().trim());
                values.put("subDivName", typeEntity.getSubDivName().trim());
                values.put("distId", typeEntity.getDistId().trim());
                String[] whereArgs = new String[]{typeEntity.getSubDivId().trim()};
                c = db.update("SUB_DIVISION", values, "subDivId=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("SUB_DIVISION", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for SUB_DIVISION");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<SubDivision> getSubDivision(String subdiv_id) {
        ArrayList<SubDivision> subDivisions = new ArrayList<>();
        subDivisions.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from SUB_DIVISION where subDivId='" + subdiv_id + "'order by subDivName ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                SubDivision subDivision = new SubDivision();
                subDivision.setSubDivId(cursor.getString(cursor.getColumnIndex("subDivId")));
                subDivision.setSubDivName(cursor.getString(cursor.getColumnIndex("subDivName")));
                subDivision.setDistId(cursor.getString(cursor.getColumnIndex("distId")));
                subDivisions.add(subDivision);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subDivisions;
    }


    public SubDivision getSubDivisionByID(String sub_div_id) {
        SubDivision subDivision = null;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from SUB_DIVISION where subDivId='" + sub_div_id + "'order by subDivName ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                subDivision = new SubDivision();
                subDivision.setSubDivId(cursor.getString(cursor.getColumnIndex("subDivId")));
                subDivision.setSubDivName(cursor.getString(cursor.getColumnIndex("subDivName")));
                subDivision.setDistId(cursor.getString(cursor.getColumnIndex("distId")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return subDivision;
    }


    public long saveThana(ArrayList<ThanaEntity> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (ThanaEntity typeEntity : res) {
                ContentValues values = new ContentValues();
                values.put("thanaCode", typeEntity.getThanaCode().trim());
                values.put("thanaName", typeEntity.getThanaName().trim());
                values.put("policeStationName", typeEntity.getPoliceStationName().trim());
                values.put("districtId", typeEntity.getDistrictId().trim());
                values.put("admDistrictId", typeEntity.getAdmDistrictId().trim());
                values.put("blockCode", typeEntity.getBlockCode().trim());
                String[] whereArgs = new String[]{typeEntity.getThanaCode().trim()};
                c = db.update("THANA", values, "thanaCode=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("THANA", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for THANA");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<ThanaEntity> getThanaAll(String block_id) {
        ArrayList<ThanaEntity> thanaEntities = new ArrayList<>();
        thanaEntities.clear();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from THANA where blockCode='" + block_id + "'order by thanaName ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                ThanaEntity thanaEntity = new ThanaEntity();
                thanaEntity.setThanaCode(cursor.getString(cursor.getColumnIndex("thanaCode")));
                thanaEntity.setThanaName(cursor.getString(cursor.getColumnIndex("thanaName")));
                thanaEntity.setPoliceStationName(cursor.getString(cursor.getColumnIndex("policeStationName")));
                thanaEntity.setDistrictId(cursor.getString(cursor.getColumnIndex("districtId")));
                thanaEntity.setAdmDistrictId(cursor.getString(cursor.getColumnIndex("admDistrictId")));
                thanaEntity.setBlockCode(cursor.getString(cursor.getColumnIndex("blockCode")));
                thanaEntities.add(thanaEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thanaEntities;
    }


    public ThanaEntity getThanaByID(String thana_id) {
        ThanaEntity thanaEntity = null;
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select * from THANA where thanaCode='" + thana_id + "'";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                thanaEntity = new ThanaEntity();
                thanaEntity.setThanaCode(cursor.getString(cursor.getColumnIndex("thanaCode")));
                thanaEntity.setThanaName(cursor.getString(cursor.getColumnIndex("thanaName")));
                thanaEntity.setPoliceStationName(cursor.getString(cursor.getColumnIndex("policeStationName")));
                thanaEntity.setDistrictId(cursor.getString(cursor.getColumnIndex("districtId")));
                thanaEntity.setAdmDistrictId(cursor.getString(cursor.getColumnIndex("admDistrictId")));
                thanaEntity.setBlockCode(cursor.getString(cursor.getColumnIndex("blockCode")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return thanaEntity;
    }

    public long saveVender(String ven_id, String app_id) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("app_id", app_id);
            values.put("vender_id", ven_id);
            String[] whereArgs = new String[]{ven_id.trim()};
            c = db.update("AddedVender", values, "vender_id=?", whereArgs);
            if (!(c > 0)) {
                c = db.insert("AddedVender", null, values);
            }

        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for AddedVender");
            // TODO: handle exception
        } finally {
            db.close();
            this.getWritableDatabase().close();
        }
        return c;
    }

    public ArrayList<String> getVendersAdded(String app_id) {
        ArrayList<String> arrayList = new ArrayList<>();
        String quary = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            quary = "select vender_id from AddedVender where app_id='" + app_id + "'order by vender_id ASC";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                arrayList.add("Applied vander ID : " + cursor.getString(cursor.getColumnIndex("vender_id")));

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return arrayList;
    }

    public long getCountVenderDetails() {
        long c = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "Select _id from VENDER_DETAILS";
            Cursor cursor = db.rawQuery(quary, null);
            c = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return c;
    }

    public long getIdVEN() {
        long c = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String quary = "Select _id from VENDER_DETAILS";
            Cursor cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                c = cursor.getInt(cursor.getColumnIndex("_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return c;
    }


    public long saveVenderJsonData(String res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            JSONObject resjsonObject = new JSONObject(res);
            JSONObject jsonObject = resjsonObject.getJSONObject("data");
            ContentValues values = new ContentValues();
            if (!jsonObject.isNull("nameOfBusinessShop"))
                values.put("shop_name", jsonObject.getString("nameOfBusinessShop").trim());
            if (!jsonObject.isNull("premisesType"))
                values.put("premise_type", jsonObject.getString("premisesType").trim());
            if (!jsonObject.isNull("address1"))
                values.put("add1", jsonObject.getString("address1").trim());
            if (!jsonObject.isNull("address2"))
                values.put("add2", jsonObject.getString("address2").trim());
            if (!jsonObject.isNull("landmark"))
                values.put("landmark", jsonObject.getString("landmark").trim());
            if (!jsonObject.isNull("city")) values.put("city", jsonObject.getString("city").trim());
            if (!jsonObject.isNull("district"))
                values.put("dis_id", jsonObject.getString("district").trim());
            if (!jsonObject.isNull("block"))
                values.put("block_id", jsonObject.getString("block").trim());
            if (!jsonObject.isNull("pincode"))
                values.put("pin", jsonObject.getString("pincode").trim());
            if (!jsonObject.isNull("mobileNo"))
                values.put("mobile", jsonObject.getString("mobileNo").trim());
            if (!jsonObject.isNull("landlineNo"))
                values.put("landline", jsonObject.getString("landlineNo").trim());
            if (!jsonObject.isNull("emailId"))
                values.put("email", jsonObject.getString("emailId").trim());
            if (!jsonObject.isNull("dateOfEstablishment"))
                values.put("date_of_est", jsonObject.getString("dateOfEstablishment").trim());
            if (!jsonObject.isNull("licenceDate"))
                values.put("licence_date", jsonObject.getString("licenceDate").trim());
            if (!jsonObject.isNull("licenceNumber"))
                values.put("licence_no", jsonObject.getString("licenceNumber").trim());
            if (!jsonObject.isNull("firmType"))
                values.put("type_firm", jsonObject.getString("firmType").trim());
            if (!jsonObject.isNull("dateOfRegistration"))
                values.put("date_of_reg", jsonObject.getString("dateOfRegistration").trim());
            if (!jsonObject.isNull("registrationNo"))
                values.put("reg_no", jsonObject.getString("registrationNo").trim());
            if (!jsonObject.isNull("commencementDate"))
                values.put("date_of_comm", jsonObject.getString("commencementDate").trim());
            if (!jsonObject.isNull("placeOfverification"))
                values.put("place_of_var", jsonObject.getString("placeOfverification").trim());
            if (!jsonObject.isNull("thanaCode"))
                values.put("thana_id", jsonObject.getString("thanaCode").trim());
            if (!jsonObject.isNull("sub_div_id"))
                values.put("sub_div_id", jsonObject.getString("subdivId").trim());
            if (!jsonObject.isNull("natureOfBusiness"))
                values.put("nat_of_bussiness", jsonObject.getString("natureOfBusiness").trim());
            if (!jsonObject.isNull("tinNo"))
                values.put("tin", jsonObject.getString("tinNo").trim());
            if (!jsonObject.isNull("panNo"))
                values.put("pan", jsonObject.getString("panNo").trim());
            if (!jsonObject.isNull("professionalTax"))
                values.put("pro", jsonObject.getString("professionalTax").trim());
            if (!jsonObject.isNull("cstNo"))
                values.put("cst", jsonObject.getString("cstNo").trim());
            if (!jsonObject.isNull("tanNo"))
                values.put("tan", jsonObject.getString("tanNo").trim());
            if (!jsonObject.isNull("gstNo"))
                values.put("gst", jsonObject.getString("gstNo").trim());
            if (getCountVenderDetails() <= 0) {
                c = db.insert("VENDER_DETAILS", null, values);
            } else {
                long id_ven = getIdVEN();
                c = db.update("VENDER_DETAILS", values, "_id=?", new String[]{String.valueOf(id_ven)});
            }
            JSONArray jsonArray1 = jsonObject.getJSONArray("vofficials");
            JSONArray jsonArray2 = jsonObject.getJSONArray("weights");
            JSONArray jsonArray3 = jsonObject.getJSONArray("instruments");
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject_pat = jsonArray1.getJSONObject(i);
                PatnerEntity patnerEntity = new PatnerEntity();
                patnerEntity.setFirm_type("0");
                patnerEntity.setDate_of_com("");
                patnerEntity.setPlace_of_var("");
                if (!jsonObject_pat.isNull("vendorId"))
                    patnerEntity.setVendorId(jsonObject_pat.getString("vendorId"));
                if (!jsonObject_pat.isNull("partnerId"))
                    patnerEntity.setPartnerId(jsonObject_pat.getString("partnerId"));
                patnerEntity.setDesignation_id(jsonObject_pat.getString("designation"));
                patnerEntity.setName(jsonObject_pat.getString("partnerName"));
                patnerEntity.setFather_name(jsonObject_pat.getString("fatherHusbandName"));
                patnerEntity.setAdhar_vid(jsonObject_pat.getString("aadhaarNo"));
                patnerEntity.setAdd1(jsonObject_pat.getString("address1"));
                patnerEntity.setAdd2(jsonObject_pat.getString("address2"));
                patnerEntity.setLandmark(jsonObject_pat.getString("landmark"));
                patnerEntity.setCity(jsonObject_pat.getString("city"));
                patnerEntity.setDistrict(jsonObject_pat.getString("district"));
                patnerEntity.setBlock(jsonObject_pat.getString("block"));
                patnerEntity.setPinCode(jsonObject_pat.getString("pincode"));
                patnerEntity.setMobile(jsonObject_pat.getString("mobileNo"));
                patnerEntity.setLandline(jsonObject_pat.getString("landlineNo"));
                if (!jsonObject.isNull("emailId"))
                 patnerEntity.setEmail(jsonObject_pat.getString("emailId"));
                if (jsonObject_pat.getBoolean("nominatedUnderSection")) {
                    patnerEntity.setIs_nom_under49("Y");
                } else {
                    patnerEntity.setIs_nom_under49("N");
                }
                c = addPartner(patnerEntity);
            }
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject_deno = jsonArray2.getJSONObject(i);
                String cat_id = jsonObject_deno.getString("categoryId");
                if (cat_id.equals("19")) {
                    DenomintionEntity denomintionEntity = this.getWeightDenominationByID(jsonObject_deno.getString("denomination"));
                    int qt = Integer.parseInt(denomintionEntity.getQuantity()) + 1;
                    VehicleTankDetails vehicleTankDetails = new VehicleTankDetails();
                    vehicleTankDetails.setDenomId(Integer.parseInt(denomintionEntity.getValue()));
                    vehicleTankDetails.setRegNumber(jsonObject_deno.getString("vechileRegistractionNo"));
                    vehicleTankDetails.setEngineNumber(jsonObject_deno.getString("vechileEngineNo"));
                    vehicleTankDetails.setChechisNumber(jsonObject_deno.getString("vechileChesisNo"));
                    vehicleTankDetails.setOwnerFirmName(jsonObject_deno.getString("vechileOwnerName"));
                    vehicleTankDetails.setCountry(jsonObject_deno.getString("countryName"));
                    addTank(vehicleTankDetails);
                    c = updateDenomination((jsonObject_deno.isNull("vendorId")) ? "" : jsonObject_deno.getString("vendorId"), (jsonObject_deno.isNull("vcId")) ? "" : jsonObject_deno.getString("vcId"), jsonObject_deno.getString("denomination"), "Y", String.valueOf(qt), 0, "0", jsonObject_deno.getString("validYear"), jsonObject_deno.getString("proposalId"), false);
                } else {
                    c = updateDenomination((jsonObject_deno.isNull("vendorId")) ? "" : jsonObject_deno.getString("vendorId"), (jsonObject_deno.isNull("vcId")) ? "" : jsonObject_deno.getString("vcId"), jsonObject_deno.getString("denomination"), "Y", jsonObject_deno.getString("quantity"), 0, "0", jsonObject_deno.getString("validYear"), jsonObject_deno.getString("proposalId"), false);
                }
            }
            for (int j = 0; j < jsonArray3.length(); j++) {
                JSONObject jsonObject_ins = jsonArray3.getJSONObject(j);
                InstrumentEntity instrumentEntity = new InstrumentEntity();
                if (!jsonObject_ins.isNull("vendorId"))
                    instrumentEntity.setVendorId(jsonObject_ins.getString("vendorId"));
                if (!jsonObject_ins.isNull("vcId"))
                    instrumentEntity.setVcId(jsonObject_ins.getString("vcId"));
                instrumentEntity.setPro_id(jsonObject_ins.getString("proposalId"));
                instrumentEntity.setCat_id(jsonObject_ins.getString("categoryId"));
                instrumentEntity.setCap_id(jsonObject_ins.getString("capacityId"));
                //instrumentEntity.setQuantity(jsonObject_ins.getString("quantity"));
                if ((instrumentEntity.getCat_id().equals("16") && instrumentEntity.getCap_id().equals("219")) || (instrumentEntity.getCat_id().equals("19") && instrumentEntity.getCap_id().equals("225")) || (instrumentEntity.getCat_id().equals("22") && instrumentEntity.getCap_id().equals("230"))) {
                    instrumentEntity.setQuantity(jsonObject_ins.getString("nozzels"));
                } else {
                    instrumentEntity.setQuantity(jsonObject_ins.getString("quantity"));
                }
                instrumentEntity.setIns_class(jsonObject_ins.getString("classId"));
                instrumentEntity.setM_or_brand(jsonObject_ins.getString("manufacturer"));
                instrumentEntity.setVal_year(jsonObject_ins.getString("validYear"));
                instrumentEntity.setCap_max(jsonObject_ins.getString("capacityMax"));
                instrumentEntity.setCap_min(jsonObject_ins.getString("capacityMin"));
                instrumentEntity.setModel_no(jsonObject_ins.getString("modelNo"));
                instrumentEntity.setSer_no(jsonObject_ins.getString("mserialNo"));
                instrumentEntity.setE_val(jsonObject_ins.getString("evalue"));
                c = addInstrument(instrumentEntity);
               /* if (c > 0) {
                    ArrayList<Nozzle> nozzles = new ArrayList<>();
                    JSONArray jsonArray_ext = jsonObject_ins.getJSONArray("extensions");
                    for (int k = 0; k < jsonArray_ext.length(); k++) {
                        JSONObject jso = jsonArray_ext.getJSONObject(k);
                        Nozzle nozzle = new Nozzle();
                        nozzle.setVcId(jso.isNull("vcId") ? null : jso.getString("vcId"));
                        nozzle.setVendorId(jso.isNull("vendorId") ? null : jso.getString("vendorId"));
                        nozzle.setNozzle_num(jso.isNull("nozalNo") ? null : jso.getString("nozalNo"));
                        nozzle.setK_factor(jso.isNull("kfactor") ? null : jso.getString("kfactor"));
                        nozzle.setTot_value(jso.isNull("totalizerValue") ? null : jso.getString("totalizerValue"));
                        nozzle.setProduct_id(jso.isNull("product") ? null : jso.getString("product"));
                        nozzle.setCal_num(jso.isNull("calNo") ? null : jso.getString("calNo"));
                        nozzles.add(nozzle);
                    }
                    long c_noz = this.addNozzle(nozzles, String.valueOf(c));
                    if (c_noz > 0) Log.d("nozzle_added", String.valueOf(c_noz));
                    else Log.e("error", "***** Nozzle not saving in json ********");
                } else {
                    Log.e("error", "***** Instrument not Saved *****");
                }*/
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for saveVenderJsonData");
            // TODO: handle exception
            return 0;
        } finally {
            db.close();
            //this.getWritableDatabase().close();
        }
        return c;
    }

    public long saveProduct(ArrayList<InsProduct> res) {
        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (InsProduct insProduct : res) {
                ContentValues values = new ContentValues();
                values.put("value", insProduct.getValue().trim());
                values.put("name", insProduct.getName());
                String[] whereArgs = new String[]{insProduct.getValue().trim()};
                c = db.update("INS_PRODUCT", values, "value=?", whereArgs);
                if (!(c > 0)) {
                    c = db.insert("INS_PRODUCT", null, values);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            Log.e("ERROR 3", " writing data in local DB for product");
            // TODO: handle exception
        } finally {
            db.close();
            //this.getWritableDatabase().close();
        }
        return c;

    }

    public ArrayList<InsProduct> getInsProduct() {
        ArrayList<InsProduct> insProducts;
        String quary = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            insProducts = new ArrayList<>();
            db = this.getReadableDatabase();
            quary = "select * from INS_PRODUCT order by name ASC";
            cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                InsProduct insProduct = new InsProduct();
                insProduct.setValue(cursor.getString(cursor.getColumnIndex("value")));
                insProduct.setName(cursor.getString(cursor.getColumnIndex("name")));
                insProducts.add(insProduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
            db.close();
        }
        return insProducts;
    }

    public InsProduct getProductByID(String product_id) {
        InsProduct insProduct = null;
        String quary = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            quary = "select * from INS_PRODUCT where value='" + product_id + "'";
            cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                insProduct = new InsProduct();
                insProduct.setValue(cursor.getString(cursor.getColumnIndex("value")));
                insProduct.setName(cursor.getString(cursor.getColumnIndex("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return insProduct;
        } finally {
            cursor.close();
            db.close();
        }
        return insProduct;
    }

    public long addNozzle(ArrayList<Nozzle> nozzles, String sl_id) {
        long c = -1;
        SQLiteDatabase db = null;
        try {
            for (Nozzle nozzle : nozzles) {
                nozzle.setSl_id(sl_id);
                db = getWritableDatabase();
                ContentValues values = new ContentValues();
                if (nozzle.getVcId() != null) values.put("vcId", nozzle.getVcId());
                if (nozzle.getVendorId() != null) values.put("vendorId", nozzle.getVendorId());
                values.put("nozzle_num", nozzle.getNozzle_num());
                values.put("product_id", nozzle.getProduct_id());
                values.put("cal_num", nozzle.getCal_num());
                values.put("k_factor", nozzle.getK_factor());
                values.put("tot_value", nozzle.getTot_value());
                values.put("sl_id", nozzle.getSl_id());
                //c = db.update("NOZZLE", values, "sl_id=? AND product_id=?", new String[]{nozzle.getSl_id(), nozzle.getProduct_id().trim()});
                //if (c <= 0) {
                c = db.insert("NOZZLE", null, values);
               /* } else {
                    Log.e("error", "....Nozzle not inserted....");
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            return c;
        } finally {
            db.close();
        }
        return c;
    }

    public ArrayList<Nozzle> getAddedNozzle(String sl_id) {
        ArrayList<Nozzle> nozzles = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        String quary = "select * from NOZZLE where sl_id='" + sl_id + "'";
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(quary, null);
            while (cursor.moveToNext()) {
                Nozzle nozzle = new Nozzle();
                nozzle.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                nozzle.setSl_id(cursor.getString(cursor.getColumnIndex("sl_id")));
                nozzle.setVcId((cursor.isNull(cursor.getColumnIndex("vcId"))) ? null : cursor.getString(cursor.getColumnIndex("vcId")));
                nozzle.setVendorId((cursor.isNull(cursor.getColumnIndex("vendorId"))) ? null : cursor.getString(cursor.getColumnIndex("vendorId")));
                nozzle.setNozzle_num(cursor.getString(cursor.getColumnIndex("nozzle_num")));
                nozzle.setProduct_id(cursor.getString(cursor.getColumnIndex("product_id")));
                nozzle.setCal_num(cursor.getString(cursor.getColumnIndex("cal_num")));
                nozzle.setK_factor(cursor.getString(cursor.getColumnIndex("k_factor")));
                nozzle.setTot_value(cursor.getString(cursor.getColumnIndex("tot_value")));
                nozzles.add(nozzle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "Exception in getting Nozzle");
            return nozzles;

        } finally {
            cursor.close();
            db.close();
        }
        return nozzles;
    }

    public void deleteNozzleofINS(String sl_id_ins) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from NOZZLE where sl_id='" + sl_id_ins + "'");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    public long deleteNozzleById(Nozzle nozzle) {
        long c = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            c = sqLiteDatabase.delete("NOZZLE", "_id=?", new String[]{String.valueOf(nozzle.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return c;
    }

    public void deleteAllNozzle() {
        try {
            if (getNozzleCountBySL_ID("") > 0) {
                SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
                sqLiteDatabase.execSQL("delete from NOZZLE");
            } else {
                Log.e("database", "no nozzle found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    public long getNozzleCountBySL_ID(String sl_id) {
        long count = 0;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            if (sl_id.equals("")) {
                cursor = db.rawQuery("select * from NOZZLE", null);
            } else {
                cursor = db.rawQuery("select * from NOZZLE where sl_id='" + sl_id + "'", null);
            }
            count = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
            return 0;
        } finally {
            cursor.close();
            db.close();
        }
        return count;
    }


    public long addTank(VehicleTankDetails vehicleTankDetail) {
        final SQLiteDatabase db = this.getWritableDatabase();
        ;
        long c = 0;
        try {
            //vehicleTankDetails.stream().forEach((vehicleTankDetail)->{
            ContentValues contentValues = new ContentValues();
            contentValues.put("reg_no", vehicleTankDetail.getRegNumber());
            contentValues.put("eng_no", vehicleTankDetail.getEngineNumber());
            contentValues.put("chechis_no", vehicleTankDetail.getChechisNumber());
            contentValues.put("firm_or_owner_name", vehicleTankDetail.getOwnerFirmName());
            contentValues.put("country", vehicleTankDetail.getCountry());
            contentValues.put("denom_id", vehicleTankDetail.getDenomId());
            c = db.insert("VEHICLE_TANK", null, contentValues);
            //});
        } catch (Exception e) {
            e.printStackTrace();
            c = -1;
        } finally {
            db.close();
        }
        return c;
    }

    public long getTankcount() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        long totalCount = 0;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select v_id from VEHICLE_TANK", null);
            cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return totalCount;
    }

    public ArrayList<VehicleTankDetails> getTotalTank(String denom_id) {
        ArrayList<VehicleTankDetails> vehicleTankDetails = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        long totalCount = 0;
        try {
            db = this.getReadableDatabase();
            String quary = "select * from VEHICLE_TANK where denom_id='" + denom_id + "'";
            cursor = db.rawQuery(quary, null);
            System.out.println("size== " + cursor.getCount());
            while (cursor.moveToNext()) {
                VehicleTankDetails vehicleTankDetail = new VehicleTankDetails();
                vehicleTankDetail.setRegNumber((cursor.isNull(cursor.getColumnIndex("reg_no"))) ? "" : cursor.getString(cursor.getColumnIndex("reg_no")));
                vehicleTankDetail.setEngineNumber((cursor.isNull(cursor.getColumnIndex("eng_no"))) ? "" : cursor.getString(cursor.getColumnIndex("eng_no")));
                vehicleTankDetail.setChechisNumber((cursor.isNull(cursor.getColumnIndex("chechis_no"))) ? "" : cursor.getString(cursor.getColumnIndex("chechis_no")));
                vehicleTankDetail.setOwnerFirmName(cursor.isNull(cursor.getColumnIndex("firm_or_owner_name")) ? "" : cursor.getString(cursor.getColumnIndex("firm_or_owner_name")));
                vehicleTankDetail.setCountry((cursor.isNull(cursor.getColumnIndex("country"))) ? "" : cursor.getString(cursor.getColumnIndex("country")));
                vehicleTankDetail.setVid((cursor.isNull(cursor.getColumnIndex("v_id"))) ? 0 : cursor.getInt(cursor.getColumnIndex("v_id")));
                vehicleTankDetail.setDenomId((cursor.isNull(cursor.getColumnIndex("denom_id"))) ? 0 : cursor.getInt(cursor.getColumnIndex("denom_id")));
                vehicleTankDetails.add(vehicleTankDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        return vehicleTankDetails;
    }

    public long deleteTankById(VehicleTankDetails tankDetail) {
        long c = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            c = sqLiteDatabase.delete("VEHICLE_TANK", "v_id=?", new String[]{String.valueOf(tankDetail.getVid())});
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
        return c;
    }

    public void deleteAllTanks() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (getTankcount() > 0) {
                db.execSQL("delete from VEHICLE_TANK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }
}