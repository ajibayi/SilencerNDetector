package ara.kuet.musta;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuet.musta.R;

import java.util.Map;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SaveLocation extends Fragment {

    public final static String TAG = SaveLocation.class.getSimpleName();
    private Button save,delete;
    private LinearLayout relativeLayout;
    private SharedPreferences spMaster,spName,spLat,spLon;
    private SharedPreferences.Editor editor,ename,elat,elon;
    private float lat,lon;
    private TextView[] tvName;
    int a = 0;


    public static SaveLocation newInstance(){
        return new SaveLocation();
    }
    public SaveLocation() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_next, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAll(view);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle("Delete Location");
                ab.setMessage("You ar going to delete a location");
                final EditText name = new EditText(getActivity());
                name.setHint("Give Mosque Name To Delete");
                ab.setView(name);
                ab.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String mnamee = name.getText().toString();

                        if (mnamee.isEmpty()) {
                            Toast.makeText(getActivity(), "At least give a Name !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Deleted Location: " + mnamee, Toast.LENGTH_LONG).show();
                            ename.remove(mnamee);
                            elat.remove(mnamee);
                            elon.remove(mnamee);
                            ename.apply();
                            elat.apply();
                            elon.apply();
                            mapper();
                        }
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog a = ab.create();
                a.setIcon(R.drawable.mosque);
                a.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                lat = spMaster.getFloat("latitude", 0);//+North,-South
                lon = spMaster.getFloat("longitude", 0);//+East,-West
                ab.setTitle("Save Location");
                ab.setMessage("Latitude:" + lat + " &" + "\nLongitude:" + lon);
                final EditText name = new EditText(getActivity());
                name.setHint("Give a Mosque Name To Save");
                ab.setView(name);
                ab.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String mname = name.getText().toString();
                        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                        if (mname.isEmpty()) {
                            Toast.makeText(getActivity(), "At least give a Name !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Saved Location: " + mname, Toast.LENGTH_LONG).show();
                            ename.putString(mname, mname);
                            elat.putFloat(mname, lat);
                            elon.putFloat(mname, lon);
                            ename.apply();
                            elat.apply();
                            elon.apply();
                            am.setRingerMode(0);
                            mapper();
                        }
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog a = ab.create();
                a.setIcon(R.drawable.mosque);
                a.show();
            }
        });
    }

    private void mapper() {
        try {
            relativeLayout.removeAllViews();
        } catch (Exception ignored)
        {

        }
        Map<String, ?> allName = spName.getAll();
        int i = 0;
        for (Map.Entry<String, ?> entry : allName.entrySet()) {
            String name = entry.getKey();
            i++;
            //Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
            TextView rowTextView = new TextView(getActivity());
            rowTextView.setGravity(Gravity.CENTER);
            rowTextView.setBackgroundResource(R.drawable.tvlocation);
            rowTextView.setText(i + "." + name);
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setTextSize(15);
            //rowTextView.setWidth(50);
            relativeLayout.addView(rowTextView);
            tvName[i - 1] = rowTextView;
        }
    }

    private void initAll(View v) {
        spMaster = getActivity().getSharedPreferences("mysp",0);
        spName = getActivity().getSharedPreferences("myspname",0);
        spLat = getActivity().getSharedPreferences("mysplat",0);
        spLon = getActivity().getSharedPreferences("mysplon",0);
        editor = spMaster.edit();
        ename = spName.edit();
        elat = spLat.edit();
        elon = spLon.edit();
        save = (Button) v.findViewById(R.id.bsave);
        delete = (Button) v.findViewById(R.id.bdelete);
        relativeLayout = (LinearLayout) v.findViewById(R.id.innerlay);
        final int n = 100;
        tvName = new TextView[n];
        mapper();
    }

}
