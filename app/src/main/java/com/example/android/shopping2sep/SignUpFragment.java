package com.example.android.shopping2sep;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping2sep.library.Common;
import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements VolleyApiListener {
    final static String TAG = SignUpFragment.class.getSimpleName();
    LoadingDialog loadingDialog;

    View parent;
    TextView header, cng_frag;
    private EditText input_con_password, input_email_id, input_password;
    AppCompatButton signup_btn;


    private String fn_signup = "user_sign_up.php", ty_signup = "sign_up";
    private TextInputLayout txt_email_id, txt_password, txt_con_password;
    private boolean valid_input = false;

    public SignUpFragment() {
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
        // Inflate the layout for this fragment
        this.parent = container.getRootView();
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        header = (TextView)  parent.findViewById(R.id.sign_header);
        header.setText(getResources().getString(R.string.sign_up));

        TextView logToReg = (TextView) parent.findViewById(R.id.log_to_reg_tv);
        logToReg.setVisibility(View.INVISIBLE);

        cng_frag= (TextView) parent.findViewById(R.id.reg_to_log_tv);
        cng_frag.setVisibility(View.VISIBLE);
        cng_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingDialog.show();
                cng_frag.setVisibility(View.GONE);
                cng_to_sign_in();
            }
        });

        initView(view);
        return view;
    }

    private void initView(View view) {
        txt_email_id = (TextInputLayout) view.findViewById( R.id.txt_email_id );
        txt_password = (TextInputLayout) view.findViewById( R.id.txt_password );
        txt_con_password = (TextInputLayout) view.findViewById( R.id.txt_con_password );

        input_email_id = (EditText) view.findViewById( R.id.input_email_id );
        input_email_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus&&(input_email_id.getText().toString().trim() == null ||
                        input_email_id.getText().toString().trim().length() == 0) ){
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
                if (!hasFocus&& (input_password.getText().toString().trim() == null || input_password.getText().toString().trim().length() == 0) ){
                    txt_password.setError("Please enter Password");
                    valid_input = false;
                } else {
                    txt_password.setError(null);
                    valid_input = true;
                }
            }
        });
        input_con_password = (EditText) view.findViewById( R.id.input_con_password );
        input_con_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!hasFocus&& (input_con_password.getText().toString().trim() == null || input_con_password.getText().toString().trim().length() == 0) ){
                        txt_con_password.setError( "Please enter confirm password" );
                        valid_input = false;
                    }
                    else if (!hasFocus&&(!(input_password.getText().toString().trim().equals( input_con_password.getText().toString().trim() )))) {
                        txt_con_password.setError( "Confirm password does not matched...." );
                        valid_input = false;
                    } else {
                        txt_con_password.setError( null );
                        valid_input = true;
                    }
                }
            }
        });

        signup_btn = (AppCompatButton)  view.findViewById( R.id.signup_btn );
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid_input){
                    signUpRequest();
                }
                else{
                    Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        checkBox_news_letter = (CheckBox) findViewById( R.id.checkBox_news_letter );
//        checkbox_terms_condition = (CheckBox) findViewById( R.id.checkbox_terms_condition );
    }


    private void signUpRequest() {
        Map<String, String> params = new HashMap<>();

        params.put("email", input_email_id.getText().toString());
        params.put("password", input_password.getText().toString());
        params.put("action",ty_signup );
        new VolleyApiRequest(getActivity(), this, ty_signup, fn_signup, params);
    }

    private void cng_to_sign_in(){
        Fragment fragment;
        fragment = new SignInFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }

    @Override
    public void onSuccessJson(String response, String type) {
        loadingDialog.hide();
//        if(type.equalsIgnoreCase(ty_signup)){
            // sign up api result
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
//        }
    }

    private void cng_to_main_act(){
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        Log.e( TAG, responseMessage);
        loadingDialog.hide();
    }
}