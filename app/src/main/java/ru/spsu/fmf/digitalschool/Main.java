package ru.spsu.fmf.digitalschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.spsu.fmf.digitalschool.adapters.newsAdapter;
import ru.spsu.fmf.digitalschool.php.$categories;
import ru.spsu.fmf.digitalschool.php.$news;
import ru.spsu.fmf.digitalschool.php.$who;
import ru.spsu.fmf.digitalschool.sqLite.ContactDbHandler;
import ru.spsu.fmf.digitalschool.structures.Contact;
import ru.spsu.fmf.digitalschool.structures.jsonCategories;
import ru.spsu.fmf.digitalschool.structures.jsonNews;


public class Main extends ActionBarActivity {

    //                  ==========================================                  //
    //                  =========Start Contact Tab Var's==========                  //
    //                  ==========================================                  //

    int longClickedItemIndex;
    private static final int EDIT = 0, CALL = 1, SMS = 2, EMAIL = 3, MAP = 4, DELETE = 5;
    private static final int CONFIRM_LOGOFF = 0, CONFIRM_DELETE_CONTACT = 1, CONFIRM_DELETE_NEWS = 2;
    private static final String TAG = "MAIN ACTIVITY";

    List<Contact> Contacts = new ArrayList<>();
    ContactDbHandler dbHandler;
    ListView contactListView;
    ArrayAdapter<Contact> contactAdapter;

    //                  =========================================                   //
    //                  =========End Contact Tab Var's===========                   //
    //                  =========================================                   //


    //                  ==========================================                  //
    //                  =========Start News Tab Var's=============                  //
    //                  ==========================================                  //

    private ArrayList<jsonNews> feedList = null;
    private ArrayList<jsonCategories> categoriesList = null;
    private ProgressBar progressbar = null;
    private ListView feedListView = null;
    private TextView errorMessage = null;
    private static final int WHO = 6, DELETE_NEWS = 7, EDIT_NEWS = 8, NEWS_INFO_DEVELOPER = 9;

    //                  =========================================                   //
    //                  =========End News Tab Var's==============                   //
    //                  =========================================                   //

    TabHost tabHostMain;
    SharedPreferences sp;
    String userAccess, userId, userClass;
    Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (LogIn.loginActivity != null)
            LogIn.loginActivity.finish();

        Drawable dAll = getRepeatingBG(this, R.drawable.main_bg_gauss);

        View view1 = this.findViewById(R.id.tabNews).getRootView();
        view1.setBackgroundDrawable(dAll);
        View view2 = this.findViewById(R.id.tabContacts).getRootView();
        view2.setBackgroundDrawable(dAll);
        //View view3 = this.findViewById(R.id.tabSubjects).getRootView();
        //view3.setBackgroundDrawable(dAll);

        tabHostMain = (TabHost) findViewById(R.id.tabHostMain);
        tabHostMain.setup();

