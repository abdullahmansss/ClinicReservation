package ahmed.silmey.clinicreservation.PatientApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ahmed.silmey.clinicreservation.Model.DoctorModel;
import ahmed.silmey.clinicreservation.Model.TimeModel;
import ahmed.silmey.clinicreservation.PatientApp.Fragments.DoctorsFragment;
import ahmed.silmey.clinicreservation.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookingActivity extends AppCompatActivity
{
    public static String DOCTOR_KEY;
    RecyclerView timerecyclerView;
    TimesListAdapter timesListAdapter;
    List<TimeModel> timelist;

    CircleImageView doctor_image;
    public static TextView doctor_name_txt;
    public static Button selectdate;
    public static String selected_date = "";

    public static String EXTRA_KEY = "doctor";
    public static String EXTRA_NAME = "name";
    public static String EXTRA_DATE = "date";
    public static String EXTRA_TIME = "time";
    public static String EXTRA_URL= "url";

    public static String doctor_name,doctor_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        DOCTOR_KEY = getIntent().getStringExtra(DoctorsFragment.EXTRA_DOCTOR_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_blue_24dp);
        getSupportActionBar().setTitle("");

        timerecyclerView = findViewById(R.id.time_recyclerview);
        doctor_image = findViewById(R.id.doctor_image);
        doctor_name_txt = findViewById(R.id.doctor_name_txt);
        selectdate = findViewById(R.id.select_date_btn);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        timerecyclerView.setLayoutManager(layoutManager);
        timerecyclerView.setHasFixedSize(true);

        timelist = new ArrayList<>();

        timelist.add(new TimeModel("09:00 am - 09:30 am", ""));
        timelist.add(new TimeModel("09:30 am - 10:00 am", ""));
        timelist.add(new TimeModel("10:00 am - 10:30 am" , ""));
        timelist.add(new TimeModel("10:30 am - 11:00 am", ""));
        timelist.add(new TimeModel("11:00 am - 11:30 am", ""));
        timelist.add(new TimeModel("11:30 am - 12:00 pm", "booked up"));

        timelist.add(new TimeModel("02:00 pm - 02:30 pm", ""));
        timelist.add(new TimeModel("02:30 pm - 03:00 pm", ""));
        timelist.add(new TimeModel("03:00 pm - 03:30 pm", "booked up"));
        timelist.add(new TimeModel("03:30 pm - 04:00 pm", ""));
        timelist.add(new TimeModel("04:00 pm - 04:30 pm", ""));
        timelist.add(new TimeModel("04:30 pm - 05:00 pm", ""));

        timesListAdapter = new TimesListAdapter(timelist);

        timerecyclerView.setAdapter(timesListAdapter);

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

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

                        doctor_image_url = doctorModel.getImageurl();

                        Picasso.get()
                                .load(doctor_image_url)
                                .placeholder(R.drawable.doctor2)
                                .error(R.drawable.doctor2)
                                .into(doctor_image);

                        doctor_name = doctorModel.getFullname();
                        doctor_name_txt.setText("Dr. " + doctor_name + "'s timetable");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.dialoge,this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return datePickerDialog ;
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            // Do something with the date chosen by the user
            int month2 = month + 1;

            selected_date = day + "/" + month2  + "/" + year;
            selectdate.setText(selected_date);
        }
    }

    public class TimesListAdapter extends RecyclerView.Adapter<TimesListAdapter.Viewholder>
    {
        List<TimeModel> timelist;

        public TimesListAdapter(List<TimeModel> timelist)
        {
            this.timelist = timelist;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_item, viewGroup, false);
            return new  Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i)
        {
            String time = timelist.get(i).getTime();
            viewholder.time.setText(time);

            String booked = timelist.get(i).getBooked();
            viewholder.booked_txt.setText(booked);

            if (booked.length() != 0)
            {
                viewholder.materialRippleLayout.setEnabled(false);
            }

            viewholder.materialRippleLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (selected_date.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "please select date firstly", Toast.LENGTH_SHORT).show();
                    } else
                        {
                            String time = viewholder.time.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), CompleteBookingActivity.class);
                            intent.putExtra(EXTRA_KEY, DOCTOR_KEY);
                            intent.putExtra(EXTRA_NAME, doctor_name);
                            intent.putExtra(EXTRA_DATE, selected_date);
                            intent.putExtra(EXTRA_TIME, time);
                            intent.putExtra(EXTRA_URL, doctor_image_url);
                            startActivity(intent);
                        }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return timelist.size();
        }

        class Viewholder extends RecyclerView.ViewHolder
        {
            View view;

            TextView time,booked_txt;
            MaterialRippleLayout materialRippleLayout;

            Viewholder(@NonNull View itemView)
            {
                super(itemView);

                view = itemView;

                time = view.findViewById(R.id.time_txt);
                booked_txt = view.findViewById(R.id.booked_txt);
                materialRippleLayout = view.findViewById(R.id.time_card);
            }
        }
    }
}
