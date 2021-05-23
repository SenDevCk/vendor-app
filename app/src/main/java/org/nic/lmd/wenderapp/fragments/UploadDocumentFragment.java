package org.nic.lmd.wenderapp.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.interfaces.TickHandler;
import org.nic.lmd.wenderapp.uploadFileUtility.RealPathUtil;
import org.nic.lmd.wenderapp.uploadFileUtility.UploadFileService;
import org.nic.lmd.wenderapp.utilities.Utiilties;


import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadDocumentFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int PICK_PDF_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button button_chose_d1, button_chose_d2, button_chose_d3, button_chose_d4, button_chose_d5, button_chose_d6, button_chose_d7, button_chose_d8, button_chose_d9, button_chose_d10,button_chose_d11;
    private Button button_d1, button_d2, button_d3, button_d4, button_d5, button_d6, button_d7, button_d8, button_d9, button_d10,button_d11;
    private TextView tv_d1, tv_d2, tv_d3, tv_d4, tv_d5, tv_d6, tv_d7, tv_d8, tv_d9, tv_d10, tv_d11;
    private Uri filePath1, filePath2, filePath3, filePath4, filePath5, filePath6, filePath7, filePath8, filePath9, filePath10, filePath11;
    private static final int REQUEST_PERMISSIONS = 100;
    LinearLayout ll_img_done1, ll_img_done2, ll_img_done3, ll_img_done4, ll_img_done5, ll_img_done6, ll_img_done7, ll_img_done8, ll_img_done9, ll_img_done10,ll_img_done11;

    public UploadDocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadDocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadDocumentFragment newInstance(String param1, String param2) {
        UploadDocumentFragment fragment = new UploadDocumentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.sync_menu).setTitle("Finish");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync_menu: {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Finish Document Upload ?")
                        .setMessage("Have you uploaded all documents ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                getActivity().finish();
                            }

                        }).create().show();
                break;
                }
            default: {
                break;
            }

        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_img_done1 =  getActivity().findViewById(R.id.ll_img_done1);
        ll_img_done2 =  getActivity().findViewById(R.id.ll_img_done2);
        ll_img_done3 =  getActivity().findViewById(R.id.ll_img_done3);
        ll_img_done4 =  getActivity().findViewById(R.id.ll_img_done4);
        ll_img_done5 =  getActivity().findViewById(R.id.ll_img_done5);
        ll_img_done6 =  getActivity().findViewById(R.id.ll_img_done6);
        ll_img_done7 =  getActivity().findViewById(R.id.ll_img_done7);
        ll_img_done8 =  getActivity().findViewById(R.id.ll_img_done8);
        ll_img_done9 =  getActivity().findViewById(R.id.ll_img_done9);
        ll_img_done10 =  getActivity().findViewById(R.id.ll_img_done10);
        ll_img_done11 =  getActivity().findViewById(R.id.ll_img_done11);

        button_chose_d1 =  getActivity().findViewById(R.id.button_chose_d1);
        button_chose_d2 =  getActivity().findViewById(R.id.button_chose_d2);
        button_chose_d3 =  getActivity().findViewById(R.id.button_chose_d3);
        button_chose_d4 =  getActivity().findViewById(R.id.button_chose_d4);
        button_chose_d5 =  getActivity().findViewById(R.id.button_chose_d5);
        button_chose_d6 =  getActivity().findViewById(R.id.button_chose_d6);
        button_chose_d7 =  getActivity().findViewById(R.id.button_chose_d7);
        button_chose_d8 =  getActivity().findViewById(R.id.button_chose_d8);
        button_chose_d9 =  getActivity().findViewById(R.id.button_chose_d9);
        button_chose_d10 =  getActivity().findViewById(R.id.button_chose_d10);
        button_chose_d11 =  getActivity().findViewById(R.id.button_chose_d11);

        button_d1 =  getActivity().findViewById(R.id.button_d1);
        button_d2 =  getActivity().findViewById(R.id.button_d2);
        button_d3 =  getActivity().findViewById(R.id.button_d3);
        button_d4 =  getActivity().findViewById(R.id.button_d4);
        button_d5 =  getActivity().findViewById(R.id.button_d5);
        button_d6 =  getActivity().findViewById(R.id.button_d6);
        button_d7 =  getActivity().findViewById(R.id.button_d7);
        button_d8 =  getActivity().findViewById(R.id.button_d8);
        button_d9 =  getActivity().findViewById(R.id.button_d9);
        button_d10 =  getActivity().findViewById(R.id.button_d10);
        button_d11 =  getActivity().findViewById(R.id.button_d11);

        tv_d1 =  getActivity().findViewById(R.id.tv_d1);
        tv_d2 =  getActivity().findViewById(R.id.tv_d2);
        tv_d3 =  getActivity().findViewById(R.id.tv_d3);
        tv_d4 =  getActivity().findViewById(R.id.tv_d4);
        tv_d5 =  getActivity().findViewById(R.id.tv_d5);
        tv_d6 =  getActivity().findViewById(R.id.tv_d6);
        tv_d7 =  getActivity().findViewById(R.id.tv_d7);
        tv_d8 =  getActivity().findViewById(R.id.tv_d8);
        tv_d9 =  getActivity().findViewById(R.id.tv_d9);
        tv_d10 =  getActivity().findViewById(R.id.tv_d10);
        tv_d11 =  getActivity().findViewById(R.id.tv_d11);

        button_chose_d1.setOnClickListener(this);
        button_d1.setOnClickListener(this);
        button_chose_d2.setOnClickListener(this);
        button_d2.setOnClickListener(this);
        button_chose_d3.setOnClickListener(this);
        button_d3.setOnClickListener(this);
        button_chose_d4.setOnClickListener(this);
        button_d4.setOnClickListener(this);
        button_chose_d5.setOnClickListener(this);
        button_d5.setOnClickListener(this);
        button_chose_d6.setOnClickListener(this);
        button_d6.setOnClickListener(this);
        button_chose_d7.setOnClickListener(this);
        button_d7.setOnClickListener(this);
        button_chose_d8.setOnClickListener(this);
        button_d8.setOnClickListener(this);
        button_chose_d9.setOnClickListener(this);
        button_d9.setOnClickListener(this);
        button_chose_d10.setOnClickListener(this);
        button_d10.setOnClickListener(this);
        button_chose_d11.setOnClickListener(this);
        button_d11.setOnClickListener(this);

    }
    ArrayList<MediaFile> files =new ArrayList<>();
    //method to show file chooser
    private void showFileChooser() {
        Intent customIconIntent = null;
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                customIconIntent = new Intent(Intent.ACTION_GET_CONTENT);
                customIconIntent.addCategory(Intent.CATEGORY_OPENABLE);
                //customIconIntent.setAction(Intent.ACTION_GET_CONTENT);
                customIconIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                customIconIntent.setType("application/pdf");
            } else {
                customIconIntent = new Intent();
                customIconIntent.setType("application/pdf");
                customIconIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                customIconIntent.setAction(Intent.ACTION_GET_CONTENT);
            }
            startActivityForResult(Intent.createChooser(customIconIntent, "Pick Pdf"), PICK_PDF_REQUEST);
            /*Intent intent = new Intent(getActivity(), FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setSelectedMediaFiles(files)
                    .setShowFiles(true)
                    .setShowImages(false)
                    .setShowAudios(false)
                    .setShowVideos(false)
                    .setIgnoreNoMedia(true)
                    .enableVideoCapture(false)
                    .enableImageCapture(false)
                    .setIgnoreHiddenFile(false)
                    .setMaxSelection(10)
                    .build());
            startActivityForResult(intent, PICK_PDF_REQUEST);*/
        }


    }

  /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
           long file_size_in_kb=files.get(0).getSize();
           String selected_file_path=files.get(0).getPath();
           switch (PICK_PDF_REQUEST) {
               case 1:
                   filePath1 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d1.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 2:
                   filePath2 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d2.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 3:
                   filePath3 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d3.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 4:
                   filePath4 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d4.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 5:
                   filePath5 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d5.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 6:
                   filePath6 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d6.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 7:
                   filePath7 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d7.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 8:
                   filePath8 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d8.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 9:
                   filePath9 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d9.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 10:
                   filePath10 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d10.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               case 11:
                   filePath11 = files.get(0).getUri();
                   if (file_size_in_kb<=1370)
                       tv_d11.setText(selected_file_path);
                   else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
                   break;
               default:
                   Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                   break;
           }
       }else{
           Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
       }
   }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fname=null;
        File file=null;
        int file_size_in_kb=0;
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (PICK_PDF_REQUEST == 1) {
                filePath1 = data.getData();
                file = new File(filePath1.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370)
                tv_d1.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 2) {
                filePath2 = data.getData();
                file = new File(filePath2.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370)
                    tv_d2.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 3) {
                filePath3 = data.getData();
                file = new File(filePath3.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d3.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 4) {
                filePath4 = data.getData();
                file = new File(filePath4.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d4.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 5) {
                filePath5 = data.getData();
                file = new File(filePath5.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d5.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 6) {
                filePath6 = data.getData();
                file = new File(filePath6.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d6.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 7) {
                filePath7 = data.getData();
                file = new File(filePath7.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d7.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 8) {
                filePath8 = data.getData();
                file = new File(filePath8.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d8.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 9) {
                filePath9 = data.getData();
                file = new File(filePath9.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d9.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            } else if (PICK_PDF_REQUEST == 10) {
                filePath10 = data.getData();
                file = new File(filePath10.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d10.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            }else if (PICK_PDF_REQUEST == 11) {
                filePath11 = data.getData();
                file = new File(filePath11.getPath());
                fname = file.getAbsolutePath();
                file_size_in_kb = Integer.parseInt(String.valueOf(file.length()/1024));
                if (file_size_in_kb<=1370) tv_d11.setText(fname);
                else Toast.makeText(getActivity(), "Max file size 1370 kb", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           case R.id.button_chose_d1 :
                PICK_PDF_REQUEST = 1;
                showFileChooser();
                break;
            case R.id.button_chose_d2 :
                PICK_PDF_REQUEST = 2;
                showFileChooser();
                break;
            case R.id.button_chose_d3 :
                PICK_PDF_REQUEST = 3;
                showFileChooser();
                break;
            case R.id.button_chose_d4 :
                PICK_PDF_REQUEST = 4;
                showFileChooser();
                break;
            case R.id.button_chose_d5 :
                PICK_PDF_REQUEST = 5;
                showFileChooser();
            case R.id.button_chose_d6 :
                PICK_PDF_REQUEST = 6;
                showFileChooser();
            case R.id.button_chose_d7 :
                PICK_PDF_REQUEST = 7;
                showFileChooser();
            case R.id.button_chose_d8 :
                PICK_PDF_REQUEST = 8;
                showFileChooser();
            case R.id.button_chose_d9 :
                PICK_PDF_REQUEST = 9;
                showFileChooser();
            case R.id.button_chose_d10 :
                PICK_PDF_REQUEST = 10;
                showFileChooser();
            case R.id.button_chose_d11 :
                PICK_PDF_REQUEST = 11;
                showFileChooser();
            default:
                UploadFileService.bindTicker(new TickHandler() {
                    @Override
                    public void sucess() {
                        tick();
                    }

                    @Override
                    public void notSuccess() {
                        unTick();
                    }
                });
                if (Utiilties.isOnline(getActivity())) {
                    switch (PICK_PDF_REQUEST) {
                        case 1:
                            if (!tv_d1.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath1, mParam1.trim(), "MOA").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            if (!tv_d2.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath2, mParam1.trim(), "REG").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            if (!tv_d3.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath3, mParam1.trim(), "ALF").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            if (!tv_d4.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath4, mParam1.trim(), "RLCA").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            if (!tv_d5.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath5, mParam1.trim(), "IR").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            if (!tv_d6.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath6, mParam1.trim(), "NOC").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 7:
                            if (!tv_d7.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath7, mParam1.trim(), "LAP").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 8:
                            if (!tv_d8.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath8, mParam1.trim(), "RAS").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 9:
                            if (!tv_d9.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath9, mParam1.trim(), "IWM").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 10:
                            if (!tv_d10.getText().toString().equals(""))
                                new UploadFileService(getActivity(), filePath10, mParam1.trim(), "VCLM").execute();
                            else
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            break;
                        case 11:
                            if (!tv_d11.getText().toString().equals("")) {
                                //String selectedFilePath = RealPathUtil.getRealPath(getActivity().getApplicationContext(), filePath11);
                                //tv_d11.setText(""+selectedFilePath);
                                new UploadFileService(getActivity(), filePath11, mParam1.trim(), "IDP").execute();
                            } else {
                                Toast.makeText(getActivity(), "Select File First", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            Toast.makeText(getActivity(), "Internet not avalable !", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            }

    }

    private void unTick() {
        if (PICK_PDF_REQUEST == 1) {
            ll_img_done1.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 2) {
            ll_img_done2.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 3) {
            ll_img_done3.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 4) {
            ll_img_done4.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 5) {
            ll_img_done5.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 6) {
            ll_img_done6.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 7) {
            ll_img_done7.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 8) {
            ll_img_done8.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 9) {
            ll_img_done9.setVisibility(View.GONE);
        } else if (PICK_PDF_REQUEST == 10) {
            ll_img_done10.setVisibility(View.GONE);
        }else if (PICK_PDF_REQUEST == 11) {
            ll_img_done11.setVisibility(View.GONE);
        }
    }

    private void tick() {
        if (PICK_PDF_REQUEST == 1) {
            ll_img_done1.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 2) {
            ll_img_done2.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 3) {
            ll_img_done3.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 4) {
            ll_img_done4.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 5) {
            ll_img_done5.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 6) {
            ll_img_done6.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 7) {
            ll_img_done7.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 8) {
            ll_img_done8.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 9) {
            ll_img_done9.setVisibility(View.VISIBLE);
        } else if (PICK_PDF_REQUEST == 10) {
            ll_img_done10.setVisibility(View.VISIBLE);
        }else if (PICK_PDF_REQUEST == 11) {
            ll_img_done11.setVisibility(View.VISIBLE);
        }
    }
}
