package ru.spsu.fmf.digitalschool.php;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ru.spsu.fmf.digitalschool.R;

public class $news extends AsyncTask <String, Void,String> {

    private Context context;
    private ProgressDialog dialog = null;

    public $news (Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, null, context.getString(R.string.loading_wait));
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String instruction = params[0];
            String link = "http://dsc.freevar.com/news.php?instructions=" + instruction;
            String data;
            URL url;
            URLConnection conn;
            OutputStreamWriter wr;
            BufferedReader reader;
            StringBuilder sb;
            String line, news_head, news_text, news_category, user_id, news_important, news_id;

            switch (instruction) {
                case "add":
                    news_head = params[1];
                    news_text = params[2];
                    news_category = params[3];
                    user_id = params[4];
                    news_important = params[5];
                    data = URLEncoder.encode("news_head", "UTF-8")
                            + "=" + URLEncoder.encode(news_head, "UTF-8");
                    data += "&" + URLEncoder.encode("news_text", "UTF-8")
                            + "=" + URLEncoder.encode(news_text, "UTF-8");
                    data += "&" + URLEncoder.encode("news_category", "UTF-8")
                            + "=" + URLEncoder.encode(news_category, "UTF-8");
                    data += "&" + URLEncoder.encode("user_id", "UTF-8")
                            + "=" + URLEncoder.encode(user_id, "UTF-8");
                    data += "&" + URLEncoder.encode("news_important", "UTF-8")
                            + "=" + URLEncoder.encode(news_important, "UTF-8");

                    url = new URL(link);
                    conn = url.openConnection();
                    conn.setDoOutput(true);
                    wr = new OutputStreamWriter
                            (conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    reader = new BufferedReader
                            (new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return String.valueOf(sb);

                case "delete":
                    news_id = params[1];

                    data = URLEncoder.encode("news_id", "UTF-8")
                            + "=" + URLEncoder.encode(news_id, "UTF-8");

                    url = new URL(link);
                    conn = url.openConnection();
                    conn.setDoOutput(true);
                    wr = new OutputStreamWriter
                            (conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    reader = new BufferedReader
                            (new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return String.valueOf(sb);

                case "update":
                    news_head = params[1];
                    news_text = params[2];
                    news_category = params[3];
                    user_id = params[4];
                    news_important = params[5];
                    news_id = params[6];
                    data = URLEncoder.encode("news_head", "UTF-8")
                            + "=" + URLEncoder.encode(news_head, "UTF-8");
                    data += "&" + URLEncoder.encode("news_text", "UTF-8")
                            + "=" + URLEncoder.encode(news_text, "UTF-8");
                    data += "&" + URLEncoder.encode("news_category", "UTF-8")
                            + "=" + URLEncoder.encode(news_category, "UTF-8");
                    data += "&" + URLEncoder.encode("user_id", "UTF-8")
                            + "=" + URLEncoder.encode(user_id, "UTF-8");
                    data += "&" + URLEncoder.encode("news_important", "UTF-8")
                            + "=" + URLEncoder.encode(news_important, "UTF-8");
                    data += "&" + URLEncoder.encode("news_id", "UTF-8")
                            + "=" + URLEncoder.encode(news_id, "UTF-8");

                    url = new URL(link);
                    conn = url.openConnection();
                    conn.setDoOutput(true);
                    wr = new OutputStreamWriter
                            (conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    reader = new BufferedReader
                            (new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return String.valueOf(sb);

                default:
                    return null;
            }
        } catch (Exception e){
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null)
            dialog.dismiss();
        switch (result){
            case "Success":
                Toast.makeText(context, R.string.loading_success, Toast.LENGTH_SHORT).show();
                break;
            case "Failure":
                Toast.makeText(context, R.string.loading_failure, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}