package com.example.android.shopping2sep;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping2sep.library.Common;
import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;
//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends Fragment implements VolleyApiListener {
    final static String TAG = SignInFragment.class.getSimpleName();
    LoadingDialog loadingDialog;

    EditText input_email_id, input_password;
    TextInputLayout txt_email_id, txt_password;
    private boolean valid_input = false;
    TextView forgot_pass;
    AppCompatButton sign_in_btn;
//    LoginButton fb_log_in;
    CheckBox checkBox;

    boolean isCheckCheckBox=false;
    boolean allow_resend =false;

//    private CallbackManager callbackManager;

    private String fn_fgpass = "user_forgot_password.php", ty_forpass_req = "forgot_pass_req", ty_forpass_chk = "forgot_pass_chk";
    private String fn_signin = "user_sign_in.php", ty_signin = "sign_in";


    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = container.getRootView();
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        TextView header = (TextView) parent.findViewById(R.id.sign_header);
        header.setText(getResources().getString(R.string.sign_in));

        TextView regToLog = (TextView) parent.findViewById(R.id.reg_to_log_tv);
        regToLog.setVisibility(View.INVISIBLE);
        TextView cng_frag  = (TextView) parent.findViewById(R.id.log_to_reg_tv);
        cng_frag.setVisibility(View.VISIBLE);
        cng_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingDialog.show();
                cng_to_sign_up();
            }
        });

        init_view(view);
        return view;
    }

    private void init_view(View view) {
        if(Prefs.getInstance().is_logged_in){
            cng_to_main_act();
        }

        txt_email_id = (TextInputLayout) view.findViewById(R.id.txt_email_id);
        txt_password = (TextInputLayout) view.findViewById(R.id.txt_password);
        input_email_id = (EditText) view.findViewById( R.id.input_email_id );
        input_email_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus&&(input_email_id.getText().toString().trim() == null || input_email_id.getText().toString().trim().length() == 0)) {
                    txt_email_id.setError( "Please enter email id" );
                    valid_input = false;
                }
                else if(!hasFocus && !new Common().checkEmail(input_email_id.getText().toString().trim())){
                    txt_email_id.setError("Please enter valid email id");
                    valid_input = false;
                }
                else {
                    txt_email_id.setError( null );
                    valid_input = true;
                }
            }
        });
        input_password = (EditText) view.findViewById( R.id.input_password );
        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && (input_password.getText().toString().trim() == null || input_password.getText().toString().trim().length() == 0) ){
                    txt_password.setError("Please enter Password");
                    valid_input = false;
                } else {
                    txt_password.setError(null);
                    valid_input = true;
                }
            }
        });



        forgot_pass = (TextView) view.findViewById(R.id.forgot_pass);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email_id.getText().toString().trim();
                if(!email.isEmpty()){
                    txt_email_id.setError( "Please enter your registered email id first" );
                }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage("Do you want to login via Email OTP?");
                    AlertDialog alertDialog = builder.create();
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           sendForgotRequest(email);
                           alertDialog.hide();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.hide();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isCheckCheckBox=true;
                }else{
                    isCheckCheckBox=false;
                }
            }
        });

        sign_in_btn = (AppCompatButton) view.findViewById(R.id.signin_btn);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
             
            public void onClick(View v) {
                if(valid_input){
                    signInRequest();
                }
                else{
                    Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
//        fb_log_in = (LoginButton) view.findViewById(R.id.fb_login_btn);
//        fb_log_in.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
//                Log.d("registerCallback", loggedIn + " ??");
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
        
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        getUserProfile(AccessToken.getCurrentAccessToken());
//    }
//
//    private void getUserProfile(AccessToken currentAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(
//                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.d("TAG", object.toString());
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
//                            String id = object.getString("id");
//
//                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//
//
//                            signInWithFacebookRequest(id,email,first_name,last_name);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }

//    private void signInWithFacebookRequest(final String Id, final String Email, final String firstName, final String lastName) {
//        loadingDialog.show();
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String response = null;
//        final String finalResponse = response;
//        StringRequest postRequest = new StringRequest(Request.Method.POST, "user/social_login_register",
//                new com.android.volley.Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        loadingDialog.hide();
//                        try {
//
//                            JSONObject obj = new JSONObject(response);
//
//                            if (obj.getString("success").equals("false")){
//
////                                Toast(SignInFragment.this, ""+obj.getString("error"));
//
//                            } else {
//                                String user_id = obj.getString("user_id");
//                                String first_name = obj.getString("first_name");
//                                String last_name = obj.getString("last_name");
//                                String user_email = obj.getString("user_email");
//                                String mobile_no = obj.getString("mobile_number");
//                                String user_type = obj.getString("user_type");
//                                String gender = obj.getString("gender");
//                                String currency_code = obj.getString("currency_code");
//
//                                String address = obj.getString("address");
//                                String zipcode = obj.getString("zipcode");
//                                String city = obj.getString("city");
//                                String country = obj.getString("country");
//                                String city_id = obj.getString("city_id");
//                                String country_id = obj.getString("country_id");
//                                String password = obj.getString("password");
//
//
////                                Prefs.getInstance().user_id = user_id;
////                                Prefs.getInstance().first_name = first_name;
////                                Prefs.getInstance().last_name = last_name;
////                                Prefs.getInstance().emailId = user_email;
////                                Prefs.getInstance().mobile_no = mobile_no;
////                                Prefs.getInstance().password=password;
////                                Prefs.getInstance().loginBy="Facebook";
////
////                                Prefs.getInstance().savePrefs(Login.this);
////
////                                if(callFrom.equalsIgnoreCase("cart")){
////                                    Intent i = new Intent(Login.this, OurCartActivity.class);
////                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                    startActivity(i);
////                                    finish();
////                                }else {
////                                    Intent i = new Intent(Login.this, MainActivity.class);
////                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                                    startActivity(i);
////                                    finish();
////                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                params.put("login_type", "Facebook");
//                params.put("id", Id);
//                params.put("first_name", firstName);
//                params.put("last_name", lastName);
//                params.put("email", Email);
////                params.put("app_id", Common.getSecureID(Login.this));
////                params.put("device_id", Common.getSecureID(Login.this));
//                params.put("app_type", "android");
////                params.put("wl_keys", Common.current_secure_key);
////                params.put("site_language_id", APPLanguage);
//
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
//
//
//    }


    private void signInRequest() {
        Map<String, String> params = new HashMap<>();

        params.put("email", input_email_id.getText().toString());
        params.put("password", input_password.getText().toString());
        params.put("action",ty_signin );
        new VolleyApiRequest(getActivity(), this, ty_signin, fn_signin, params);
    }

    private void sendForgotRequest(final String email) {
        Log.d(TAG, "sendForgotRequest()");
        loadingDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("action", ty_forpass_req);
        new VolleyApiRequest(getActivity(), SignInFragment.this, ty_forpass_req,
                fn_fgpass, params);
    }

    private void cng_to_sign_up(){
        Fragment fragment;
        fragment = new SignUpFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }

    private void cng_to_main_act(){
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onSuccessJson(String response, String type) {
        loadingDialog.hide();
        Log.d(TAG, response);
        if(type.equalsIgnoreCase(ty_signin)){
            // sign in api result
            try {

                JSONObject obj = new JSONObject(response);

                if (obj.getString("status").equals("success")){

                    String user_id = obj.getString("user_id");
                    String user_email = obj.getString("email");
                    int cart_count = obj.getInt("cart_count");

                    Prefs.getInstance().user_id = user_id;
                    Prefs.getInstance().emailId = user_email;
                    Prefs.getInstance().password= input_password.getText().toString().trim();
                    Prefs.getInstance().cart_count = cart_count;
                    Prefs.getInstance().is_logged_in = true;
                    Prefs.getInstance().is_remember_me = isCheckCheckBox;
//                    Prefs.getInstance().loginBy="Normal";

                    Prefs.getInstance().savePrefs(getContext());
                    cng_to_main_act();
                }
                else{
                    String error = obj.getString("error_msg");
                    Log.d(TAG, error);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(ty_forpass_req)){
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equals("success")){
                    String otp = obj.getString("otp");
                    getOTP(otp);
                }
                else{
                    String error = obj.getString("error_msg");
                    Log.d(TAG, error);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{

        }
    }

    private void getOTP(String otp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.alert_get_otp, null);
        builder.setView(view);

        EditText otp_et = (EditText) view.findViewById(R.id.otp_et);
        AppCompatButton submit = (AppCompatButton) view.findViewById(R.id.submit_otp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_et.getText().toString().trim().length()!=4){
                    otp_et.setError("Invalid OTP");
                }
                else{
                    if (otp.contentEquals(otp_et.getText().toString().trim()) ){
                        alertDialog.hide();
                        cng_to_main_act();
                    }
                    else{
                        Toast.makeText(getContext(), "OTP entered is incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        TextView resend = (TextView) view.findViewById(R.id.resend_otp_tv);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allow_resend=true){
                    alertDialog.hide();
                    loadingDialog.show();
                    sendForgotRequest(input_email_id.getText().toString());
                }
            }
        });
        TextView timer = (TextView) view.findViewById(R.id.resend_otp_timer);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(( millisUntilFinished / 1000)+"s");
            }

            public void onFinish() {
                timer.setVisibility(View.INVISIBLE);
                resend.setTextColor(getResources().getColor(R.color.black));
                allow_resend = true;
            }
        }.start();
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        Log.e( TAG, responseMessage);
        loadingDialog.hide();
    }

}