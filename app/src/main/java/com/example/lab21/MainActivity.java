package com.example.lab21;

import android.hardware.Sensor;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lab21.fragments.BehaviorAnalysisFragment;
import com.example.lab21.fragments.DirectionFinderFragment;
import com.example.lab21.fragments.DynamicMotionFragment;
import com.example.lab21.fragments.PedometerFragment;
import com.example.lab21.fragments.SensorMonitorFragment;
import com.example.lab21.fragments.SensorsOverviewFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout sideDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        sideDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, sideDrawer, mainToolbar, R.string.app_name, R.string.app_name);
        sideDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (savedInstanceState == null) {
            loadNewFragment(new SensorsOverviewFragment());
            navigation.setCheckedItem(R.id.nav_overview);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem navItem) {
        int itemSelector = navItem.getItemId();

        if (itemSelector == R.id.nav_overview) {
            loadNewFragment(new SensorsOverviewFragment());
        } else if (itemSelector == R.id.nav_temp) {
            loadNewFragment(SensorMonitorFragment.create(Sensor.TYPE_AMBIENT_TEMPERATURE, "Ambient Temperature", "SINGLE"));
        } else if (itemSelector == R.id.nav_humid) {
            loadNewFragment(SensorMonitorFragment.create(Sensor.TYPE_RELATIVE_HUMIDITY, "Humidity Level", "SINGLE"));
        } else if (itemSelector == R.id.nav_prox) {
            loadNewFragment(SensorMonitorFragment.create(Sensor.TYPE_PROXIMITY, "Proximity Sensor", "SINGLE"));
        } else if (itemSelector == R.id.nav_magn) {
            loadNewFragment(SensorMonitorFragment.create(Sensor.TYPE_MAGNETIC_FIELD, "Magnetic Magnitude", "VECTOR_LENGTH"));
        } else if (itemSelector == R.id.nav_accel) {
            loadNewFragment(DynamicMotionFragment.build(Sensor.TYPE_ACCELEROMETER, "Accelerometer Data"));
        } else if (itemSelector == R.id.nav_grav) {
            loadNewFragment(DynamicMotionFragment.build(Sensor.TYPE_GRAVITY, "Gravity Vector"));
        } else if (itemSelector == R.id.nav_gyro) {
            loadNewFragment(DynamicMotionFragment.build(Sensor.TYPE_GYROSCOPE, "Rotation Rate (rad/s)"));
        } else if (itemSelector == R.id.nav_steps) {
            loadNewFragment(new PedometerFragment());
        } else if (itemSelector == R.id.nav_compass) {
            loadNewFragment(new DirectionFinderFragment());
        } else if (itemSelector == R.id.nav_behavior) {
            loadNewFragment(new BehaviorAnalysisFragment());
        }

        sideDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadNewFragment(Fragment fragmentInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragmentInstance)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (sideDrawer.isDrawerOpen(GravityCompat.START)) {
            sideDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
