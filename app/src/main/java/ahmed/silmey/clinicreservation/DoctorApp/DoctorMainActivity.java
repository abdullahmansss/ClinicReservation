package ahmed.silmey.clinicreservation.DoctorApp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ahmed.silmey.clinicreservation.DoctorApp.Fragments.BookingsFragment;
import ahmed.silmey.clinicreservation.DoctorApp.Fragments.DoctorProfileFragment;
import ahmed.silmey.clinicreservation.MainActivity;
import ahmed.silmey.clinicreservation.PatientApp.Fragments.DoctorsFragment;
import ahmed.silmey.clinicreservation.PatientApp.Fragments.PatientProfileFragment;
import ahmed.silmey.clinicreservation.R;

public class DoctorMainActivity extends AppCompatActivity
{
    BottomNavigationView navigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        navigation = findViewById(R.id.navigation);

        fragmentManager = getSupportFragmentManager();

        Fragment bookingsFragment = new BookingsFragment();
        loadFragment(bookingsFragment);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.bookings:
                        //navigation.setBackgroundColor(getResources().getColor(R.color.blue_grey_700));
                        Fragment bookingsFragment1 = new BookingsFragment();
                        loadFragment(bookingsFragment1);
                        return true;
                    case R.id.profile:
                        //navigation.setBackgroundColor(getResources().getColor(R.color.blue_grey_700));
                        Fragment doctorProfileFragment = new DoctorProfileFragment();
                        loadFragment(doctorProfileFragment);
                        return true;
                }
                return false;
            }
        });
    }

    public void loadFragment(Fragment fragment)
    {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        getFragmentManager().popBackStack();
        // Commit the transaction
        fragmentTransaction.commit();
    }

    private long exitTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        doExitApp();
    }
}