        tabHostMain.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                switch (tag) {
                    case "news":
                        getSupportActionBar().setTitle(getString(R.string.tab1));
                        break;
                    case "contacts":
                        getSupportActionBar().setTitle(getString(R.string.tab2));
                        break;
                    //case "subjects":
                    //    getSupportActionBar().setTitle(getString(R.string.tab3));
                    //   break;
                    default:
                        break;
                }
                supportInvalidateOptionsMenu();
            }
        });

        this.setNewTab(tabHostMain, "news", R.string.empty, R.drawable.ic_action_chat, R.id.tabNews);
        this.setNewTab(tabHostMain, "contacts", R.string.empty, R.drawable.ic_action_cc_bcc, R.id.tabContacts);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccess = sp.getString("level", "0");
        userId = sp.getString("id", "-1");
        userClass = sp.getString("class", "0");

        /*if (Integer.valueOf(userAccess) > 1) {
            this.setNewTab(tabHostMain, "subjects", R.string.empty, R.drawable.ic_action_view_as_list, R.id.tabSubjects);
            View lowLvl = findViewById(R.id.lowLvlLayout);
            lowLvl.setVisibility(View.GONE);
        }
        else {
            this.setNewTab(tabHostMain, "subjects", R.string.empty, R.drawable.ic_action_view_as_list, R.id.lowLvlLayout);
            View tabSubjects = findViewById(R.id.tabSubjects);
            tabSubjects.setVisibility(View.GONE);
        }*/

        // Contact list
        contactListView = (ListView) findViewById(R.id.contactListView);

        dbHandler = new ContactDbHandler(getApplicationContext());

        if (dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts());

        populateList();

        registerForContextMenu(contactListView);
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });
        //End Contact List

        //----------------------------------------------------------------------------------------------------------------------------------

        //Start News List
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        errorMessage = (TextView) findViewById(R.id.news_error_message);

        new DownloadFilesTask().execute();


        //End News List
    }

    //                  ============================================                //
    //                  =========Start Contact Tab methods==========                //
    //                  ============================================                //

    private void populateList(){
        contactAdapter = new ContactListAdapter();
        contactListView.setAdapter(contactAdapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact>{
        public ContactListAdapter(){
            super(Main.this, R.layout.contact_listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.contact_listview_item, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.getName());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.getEmail());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            ImageView image = (ImageView) view.findViewById(R.id.ivContactImage);
            image.setImageURI(currentContact.getImageURI());

            return view;
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle(getText(R.string.menu_header_opt));
        switch (view.getId()) {
            case R.id.contactListView:
                //menu.setHeaderIcon(R.drawable.edit_icon);
                menu.add(Menu.NONE, EDIT, Menu.NONE, R.string.menu_edit);
                menu.add(Menu.NONE, CALL, Menu.NONE, R.string.menu_call);
                menu.add(Menu.NONE, SMS, Menu.NONE, R.string.menu_sms);
                menu.add(Menu.NONE, EMAIL, Menu.NONE, R.string.menu_email);
                menu.add(Menu.NONE, MAP, Menu.NONE, R.string.menu_map);
                menu.add(Menu.NONE, DELETE, Menu.NONE, R.string.menu_delete);
                break;
            case R.id.newsListView:
                if (Integer.valueOf(userAccess) >= 3){
                    menu.add(Menu.NONE, WHO, Menu.NONE, getText(R.string.menu_who));
                    if (Integer.valueOf(userAccess) >= 5 || feedList.get(longClickedItemIndex).getWhoAdd() == Integer.valueOf(userId)) {
                        menu.add(Menu.NONE, DELETE_NEWS, Menu.NONE, getText(R.string.menu_delete));
                        menu.add(Menu.NONE, EDIT_NEWS, Menu.NONE, getText(R.string.menu_edit));
                    }
                    //menu.add(Menu.NONE, NEWS_INFO_DEVELOPER, Menu.NONE, "Информация по новости");
                }
                break;
        }
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case EDIT:
                Intent intent = new Intent(this, ContactEditor.class);
                intent.putExtra("id", Contacts.get(longClickedItemIndex).getID());
                startActivity(intent);
                break;
            case CALL:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Contacts.get(longClickedItemIndex).getPhone()));
                    startActivity(Intent.createChooser(callIntent, getString(R.string.menu_call)));
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Вызов", "Звонок не удался", activityException);
                }
                break;
            case SMS:
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setData(Uri.parse("sms:" + Contacts.get(longClickedItemIndex).getPhone()));
                    startActivity(Intent.createChooser(smsIntent, getString(R.string.menu_sms)));
                }catch (ActivityNotFoundException activityException) {
                    Log.e("Отправка SMS", "Отправка не удалась", activityException);
                }
                break;
            case EMAIL:
                try{
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); /*, Uri.fromParts("mailto",Contacts.get(longClickedItemIndex).getEmail(), null)*/
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, Contacts.get(longClickedItemIndex).getEmail());
                    emailIntent.setType("plain/text");
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.menu_email)));
                }catch (ActivityNotFoundException activityException) {
                    Log.e("Отправка почты", "Отправка не удалась", activityException);
                }
                break;
            case MAP:
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Contacts.get(longClickedItemIndex).getAddress()));
                startActivity(Intent.createChooser(mapIntent, getString(R.string.menu_map)));
                break;
            case DELETE:
                alertMessageTwoButtons(getString(R.string.dialog_del_header), getString(R.string.dialog_del_contact_message), CONFIRM_DELETE_CONTACT);
                break;

            /////////////News listView code/////////////--------------------------------------------------------------------------------------------------------------------------------------------------------------------

            case WHO:
                try {
                    showAuthorInfo(new $who(this).execute(String.valueOf(feedList.get(longClickedItemIndex).getWhoAdd())).get());
                } catch (InterruptedException | JSONException | ExecutionException e) {
                    Log.e("Main", e.getMessage());
                }
                break;
            case DELETE_NEWS:
                alertMessageTwoButtons(getString(R.string.dialog_del_header), getString(R.string.dialog_del_news_message), CONFIRM_DELETE_NEWS);
                break;
            case EDIT_NEWS:
                String[] categoriesStingList;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.dialog_add_news, null);

                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.addNewsSpinner);
                final EditText newsHeader = (EditText) dialogView.findViewById(R.id.addNewsHeader);
                final EditText newsBody = (EditText) dialogView.findViewById(R.id.addNewsBody);
                final CheckBox isImportant = (CheckBox) dialogView.findViewById(R.id.addNewsImportant);

                newsHeader.setText(feedList.get(longClickedItemIndex).getNewsHead());
                newsBody.setText(feedList.get(longClickedItemIndex).getNewsText());
                isImportant.setChecked(feedList.get(longClickedItemIndex).getImportant() == 1);

                int category_id = feedList.get(longClickedItemIndex).getNewsCategory();
                int position = 0;

                try {
                    String result = new $categories(this).execute().get();
                    JSONObject json = new JSONObject(result);
                    JSONArray categories = json.getJSONArray("categories");
                    categoriesList = new ArrayList<>();
                    categoriesStingList = new String[categories.length()];

                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject category = categories.getJSONObject(i);
                        jsonCategories categoryItem = new jsonCategories();
                        categoryItem.setId(category.getInt("id"));
                        categoryItem.setName(category.getString("name"));
                        categoriesList.add(categoryItem);
                        categoriesStingList[i] = category.getString("name");

                        if (categoryItem.getId() == category_id)
                            position = i;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesStingList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(position);

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }

                builder.setView(dialogView)
                        .setTitle(getString(R.string.dialog_edit_news_header))
                        .setPositiveButton(R.string.action_edit_news, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                new $news(Main.this).execute("update", String.valueOf(newsHeader.getText()),
                                        String.valueOf(newsBody.getText()),
                                        String.valueOf(categoriesList.get(spinner.getSelectedItemPosition()).getId()),
                                        userId,
                                        isImportant.isChecked() ? "1":"0",
                                        String.valueOf(feedList.get(longClickedItemIndex).getNewsId())
                                        );
                                updateListOnRefresh(myMenu.findItem(R.id.news_action_refresh));
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //nothing...
                            }
                        });
                builder.show();
                break;
            case NEWS_INFO_DEVELOPER:
                alertMessageOneButtons("Info",
                        "News id: " + String.valueOf(feedList.get(longClickedItemIndex).getNewsId())
                        + "\nNews category: " + String.valueOf(feedList.get(longClickedItemIndex).getNewsCategory())
                        + "\nNews important: " + String.valueOf(feedList.get(longClickedItemIndex).getImportant())
                        + "\nNews position: " + String.valueOf(longClickedItemIndex));
                break;
        }

        return super.onContextItemSelected(item);
    }

    //                  ===========================================                 //
    //                  =========End Contact Tab methods===========                 //
    //                  ===========================================                 //


    //                  ============================================                //
    //                  =========Start News Tab methods=============                //
    //                  ============================================                //

    private void showAuthorInfo(String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author_info, null);

        TextView fio = (TextView) dialogView.findViewById(R.id.dialog_author_fio);
        fio.setText(object.getString("fio").trim());

        TextView phone = (TextView) dialogView.findViewById(R.id.dialog_author_phone);
        phone.setText(object.getString("phone").trim());

        TextView cl = (TextView) dialogView.findViewById(R.id.dialog_author_class);
        cl.setText(object.getString("class").trim());

        TextView access = (TextView) dialogView.findViewById(R.id.dialog_author_access);
        access.setText(object.getString("access").trim());

        TextView date = (TextView) dialogView.findViewById(R.id.dialog_author_date);
        date.setText(object.getString("last_login").trim());

        builder.setView(dialogView)
                //Add header
                .setTitle(getString(R.string.menu_who))
                .setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                    }
                });
        builder.show();

    }

    public void updateList() {
        feedListView = (ListView) findViewById(R.id.newsListView);
        feedListView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);

        feedListView.setAdapter(new newsAdapter(this, feedList));

        registerForContextMenu(feedListView);
        feedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });
    }

    private void updateListOnError(String result) {
        errorMessage.setText(result);
        errorMessage.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        feedListView.setVisibility(View.GONE);
    }

    private void updateListOnRefresh(MenuItem item) {
        errorMessage.setText(null);
        feedListView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        feedList = null;

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView)inflater.inflate(R.layout.refresh, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        MenuItemCompat.setActionView(item, iv);

        new DownloadFilesTask().execute();
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // getting JSON string from URL
                JSONObject json = getJSONFromUrl();

                //parsing json data
                parseJson(json);

                return null;
            } catch (Exception ex){
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null)
                if (feedList != null)
                    updateList();
                else
                    updateListOnError("Не удалось получить список новостей\nПопробуйте обновить позже");
            else
                updateListOnError(result);

            resetUpdating();
        }
    }

    public JSONObject getJSONFromUrl() {
        JSONObject jObj = null;
        String json = null;

        try {
            String data = URLEncoder.encode("access", "UTF-8")
                    + "=" + URLEncoder.encode(userAccess, "UTF-8");

            data += "&" + URLEncoder.encode("class", "UTF-8")
                    + "=" + URLEncoder.encode(userClass, "UTF-8");

            data += "&" + URLEncoder.encode("user_id", "UTF-8")
                    + "=" + URLEncoder.encode(userId, "UTF-8");

            String newsUrl = "http://dsc.freevar.com/news.php";
            URL url = new URL(newsUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            json = sb.toString();
        } catch (IOException e) {
            Log.e("Main", e.getMessage());
        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public void parseJson(JSONObject json) {
        try {
            JSONArray posts = json.getJSONArray("news");

            feedList = new ArrayList<>();

            for (int i = posts.length()-1; i >=0 ; i--) {
                JSONObject post = posts.getJSONObject(i);
                jsonNews item = new jsonNews();
                item.setNewsId(post.getInt("id"));
                item.setNewsHead(post.getString("news_head"));
                item.setNewsText(post.getString("news_text"));
                item.setPublTime(post.getString("publishing_time"));
                item.setNewsCategory(post.getInt("category"));
                item.setWhoAdd(post.getInt("who_add"));
                item.setImportant(post.getInt("important"));

                feedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //                  ===========================================                 //
    //                  =========End News Tab methods==============                 //
    //                  ===========================================                 //

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (tabHostMain.getCurrentTabTag()){
            case "news":
                //news menus
                //menu.findItem(R.id.news_action_search).setVisible(true);
                menu.findItem(R.id.news_action_refresh).setVisible(true);
                menu.findItem(R.id.news_action_write).setVisible(Integer.valueOf(userAccess) > 2);

                //contact menus
                menu.findItem(R.id.contacts_action_add_person).setVisible(false);
                break;
            case "contacts":
                //news menus
                //menu.findItem(R.id.news_action_search).setVisible(false);
                menu.findItem(R.id.news_action_write).setVisible(false);
                menu.findItem(R.id.news_action_refresh).setVisible(false);

                //contact menus
                menu.findItem(R.id.contacts_action_add_person).setVisible(true);
                break;
            case "subjects":
                //news menus
                //menu.findItem(R.id.news_action_search).setVisible(false);
                menu.findItem(R.id.news_action_write).setVisible(false);
                menu.findItem(R.id.news_action_refresh).setVisible(false);

                //contact menus
                menu.findItem(R.id.contacts_action_add_person).setVisible(false);
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.contacts_action_add_person){
            Intent intent = new Intent(this, ContactEditor.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout){
            alertMessageTwoButtons(getString(R.string.dialog_logout_header), getString(R.string.dialog_logout_message), CONFIRM_LOGOFF);
            return true;
        }
        if (id == R.id.news_action_refresh){
            updateListOnRefresh(item);
            return true;
        }
        if (id == R.id.news_action_write){

            String[] categoriesStingList;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            View dialogView = inflater.inflate(R.layout.dialog_add_news, null);

            final Spinner spinner = (Spinner) dialogView.findViewById(R.id.addNewsSpinner);
            final EditText newsHeader = (EditText) dialogView.findViewById(R.id.addNewsHeader);
            final EditText newsBody = (EditText) dialogView.findViewById(R.id.addNewsBody);
            final CheckBox isImportant = (CheckBox) dialogView.findViewById(R.id.addNewsImportant);

            try {
                String result = new $categories(this).execute().get();
                JSONObject json = new JSONObject(result);
                JSONArray categories = json.getJSONArray("categories");
                categoriesList = new ArrayList<>();

                categoriesStingList = new String[categories.length()];
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i);
                    jsonCategories categoryItem = new jsonCategories();
                    categoryItem.setId(category.getInt("id"));
                    categoryItem.setName(category.getString("name"));
                    categoriesList.add(categoryItem);
                    categoriesStingList[i] = category.getString("name");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesStingList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);

            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

            builder.setView(dialogView)
                    .setTitle(getString(R.string.dialog_add_news_header))
                    .setPositiveButton(R.string.action_add_news, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new $news(Main.this).execute("add", String.valueOf(newsHeader.getText()),
                                    String.valueOf(newsBody.getText()),
                                    String.valueOf(categoriesList.get(spinner.getSelectedItemPosition()).getId()),
                                    userId,
                                    isImportant.isChecked() ? "1":"0"
                            );
                            updateListOnRefresh(myMenu.findItem(R.id.news_action_refresh));
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //nothing...
                        }
                    });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetUpdating()
    {
        MenuItem m = myMenu.findItem(R.id.news_action_refresh);
        if(MenuItemCompat.getActionView(m) != null)
        {
            MenuItemCompat.getActionView(m).clearAnimation();
            MenuItemCompat.setActionView(m, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHandler = new ContactDbHandler(getApplicationContext());
        Contacts = new ArrayList<>();
        if (dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts());

        populateList();

        Log.i(TAG, "onResume()");
    }

    private void forgotUser() {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        prefs.putBoolean("remember_user", false);
        prefs.putString("address", null);
        prefs.putString("password", null);
        prefs.apply();
    }

    private void setNewTab(TabHost tabHost, String tag, int title, int icon, int contentID ){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabIndicator(tabHost.getContext(), title, icon));
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    private void alertMessageTwoButtons(String title, String text, int id) {
        DialogInterface.OnClickListener logoffDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                        Intent login = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(login);
                        forgotUser();
                        Main.this.finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        break;
                }
            }
        };

        DialogInterface.OnClickListener deleteContactDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                        dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
                        Contacts.remove(longClickedItemIndex);
                        contactAdapter.notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        break;
                }
            }
        };

        DialogInterface.OnClickListener deleteNewsDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                        new $news(Main.this).execute("delete", String.valueOf(feedList.get(longClickedItemIndex).getNewsId()));
                        updateListOnRefresh(myMenu.findItem(R.id.news_action_refresh));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case CONFIRM_LOGOFF:
                builder.setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(R.string.btn_yes, logoffDialogClickListener)
                        .setNegativeButton(R.string.btn_no, logoffDialogClickListener).show();
                break;
            case CONFIRM_DELETE_CONTACT:
                builder.setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(R.string.btn_yes, deleteContactDialogClickListener)
                        .setNegativeButton(R.string.btn_no, deleteContactDialogClickListener).show();
                break;
            case CONFIRM_DELETE_NEWS:
                builder.setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(R.string.btn_yes, deleteNewsDialogClickListener)
                        .setNegativeButton(R.string.btn_no, deleteNewsDialogClickListener).show();
                break;
        }
    }

    private void alertMessageOneButtons(String title, String text) {
        DialogInterface.OnClickListener okDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL: // No button clicked // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(R.string.btn_ok, okDialogClickListener).show();

    }
}
