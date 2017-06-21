package ara.kuet.musta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.Toast;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestService extends Service {
    private AudioManager audioManager;
    private TimerTask task;
    private Timer timer = new Timer();
    private Time time = new Time();

    private SharedPreferences spMaster, spLat, spLon;
    private SharedPreferences.Editor editor;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private Criteria criteria = new Criteria();
    private float lat;
    private float lon;

    boolean latOk = false;
    boolean lonOk = false;

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        initAll();
        initLocation();
        runTask();
        super.onCreate();
    }

    private void initAll() {
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        task = new CustomTimerTask(getApplicationContext());
        spMaster = getApplicationContext().getSharedPreferences("mysp", 0);
        spLat = getApplicationContext().getSharedPreferences("mysplat", 0);
        spLon = getApplicationContext().getSharedPreferences("mysplon", 0);
        editor = spMaster.edit();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(true);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String bestProvider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates("gps", 2000, 2, locationListener);
    }

    public void runTask() {
        time.setToNow();
        int period = 60000;
        int delay = period - time.second * 1000;
        timer.scheduleAtFixedRate(task, delay, period);
    }

    protected final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            lon = (float) location.getLongitude();
            lat = (float) location.getLatitude();
            editor.putFloat("latitude", lat);
            editor.putFloat("longitude", lon);
            editor.apply();
            angleCalculation();
            checkSavingLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
            try {
                //Toast.makeText(getApplicationContext(),"Enable "+ provider.toUpperCase()+" For Correctness !",Toast.LENGTH_SHORT).show();

            } catch (Exception ignored) {

            }
        }
    };

    private void checkSavingLocation() {
        float aa = spMaster.getFloat("latitude", (float) 22.48);//+North,-South
        float bb = spMaster.getFloat("longitude", (float) 89.45);//+East,-West
        Map<String, ?> allSplat = spLat.getAll();
        Map<String, ?> allSplon = spLon.getAll();
        for (Map.Entry<String, ?> entry1 : allSplat.entrySet()) {
            float xx = (Float) entry1.getValue();
            if ((aa >= (xx - 0.0002) && (aa <= (xx + 0.0002)))) {
                latOk = true;
                break;
            }
        }
        for (Map.Entry<String, ?> entry2 : allSplon.entrySet()) {
            float yy = (Float) entry2.getValue();
            if ((bb >= (yy - 0.000132)) && (bb <= (yy + 0.000132))) {
                lonOk = true;
                break;
            }
        }
        if (latOk && lonOk) {
            audioManager.setRingerMode(0);
            latOk = false;
            lonOk = false;
        } else {
            audioManager.setRingerMode(2);
            latOk = false;
            lonOk = false;
        }
    }

    public void angleCalculation() {

        String ak;
        double upper = Math.sin(Math.PI / 180 * (lon - 39.8233));
        double lower = Math.cos(Math.PI / 180 * lat) * Math.tan(Math.PI / 180 * 21.42330) - Math.sin(Math.PI / 180 * lat) * Math.cos(Math.PI / 180 * (lon - 39.8230));
        double cal0 = (upper / lower);
        double cal1 = Math.atan(cal0);
        cal1 = (cal1 * 180 / Math.PI);
        float myDegree = (float) cal1;
        int angle = (int) Math.ceil(myDegree);
        if (angle > 0 && angle <= 180) {
            ak = String.valueOf(angle) + "' West From North";
        } else {
            angle = -angle;
            ak = String.valueOf(angle) + "' East From North";
        }
        editor.putString("angle", ak);
        editor.apply();
        try {
            QiblaDirection.angleKeeper.setText(ak);
        } catch (Exception ignored) {
        }
    }
}