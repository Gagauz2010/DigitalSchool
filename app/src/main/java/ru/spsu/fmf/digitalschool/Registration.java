package ru.spsu.fmf.digitalschool;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.spsu.fmf.digitalschool.php.$registration;

@SuppressWarnings("deprecation")
public class Registration extends ActionBarActivity {

    private EditText email, email1, pas, pas1, fio, phone;
    public static Activity regActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        View view = this.findViewById(R.id.regMainView).getRootView();
        Drawable d = getRepeatingBG(this, R.drawable.main_bg_gauss);
        view.setBackgroundDrawable(d);

        regActivity = this;

        email = (EditText) findViewById(R.id.regEmail);
        email1 = (EditText) findViewById(R.id.regConfEmail);
        pas = (EditText) findViewById(R.id.regPas);
        pas1 = (EditText) findViewById(R.id.regConfPas);
        fio = (EditText) findViewById(R.id.regFIO);
        phone = (EditText) findViewById(R.id.regPhone);

        //email.setPadding(10,10,10,10);
        //email1.setPadding(10,10,10,10);
        //pas.setPadding(10,10,10,10);
        //pas1.setPadding(10,10,10,10);
        //fio.setPadding(10,10,10,10);
        //phone.setPadding(10,10,10,10);

        Button reg = (Button) findViewById(R.id.regRegBtn);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(email.getText()).trim().length() == 0 ||
                        String.valueOf(email1.getText()).trim().length() == 0 ||
                        String.valueOf(pas.getText()).trim().length() == 0 ||
                        String.valueOf(pas1.getText()).trim().length() == 0 ||
                        String.valueOf(fio.getText()).trim().length() == 0 ||
                        String.valueOf(phone.getText()).trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!String.valueOf(email.getText()).equals(String.valueOf(email1.getText()))) {
                    Toast.makeText(getApplicationContext(), R.string.different_emails, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!String.valueOf(pas.getText()).equals(String.valueOf(pas1.getText()))) {
                    Toast.makeText(getApplicationContext(), R.string.different_passwords, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (String.valueOf(pas.getText()).trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), R.string.password_not_strong, Toast.LENGTH_SHORT).show();
                    return;
                }

                String emailText, passwordText, fioText, phoneText;
                emailText = String.valueOf(email.getText());
                passwordText = String.valueOf(pas.getText());
                fioText = String.valueOf(fio.getText());
                phoneText = String.valueOf(phone.getText());

                new $registration(getApplicationContext()).execute(emailText, passEncrypt(passwordText), fioText, phoneText);
            }
        });
    }

    private String passEncrypt(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String salt = "50me_r@nd0m_s41t";
            String passWithSalt = password + salt;
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

    private Drawable getRepeatingBG(Activity activity, int center)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled=true;

        Bitmap center_bmp = BitmapFactory.decodeResource(activity.getResources(), center, options);
        center_bmp.setDensity(Bitmap.DENSITY_NONE);
        center_bmp=Bitmap.createScaledBitmap(center_bmp, center_bmp.getWidth()/(center_bmp.getHeight()/dm.heightPixels), dm.heightPixels, true);

        BitmapDrawable center_drawable = new BitmapDrawable(activity.getResources(),center_bmp);
        center_drawable.setTileModeX(Shader.TileMode.REPEAT);

        return center_drawable;
    }
}
