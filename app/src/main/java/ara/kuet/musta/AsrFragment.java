package ara.kuet.musta;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kuet.musta.R;


public class AsrFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    SharedPreferences spmaster;
    SharedPreferences.Editor editor;
    Button update;
    TextView clock;
    Spinner mode, engage, release;
    String tclock;
    int hour, minute;
    int define_hour = 15;
    int define_minute = 33;
    int define_engage = 5;
    int define_release = 10 + 5;

    public AsrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.asr_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spmaster = getActivity().getSharedPreferences("mysp", 0);
        editor = spmaster.edit();

        update = (Button) view.findViewById(R.id.but_asr_update);
        clock = (TextView) view.findViewById(R.id.tv_asr_clock);
        clock.setText(spmaster.getString("asr_clock", clockMaker(define_hour, define_minute)));

        mode = (Spinner) view.findViewById(R.id.spinner_asr_pmode);
        engage = (Spinner) view.findViewById(R.id.spinner_asr_eggage);
        release = (Spinner) view.findViewById(R.id.spinner_asr_release);

        LoadSpinner();

        Button_update();
    }

    private void Button_update() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                hour = spmaster.getInt("asr_hour", define_hour);
                minute = spmaster.getInt("asr_minute", define_minute);
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                TimePickerDialogFragment dialog = new TimePickerDialogFragment(AsrFragment.this);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "Dialog");
            }
        });
    }

    private void LoadSpinner() {

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.phone_mode, android.R.layout.simple_list_item_checked);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mode.setAdapter(adapter1);
        mode.setSelection(spmaster.getInt("asr_phone_mode", 0));
        mode.setOnItemSelectedListener(new modeAsrSpinnerItemListener());

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.before_time_05, android.R.layout.simple_list_item_checked);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        engage.setAdapter(adapter2);
        engage.setSelection(spmaster.getInt("asr_engage_poss", 0));
        engage.setOnItemSelectedListener(new engageAsrSpinnerItemListener());

        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.release_time_15, android.R.layout.simple_list_item_checked);
        adapter3.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        release.setAdapter(adapter3);
        release.setSelection(spmaster.getInt("asr_release_poss", 0));
        release.setOnItemSelectedListener(new releaseAsrSpinnerItemListener());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editor.putInt("asr_hour", hourOfDay);
        editor.putInt("asr_minute", minute);
        String nclock = clockMaker(hourOfDay, minute);
        editor.putString("asr_clock", nclock);
        clock.setText(nclock);
        editor.apply();
        engage_release_actualTime();

    }

    public String clockMaker(int hourOfDay, int minute) {
        if (hourOfDay >= 12) {
            tclock = pad(hourOfDay - 12) + ":" + pad(minute) + " PM";
        } else {
            tclock = pad(hourOfDay) + ":" + pad(minute) + " AM";
        }
        return tclock;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public class modeAsrSpinnerItemListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            editor.putInt("asr_phone_mode", position);
            editor.apply();
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

        }
    }

    public class engageAsrSpinnerItemListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            editor.putInt("asr_engage_poss", position);
            editor.putInt("asr_engage_minute", position + 5);
            editor.apply();
            engage_release_actualTime();
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

        }
    }

    public class releaseAsrSpinnerItemListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            editor.putInt("asr_release_poss", position);
            editor.putInt("asr_release_minute", position + 15);
            editor.apply();
            engage_release_actualTime();
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

        }
    }

    private void engage_release_actualTime() {

        int engage_actual_minute, release_actual_minute;
        int hour = spmaster.getInt("asr_hour", define_hour);
        int minute = spmaster.getInt("asr_minute", define_minute);
        int engage_minute = spmaster.getInt("asr_engage_minute", define_engage);
        int release_minute = spmaster.getInt("asr_release_minute", define_release);
        engage_actual_minute = minute - engage_minute;
        if (engage_actual_minute < 0) {
            hour--;
            engage_actual_minute = 60 + engage_actual_minute;
        }
        editor.putInt("asr_engage_actual_hour", hour);
        editor.putInt("asr_engage_actual_minute", engage_actual_minute);
        release_actual_minute = minute + release_minute;
        if (release_actual_minute >= 60) {
            release_actual_minute = release_actual_minute - 60;
            hour++;
        }
        editor.putInt("asr_release_actual_hour", hour);
        editor.putInt("asr_release_actual_minute", release_actual_minute);
        editor.apply();
    }
}
