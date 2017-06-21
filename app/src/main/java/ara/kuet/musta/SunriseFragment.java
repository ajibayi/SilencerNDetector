package ara.kuet.musta;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kuet.musta.R;

public class SunriseFragment extends Fragment {

    static TextView uptime;
    static TextView upnamaz;
    static TextView f, z, a, m, i;
    SharedPreferences sp;

    public SunriseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sunrise_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceStateSun) {
        super.onViewCreated(view, savedInstanceStateSun);
        //Toast.makeText(getActivity(),"onCreate",Toast.LENGTH_LONG).show();
        sp = getActivity().getSharedPreferences("mysp", 0);
        f = (TextView) view.findViewById(R.id.ftime);
        z = (TextView) view.findViewById(R.id.ztime);
        a = (TextView) view.findViewById(R.id.atime);
        m = (TextView) view.findViewById(R.id.mtime);
        i = (TextView) view.findViewById(R.id.itime);

    }

    @Override
    public void onResume() {
        super.onResume();
        f.setText("Fajr Time : " + sp.getString("fajr_clock", "Not Set"));
        z.setText("Duhr Time : " + sp.getString("zuhr_clock", "Not Set"));
        a.setText("Asr Time : " + sp.getString("asr_clock", "Not Set"));
        m.setText("Maghrib Time : " + sp.getString("maghrib_clock", "Not Set"));
        i.setText("Isha Time : " + sp.getString("isha_clock", "Not Set"));
        //Toast.makeText(getActivity(),"onResume",Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getActivity(),"onDestroy",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Toast.makeText(getActivity(),"onPause",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Toast.makeText(getActivity(),"onStart",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Toast.makeText(getActivity(), "onStop", Toast.LENGTH_LONG).show();
//    }

}
