package ara.kuet.musta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kuet.musta.R;

public class QiblaDirection extends Fragment implements SensorEventListener {

    public final static String TAG = QiblaDirection.class.getSimpleName();

    private SharedPreferences spMaster;
    private SharedPreferences.Editor ename,elat,elon;
    private ImageView compass,indicator;
    private float currentDegree = 0f;
    private SensorManager smanage;
    private float myDegree,lat,lon;
    static  TextView angleKeeper;
    private Button save;

    public QiblaDirection() {
        // TODO Auto-generated constructor stub
    }

    public static QiblaDirection newInstance() {
        return new QiblaDirection();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qibla, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferences(view);
    }

    private void initPreferences( View v) {
        smanage = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        spMaster = getActivity().getSharedPreferences("mysp",0);
        SharedPreferences spName = getActivity().getSharedPreferences("myspname", 0);
        SharedPreferences spLat = getActivity().getSharedPreferences("mysplat", 0);
        SharedPreferences spLon = getActivity().getSharedPreferences("mysplon", 0);
        ename = spName.edit();
        elat = spLat.edit();
        elon = spLon.edit();
        compass = (ImageView) v.findViewById(R.id.upper);
        indicator = (ImageView) v.findViewById(R.id.imgindicator);
        angleKeeper = (TextView) v.findViewById(R.id.tvqibla);
        angleKeeper.setText(spMaster.getString("angle", null));

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();
        smanage.registerListener(this, smanage.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }
    public void onPause() {
        super.onPause();
        smanage.unregisterListener(this);
        //lm.removeUpdates(locationListener);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        calculation();

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);
        compass.startAnimation(ra);

        ra.setDuration(210);
        ra.setFillAfter(true);
        RotateAnimation ri = new RotateAnimation(
                currentDegree-(41+myDegree),
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        indicator.startAnimation(ri);
        ri.setDuration(10000);
        ri.setFillAfter(true);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void  calculation()  {
            String ak;
            lat = spMaster.getFloat("latitude",(float) 22.48);//+North,-South
            lon = spMaster.getFloat("longitude",(float) 89.45);//+East,-West
            double upper = Math.sin(Math.PI / 180 * (lon-39.8233));
            double lower = Math.cos(Math.PI/180*lat)*Math.tan(Math.PI/180*21.42330)-Math.sin(Math.PI/180*lat)* Math.cos(Math.PI/180*(lon-39.8230));
            double cal0 = (upper/lower);
            double cal1 = Math.atan(cal0);
            cal1 = (cal1*180/Math.PI);
            myDegree = (float) cal1;
            int angle = (int) Math.ceil(myDegree);
        if(angle>0&&angle<=180)
        {
            ak = String.valueOf(angle)+"' West To North";
        }
        else
        {
            angle = - angle;
            ak = String.valueOf(angle)+"' East To North";
        }
            angleKeeper.setText(ak);
        }
}
