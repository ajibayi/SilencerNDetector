package ara.kuet.musta;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.text.format.Time;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {

    private AudioManager audioManager;

    private Context context;
    private Handler mHandler = new Handler();

    Time time = new Time();

    SharedPreferences spMaster;
    SharedPreferences.Editor editor;

    int getMode;
    int fm, feh, fem, frh, frm;
    boolean fajrEngage = false, fajrRelease = false;
    int zm, zeh, zem, zrh, zrm;
    boolean zuhrEngage = false, zuhrRelease = false;
    int am, aeh, aem, arh, arm;
    boolean asrEngage = false, asrRelease = false;
    int mm, meh, mem, mrh, mrm;
    boolean maghribEngage = false, maghribRelease = false;
    int im, ieh, iem, irh, irm;
    boolean ishaEngage = false, ishaRelease = false;

    public CustomTimerTask(Context con) {
        this.context = con;
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void run() {
                audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                spMaster = context.getSharedPreferences("mysp", 0);
                editor = spMaster.edit();
                time.setToNow();
                getMode = audioManager.getRingerMode();
                fm = spMaster.getInt("fajr_phone_mode", 0);
                feh = spMaster.getInt("fajr_engage_actual_hour", 0);
                fem = spMaster.getInt("fajr_engage_actual_minute", 0);
                frh = spMaster.getInt("fajr_release_actual_hour", 0);
                frm = spMaster.getInt("fajr_release_actual_minute", 0);

                zm = spMaster.getInt("zuhr_phone_mode", 0);
                zeh = spMaster.getInt("zuhr_engage_actual_hour", 0);
                zem = spMaster.getInt("zuhr_engage_actual_minute", 0);
                zrh = spMaster.getInt("zuhr_release_actual_hour", 0);
                zrm = spMaster.getInt("zuhr_release_actual_minute", 0);

                am = spMaster.getInt("asr_phone_mode", 0);
                aeh = spMaster.getInt("asr_engage_actual_hour", 0);
                aem = spMaster.getInt("asr_engage_actual_minute", 0);
                arh = spMaster.getInt("asr_release_actual_hour", 0);
                arm = spMaster.getInt("asr_release_actual_minute", 0);

                mm = spMaster.getInt("maghrib_phone_mode", 0);
                meh = spMaster.getInt("maghrib_engage_actual_hour", 0);
                mem = spMaster.getInt("maghrib_engage_actual_minute", 0);
                mrh = spMaster.getInt("maghrib_release_actual_hour", 0);
                mrm = spMaster.getInt("maghrib_release_actual_minute", 0);

                im = spMaster.getInt("isha_phone_mode", 0);
                ieh = spMaster.getInt("isha_engage_actual_hour", 0);
                iem = spMaster.getInt("isha_engage_actual_minute", 0);
                irh = spMaster.getInt("isha_release_actual_hour", 0);
                irm = spMaster.getInt("isha_release_actual_minute", 0);

                fajrEngage = ((feh == time.hour && fem == time.minute) || (time.hour == spMaster.getInt("fajr_hour", 0) && time.minute == spMaster.getInt("fajr_minute", 0))) && (getMode != fm);
                fajrRelease = (frh == time.hour) && (frm == time.minute);

                zuhrEngage = ((zeh == time.hour && zem == time.minute) || (time.hour == spMaster.getInt("zuhr_hour", 0) && time.minute == spMaster.getInt("zuhr_minute", 0))) && (getMode != zm);
                zuhrRelease = (zrh == time.hour) && (zrm == time.minute);

                asrEngage = ((aeh == time.hour && aem == time.minute) || (time.hour == spMaster.getInt("asr_hour", 0) && time.minute == spMaster.getInt("asr_minute", 0))) && (getMode != am);
                asrRelease = (arh == time.hour) && (arm == time.minute);

                maghribEngage = ((meh == time.hour && mem == time.minute) || (time.hour == spMaster.getInt("maghrib_hour", 0) && time.minute == spMaster.getInt("maghrib_minute", 0))) && (getMode != mm);
                maghribRelease = (mrh == time.hour) && (mrm == time.minute);

                ishaEngage = ((ieh == time.hour && iem == time.minute) || (time.hour == spMaster.getInt("isha_hour", 0) && time.minute == spMaster.getInt("isha_minute", 0))) && (getMode != im);
                ishaRelease = (irh == time.hour) && (irm == time.minute);


                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (fajrEngage) {
                            silentMaker(fm);
                        } else if (fajrRelease) {
                            if (spMaster.getBoolean("run_flag", false)) {
                                if (getMode == fm) {
                                    audioManager.setRingerMode(spMaster.getInt("before_mode", 2));
                                }
                                editor.putBoolean("run_flag", false);
                                editor.apply();
                            }
                        } else if (zuhrEngage) {
                            silentMaker(zm);
                        } else if (zuhrRelease) {
                            if (spMaster.getBoolean("run_flag", false)) {
                                if (getMode == zm) {
                                    audioManager.setRingerMode(spMaster.getInt("before_mode", 2));
                                }
                                editor.putBoolean("run_flag", false);
                                editor.apply();
                            }
                        } else if (asrEngage) {
                            silentMaker(am);
                        } else if (asrRelease) {
                            if (spMaster.getBoolean("run_flag", false)) {
                                if (getMode == am) {
                                    audioManager.setRingerMode(spMaster.getInt("before_mode", 2));
                                }
                                editor.putBoolean("run_flag", false);
                                editor.apply();
                            }
                        } else if (maghribEngage) {
                            silentMaker(mm);
                        } else if (maghribRelease) {
                            if (spMaster.getBoolean("run_flag", false)) {
                                if (getMode == mm) {
                                    audioManager.setRingerMode(spMaster.getInt("before_mode", 2));
                                }
                                editor.putBoolean("run_flag", false);
                                editor.apply();
                            }
                        } else if (ishaEngage) {
                            silentMaker(im);
                        } else if (ishaRelease) {
                            if (spMaster.getBoolean("run_flag", false)) {
                                if (getMode == im) {
                                    audioManager.setRingerMode(spMaster.getInt("before_mode", 2));
                                }
                                editor.putBoolean("run_flag", false);
                                editor.apply();
                            }
                        }
                        // forSunrise();
                    }
                });
            }
        }).start();

    }

    private void silentMaker(int fm) {
        editor.putInt("before_mode", getMode);
        audioManager.setRingerMode(fm);
        editor.putBoolean("run_flag", true);
        editor.apply();
    }

    private void forSunrise() {
        String upsalat = null, uptime = null;
        int h = spMaster.getInt("fajr_hour", 5) * 60 + spMaster.getInt("fajr_minute", 10);
        int th = time.hour * 60 + time.minute;
        int ultimate = h - th;
        //
        int h1 = spMaster.getInt("zuhr_hour", 12) * 60 + spMaster.getInt("zuhr_minute", 12);
        int th1 = time.hour * 60 + time.minute;
        int ultimate1 = h1 - th1;
        //
        int h2 = spMaster.getInt("asr_hour", 16) * 60 + spMaster.getInt("asr_minute", 22);
        int th2 = time.hour * 60 + time.minute;
        int ultimate2 = h2 - th2;
        //
        int h3 = spMaster.getInt("maghrib_hour", 18) * 60 + spMaster.getInt("maghrib_minute", 2);
        int th3 = time.hour * 60 + time.minute;
        int ultimate3 = h3 - th3;
        //
        int h4 = spMaster.getInt("isha_hour", 19) * 60 + spMaster.getInt("isha_minute", 15);
        int th4 = time.hour * 60 + time.minute;
        int ultimate4 = h4 - th4;
        //
        if (ultimate >= 0) {
            upsalat = "Fajr Salat";
            if (ultimate != 0) {
                uptime = String.valueOf(ultimate / 60) + " Hour:" + String.valueOf(ultimate % 60) + " Minute";
            } else {
                upsalat = "On Going...";
            }
        } else if (ultimate1 >= 0) {
            upsalat = "Duhr Namaz";
            if (ultimate1 != 0) {
                uptime = String.valueOf(ultimate1 / 60) + " Hour:" + String.valueOf(ultimate1 % 60) + " Minute";
            } else {
                upsalat = "On Going...";
            }
        } else if (ultimate2 >= 0) {
            upsalat = "Asr Namaz";
            if (ultimate2 != 0) {
                uptime = String.valueOf(ultimate2 / 60) + " Hour:" + String.valueOf(ultimate2 % 60) + " Minute";
            } else {
                upsalat = "On Going...";
            }
        } else if (ultimate3 >= 0) {
            upsalat = "Maghrib Namaz";
            if (ultimate3 != 0) {
                uptime = String.valueOf(ultimate3 / 60) + " Hour:" + String.valueOf(ultimate3 % 60) + " Minute";
            } else {
                upsalat = "On Going...";
            }
        } else if (ultimate4 >= 0) {
            upsalat = "Isha Namaz";
            if (ultimate4 != 0) {
                uptime = String.valueOf(ultimate4 / 60) + " Hour:" + String.valueOf(ultimate4 % 60) + " Minute";
            } else {
                upsalat = "On Going...";
            }
        }

        //just save
        editor.putString("upnamaz", upsalat);
        editor.putString("uptime", uptime);
        editor.apply();
        try {
            SunriseFragment.uptime.setText(uptime);
            SunriseFragment.upnamaz.setText(upsalat);
        } catch (Exception ignored) {
        }
    }
}