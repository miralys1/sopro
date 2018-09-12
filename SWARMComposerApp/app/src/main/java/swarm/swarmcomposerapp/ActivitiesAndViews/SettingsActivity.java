package swarm.swarmcomposerapp.ActivitiesAndViews;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import swarm.swarmcomposerapp.Model.LocalCache;
import swarm.swarmcomposerapp.R;

public class SettingsActivity extends AppCompatActivity implements IResponse {

    private Button bLogin, bLogout;
    private EditText tEmail, tPassword, tServerAdress;
    private TextView tLogin;
    private String email;
    private String token;
    private String serverAddress;
    private static final String PREFERENCE_NAME = "app_settings";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean changesWereMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

         preferences = getSharedPreferences(PREFERENCE_NAME, 0);
         editor = preferences.edit();

        //TODO check how it looks on tablet
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tLogin = findViewById(R.id.text_login);
        tPassword = findViewById(R.id.text_password);
        tEmail = findViewById(R.id.text_username);
        tServerAdress = findViewById(R.id.text_serverAddress);
        bLogin = findViewById(R.id.button_login);
        bLogout = findViewById(R.id.button_logout);
    }

    private void setLoginView(){
        bLogin.setVisibility(View.VISIBLE);
        bLogout.setVisibility(View.GONE);
        tLogin.setText(R.string.login);
        tEmail.setVisibility(View.VISIBLE);
        tPassword.setVisibility(View.VISIBLE);
    }

    /**
     *
     * @param username (formatted) to be displayed to the user
     */
    private void setLogoutView(String username){
        bLogin.setVisibility(View.GONE);
        bLogout.setVisibility(View.VISIBLE);
        tLogin.setText(getString(R.string.logged_in_as)+" "+username);
        tEmail.setVisibility(View.GONE);
        tPassword.setVisibility(View.GONE);
    }

    public void showErrorMessage(){

    }

    public void login(View v){
        String password = tPassword.getText().toString();
        email = tEmail.getText().toString();
        setLoadingView(true);
        if(email.isEmpty() || password.isEmpty()){
            showErrorMessage();
        }
        //TODO send server request
        //TODO if successful, pass email and token to localCache, show logoutView, turn off loadingView
        //TODO if failure, show error message, turn off loadingView
        setLogoutView("Felix Gröner");
        editor.putString("EMAIL", email);
        editor.putString("TOKEN", token);
        editor.commit();
        changesWereMade = true;
        setLoadingView(false);
    }

    public void logout(View v){
        //TODO set loadingView
        //TODO send server request
        //TODO if successful, delete email and token in localCache, show loginView, turn off loadingView
        //TODO if failure, show error message, turn off loadingView
        setLoginView();
        editor.remove("TOKEN");
        editor.remove("EMAIL");
        editor.commit();
        changesWereMade = true;
    }

    private void setLoadingView(boolean start){
        if(start){
            //TODO show loading
        } else {
            //TODO remove loading
        }
    }

    public void updateServerAddress(View v){

        //TODO set loadingView
        //TODO send server request
        //TODO if successful, pass webaddress to localCache, turn off loadingView
        //TODO if failure, show error message, turn off loadingView
        editor.putString("SERVERADDRESS", serverAddress);
        editor.commit();
        changesWereMade = true;

    }

    public void goBackToList(View v){
        super.onBackPressed();
        if(changesWereMade)
            LocalCache.getInstance().hardRefresh(this);
    }

    public void showHelp(View v){
        //TODO implement: Show handbook & credits
    }

    @Override
    public void notify(Boolean successful) {

    }
}
