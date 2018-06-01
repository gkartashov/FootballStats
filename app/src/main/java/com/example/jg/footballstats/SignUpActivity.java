package com.example.jg.footballstats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import com.example.jg.footballstats.db.User;

import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private UserSignUpTask mSignUpTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Constants.IS_THEME_DARK ? R.style.AppTheme : R.style.AppTheme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageView logo = findViewById(R.id.login_image_view);
        logo.setImageResource(Constants.IS_THEME_DARK ? R.drawable.ic_logo_final_without_title_dark : R.drawable.ic_logo_final_without_title);

        mUsernameView = findViewById(R.id.sign_up_username);
        mEmailView = findViewById(R.id.sign_up_email);
        mPasswordView = findViewById(R.id.sign_up_password);
        mNameView = findViewById(R.id.sign_up_name);

        Button mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSignUpFormView.getWindowToken(),0);
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.sign_up_form);
        mProgressView = findViewById(R.id.sign_up_progress);
        TextView mLoginLinkView = findViewById(R.id.sign_up_form_link_login);
        mLoginLinkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentSignUp);
                finish();
            }
        });
    }

    private void attemptSignUp() {
        if (mSignUpTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mNameView.setError(null);
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String username = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(name) && !isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mSignUpTask = new UserSignUpTask(username, password, email, name);
            mSignUpTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isUsernameValid(String username) {
        return Constants.VALID_USERNAME_REGEX.matcher(username).find();
    }

    private boolean isNameValid(String name) {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, User> {

        private final String mEmail;
        private final String mPassword;
        private final String mUsername;
        private final String mName;
        private String mMessage = "";
        private User mUser = null;

        UserSignUpTask(String username, String password, String email, String name) {
            mEmail = email;
            mPassword = password;
            mUsername = username;
            mName = name;
        }

        @Override
        protected User doInBackground(Void... params) {
            Response response = null;
            try {
                response = DatabaseAPIController.getInstance().getAPI().register(mUsername, mPassword,mEmail,mName).execute();
            } catch (IOException e) {
                mMessage += e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1);
            }
            if (response != null)
                if(response.body() != null)
                    mUser = (User) response.body();
                else if (response.code() == 409)
                    mMessage = getString(R.string.error_existed_user);

            return mUser;
        }

        @Override
        protected void onPostExecute(final User user) {
            mSignUpTask = null;
            showProgress(false);

            if (user != null) {
                Constants.USER = mUser;
                Constants.BETS_LIST.clear();
                userToSharedPref(mUser);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
            else
                Snackbar.make(mSignUpFormView,mMessage,Snackbar.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mSignUpTask = null;
            mUser = null;
            showProgress(false);
        }

        private void userToSharedPref(User user) {
            SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("User",ObjectSerializer.serialize(user));
            editor.apply();
        }
    }
}

