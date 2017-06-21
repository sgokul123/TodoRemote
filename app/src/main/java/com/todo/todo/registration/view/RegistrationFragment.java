package com.todo.todo.registration.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.presenter.RegistrationPresenter;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment implements View.OnClickListener, RegistrationInterface, View.OnFocusChangeListener {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    AppCompatEditText mEditTextName, mEditTextLName, mEditTextEmail, mEditTextPassword2, mEditTextMobile, mEditTextPassword;
    TextInputLayout mTextlayoutname, mTextlayoutlname, mTextlayoutemail, mTextlayoutpass1, mTextlayoutmobil, mTextlayoutpasss2;
    AppCompatButton mButtonRegistration;
    ProgressUtil mProgressUtil;
    RegistrationPresenter registrationPresenter;
    private String TAG = "RegistrationFragment";
    private Pattern mPattern;
    private Matcher mMatcher;
    private SharedPreferences.Editor mSharedPref_editor;
    private SharedPreferences pref;

    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        // Inflate the layout for this fragment

        mButtonRegistration = (AppCompatButton) view.findViewById(R.id.button_registration_register);
        mEditTextEmail = (AppCompatEditText) view.findViewById(R.id.edittext_registration_email);
        mEditTextMobile = (AppCompatEditText) view.findViewById(R.id.edittext_registration_phone);
        mEditTextName = (AppCompatEditText) view.findViewById(R.id.edittext_registration_fname);
        mEditTextLName = (AppCompatEditText) view.findViewById(R.id.edittext_registration_lname);
        mEditTextPassword2 = (AppCompatEditText) view.findViewById(R.id.edittext_registration_re_password);
        mEditTextPassword = (AppCompatEditText) view.findViewById(R.id.edittext_registration_password);

        mTextlayoutlname = (TextInputLayout) view.findViewById(R.id.input_layout_register_lname);
        mTextlayoutname = (TextInputLayout) view.findViewById(R.id.input_layout_register_fname);
        mTextlayoutemail = (TextInputLayout) view.findViewById(R.id.input_layout_register_email);
        mTextlayoutpass1 = (TextInputLayout) view.findViewById(R.id.input_layout_register_password);
        mTextlayoutmobil = (TextInputLayout) view.findViewById(R.id.input_layout_register_phone);
        mTextlayoutpasss2 = (TextInputLayout) view.findViewById(R.id.input_layout_password);


        pref = getActivity().getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mSharedPref_editor = pref.edit();
        mButtonRegistration.setOnClickListener(this);
        mProgressUtil = new ProgressUtil(getActivity());

        mEditTextEmail.setOnFocusChangeListener(this);
        mEditTextMobile.setOnFocusChangeListener(this);
        mEditTextName.setOnFocusChangeListener(this);
        mEditTextLName.setOnFocusChangeListener(this);
        mEditTextPassword2.setOnFocusChangeListener(this);
        mEditTextPassword.setOnFocusChangeListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        RegistrationModel model = new RegistrationModel();
        model.setUserLastName(mEditTextLName.getText().toString());
        model.setUserFirstName(mEditTextName.getText().toString());
        model.setMailid(mEditTextEmail.getText().toString());
        model.setMobileNo(mEditTextMobile.getText().toString());
        model.setUserPassword(mEditTextPassword.getText().toString());
        model.setUserProfileImgurl(mEditTextName.getText().toString());//giv url

        Log.i(TAG, "onClick: " + model.getMailid());

        if (validateAll()) {
            registrationPresenter = new RegistrationPresenter(getActivity(), this);
            registrationPresenter.setNewUser(model);
        } else {
            Toast.makeText(getActivity(), getString(R.string.registration_fail), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getFailuar() {
        Toast.makeText(getActivity(), getString(R.string.registration_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {

        mProgressUtil.showProgress("Please Wait while loading data");
    }

    @Override
    public void closeProgressDialog() {
        mProgressUtil.dismissProgress();
    }

    @Override
    public void getResponce(String uid, RegistrationModel model) {
        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, getString(R.string.flag_true));
        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL, model.getMailid());
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, uid);
        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, model.getUserFirstName());
        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME, model.getUserLastName());
        mSharedPref_editor.putString(Constants.ProfileeKey.MOBILE_NO, model.getMobileNo());
        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL, model.getUserProfileImgurl());
        mSharedPref_editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_true));
        mSharedPref_editor.commit();
        ///  show registration page again
        Intent intent = new Intent(getActivity(), ToDoActivity.class);
        startActivity(intent);
        getActivity().finish();

      /*  Intent intent=new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();*/
        // Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();

        mEditTextEmail.setText("");
        mEditTextLName.setText("");
        mEditTextMobile.setText("");
        mEditTextLName.setText("");
        mEditTextPassword.setText("");
        mEditTextPassword2.setText("");
    }

    @Override
    public void getLocalResponce(RegistrationModel model) {
        Connection con =new Connection(getActivity());
        mSharedPref_editor.putString(Constants.BundleKey.USER_REGISTER, getString(R.string.flag_true));
        mSharedPref_editor.putString(Constants.BundleKey.USER_EMAIL, model.getMailid());
        mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.LOCAL_REGISTER);
        mSharedPref_editor.putString(Constants.ProfileeKey.FIRST_NAME, model.getUserFirstName());
        mSharedPref_editor.putString(Constants.ProfileeKey.LAST_NAME, model.getUserLastName());
        mSharedPref_editor.putString(Constants.ProfileeKey.MOBILE_NO, model.getMobileNo());
        mSharedPref_editor.putString(Constants.ProfileeKey.PROFILE_IMAGE_URL, model.getUserProfileImgurl());
        mSharedPref_editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_true));
        if(!con.isNetworkConnected()){
            mSharedPref_editor.putString(Constants.BundleKey.USER_USER_UID_LOCAL, model.getMailid().substring(0,model.getMailid().indexOf("@")));
            mSharedPref_editor.putBoolean(Constants.BundleKey.USER__LOCAL_REGISTER, true);
        }
        mSharedPref_editor.commit();
        Intent intent = new Intent(getActivity(),ToDoActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public boolean validateAll() {
        boolean mobil = false, name = false, lname = false, pass = false, email = false;
        if (mEditTextMobile.getText().toString().length() == 10) {
            mobil = true;
            Log.i(TAG, "validateAll: mobil");
        }

        mPattern = Pattern.compile(EMAIL_PATTERN);
        mMatcher = mPattern.matcher(mEditTextEmail.getText().toString());
        if (mMatcher.matches()) {
            email = true;
            Log.i(TAG, "validateAll:email ");
        }

        if (!mEditTextName.getText().toString().equals("")) {
            name = true;
            Log.i(TAG, "validateAll: name");
        }

        if (!mEditTextLName.getText().toString().equals("")) {
            lname = true;
            Log.i(TAG, "validateAll: name");
        }
        //Password validation

        if ((mEditTextPassword.getText().toString().length() >= 5) && (!(mEditTextPassword.getText().toString().equals("")))) {
            if (mEditTextPassword.getText().toString().equals(mEditTextPassword2.getText().toString())) {
                Log.i(TAG, "validateAll:pass ");
                pass = true;
            }
        }


        return mobil && name && pass && email && lname;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppCompatEditText editText = new AppCompatEditText(getActivity());
        switch (v.getId()) {

            case R.id.edittext_registration_email:
                editText = mEditTextEmail;
                break;
            case R.id.edittext_registration_fname:
                editText = mEditTextName;
                break;
            case R.id.edittext_registration_lname:
                editText = mEditTextLName;
                break;
            case R.id.edittext_registration_password:
                editText = mEditTextPassword;
                break;
            case R.id.edittext_registration_phone:
                editText = mEditTextMobile;
                break;
            case R.id.edittext_registration_re_password:
                editText = mEditTextPassword2;

                break;
            default:

                break;

        }
        if (hasFocus) {
        } else {
            // Toast.makeText(getActivity(), "lost the focus", Toast.LENGTH_LONG).show();
            isValid(editText);
        }
    }


    public void isValid(AppCompatEditText editText) {

        //Mobile validation
        if (editText == mEditTextMobile) {

            if (mEditTextMobile.getText().toString().length() == 10) {
                mEditTextMobile.setError(null);
            } else if (mEditTextMobile.getText().toString().equals("")) {


                mEditTextMobile.setError("Mobile Number Should not blank");
            } else {
                mEditTextMobile.setError("Mobile Number Must be 10 Digit");
            }
        }
        //Mail validation
        else if (editText == mEditTextEmail) {
            mPattern = Pattern.compile(EMAIL_PATTERN);
            mMatcher = mPattern.matcher(mEditTextEmail.getText().toString());
            if (mEditTextEmail.getText().toString().equals("")) {


                mEditTextEmail.setError("Email should not be blank");
                Log.i(TAG, "isValid: ");

            } else if (!mMatcher.matches()) {

                mEditTextEmail.setError("Email is Not Valid");

            } else {

            }

        }
        //Name validation
        else if (editText == mEditTextName) {
            if (mEditTextName.getText().toString().equals("")) {

                mEditTextName.setError("Name should not be Blank...");
            } else {
            }

        }
        //Name validation
        else if (editText == mEditTextLName) {
            if (mEditTextLName.getText().toString().equals("")) {
                mEditTextLName.setError("Last Name should not blank");
            } else {
            }

        }
        //Password validation
        else if (editText == mEditTextPassword) {
            if (mEditTextPassword.getText().toString().equals("")) {
                mEditTextPassword.setError("should not blank");
            } else if (mEditTextPassword.getText().toString().length() <= 5) {
                mEditTextPassword.setError("should not be less then 6 laters");
            } else {
            }
        }
        //Re-entered Pasword
        else if (editText == mEditTextPassword2) {
            if (mEditTextPassword.getText().toString().equals("")) {
                mEditTextPassword.setError("should not blank");
            } else if (mEditTextPassword.getText().toString().equals(mEditTextPassword2.getText().toString())) {
                Log.i(TAG, "isValid: ");
            } else {
                mEditTextPassword2.setError("Re-Entered Password Not Matched");
            }

        }

    }

}
