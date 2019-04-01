package ahmed.silmey.clinicreservation.PatientApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ahmed.silmey.clinicreservation.PatientApp.Fragments.DoctorsFragment;
import ahmed.silmey.clinicreservation.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteBookingActivity extends AppCompatActivity
{
    String KEY, NAME, DATE, TIME, URL;

    CircleImageView circleImageView;
    TextView doctorname, date, time;
    Button book_now_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_blue_24dp);
        getSupportActionBar().setTitle("");

        KEY = getIntent().getStringExtra(BookingActivity.EXTRA_KEY);
        NAME = getIntent().getStringExtra(BookingActivity.EXTRA_NAME);
        DATE = getIntent().getStringExtra(BookingActivity.EXTRA_DATE);
        TIME = getIntent().getStringExtra(BookingActivity.EXTRA_TIME);
        URL = getIntent().getStringExtra(BookingActivity.EXTRA_URL);

        circleImageView = findViewById(R.id.doctor_image);
        doctorname = findViewById(R.id.doctor_name_txt);
        date = findViewById(R.id.date_txt);
        time = findViewById(R.id.time_txt);

        book_now_btn = findViewById(R.id.book_btn);

        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.doctor2)
                .error(R.drawable.doctor2)
                .into(circleImageView);

        doctorname.setText("Dr. " + NAME + "'s appointment");
        date.setText(DATE);
        time.setText(TIME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
