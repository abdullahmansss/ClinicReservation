package ahmed.silmey.clinicreservation.DoctorApp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import ahmed.silmey.clinicreservation.MainActivity;
import ahmed.silmey.clinicreservation.R;

public class BookingsFragment extends Fragment
{
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.doctor_recent_bookings_fragment, container, false);

        return view;
    }
}
