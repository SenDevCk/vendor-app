package org.nic.lmd.wenderapp.uploadFileUtility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import org.json.JSONObject;
import org.nic.lmd.wenderapp.interfaces.TickHandler;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadFileService extends AsyncTask<String, Void, String> {
    Uri uri;
    private ProgressDialog dialog1;
    Activity activity;
    private String venderid, doctype;
    private static TickHandler tickHandler;

    public UploadFileService() {
    }

    public UploadFileService(Activity activity, Uri uri, String venderid, String doctype) {
        this.uri = uri;
        this.activity = activity;
        this.venderid = venderid;
        this.doctype = doctype;
        dialog1 = new ProgressDialog(this.activity);
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("Processing...");
        this.dialog1.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        Map<String, String> headers = new HashMap<>();
        String res = null;
        //headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        try {
           /* DocumentFile filed=DocumentFile.fromSingleUri(activity.getApplicationContext(),uri);
            File selectedfile= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                selectedfile = getFile(activity.getApplicationContext(),filed);
            }else {
                String selectedFilePath = FileUtils.getPath(activity, uri);
                //String selectedFilePath = RealPathUtil.getRealPath(activity.getApplicationContext(), uri);
                selectedfile = new File(selectedFilePath);
            }*/

            HttpPostMultipart httpPostMultipart = new HttpPostMultipart(Urls_this_pro.LOAD_DOC + venderid + "&docType=" + doctype, "utf-8", headers);
            //httpPostMultipart.addFilePart("file", selectedfile);
            httpPostMultipart.addFilePart("file", uri, activity,doctype);
            res = httpPostMultipart.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if (dialog1.isShowing()) dialog1.dismiss();
        Log.d("log:", "" + res);

        try {
            JSONObject jsonObject = new JSONObject(res);
            if (jsonObject.has("timestamp")) {
                Toast.makeText(activity, "" + jsonObject.getString("error") + ": " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                tickHandler.notSuccess();
            } else {
                Toast.makeText(activity, "" + jsonObject.getString("remarks"), Toast.LENGTH_SHORT).show();
                if (jsonObject.getString("remarks").equals("success")) {
                    tickHandler.sucess();
                } else {
                    tickHandler.notSuccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tickHandler.notSuccess();
        }
    }

    public static void bindTicker(TickHandler tickHandler1) {
        tickHandler = tickHandler1;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    public static File getFile(@NonNull final Context context, @NonNull final DocumentFile document)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
        {
            return null;
        }

        try
        {
            final List<StorageVolume> volumeList = context
                    .getSystemService(StorageManager.class)
                    .getStorageVolumes();

            if ((volumeList == null) || volumeList.isEmpty())
            {
                return null;
            }

            // There must be a better way to get the document segment
            final String documentId      = DocumentsContract.getDocumentId(document.getUri());
            final String documentSegment = documentId.substring(documentId.lastIndexOf(':') + 1);

            for (final StorageVolume volume : volumeList)
            {
                 String volumePath=null;

                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
                {
                    final Class<?> class_StorageVolume = Class.forName("android.os.storage.StorageVolume");

                    @SuppressWarnings("JavaReflectionMemberAccess")
                    @SuppressLint("DiscouragedPrivateApi")
                    final Method method_getPath = class_StorageVolume.getDeclaredMethod("getPath");

                    volumePath = (String)method_getPath.invoke(volume);
                }
                else  if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
                {
                    // API 30
                    volumePath = volume.getDirectory().getPath();
                }

                final File storageFile = new File(volumePath + File.separator + documentSegment);

                // Should improve with other checks, because there is the
                // remote possibility that a copy could exist in a different
                // volume (SD-card) under a matching path structure and even
                // same file name, (maybe a user's backup in the SD-card).
                // Checking for the volume Uuid could be an option but
                // as per the documentation the Uuid can be empty.

                final boolean isTarget = (storageFile.exists())
                        && (storageFile.lastModified() == document.lastModified())
                        && (storageFile.length() == document.length());

               /* if (isTarget)
                {*/
                    return storageFile;
                //}
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
