package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.R;

/**
 * The user can log in, log out and change the address of the server here.
 */
public class SettingsActivity extends AppCompatActivity implements IResponse {

    private enum LastRequest {
        //a state to remember the last server request sent, in order to correctly react to notify() called on response
        NONE, LOGIN, LOGOUT, ADDRESS_CHANGE
    }
    private LastRequest state = LastRequest.NONE;
    private Button bLogin, bLogout, bAddress;
    private EditText tEmail, tPassword, tServerAddress;
    private TextView tLogin, tLoading, bList, bHelp;
    private ProgressBar progressBar;
    private String email;
    private String password;
    private String serverAddress;
    private static final String PREFERENCE_NAME = "app_settings";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LocalCache cache = LocalCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //preferences used to store data even after app has been stopped
         preferences = getSharedPreferences(PREFERENCE_NAME, 0);
         editor = preferences.edit();

         //display the onscreen keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tLogin = findViewById(R.id.text_login);
        tPassword = findViewById(R.id.text_password);
        tEmail = findViewById(R.id.text_username);
        tServerAddress = findViewById(R.id.text_serverAddress);
        bLogin = findViewById(R.id.button_login);
        bLogout = findViewById(R.id.button_logout);
        bAddress = findViewById(R.id.button_address);
        progressBar = findViewById(R.id.progressBar2);
        tLoading = findViewById(R.id.text_loading2);
        bList = findViewById(R.id.button_list);
        bHelp = findViewById(R.id.button_help);

        autoFill(); //fill in the fields with saved data
        if(cache.getPassword() != null){
            setLogoutView();
        }
    }

    /**
     * change the view to show the login fields
     */
    private void setLoginView(){
        bLogin.setVisibility(View.VISIBLE);
        bLogout.setVisibility(View.GONE);
        tLogin.setText(R.string.login);
        tEmail.setVisibility(View.VISIBLE);
        tPassword.setVisibility(View.VISIBLE);
    }

    /**
     * change the views to show the logout button as well as the username
     */
    private void setLogoutView(){
        //TODO get username
        bLogin.setVisibility(View.GONE);
        bLogout.setVisibility(View.VISIBLE);
        tLogin.setText(getString(R.string.logged_in_as)+cache.getEmail());
        tEmail.setVisibility(View.GONE);
        tPassword.setVisibility(View.GONE);
    }

    /**
     * try to log in with the given credentials (server request)
     * @param v
     */
    public void login(View v){
        password = tPassword.getText().toString();
        email = tEmail.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), getText(R.string.err_text_emailpasswordmissing), Toast.LENGTH_SHORT).show();
        } else {
            //invalidate all data in LocalCache and try to reload with new user information
            showLoading(true);
            state = LastRequest.LOGIN;
            cache.setEmail(email);
            cache.setPassword(password);
            cache.hardRefresh(this);
        }
    }

    /**
     * invalidate all data in LocalCache and reload with public compositions only
     * @param v
     */
    public void logout(View v){
        showLoading(true);
        state = LastRequest.LOGOUT;
        //remove user information in cache
        cache.setPassword(null);
        cache.setEmail(null);
        cache.hardRefresh(this);
    }

    /**
     * invalidate all data in LocalCache and try to reload from the new server address
     * @param v
     */
    public void updateServerAddress(View v){
        //TODO also log out if logged in??
        serverAddress = tServerAddress.getText().toString();
        if(serverAddress.isEmpty()){
            Toast.makeText(getApplicationContext(), getText(R.string.err_text_serveraddressmissing), Toast.LENGTH_SHORT).show();
        } else {
            try {
                cache.setServerAddress(serverAddress);
                cache.hardRefresh(this);
                showLoading(true);
                state = LastRequest.ADDRESS_CHANGE;
            } catch(Exception e){
                Toast.makeText(getApplicationContext(), getText(R.string.err_text_illegalserveraddress), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void goBackToList(View v){
        super.onBackPressed();
    }

    /**
     * open the help dialog with useful information of how to use the app
     * @param v
     */
    public void showHelp(View v){
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_dialog, null);
        ((TextView) layout.findViewById(R.id.d_title)).setText(getText(R.string.d_instructions_title));
        ((TextView) layout.findViewById(R.id.d_text)).setText(getText(R.string.d_instructions_text));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNeutralButton(getText(R.string.d_button_close), null);
        alertDialogBuilder.setView(layout);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * changes the views to display a loading bar and text as well as disabling all inputs
     * @param activate turn loading view on
     */
    private void showLoading(boolean activate){
        if(activate)
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tLoading.setVisibility(activate ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(activate ? View.VISIBLE : View.GONE);
        bHelp.setEnabled(!activate);
        bList.setEnabled(!activate);
        tPassword.setEnabled(!activate);
        tServerAddress.setEnabled(!activate);
        tEmail.setEnabled(!activate);
        bLogout.setEnabled(!activate);
        bLogin.setEnabled(!activate);
        bAddress.setEnabled(!activate);
    }

    @Override
    public void notify(boolean successful) {
        Log.i("LOGIN", "settings notify successful = "+successful+", state "+state);
        if(successful){
            LocalCache cache = LocalCache.getInstance();
            switch (state){
                case LOGIN: {
                    //login was successful; store new email in preferences
                    setLogoutView();
                    editor.putString("EMAIL", email);
                    editor.commit();
                }; break;
                case LOGOUT: {
                    //logout was successfull; remove email in preferences
                    setLoginView();
                    editor.putString("EMAIL", null);
                    editor.commit();
                }; break;
                case ADDRESS_CHANGE: {
                    //address change was successfull; store new server address in preferences
                    editor.putString("SERVERADDRESS", serverAddress);
                    editor.commit();
                }; break;
            }
        } else {
            //Something went wrong; reload old preferences into cache and text fields
            switch (state){
                case LOGOUT: {
                    //logout failed
                    //TODO show error dialog with tips
                    Toast.makeText(getApplicationContext(), getText(R.string.err_text_logout), Toast.LENGTH_SHORT).show();
                    cache.setEmail(preferences.getString("EMAIL", ""));
                }; break;
                case LOGIN: {
                    //login failed, remove faulty user data
                    //TODO show error dialog with tips
                    Toast.makeText(getApplicationContext(), getText(R.string.err_text_login), Toast.LENGTH_SHORT).show();
                    cache.setEmail(null);
                    cache.setPassword(null);
                }; break;
                case ADDRESS_CHANGE: {
                    //address change failed; remove faulty address
                    //TODO show error dialog with tips
                    Toast.makeText(getApplicationContext(), getText(R.string.err_text_serveraddress), Toast.LENGTH_SHORT).show();
                    cache.setServerAddress(preferences.getString("SERVERADDRESS", "http://default.de/"));
                }; break;
            }
        }
        autoFill();
        //fill in fields with new/old information
        showLoading(false);
    }

    /**
     * retrieve email and server address from LocalCache and pre fill in the fields
     */
    private void autoFill(){
        password = "";
        tPassword.setText(password);
        email = cache.getEmail();
        if(email != null)
            tEmail.setText(email);
        serverAddress = cache.getServerAddress();
        if(serverAddress != null)
            tServerAddress.setText(serverAddress);
    }
}


