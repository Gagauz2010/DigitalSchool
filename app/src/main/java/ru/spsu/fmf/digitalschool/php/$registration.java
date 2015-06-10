package ru.spsu.fmf.digitalschool.php;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ru.spsu.fmf.digitalschool.R;

public class $registration extends AsyncTask<String,Void,String> {

    private Context context;
    private ProgressDialog dialog;

    public $registration(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        dialog = ProgressDialog.show(this.context, "", "Регистрация...", true);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            String email = arg0[0];
            String password = arg0[1];
            String fio = arg0[2];
            String phone = arg0[3];

            String link = "http://dsc.freevar.com/register.php";

            String data = URLEncoder.encode("email", "UTF-8")
                    + "=" + URLEncoder.encode(email, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

            data += "&" + URLEncoder.encode("fio", "UTF-8")
                    + "=" + URLEncoder.encode(fio, "UTF-8");

            data += "&" + URLEncoder.encode("phone", "UTF-8")
                    + "=" + URLEncoder.encode(phone, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return String.valueOf(sb);
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null)
            dialog.dismiss();

        if (result.contains("Message sent!")) {
            alertMessage("Регистрация", "Вы успешно зарегестрировались. Осталось подтвердить Ваш Email.\n" +
                    "Пожалуйста проверьте почту!");
        }
        else if (result.contains("ERROR sending message.")) {
            alertMessage("Регистрация", "Что-то пошло не так, повторите попытку позже");
        }
        else if (result.contains("Email already registered")) {
            alertMessage("Регистрация", "Пользователь с данным Email адресом уже зарегистрирован");
        }
        else{
            Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
        }
    }

    public void alertMessage(String title, String text) {
        DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL: // No button clicked // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title)
                        .setMessage(text)
                        .setNeutralButton(R.string.btn_ok, deleteDialogClickListener).show();

    }
}
