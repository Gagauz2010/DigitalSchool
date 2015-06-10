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

public class $who extends AsyncTask<String,Void,String> {

    private Context context;
    private ProgressDialog dialog = null;

    public $who(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(this.context, "", context.getString(R.string.loading_data), true);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            String id = arg0[0];

            String link = "http://dsc.freevar.com/users.php";

            String data = URLEncoder.encode("id", "UTF-8")
                    + "=" + URLEncoder.encode(id, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

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
