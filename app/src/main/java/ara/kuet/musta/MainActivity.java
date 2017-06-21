package ara.kuet.musta;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kuet.musta.R;

public class MainActivity extends FragmentActivity {

    public SharedPreferences spMaster;
    public SharedPreferences.Editor editor;
    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceStateMain) {
        super.onCreate(savedInstanceStateMain);
        setContentView(R.layout.activity_main);
        spMaster = getSharedPreferences("mysp", 0);
        editor = spMaster.edit();

        initializer();
        assignPreference();
        enableService();

        mTitle = mDrawerTitle = getTitle();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceStateMain == null) {
            navigateTo(1);
        }

    }

    private void assignPreference() {
        if (spMaster.getInt("fajr_hour", -1) == -1) {
            editor.putInt("fajr_hour", 4);
            editor.putInt("fajr_minute", 58);
        }
        if (spMaster.getInt("zuhr_hour", -1) == -1) {
            editor.putInt("zuhr_hour", 12);
            editor.putInt("zuhr_minute", 10);
        }
        if (spMaster.getInt("asr_hour", -1) == -1) {
            editor.putInt("asr_hour", 15);
            editor.putInt("asr_minute", 33);
        }
        if (spMaster.getInt("maghrib_hour", -1) == -1) {
            editor.putInt("maghrib_hour", 18);
            editor.putInt("maghrib_minute", 7);
        }
        if (spMaster.getInt("isha_hour", -1) == -1) {
            editor.putInt("isha_hour", 19);
            editor.putInt("isha_minute", 22);
        }
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(this,"OnBackPressed !",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this,"onPause",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this,"onStart",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this,"onStop",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this,"onRestart",Toast.LENGTH_LONG).show();
    }

    private void enableService() {
        if (isMyServiceRunning(TestService.class)) {
            Intent service = new Intent(this, TestService.class);
            startService(service);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    public void initializer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        myAdapter = new MyAdapter(this);
        mDrawerList.setAdapter(myAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Log.v(TAG, "ponies");
            navigateTo(position);
        }
    }

    private void navigateTo(int position) {
        //Log.v(TAG, "List View Item: " + position);
        switch (position) {
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                QiblaDirection.newInstance(),
                                QiblaDirection.TAG).commit();

                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                TabbedActivity.newInstance(),
                                TabbedActivity.TAG).commit();


                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                SaveLocation.newInstance(),
                                SaveLocation.TAG).commit();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                AboutUs.newInstances(),
                                AboutUs.TAG).commit();
                break;
            default:
                break;

        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

}

class MyAdapter extends BaseAdapter {
    private Context context;

    String[] mDrawerItems;
    int[] images = {R.drawable.icon_compass9, R.drawable.nav_silencer, R.drawable.mosque, R.drawable.drawer_about_boy};

    public MyAdapter(Context context) {
        this.context = context;
        mDrawerItems = context.getResources().getStringArray(R.array.drawer_titles);
    }

    @Override
    public int getCount() {
        return mDrawerItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mDrawerItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, parent, false);
        } else {
            row = convertView;
        }
        TextView titleTextView = (TextView) row.findViewById(R.id.textView1);
        ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView1);
        titleTextView.setText(mDrawerItems[position]);
        titleImageView.setImageResource(images[position]);
        return row;
    }
}
