package ara.kuet.musta;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private Fragment mCurrentFragment;
    public TimePickerDialogFragment(Fragment fragment) {
        mCurrentFragment = fragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        bundle = getArguments();
        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");
        return new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener)mCurrentFragment,hour,
                minute, false);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}