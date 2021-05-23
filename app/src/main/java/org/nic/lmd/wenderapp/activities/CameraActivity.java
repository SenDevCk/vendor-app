package org.nic.lmd.wenderapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.MarshmallowPermission;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class CameraActivity extends AppCompatActivity {
	private static final int PERMISSIONS_MULTIPLE_REQUEST = 1;
	private Camera mCamera;
	private CameraPreview mPreview;
	Location LastLocation = null;
	LocationManager mlocManager = null;

	AlertDialog.Builder alert;

	private final int UPDATE_ADDRESS = 1;
	private final int UPDATE_LATLNG = 2;
	private static final String TAG = "MyActivity";
	private Handler mHandler;
	private static byte[] CompressedImageByteArray;
	private static Bitmap CompressedImage;
	private boolean isTimerStarted = false;
	Chronometer chronometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{
					Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,
					Manifest.permission.LOCATION_HARDWARE}, PERMISSIONS_MULTIPLE_REQUEST);

		} else {
			// write your logic here
			initializeCamera();
			super.onResume();
		}

	}



	private void initializeCamera() {
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		isTimerStarted = false;
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
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);

		} catch (Exception e) {
			finish();
		}
		if (!GlobalVariable.isOfflineGPS) {
			mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				alert.setTitle("GPS");
				alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..");
				alert.setPositiveButton("Turn on GPS",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int whichButton) {

								GlobalVariable.isOfflineGPS = false;
								if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
									// TODO: Consider calling
									return;
								}
								mlocManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER, 0,
										(float) 0.01, listener);

								Intent I = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(I);
								finish();

							}
						});
				alert.setNegativeButton("Continue Without GPS",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int whichButton) {
								Toast.makeText(CameraActivity.this,
										"Working in offline mode",
										Toast.LENGTH_LONG).show();

								GlobalVariable.isOfflineGPS = true;
								LastLocation = null;
								Button takePhoto = (Button) findViewById(R.id.btnCapture);
								takePhoto.setText("Take Photo");
								takePhoto.setEnabled(true);
							}
						});
				alert.show();
			}

			else {
				mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
						(float) 0.05, listener);

				mlocManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, (float) 0.01, listener);

			}
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

					case UPDATE_ADDRESS:
					case UPDATE_LATLNG:
						String[] LatLon = ((String) msg.obj).split("-");
						TextView tv_Lat =  findViewById(R.id.tvLat);
						TextView tv_Lon =  findViewById(R.id.tvLon);
						TextView tvAcuracy =  findViewById(R.id.tvAcuracy);

						tv_Lat.setText(LatLon[0]);
						tv_Lon.setText(LatLon[1]);
						tvAcuracy.setText(LatLon[2] + " metres");

						Log.e("", "Lat-Long" + LatLon[0] + "   " + LatLon[1]);

						if (!isTimerStarted) {
							startTimer();
						}

						break;
				}
			}
		};
	}

	public String setCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		String provider = mlocManager.getBestProvider(criteria, true);
		return provider;
	}

	public void startTimer() {
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();
		isTimerStarted = true;
	}


	public static Camera getCameraInstance() {
		// Camera c = null;
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


	private void updateUILocation(Location location) {

		Message.obtain(
				mHandler,
				UPDATE_LATLNG,
				new DecimalFormat("#.0000000").format(location.getLatitude())
						+ "-"
						+ new DecimalFormat("#.0000000").format(location
								.getLongitude()) + "-" + location.getAccuracy())
				.sendToTarget();

	}

	private final LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			if (GlobalVariable.isOfflineGPS == false) {

				updateUILocation(location);
				if (location.getLatitude() > 0.0) {

					long elapsedMillis = SystemClock.elapsedRealtime()
							- chronometer.getBase();

					Button takePhoto = findViewById(R.id.btnCapture);

					if (location.getAccuracy() < 150) {
						LastLocation = location;
						takePhoto.setText("Take Photo");
						takePhoto.setEnabled(true);
					} else {
						takePhoto.setText("Wait for GPS to Stable");
						takePhoto.setEnabled(false);
					}

				}

			} else {
				LastLocation.setLatitude(0.0);
				LastLocation.setLongitude(0.0);
				updateUILocation(LastLocation);
				Button takePhoto = (Button) findViewById(R.id.btnCapture);
				takePhoto.setText("Take Photo");
				takePhoto.setEnabled(true);
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	};
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
				setCameraImage(Utiilties
						.GenerateThumbnail(bMapRotate, 500, 700));

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

		String DisplayText1 = "", DisplayText2 = "", DisplayText3 = "", DisplayText4 = "";
		if (LastLocation != null) {
			DisplayText1 = "Lat : "
					+ String.valueOf(new DecimalFormat("#.0000000")
							.format(LastLocation.getLatitude()));
			DisplayText2 = "Lon : "
					+ String.valueOf(new DecimalFormat("#.0000000")
							.format(LastLocation.getLongitude()));
			DisplayText3 = Utiilties.getDateString("MMM dd, yyyy hh:mm a");

		} else {
			// DisplayText = "Lat : NA Lon : NA ";
		}

		DisplayText4 = GlobalVariable.Pid + " (" + GlobalVariable.area + ")";
		// DisplayText4=GlobalVariables.Pid;

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
		bitmap = Utiilties.DrawText(CameraActivity.this,bitmap, DisplayText1, DisplayText2,
				DisplayText3, DisplayText4);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		byte[] byte_arr = stream.toByteArray();
		CompressedImageByteArray = byte_arr;

		bitmap.recycle();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("CapturedImage", CompressedImageByteArray);
		returnIntent.putExtra("Lat", new DecimalFormat("#.0000000")
				.format(LastLocation.getLatitude()));
		returnIntent.putExtra("Lng", new DecimalFormat("#.0000000")
				.format(LastLocation.getLongitude()));
		returnIntent.putExtra("CapturedTime", Utiilties.getDateString());
		returnIntent.putExtra("KEY_PIC",Integer.parseInt(getIntent().getStringExtra("KEY_PIC")));
		// returnIntent.putExtra("ss", 0);
		setResult(RESULT_OK, returnIntent);
		finish();


	}

	public static byte[] getCompressedImage() {
		return CompressedImageByteArray;
	}

	public static void setCompressedImage(byte[] compressedImageByteArray) {
		CompressedImageByteArray = compressedImageByteArray;
	}

	public static Bitmap getCompressedBitmap() {
		return CompressedImage;
	}

	public static void setCompressedBitmap(Bitmap compressedImage) {
		CompressedImage = compressedImage;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {

		switch (requestCode) {
			case PERMISSIONS_MULTIPLE_REQUEST:
				if (grantResults.length > 0) {
					boolean mPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					boolean mPermission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
					//boolean mPermission2 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
					if(mPermission && mPermission1)
					{
						// write your logic here
						initializeCamera();
						super.onResume();

					} else {
						requestPermissions(new String[]{
								Manifest.permission.READ_CONTACTS,
								Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_MULTIPLE_REQUEST);
					}
				}
				break;
		}
	}
}
