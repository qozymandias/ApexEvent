package com.eventblock.eventblock;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    ImageView imageToUpload;
    Uri filePath;

    Bitmap bitmap;

    private EditText etUserName;
    private EditText etAge;
    private EditText etName;
    private EditText etPassword;
    private EditText etEmail;
    private Button bRegister;
    private EditText etPasswordConfirm;

    CallbackManager callbackManager;

    private static final String UPLOAD_URL = "http://eventblock.xyz/Upload/upload.php";

    private boolean verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        requestStoragePermission();

        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPassword2);

        etEmail = (EditText) findViewById(R.id.etEmail);

        bRegister = (Button) findViewById(R.id.bRegister);

        verify = false;


        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();

            }
        });

        LoginButton fbRegister = (LoginButton) findViewById(R.id.bRegister2);



        fbRegister.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        fbRegister.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            this.stopTracking();
                            Profile.setCurrentProfile(currentProfile);

                        }
                    };
                    mProfileTracker.startTracking();

                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());

                }


                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    final String email = object.getString("email");
                                    final String birthday = object.getString("birthday"); // 01/31/1980 format
                                    final String name = object.getString("name");
                                    final String id = object.getString("id");

                                    final String fName = object.getString("first_name");
                                    final String lName = object.getString("last_name");


                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);

                                                boolean success = jsonResponse.getBoolean("success");

                                                if(success) {

                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                                    intent.putExtra("name", name);
                                                    intent.putExtra("username", fName+"."+lName+id);
                                                    intent.putExtra("age", 20);
                                                    intent.putExtra("email", email);

                                                    RegisterActivity.this.startActivity(intent);
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                    builder.setMessage("Register failed")
                                                            .setNegativeButton("Retry", null)
                                                            .create()
                                                            .show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };


                                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                                    FBRegisterRequest registerRequest = new FBRegisterRequest(name,
                                            fName+"."+lName+id,20, id + "" ,
                                            email, id + "",  responseListener);
                                    registerRequest.setShouldCache(false);
                                    queue.add(registerRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                /*Bundle b = request.getParameters();

                ArrayList<String> paras = b.getStringArrayList("fields");

                Toast.makeText(getApplicationContext(), "Email: " + paras.get(2)
                        + "\n" + "Birthday: " + paras.get(4)
                        + "\n" + "Name: " + paras.get(1), Toast.LENGTH_LONG).show();*/


            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // variables
                final String name = etName.getText().toString();
                final String username = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                final String passwordConfirm = etPasswordConfirm.getText().toString();
                final int age = Integer.parseInt(etAge.getText().toString());
                final String email = etEmail.getText().toString();

                // picture upload
                /*Toast.makeText(getApplicationContext(), "Uploading image", Toast.LENGTH_SHORT)
                        .show();
                Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();

                (new UploadPicture.UploadImage(image, username) ).execute();
                */

                if (!validateEmail(email, username, password)) {
                    etEmail.setError("Invalid Email");
                    etEmail.requestFocus();
                } else if (!validatePassword(password)) {
                    etPassword.setError("Invalid Password must be greater than 9");
                    etPassword.requestFocus();
                } else if (!password.equals(passwordConfirm)) {
                    etPasswordConfirm.setError("Password does not match");
                    etPasswordConfirm.requestFocus();
                } else {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                boolean success = jsonResponse.getBoolean("success");

                                if(success) {

                                    if(imageToUpload != null) uploadImage();

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                    RegisterRequest registerRequest = new RegisterRequest(name,username,age,password, email, responseListener);
                    registerRequest.setShouldCache(false);
                    queue.add(registerRequest);

                }



            }


        });

    }


    //Return true if password is valid and false if password is invalid
    protected boolean validatePassword(String password) {
        if(password!=null && password.length()>9) {
            return true;
        } else {
            return false;
        }
    }

    //Return true if email is valid and false if email is invalid
    protected boolean validateEmail(String email, String username, String password) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);


        if(matcher.matches() ) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        boolean success = jsonResponse.getBoolean("success");

                        if(success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Email sent! Please check emails to verify")
                                    .setNegativeButton("Continue", null)
                                    .create()
                                    .show();

                            verify = true;
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Register failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                            verify = false;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };


            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

            EmailRequest emailRequest = new EmailRequest(email,username, password, responseListener);
            emailRequest.setShouldCache(false);
            queue.add(emailRequest);


            return true;
        }

        return false;
    }

    private void requestStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT)
                    .show();
            } else {
                Toast.makeText(this,"Permission not granted", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageToUpload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

    }


    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        cursor.close();

        return path;
    }


    private void uploadImage() {
        try {
            String name = etUserName.getText().toString().trim();
            String path = getPath(filePath);

            try {
                String uploadID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, uploadID, UPLOAD_URL)
                        .addFileToUpload(path, "image")
                        .addParameter("name", name)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();

                Toast.makeText(getApplicationContext(), "Uploaded image", Toast.LENGTH_SHORT)
                        .show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
