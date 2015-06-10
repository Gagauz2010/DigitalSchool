package ru.spsu.fmf.digitalschool.php;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ru.spsu.fmf.digitalschool.R;

public class $login extends AsyncTask<String,Void,String> {

    private Context context;
    private ProgressDialog dialog = null;

    public $login(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(this.context, "", context.getString(R.string.loading_auth), true);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            String email = arg0[0];
            String password = arg0[1];

            String link = "http://dsc.freevar.com/login.php";

            String data = URLEncoder.encode("email", "UTF-8")
                    + "=" + URLEncoder.encode(email, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(5000);

            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null)
            dialog.dismiss();
        super.onPostExecute(result);
    }
}