package ahmed.silmey.clinicreservation.PatientApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import ahmed.silmey.clinicreservation.Model.DoctorModel;
import ahmed.silmey.clinicreservation.PatientApp.Fragments.DoctorsFragment;
import ahmed.silmey.clinicreservation.R;

public class BookingActivity extends AppCompatActivity
{
    String DOCTOR_KEY;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        DOCTOR_KEY = getIntent().getStringExtra(DoctorsFragment.EXTRA_DOCTOR_KEY);

        textView = findViewById(R.id.doc_name);

        returndata(DOCTOR_KEY);
    }

    public void returndata(String key)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);


        mDatabase.child("AllUsers").child("Doctors").child(key).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        DoctorModel doctorModel = dataSnapshot.getValue(DoctorModel.class);

                        textView.setText("Dr. " + doctorModel.getFullname());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
