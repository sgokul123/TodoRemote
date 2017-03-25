package com.todo.todo.registration.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.login.presenter.LoginLoginPresenter;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.presenter.RegistrationPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Fragment implements View.OnClickListener,RegistrationInterface, View.OnFocusChangeListener {
    private  String TAG ="Registration";
    AppCompatEditText mEditTextName,mEditTextEmail,mEditTextPassword2,mEditTextMobile,mEditTextPassword;
    TextInputLayout mTextlayoutname,mTextlayoutemail,mTextlayoutpass1,mTextlayoutmobil,mTextlayoutpasss2;
    AppCompatButton mButtonRegistration;
    ProgressDialog mProgressDialog;
    private Pattern mPattern;
    private Matcher mMatcher;
    RegistrationPresenter registrationPresenter;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static Registration newInstance(String param1, String param2) {
        Registration fragment = new Registration();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_registration,container,false);
        // Inflate the layout for this fragment

        mButtonRegistration=(AppCompatButton) view.findViewById(R.id.button_registration_register);
        mEditTextEmail=(AppCompatEditText) view.findViewById(R.id.edittext_registration_email);
        mEditTextMobile=(AppCompatEditText) view.findViewById(R.id.edittext_registration_phone);
        mEditTextName=(AppCompatEditText) view.findViewById(R.id.edittext_registration_name);
        mEditTextPassword2=(AppCompatEditText) view.findViewById(R.id.edittext_registration_re_password);
        mEditTextPassword=(AppCompatEditText) view.findViewById(R.id.edittext_registration_password);

        mTextlayoutname=(TextInputLayout)view.findViewById(R.id.input_layout_register_name);
        mTextlayoutemail=(TextInputLayout)view.findViewById(R.id.input_layout_register_email);
        mTextlayoutpass1=(TextInputLayout)view.findViewById(R.id.input_layout_register_password);
        mTextlayoutmobil=(TextInputLayout)view.findViewById(R.id.input_layout_register_phone);
        mTextlayoutpasss2=(TextInputLayout)view.findViewById(R.id.input_layout_password);

       mButtonRegistration.setOnClickListener(this);
        mEditTextEmail.setOnFocusChangeListener(this);
        mEditTextMobile.setOnFocusChangeListener(this);
        mEditTextName.setOnFocusChangeListener(this);
        mEditTextPassword2.setOnFocusChangeListener(this);
        mEditTextPassword.setOnFocusChangeListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        RegistrationModel model=new RegistrationModel();
        model.setmName(mEditTextName.getText().toString());
        model.setmEmail(mEditTextEmail.getText().toString());
        model.setmMobile(mEditTextMobile.getText().toString());
        model.setmPassword(mEditTextPassword.getText().toString());

        Log.i(TAG, "onClick: "+model.getmEmail());

    //    registrationPresenter=new RegistrationPresenter(getActivity(),Registration.this);
       // registrationPresenter.setNewUser(model);
       if(validateAll()){
           Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
       }
       else
       {
           Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
       }

    }

    @Override
    public void showProgressDialog() {

        mProgressDialog.setMessage("Please Wait while loading data");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
            mProgressDialog.dismiss();
    }

    @Override
    public void getResponce(boolean flag) {

        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
    }



        public boolean validateAll(){

        boolean mobil=false,name=false,pass=false,email=false;

                //mobile
                if(mEditTextMobile.getText().toString().length()==10){
                    //  Log.i(TAG, "isValid: ");
                    mobil=true;
                    Log.i(TAG, "validateAll: mobil");
                }
            //Mail validation
                mPattern = Pattern.compile(EMAIL_PATTERN);
                mMatcher = mPattern.matcher(mEditTextEmail.getText().toString());
                 if(mMatcher.matches()){
                     email=true;
                     Log.i(TAG, "validateAll:email ");
                }
            //Name validation

                if(!mEditTextName.getText().toString().equals("")){
                    name=true;
                    Log.i(TAG, "validateAll: name");
                    //  mEditTextName.setSelection(mEditTextName.getText().length());
                }
            //Password validation

             if((mEditTextPassword.getText().toString().length()>=5)&&(!(mEditTextPassword.getText().toString().equals("")))){

                   if(mEditTextPassword.getText().toString().equals(mEditTextPassword2.getText().toString()))
                   {
                       Log.i(TAG, "validateAll:pass ");
                       pass=true;
                   }
                }


            return  mobil&&name&&pass&&email;
        }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppCompatEditText editText=new AppCompatEditText(getActivity());
        switch (v.getId()){

            case R.id.edittext_registration_email:
                editText=mEditTextEmail;
                break;
            case R.id.edittext_registration_name:
                editText=mEditTextName;
                break;
            case R.id.edittext_registration_password:
                editText=mEditTextPassword;
                break;
            case R.id.edittext_registration_phone:
                editText=mEditTextMobile;
                break;
            case R.id.edittext_registration_re_password:
                editText=mEditTextPassword2;

                break;
            default:

                break;

        }
        if(hasFocus){
            //Toast.makeText(getActivity(), "got the focus", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(), "lost the focus", Toast.LENGTH_LONG).show();
            isValid (editText);
        }
    }


    public  void isValid(AppCompatEditText editText){

        //Mobile validation
        if(editText==mEditTextMobile){

            if(mEditTextMobile.getText().toString().length()==10){
                mTextlayoutmobil.setError(null);
              //  Log.i(TAG, "isValid: ");
            }
            else if(mEditTextMobile.getText().toString().equals("")) {

              //  mTextlayoutmobil.setErrorEnabled(true);
                mTextlayoutmobil.setError("Mobile Number Should not blank");
               // mEditTextMobile.setError("Mobile Number Should not blank");
                }
                else{
               // mTextlayoutmobil.setErrorEnabled(true);
                mTextlayoutmobil.setError("Mobile Number Must be 10 Digit");
                   /// mEditTextMobile.setError("Mobile Number Must be 10 Digit");
             //   mEditTextMobile.setSelection(mEditTextMobile.getText().length());
                }
        }
        //Mail validation
        else if(editText==mEditTextEmail){
            mPattern = Pattern.compile(EMAIL_PATTERN);
            mMatcher = mPattern.matcher(mEditTextEmail.getText().toString());
            if(mEditTextEmail.getText().toString().equals("")){

             //   mTextlayoutemail.setErrorEnabled(true);
                mTextlayoutemail.setError("Email should not be blank");
                //mEditTextEmail.setError("Email should not be blank");
                Log.i(TAG, "isValid: ");

            }else if(!mMatcher.matches()){
          //      mTextlayoutemail.setErrorEnabled(true);
                mTextlayoutemail.setError("Email is Not Valid");
               // mEditTextEmail.setError("Email is Not Valid");
             //   mEditTextEmail.setSelection(mEditTextEmail.getText().length());
            }else {
                mTextlayoutemail.setError(null);
            }

        }
        //Name validation
        else if(editText==mEditTextName){
            if(mEditTextName.getText().toString().equals("")){
            //    mTextlayoutname.setErrorEnabled(true);
                mTextlayoutname.setError("Mobile Number Must be 10 Digit");
               // mEditTextName.setError("Name should not blank");
              //  mEditTextName.setSelection(mEditTextName.getText().length());
            }
            else {
                mTextlayoutname.setError(null);
            }

        }
        //Password validation
        else if(editText==mEditTextPassword){
            if(mEditTextPassword.getText().toString().equals("")){
             //   mTextlayoutpass1.setErrorEnabled(true);
            //    mTextlayoutpass1.setError("should not blank");
                mEditTextPassword.setError("should not blank");
           //     mEditTextPassword.setSelection(mEditTextPassword.getText().length());
            }else if(mEditTextPassword.getText().toString().length()<=5){
              //  mTextlayoutpass1.setErrorEnabled(true);
               // mTextlayoutpass1.setError("should not be less then 6 laters");
                mEditTextPassword.setError("should not be less then 6 laters");
            }else {
                //mTextlayoutpass1.setError(null);
            }
        }
       //Re-entered Pasword
        else if(editText==mEditTextPassword2){
            if(mEditTextPassword.getText().toString().equals("")){
             //   mTextlayoutpasss2.setErrorEnabled(true);
              ///  mTextlayoutpasss2.setError("should not blank");
               mEditTextPassword.setError("should not blank");
             //   mEditTextPassword.setSelection(mEditTextPassword.getText().length());
            }
           else if(mEditTextPassword.getText().toString().equals(mEditTextPassword2.getText().toString())){
                //mTextlayoutpasss2.setError(null);
                Log.i(TAG, "isValid: ");
            }else{
               // mTextlayoutpasss2.setErrorEnabled(true);
              //  mTextlayoutpasss2.setError("Re-Entered Password Not Matched");
                mEditTextPassword2.setError("Re-Entered Password Not Matched");
               // mEditTextPassword2.setSelection(mEditTextPassword2.getText().length());
            }

        }

    }

}
