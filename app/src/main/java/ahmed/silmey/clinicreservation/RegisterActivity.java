package ahmed.silmey.clinicreservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ahmed.silmey.clinicreservation.DoctorApp.DoctorMainActivity;
import ahmed.silmey.clinicreservation.Model.DoctorModel;
import ahmed.silmey.clinicreservation.Model.PatientModel;
import ahmed.silmey.clinicreservation.PatientApp.PatinentMainActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity
{
    MaterialRippleLayout doctor_card,patient_card;

    CircleImageView profile_image;
    static EditText first_name,last_name,email_address,password,phone_number,address;
    String first_name_txt,last_name_txt,full_name_txt,email_txt,password_txt,mobile_txt,address_txt;
    Button sign_up_btn,cancel_btn;
    Spinner specialization_spinner;
    String specialization_txt;
    String selectedimageurl;

    Uri photoPath;
    ProgressDialog progressDialog;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        doctor_card = findViewById(R.id.doctor_sign_up_card);
        patient_card = findViewById(R.id.patient_sign_up_card);

        doctor_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoctorDialog();
            }
        });

        patient_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatientDialog();
            }
        });
    }

    private void showDoctorDialog()
    {
        final Dialog dialog = new Dialog(RegisterActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.doctor_register_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        profile_image = dialog.findViewById(R.id.profile_image);
        first_name = dialog.findViewById(R.id.first_name_field);
        last_name = dialog.findViewById(R.id.last_name_field);
        email_address = dialog.findViewById(R.id.email_field);
        password = dialog.findViewById(R.id.password_field);
        phone_number = dialog.findViewById(R.id.mobile_field);
        address = dialog.findViewById(R.id.address_field);
        specialization_spinner = dialog.findViewById(R.id.specialization_spinner);

        sign_up_btn = dialog.findViewById(R.id.sign_up_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.department, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        specialization_spinner.setAdapter(adapter1);

        specialization_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                specialization_txt = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                first_name_txt = first_name.getText().toString();
                last_name_txt = last_name.getText().toString();
                full_name_txt = first_name_txt + " " + last_name_txt;
                email_txt = email_address.getText().toString();
                password_txt = password.getText().toString();
                mobile_txt = phone_number.getText().toString();
                address_txt = address.getText().toString();

                if (TextUtils.isEmpty(first_name_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(last_name_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password_txt.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "short password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoPath == null)
                {
                    Toast.makeText(getApplicationContext(), "please add your picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (specialization_txt.equals("Select your specialty"))
                {
                    Toast.makeText(getApplicationContext(), "please select your specialization", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Doctor Sign Up");
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                CreateDoctorAccount(email_txt,password_txt,full_name_txt,mobile_txt,specialization_txt,address_txt);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(RegisterActivity.this);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showPatientDialog()
    {
        final Dialog dialog = new Dialog(RegisterActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.patient_register_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        profile_image = dialog.findViewById(R.id.profile_image);
        first_name = dialog.findViewById(R.id.first_name_field);
        last_name = dialog.findViewById(R.id.last_name_field);
        email_address = dialog.findViewById(R.id.email_field);
        password = dialog.findViewById(R.id.password_field);
        phone_number = dialog.findViewById(R.id.mobile_field);
        address = dialog.findViewById(R.id.address_field);

        sign_up_btn = dialog.findViewById(R.id.sign_up_btn);
        cancel_btn = dialog.findViewById(R.id.cancel_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                first_name_txt = first_name.getText().toString();
                last_name_txt = last_name.getText().toString();
                full_name_txt = first_name_txt + " " + last_name_txt;
                email_txt = email_address.getText().toString();
                password_txt = password.getText().toString();
                mobile_txt = phone_number.getText().toString();
                address_txt = address.getText().toString();

                if (TextUtils.isEmpty(first_name_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(last_name_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password_txt.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "short password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address_txt))
                {
                    Toast.makeText(getApplicationContext(), "please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoPath == null)
                {
                    Toast.makeText(getApplicationContext(), "please add your picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Patient Sign Up");
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                CreatePatientAccount(email_txt,password_txt,full_name_txt,mobile_txt,address_txt);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(RegisterActivity.this);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK)
            {
                if (result != null)
                {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.addphoto)
                            .error(R.drawable.addphoto)
                            .into(profile_image);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void CreateDoctorAccount(final String email, String password, final String fullname, final String mobilenumber, final String specialty, final String address)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            uploadImage(fullname,email,mobilenumber,specialty,address);
                        } else
                        {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImage(final String fullname,final String email,final String mobilenumber,final String specialty, final String address)
    {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                Uri downloadUri = task.getResult();

                selectedimageurl = downloadUri.toString();

                AddDoctortoDB(fullname,email,mobilenumber,specialty,address,selectedimageurl);
                progressDialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), DoctorMainActivity.class);
                startActivity(intent);
                //Toast.makeText(getContext(), "successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void AddDoctortoDB(String fullname, String email, String mobilenumber, String specialty, String address, String imageurl)
    {
        DoctorModel doctorModel = new DoctorModel(fullname,email,mobilenumber,specialty,address,imageurl);

        databaseReference.child("Doctors").child(specialty).child(getUID()).setValue(doctorModel);
        databaseReference.child("AllUsers").child("Doctors").child(getUID()).setValue(doctorModel);
    }

    private void CreatePatientAccount(final String email, String password, final String fullname, final String mobilenumber, final String address)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            uploadImagePatient(fullname,email,mobilenumber,address);
                        } else
                        {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImagePatient (final String fullname,final String email,final String mobilenumber, final String address)
    {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                Uri downloadUri = task.getResult();

                selectedimageurl = downloadUri.toString();

                AddPatienttoDB(fullname,email,mobilenumber,address,selectedimageurl);
                progressDialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), PatinentMainActivity.class);
                startActivity(intent);
                //Toast.makeText(getContext(), "successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void AddPatienttoDB(String fullname, String email, String mobilenumber, String address, String imageurl)
    {
        PatientModel patientModel = new PatientModel(fullname,email,mobilenumber,address,imageurl);

        //databaseReference.child("Patients").child(specialty).child(getUID()).setValue(patientModel);
        databaseReference.child("AllUsers").child("Patients").child(getUID()).setValue(patientModel);
    }

    private String getUID()
    {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}
