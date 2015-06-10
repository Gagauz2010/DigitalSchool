package ru.spsu.fmf.digitalschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.spsu.fmf.digitalschool.php.$login;


@SuppressWarnings("deprecation")
public class LogIn extends ActionBarActivity {
    private EditText emailTxt, passwordTxt;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().hide();

        View view = this.findViewById(R.id.logMainView).getRootView();
        Drawable d = getRepeatingBG(this, R.drawable.main_bg);
        view.setBackgroundDrawable(d);

        loginActivity = this;

        TextView site = (TextView) findViewById(R.id.textViewSite);
        TextView forgetPassword = (TextView) findViewById(R.id.textViewPassword);

        String htmlSite = getString(R.string.site);
        String htmlForget = getString(R.string.forget_password);

        site.setText(Html.fromHtml(htmlSite));
        site.setMovementMethod(LinkMovementMethod.getInstance());
        forgetPassword.setText(Html.fromHtml(htmlForget));
        forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());

        final Button loginBtn = (Button) findViewById(R.id.logBtnLogin);
        final Button registerBtn = (Button) findViewById(R.id.logBtnRegister);
        emailTxt = (EditText) findViewById(R.id.logEditEmail);
        passwordTxt = (EditText) findViewById(R.id.logEditPassword);

        //emailTxt.setPadding(10,10,10,10);
        //passwordTxt.setPadding(10, 10, 10, 10);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean remember = sp.getBoolean("remember_user", false);
        String address = sp.getString("address", "");
        String password = sp.getString("password", "");

        if (remember) {
            if (!isNetworkOnline()) {
                Toast.makeText(getApplicationContext(), getText(R.string.no_internet_message), Toast.LENGTH_LONG).show();
            } else {
                emailTxt.setText(address);
                passwordTxt.setText(password);

                auth(String.valueOf(emailTxt.getText()), passEncrypt(String.valueOf(passwordTxt.getText())));
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(emailTxt.getText()).trim().length() == 0 || String.valueOf(passwordTxt.getText()).trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = String.valueOf(emailTxt.getText());
                String password = passEncrypt(String.valueOf(passwordTxt.getText()));

                if (!isNetworkOnline())
                    Toast.makeText(getApplicationContext(), getText(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
                else {
                    auth(email, password);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });
    }

    private void auth(String email, String password) {
        try {
            tryToAuth(new $login(this).execute(email, password).get());
        }
        catch (Exception ex) {
            Log.e("LogIn", ex.getMessage());
        }
    }

    private void tryToAuth(String result) {
        if (!result.equals("User not found")) {
            try {
                JSONObject object = new JSONObject(result);

                String cl, access, id;
                access = object.getString("access").trim();
                id = object.getString("id").trim();
                cl = object.getString("class").trim();

                if (access.contains("0")) {
                    forgotUser();

                    DialogInterface.OnClickListener okDialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                                    break;
                                case DialogInterface.BUTTON_NEUTRAL: // Ok button clicked // do nothing
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.auth))
                            .setMessage(getString(R.string.contact_need_to_confirm))
                            .setNeutralButton(R.string.btn_ok, okDialogClickListener).show();
                } else {
                    rememberUser(id, access, cl);
                }
            }
            catch (JSONException e){
                Log.e("login", e.getMessage());
            }
        }
        else if (result.contains("User not found")){
            forgotUser();

            Toast.makeText(getApplicationContext(), getText(R.string.post_auth_wrong_data), Toast.LENGTH_SHORT).show();
        }
        else if (result.contains("Exception: Unable to resolve host")) {
            Toast.makeText(getApplicationContext(), getText(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
        else{
            forgotUser();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    private void rememberUser(String id, String access, String cl) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefs.putBoolean("remember_user", true);
        prefs.putString("address", String.valueOf(emailTxt.getText()));
        prefs.putString("password", String.valueOf(passwordTxt.getText()));
        prefs.putString("level", access);
        prefs.putString("id", id);
        prefs.putString("class", cl);
        prefs.apply();

        Intent intent = new Intent(LogIn.this, Main.class);
        startActivity(intent);
    }

    private void forgotUser() {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefs.putBoolean("remember_user", false);
        prefs.putString("address", null);
        prefs.putString("password", null);
        prefs.putString("level", null);
        prefs.putString("id", null);
        prefs.putString("class", null);
        prefs.apply();
    }

    private Drawable getRepeatingBG(Activity activity, int center)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;

        Bitmap center_bmp = BitmapFactory.decodeResource(activity.getResources(), center, options);
        center_bmp.setDensity(Bitmap.DENSITY_NONE);
        center_bmp=Bitmap.createScaledBitmap(center_bmp, center_bmp.getWidth()/(center_bmp.getHeight()/dm.heightPixels), dm.heightPixels, true);

        BitmapDrawable center_drawable = new BitmapDrawable(activity.getResources(),center_bmp);
        center_drawable.setTileModeX(Shader.TileMode.REPEAT);

        return center_drawable;
    }

    private String passEncrypt(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String salt = "50me_r@nd0m_s41t";
            String passWithSalt;
            passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (byte aPassHash : passHash) {
                sb.append(Integer.toString((aPassHash & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }
}
