package org.nic.lmd.wenderapp.uploadFileUtility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PdfOpenHelper {
    private static ProgressDialog dialog;
    public static void openPdfFromUrl(final String pdfUrl, final Activity activity) {
        Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                //dialog=new ProgressDialog(activity);
                //dialog.setMessage("Loading file");
                //dialog.show();
                try {
                    URL url = new URL(pdfUrl);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // download the file
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    File dir = new File(activity.getFilesDir(), "/shared_pdf");
                    dir.mkdir();
                    File file = new File(dir, "lmd_doc.pdf");
                    OutputStream output = new FileOutputStream(file);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                       //if (dialog.isShowing())dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //if (dialog.isShowing())dialog.dismiss();
                        e.printStackTrace();
                        Log.e("error",""+e.getMessage());
                    }

                    @Override
                    public void onNext(File file) {
                        //if (dialog.isShowing())dialog.dismiss();
                        //String authority = activity.getApplicationContext().getPackageName() + ".fileprovider";
                        Uri uriToFile = FileProvider.getUriForFile(activity, "org.nic.lmd.wenderapp.fileprovider", file);
                        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                        shareIntent.setDataAndType(uriToFile, "application/pdf");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(shareIntent);
                        //}
                    }
                });
    }
}
