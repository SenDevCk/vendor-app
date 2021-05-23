package org.nic.lmd.wenderapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Camera2Activity extends AppCompatActivity {

	private static final int PERMISSIONS_MULTIPLE_REQUEST = 1;
	private Camera mCamera;
	private CameraPreview mPreview;
	Location LastLocation = null;

	AlertDialog.Builder alert;
	private static final String TAG = "MyActivity";
	private static byte[] CompressedImageByteArray;
	private static Bitmap CompressedImage;
	Chronometer chronometer;
    LinearLayout ll_accu,rel_layout_lat,rel_layout_lon;
    Button btnCapture;

	@SuppressLint("WrongViewCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		rel_layout_lat=findViewById(R.id.rel_layout_lat);
		rel_layout_lon=findViewById(R.id.rel_layout_lon);
		ll_accu=findViewById(R.id.ll_accu);
		btnCapture=findViewById(R.id.btnCapture);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_MULTIPLE_REQUEST);
		} else {
			initializeCamera();
			super.onResume();
		}
	}



	private void initializeCamera() {
		rel_layout_lat.setVisibility(View.GONE);
		rel_layout_lon.setVisibility(View.GONE);
		ll_accu.setVisibility(View.GONE);
		chronometer = findViewById(R.id.chronometer1);
		mCamera=Camera.open();
		Parameters param = mCamera.getParameters();
		List<Size> sizes = param.getSupportedPictureSizes();
		int iTarget = 0;
		for (int i = 0; i < sizes.size(); i++) {
			Size size = sizes.get(i);
			if (size.width < 1000) {
				iTarget = i;
				break;
			}

		}
		param.setJpegQuality(20);
		param.setPictureSize(sizes.get(iTarget).width,
				sizes.get(iTarget).height);
		mCamera.setParameters(param);
		alert = new AlertDialog.Builder(this);
		Display getOrient = getWindowManager().getDefaultDisplay();
		int rotation = getOrient.getRotation();
		switch (rotation) {
			case Surface.ROTATION_0:
				mCamera.setDisplayOrientation(90);
				break;
			case Surface.ROTATION_90:
				break;
			case Surface.ROTATION_180:
				break;
			case Surface.ROTATION_270:
				mCamera.setDisplayOrientation(90);
				break;
			default:
				break;
		}
		try {
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = findViewById(R.id.camera_preview);
			preview.addView(mPreview);
			btnCapture.setEnabled(true);
			btnCapture.setText("TAKE PHOTO");
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}




	public static Camera getCameraInstance() {
		try {

			int numberOfCameras = Camera.getNumberOfCameras();
			int cameraId = 0;
			for (int i = 0; i < numberOfCameras; i++) {
				CameraInfo info = new CameraInfo();
				Camera.getCameraInfo(i, info);
				if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
					cameraId = i;
					break;
				}
			}
			return Camera.open(cameraId);
		} catch (Exception e) {
			return null;
		}
	}





	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				Log.d(TAG, "Start");
				Bitmap bmp = BitmapFactory
						.decodeByteArray(data, 0, data.length);
				Matrix mat = new Matrix();
				mat.postRotate(90);
				Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0,
						bmp.getWidth(), bmp.getHeight(), mat, true);
				setCameraImage(Utiilties.GenerateThumbnail(bMapRotate, 500, 700));
			} catch (Exception ex) {
				Log.d(TAG, ex.getMessage());
			}
		}
	};

	ShutterCallback shutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};
	PictureCallback rawCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	public void onCaptureClick(View view) {
		// System.gc();
		mCamera.takePicture(shutterCallback, rawCallback, mPicture);
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.stop();
	}

	private void setCameraImage(Bitmap bitmap) {
		Display getOrient = getWindowManager().getDefaultDisplay();
		int rotation = getOrient.getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			break;
		case Surface.ROTATION_270:
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			break;
		case Surface.ROTATION_90:
			break;
		case Surface.ROTATION_180:
			break;
		default:
			break;
		}
		Bitmap bitmap_icon=null;
		bitmap_icon=((BitmapDrawable)getResources().getDrawable(R.drawable.ic2)).getBitmap();
		bitmap_icon=Utiilties.GenerateThumbnail(bitmap_icon,60,60);
		bitmap = Utiilties.overlay(bitmap, bitmap_icon);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		byte[] byte_arr = stream.toByteArray();
		CompressedImageByteArray = byte_arr;
		bitmap.recycle();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("CapturedImage", CompressedImageByteArray);
		returnIntent.putExtra("CapturedTime", Utiilties.getDateString());
		returnIntent.putExtra("KEY_PIC",Integer.parseInt(getIntent().getStringExtra("KEY_PIC")));
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public static void setCompressedImage(byte[] compressedImageByteArray) {
		CompressedImageByteArray = compressedImageByteArray;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_MULTIPLE_REQUEST:
				if (grantResults.length > 0) {
					boolean mPermission =(grantResults[0] == PackageManager.PERMISSION_GRANTED);
					if(mPermission)
					{
						initializeCamera();
						super.onResume();
					} else {
						requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_MULTIPLE_REQUEST);
					}
				}
				break;
		}
	}
}
