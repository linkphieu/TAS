package hanelsoft.vn.timeattendance.service;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {
	CountDownTimer cdt = null;

	@Override
	public void onCreate() {
		super.onCreate();
		//Log.i("Luong", "----------------------Starting timer...");
		cdt = new CountDownTimer(60000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				//Log.i("Luong","----------------------Countdown seconds remaining: "+ millisUntilFinished / 1000);
			}

			@Override
			public void onFinish() {
				//Log.i("Luong", "--------------------------Timer finished");
				ConstCommon.PROJECT_IS_SKIP = true;
				stopSelf();
			}
		};
		cdt.start();
	}

	@Override
	public void onDestroy() {
		//Log.i("Luong", "----------------------Kill Service");
		cdt.cancel();
		cdt = null;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
