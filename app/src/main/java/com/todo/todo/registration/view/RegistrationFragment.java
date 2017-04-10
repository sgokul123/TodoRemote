package com.todo.todo.registration.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.registration.model.RegistrationModel;
import com.todo.todo.registration.presenter.RegistrationPresenter;
import com.todo.todo.util.ProgressUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment implements View.OnClickListener,RegistrationInterface, View.OnFocusChangeListener {
    private  String TAG ="RegistrationFragment";
    AppCompatEditText mEditTextName,mEditTextLName,mEditTextEmail,mEditTextPassword2,mEditTextMobile,mEditTextPassword;
    TextInputLayout mTextlayoutname,mTextlayoutlname,mTextlayoutemail,mTextlayoutpass1,mTextlayoutmobil,mTextlayoutpasss2;
    AppCompatButton mButtonRegistration;
    ProgressUtil mProgressUtil;
    private Pattern mPattern;
    private Matcher mMatcher;
    RegistrationPresenter registrationPresenter;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        mEditTextName=(AppCompatEditText) view.findViewById(R.id.edittext_registration_fname);
        mEditTextLName=(AppCompatEditText) view.findViewById(R.id.edittext_registration_lname);
        mEditTextPassword2=(AppCompatEditText) view.findViewById(R.id.edittext_registration_re_password);
        mEditTextPassword=(AppCompatEditText) view.findViewById(R.id.edittext_registration_password);

        mTextlayoutlname=(TextInputLayout)view.findViewById(R.id.input_layout_register_lname);
        mTextlayoutname=(TextInputLayout)view.findViewById(R.id.input_layout_register_fname);
        mTextlayoutemail=(TextInputLayout)view.findViewById(R.id.input_layout_register_email);
        mTextlayoutpass1=(TextInputLayout)view.findViewById(R.id.input_layout_register_password);
        mTextlayoutmobil=(TextInputLayout)view.findViewById(R.id.input_layout_register_phone);
        mTextlayoutpasss2=(TextInputLayout)view.findViewById(R.id.input_layout_password);

       mButtonRegistration.setOnClickListener(this);
        mProgressUtil=new ProgressUtil(getActivity());

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

        RegistrationModel model=new RegistrationModel();
        model.setUserLastName(mEditTextLName.getText().toString());
        model.setUserFirstName(mEditTextName.getText().toString());
        model.setMailid(mEditTextEmail.getText().toString());
        model.setMobileNo(mEditTextMobile.getText().toString());
        model.setUserPassword(mEditTextPassword.getText().toString());
        model.setUserProfileImgurl(mEditTextName.getText().toString());//giv url
       // model.setUserLastName();

        Log.i(TAG, "onClick: "+model.getMailid());

    //    registrationPresenter=new RegistrationPresenter(getActivity(),RegistrationFragment.this);
       // registrationPresenter.setNewUser(model);
       if(validateAll()){
           registrationPresenter=new RegistrationPresenter(getActivity(),this);
           registrationPresenter.setNewUser(model);
           Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
       }
       else
       {
           Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
       }

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
    public void getResponce(boolean flag) {
        if(flag){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
        }else {

            mEditTextEmail.setText("");
            mEditTextLName.setText("");
            mEditTextMobile.setText("");
            mEditTextLName.setText("");
            mEditTextPassword.setText("");
            mEditTextPassword2.setText("");
            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
        }

    }
        public boolean validateAll(){

        boolean mobil=false,name=false,lname=false,pass=false,email=false;

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
            //Last Name validation

            if(!mEditTextLName.getText().toString().equals("")){
                lname=true;
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


            return  mobil&&name&&pass&&email&&lname;
        }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppCompatEditText editText=new AppCompatEditText(getActivity());
        switch (v.getId()){

            case R.id.edittext_registration_email:
                editText=mEditTextEmail;
                break;
            case R.id.edittext_registration_fname:
                editText=mEditTextName;
                break;
            case R.id.edittext_registration_lname:
                editText=mEditTextLName;
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
                mEditTextMobile.setError(null);
              //  Log.i(TAG, "isValid: ");mTextlayoutmobil
            }
            else if(mEditTextMobile.getText().toString().equals("")) {

              //  mTextlayoutmobil.setErrorEnabled(true);
                mEditTextMobile.setError("Mobile Number Should not blank");
               // mEditTextMobile.setError("Mobile Number Should not blank");mTextlayoutmobil
                }
                else{
               // mTextlayoutmobil.setErrorEnabled(true);
                mEditTextMobile.setError("Mobile Number Must be 10 Digit");
                   /// mEditTextMobile.setError("Mobile Number Must be 10 Digit");
             //   mEditTextMobile.setSelection(mEditTextMobile.getText().length());
                }
        }
        //Mail validation
        else if(editText==mEditTextEmail){
            mPattern = Pattern.compile(EMAIL_PATTERN);
            mMatcher = mPattern.matcher(mEditTextEmail.getText().toString());
            if(mEditTextEmail.getText().toString().equals("")){

             //   mTextlayoutemail.setErrorEnabled(true);mTextlayoutemail
                mEditTextEmail.setError("Email should not be blank");
                //mEditTextEmail.setError("Email should not be blank");
                Log.i(TAG, "isValid: ");

            }else if(!mMatcher.matches()){
          //      mTextlayoutemail.setErrorEnabled(true);
                mEditTextEmail.setError("Email is Not Valid");
               // mEditTextEmail.setError("Email is Not Valid");
             //   mEditTextEmail.setSelection(mEditTextEmail.getText().length());
            }else {
               // mEditTextEmail.setError(null);
            }

        }
        //Name validation
        else if(editText==mEditTextName){
            if(mEditTextName.getText().toString().equals("")){
            //    mTextlayoutname.setErrorEnabled(true);mTextlayoutname
                mEditTextName.setError("Mobile Number Must be 10 Digit");
               // mEditTextName.setError("Name should not blank");
              //  mEditTextName.setSelection(mEditTextName.getText().length());
            }
            else {
               // mEditTextName.setError(null);
            }

        }
        //Name validation
        else if(editText==mEditTextLName){
            if(mEditTextLName.getText().toString().equals("")){
                //    mTextlayoutlname.setErrorEnabled(true);
                //mTextlayoutlname.setError("Mobile Number Must be 10 Digit");
                 mEditTextLName.setError("Name should not blank");
                //  mEditTextLName.setSelection(mEditTextName.getText().length());
            }
            else {
              //  mTextlayoutlname.setError(null);
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
