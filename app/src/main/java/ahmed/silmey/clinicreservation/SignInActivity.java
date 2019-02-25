package ahmed.silmey.clinicreservation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.Timer;
import java.util.TimerTask;

import ahmed.silmey.clinicreservation.DoctorApp.DoctorMainActivity;
import ahmed.silmey.clinicreservation.PatientApp.PatinentMainActivity;

public class SignInActivity extends AppCompatActivity
{
    Button sign_in_btn;
    EditText email_field,password_field;
    RotateLoading rotateLoading;

    String email,password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        rotateLoading = findViewById(R.id.signinrotateloading);

        sign_in_btn = findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = email_field.getText().toString();
                password = password_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                rotateLoading.start();

                UserLogin(email,password);
            }
        });
    }

    private void UserLogin(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            category();
                        } else
                        {
                            rotateLoading.stop();
                            String taskmessage = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), taskmessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void category()
    {
        final String id = getUID();
        FirebaseDatabase firebaseDatabase;
        final DatabaseReference databaseReference;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("AllUsers").child("Doctors").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild(id))
                        {
                            updateDoctorUI();
                        } else
                        {
                            databaseReference.child("AllUsers").child("Patients").addListenerForSingleValueEvent(
                                    new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if (dataSnapshot.hasChild(id))
                                            {
                                                updatePatientUI();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                        {
                                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePatientUI()
    {
        Intent i = new Intent(getApplicationContext(), PatinentMainActivity.class);
        startActivity(i);
    }

    private void updateDoctorUI()
    {
        Intent i = new Intent(getApplicationContext(), DoctorMainActivity.class);
        startActivity(i);
    }

    private String getUID()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();

        return UserID;
    }
}
