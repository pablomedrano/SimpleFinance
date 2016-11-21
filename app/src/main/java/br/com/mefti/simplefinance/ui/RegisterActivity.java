package br.com.mefti.simplefinance.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import br.com.mefti.simplefinance.R;
import br.com.mefti.simplefinance.modelo.Categorias;
import br.com.mefti.simplefinance.modelo.Usuarios;
import br.com.mefti.simplefinance.sqlite.BaseDadosSF;
import br.com.mefti.simplefinance.sqlite.ContratoSF;


import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mRegisterEmailView;
    private EditText mRegisterPasswordView;
    private EditText mRegisterConfirmPasswordView;
    private EditText mUserNameView;
    private View mProgressView;
    private View mLoginFormView;

    BaseDadosSF dados = new BaseDadosSF(this);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Set up the login form.
        mRegisterEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        populateAutoComplete();

        final String registerEmail1 = mRegisterEmailView.getText().toString();

        mRegisterPasswordView = (EditText) findViewById(R.id.register_password);
        mRegisterPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        /*
        mRegisterConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mRegisterConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        */


        Button mRegisterEmailSignInButton = (Button) findViewById(R.id.register_button);
        mRegisterEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mUserNameView = (EditText) findViewById(R.id.user_name);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mRegisterEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mRegisterEmailView.setError(null);
        mRegisterPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String registerEmail = mRegisterEmailView.getText().toString();
        String password = mRegisterPasswordView.getText().toString();
        //String confirmPassword = mRegisterConfirmPasswordView.getText().toString();
        String userName = mUserNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valir user name.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        // Compare passwords
        /*
        if (!password.equals(confirmPassword)) {
            mRegisterConfirmPasswordView.setError(getString(R.string.error_pass_not_equal));
            focusView = mRegisterConfirmPasswordView;
            cancel = true;
        }
        */

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mRegisterPasswordView.setError(getString(R.string.error_field_required));
            focusView = mRegisterPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mRegisterPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRegisterPasswordView;
            cancel = true;
        }

        // Check for a valid confirm password.
        /*
        if (TextUtils.isEmpty(confirmPassword)) {
            mRegisterConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mRegisterConfirmPasswordView;
            cancel = true;
        } else if (!isPasswordValid(confirmPassword)) {
            mRegisterConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRegisterConfirmPasswordView;
            cancel = true;
        }
        */

        // Check for a valid email address.
        if (TextUtils.isEmpty(registerEmail)) {
            mRegisterEmailView.setError(getString(R.string.error_field_required));
            focusView = mRegisterEmailView;
            cancel = true;
        } else if (!isEmailValid(registerEmail)) {
            mRegisterEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mRegisterEmailView;
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

            //dados = OperacoesBaseDados.obterInstancia(getApplicationContext());

            mAuthTask = new UserLoginTask(userName, registerEmail, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        String emailExist = dados.ObterUsuarioPorEmail(email);
        if (emailExist == "not found" && email.contains("@")){
            return true;
        }else{
            return false;
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mRegisterEmailView.setAdapter(adapter);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String name, String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //Inicio inserir usuario
            Usuarios usuarios = new Usuarios();
            usuarios.setNome(mName);
            usuarios.setEmail(mEmail);
            usuarios.setSenha(mPassword);
            usuarios.setEstado("1");
            dados.inserirUsuario(usuarios);
            //fin inserir usuario

            //inserimos categorias
            String cod_usuario = "";
            Cursor cursor = dados.ObterUsuarioConectado();
            if(cursor.moveToFirst()){
                cod_usuario = cursor.getString(1);
            }

            Categorias categorias = new Categorias();
            categorias.setCod_usuario(cod_usuario);
            categorias.setNome("Alimentação");
            categorias.setTp_lancamento("d");
            dados.inserirCategoria(categorias);

            Categorias categorias1 = new Categorias();
            categorias1.setCod_usuario(cod_usuario);
            categorias1.setNome("Carro");
            categorias1.setTp_lancamento("d");
            dados.inserirCategoria(categorias1);

            Categorias categorias2 = new Categorias();
            categorias2.setCod_usuario(cod_usuario);
            categorias2.setNome("Lazer");
            categorias2.setTp_lancamento("d");
            dados.inserirCategoria(categorias2);

            Categorias categorias3 = new Categorias();
            categorias3.setCod_usuario(cod_usuario);
            categorias3.setNome("Salário");
            categorias3.setTp_lancamento("r");
            dados.inserirCategoria(categorias3);

            Categorias categorias4 = new Categorias();
            categorias4.setCod_usuario(cod_usuario);
            categorias4.setNome("Pensão");
            categorias4.setTp_lancamento("r");
            dados.inserirCategoria(categorias4);

            Categorias categorias5 = new Categorias();
            categorias5.setCod_usuario(cod_usuario);
            categorias5.setNome("Aluguel");
            categorias5.setTp_lancamento("r");
            dados.inserirCategoria(categorias5);
            // fin inserir catergorias

            Intent i = new Intent(RegisterActivity.this, ExtratoActivity.class);
            startActivity(i);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mRegisterPasswordView.setError(getString(R.string.error_incorrect_password));
                mRegisterPasswordView.requestFocus();

                //mRegisterConfirmPasswordView.setError(getString(R.string.error_incorrect_password));
                //mRegisterPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

