package com.freechetwithyounme.chettingapp.MyAccount;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.LocalDB.SQLiteDB;
import com.freechetwithyounme.chettingapp.LogIn;
import com.freechetwithyounme.chettingapp.MainActivity;
import com.freechetwithyounme.chettingapp.Model.User_Update;
import com.freechetwithyounme.chettingapp.R;
import com.freechetwithyounme.chettingapp.Register;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProSetting extends AppCompatActivity implements PopupBirthDate.BirthDate {
    private static final int GELLARY_REQUEST_CODE = 1;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Uri imageUri;
    int count = 0, count1 = 0, birthDateCount = 0;

    private ProgressDialog progressDialog;
    private ImageView image_choose;
    private Button update;
    private MaterialEditText edit_displayName, edit_status, edit_location, edit_number, edit_company, edit_institute, edit_bio;

    private TextView selectBirthDate, selectImageFromTextview;
    private DatePicker datePicker;

    private Spinner mUserGender;
    private RadioButton Std, Jobs;
    private RadioGroup radioGroup;

    private int imageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_setting);
        Initialize();

        Toolbar toolbar = findViewById(R.id.toolbar_prositting);
        ImageView back= findViewById(R.id.toolbar_back);
        TextView toolbarText= toolbar.findViewById(R.id.toolbar_text);
        toolbarText.setText("Profile Setting's");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProSetting.this, MyProfile.class));
            }
        });

        radioGroup = findViewById(R.id.radiobtnGroup);
        Std = findViewById(R.id.selectStudents);
        Jobs = findViewById(R.id.selectJobs);

        Std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProSetting.this, "Selected" + Std.getText().toString(), Toast.LENGTH_SHORT).show();
                edit_company.setVisibility(GONE);
                edit_institute.setVisibility(VISIBLE);
                count++;
                count1 = 0;
            }
        });
        Jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProSetting.this, "Selected" + Jobs.getText().toString(), Toast.LENGTH_SHORT).show();
                edit_institute.setVisibility(GONE);
                edit_company.setVisibility(VISIBLE);
                count1++;
                count = 0;
            }
        });

        String[] mUser_gender = getResources().getStringArray(R.array.user_gander);
        ArrayAdapter<String> userGender = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mUser_gender);
        mUserGender.setAdapter(userGender);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(uId);
        storageReference = FirebaseStorage.getInstance().getReference("update");

        progressDialog = new ProgressDialog(this);

        selectImageFromTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCount = 100;
                CharSequence charSequences[] = new CharSequence[]{
                        Html.fromHtml("<b><font color='black'>Choose Photo</font></b>"), "Take from camera", "Select Photo Gallery"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ProSetting.this);
                /*LayoutInflater inflater= getLayoutInflater();
                View birthDateView=inflater.inflate(R.layout.popupselectphoto,null);*/
                builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            birthDateCount++;
                            Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(camaraIntent, 0);
                        } else if (i == 2) {
                            Intent gallary = new Intent();
                            gallary.setType("image/*");
                            gallary.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(gallary, "Select Image"), GELLARY_REQUEST_CODE);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.BOTTOM;// | Gravity.LEFT;
                //wmlp.x = 100;   x position
                //wmlp.y = 100;   y position
                dialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String d_n = edit_displayName.getText().toString().trim();
                    String st = edit_status.getText().toString().trim();
                    String lo = edit_location.getText().toString().trim();
                    String num = edit_number.getText().toString().trim();
                    String bio = edit_bio.getText().toString().trim();

                    if (d_n.isEmpty()) {
                        edit_displayName.setError("Enter Name");
                        edit_displayName.requestFocus();
                        return;
                    } else if (num.isEmpty()) {
                        edit_number.setError("Enter Contact Number");
                        edit_number.requestFocus();
                        return;
                    } else if (st.isEmpty()) {
                        edit_status.setError("Enter status");
                        edit_status.requestFocus();
                        return;
                    } else if (lo.isEmpty()) {
                        edit_location.setError("Enter location");
                        edit_location.requestFocus();
                        return;
                    }  else if (bio.isEmpty()) {
                        edit_bio.setError("Enter somethings about you...");
                        edit_bio.requestFocus();
                        return;
                    } else if (imageCount == 0) {
                        Toast.makeText(ProSetting.this, "Please Select profile image", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        progressDialog.setTitle("User Updating...");
                        progressDialog.setMessage("please wait while updating");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        saveProfileData();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Update_Error: ", e.getMessage());
                }
            }
        });

        selectBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupBirthDate popupBirthDate = new PopupBirthDate();
                popupBirthDate.show(getSupportFragmentManager(), "birthDate");
            }
        });
    }

    @Override
    public void getBirthDay(String BirthDay) {
        selectBirthDate.setText(BirthDay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (birthDateCount > 0) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                image_choose.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.d("Image Token Error", e.getMessage());
            }

        } else {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    image_choose.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.d("Image Select Error", e.getMessage());
                }
            }
        }
    }

    public String getFileExtantion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void saveProfileData() {

        String email = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        final String dis_Name = edit_displayName.getText().toString().trim();
        final String sta = edit_status.getText().toString().trim();
        final String loc = edit_location.getText().toString().trim();
        final String gender = mUserGender.getSelectedItem().toString();
        final String age = selectBirthDate.getText().toString();
        final String num = edit_number.getText().toString().trim();

        final String com = edit_company.getText().toString().trim();
        final String ins = edit_institute.getText().toString().trim();
        final String bio = edit_bio.getText().toString().trim();

        if (dis_Name.isEmpty()) {
            edit_displayName.setError("Enter status");
            edit_displayName.requestFocus();
            return;
        } else if (sta.isEmpty()) {
            edit_status.setError("Enter status");
            edit_status.requestFocus();
            return;
        } else if (loc.isEmpty()) {
            edit_location.setError("Enter location");
            edit_location.requestFocus();
            return;
        } else if (num.isEmpty()) {
            edit_number.setError("Enter Contact Number");
            edit_number.requestFocus();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(this, "You Have No Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (bio.isEmpty()) {
            edit_bio.setError("Enter somethings about you...");
            edit_bio.requestFocus();
            return;
        }

        StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExtantion(imageUri));
        final String finalEmail = email;
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri getUri = uriTask.getResult();

                        HashMap<String, String> updateHashMap = new HashMap<>();
                        updateHashMap.put("name", dis_Name);
                        updateHashMap.put("status", sta);
                        updateHashMap.put("location", loc);
                        updateHashMap.put("gender", gender);
                        updateHashMap.put("age", age);
                        updateHashMap.put("number", num);
                        updateHashMap.put("email", finalEmail);
                        if (count1 > 0) {
                            updateHashMap.put("profession", "Job Holder");
                            updateHashMap.put("institute", com);
                        }
                        if (count > 0) {
                            updateHashMap.put("profession", "Studests");
                            updateHashMap.put("institute", ins);
                        }
                        updateHashMap.put("bio", bio);
                        updateHashMap.put("imageuri", getUri.toString());

                        databaseReference.setValue(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProSetting.this, "Update successfully !!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProSetting.this, MyProfile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ProSetting.this, "Update not successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void Initialize() {
        edit_displayName = findViewById(R.id.edit_display_name);
        image_choose = findViewById(R.id.choose_profile_image);
        edit_status = findViewById(R.id.edit_status);
        edit_location = findViewById(R.id.edit_location);
        update = findViewById(R.id.update_info);
        mUserGender = findViewById(R.id.select_gender);
        edit_number = findViewById(R.id.contact_number);
        //edit_email= findViewById(R.id.contact_email);
        edit_company = findViewById(R.id.your_company);
        edit_institute = findViewById(R.id.your_institute);
        edit_bio = findViewById(R.id.profile_bio);
        selectBirthDate = findViewById(R.id.select_birtd_date);
        selectImageFromTextview = findViewById(R.id.select_image_form_textView);
    }

    @Override
    public void onBackPressed() {
    }
}
