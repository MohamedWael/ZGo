package com.blogspot.mowael.zgo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.fragments.SignInFragment;
import com.blogspot.mowael.zgo.utilities.Constants;
import com.blogspot.mowael.zgo.utilities.VolleySingleton;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivProfilePic;
    private EditText etUserName, etPassword, etMail;
    private String userName, password, email;
    private Button btnSignIn, btnRegister;
    private SignInFragment signInFragment;
    private SharedPreferences.Editor editor;
    private Intent mapActivityIntent;
    private TextView tvNotifier;
    private VolleySingleton volley;
    private final int PICTURE_TAKEN_FROM_GALLERY = 1;
    private Bitmap accountPhoto;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initComponents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_TAKEN_FROM_GALLERY) {
                Log.d("requestCode", requestCode + "");
                imageUri = data.getData();
                accountPhoto = volley.handleResultFromChooser(data);

                ivProfilePic.setImageBitmap(accountPhoto);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                finish();
                break;
            case R.id.btnRegister:
                userName = etUserName.getText().toString().trim();
                email = etMail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (!userName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (email.matches(Constants.REGEX_Mail)) {
                        if (userName.length() < 6 || password.length() < 6) {
                            tvNotifier.setTextColor(Color.RED);
                            tvNotifier.setError("");
                            tvNotifier.setText("User name and password must be > 6 characters");

                        } else {
                            storeRegisterInfo(userName, email, password, imageUri != null ? imageUri.toString() : "");
                            signInFragment.setRegisterListener(new SignInFragment.OnRegisterListener() {
                                @Override
                                public void onRegister(FragmentActivity activity) {
                                    activity.finish();
                                }
                            });
                            startActivity(mapActivityIntent);
                            finish();
                        }

                    } else {
                        etMail.setError("you have to enter a valid email address");
                    }
                } else {
                    if (userName.isEmpty()) {
                        etUserName.setError("you must enter your username");
                    } else if (email.isEmpty()) {
                        etMail.setError("you must enter your email");
                    } else if (password.isEmpty()) {
                        etPassword.setError("you must enter your password");
                    }
                }
                break;
            case R.id.ivProfilePic:
                startActivityForResult(volley.getPictureFromGalleryIntent(), PICTURE_TAKEN_FROM_GALLERY);
                break;
        }
    }

    private void storeRegisterInfo(String userName, String email, String password, String imageUri) {
        editor.putString(Constants.USER_NAME, userName);
        editor.putString(Constants.EMAIL, email);
        editor.putString(Constants.PASSWORD, password);
        editor.putString(Constants.IMAGE_URI, imageUri);
        editor.putBoolean(Constants.LOGIN_AUTOMATICALLY, false);
        editor.commit();
    }

    private void initComponents() {
        editor = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etMail = (EditText) findViewById(R.id.etMail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvNotifier = (TextView) findViewById(R.id.tvNotifier);
        signInFragment = SignInFragment.newInstance("", "");
        mapActivityIntent = new Intent(this, MapActivity.class);
        volley = VolleySingleton.getInstance(this);
        btnRegister.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);
    }
}
