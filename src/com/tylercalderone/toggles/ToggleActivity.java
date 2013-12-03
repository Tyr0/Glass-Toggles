package com.tylercalderone.toggles;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ToggleActivity extends Activity {
	private static final String TAG = "ToggleActivity";
	
	private WifiManager mWifiManager;
	private BluetoothAdapter mBluetoothAdapter;
	
	private TextView mTextView;
	private ProgressBar mProgressBar;
	
	private static final String TOGGLE_WIFI_TAG = "TOGGLE_WIFI";
	private static final String TOGGLE_BLUETOOTH_TAG = "TOGGLE_BLUETOOTH";
	
	private long FINISH_DELAY = 1500;
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
	        mSoundPool.play(mSuccessSoundId,
                    1 /* leftVolume */,
                    1 /* rightVolume */,
                    SOUND_PRIORITY,
                    0 /* loop */,
                    1 /* rate */);
	        
			mProgressBar.setVisibility(ProgressBar.GONE);
			mTextView.setText(mHandlerMessage);
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					finish();
				}
			};
			Handler handler = new Handler();
			handler.postDelayed(runnable, 1000);
		}
	};
	private int mHandlerMessage;
	
	private static final int SOUND_PRIORITY = 1;
	private static final int MAX_STREAMS = 1;
	private SoundPool mSoundPool;
	private int mSuccessSoundId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mWifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		mTextView = (TextView)findViewById(R.id.textView);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		mSuccessSoundId = mSoundPool.load(this, R.raw.sound_success, SOUND_PRIORITY);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Log.d(TAG, "onStart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d(TAG, "onResume");
		
		String className = getLocalClassName();
		
		mProgressBar.setVisibility(ProgressBar.VISIBLE);
		if (className.equals(TOGGLE_WIFI_TAG)) {
			 toggleWifi();
		} else if (className.equals(TOGGLE_BLUETOOTH_TAG)) {
			 toggleBluetooth();
		} else {
			 Log.d(TAG, "else: "+className);
		}
	}
	
	private void toggleWifi() {
		
		if (mWifiManager.isWifiEnabled()) {
			mTextView.setText(R.string.toggling_wifi_off);
			mHandlerMessage = R.string.toggled_wifi_off;
			mWifiManager.setWifiEnabled(false);
		} else {
			mTextView.setText(R.string.toggling_wifi_on);
			mHandlerMessage = R.string.toggled_wifi_on;
			mWifiManager.setWifiEnabled(true);
		}
		
		mHandler.postDelayed(mRunnable, FINISH_DELAY);
	}
	
	private void toggleBluetooth() {
		
		if (mBluetoothAdapter.isEnabled()) {
			mTextView.setText(R.string.toggling_bluetooth_off);
			mHandlerMessage = R.string.toggled_bluetooth_off;
			mBluetoothAdapter.disable();
		} else {
			mTextView.setText(R.string.toggling_bluetooth_on);
			mHandlerMessage = R.string.toggled_bluetooth_on;
			mBluetoothAdapter.enable();
		}

		mHandler.postDelayed(mRunnable, FINISH_DELAY);
	}
}