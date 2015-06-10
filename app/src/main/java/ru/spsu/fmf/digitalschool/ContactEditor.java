package ru.spsu.fmf.digitalschool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ru.spsu.fmf.digitalschool.sqLite.ContactDbHandler;
import ru.spsu.fmf.digitalschool.structures.Contact;

public class ContactEditor extends ActionBarActivity {

    Uri imageURI = Uri.parse("android.resource://ru.spsu.fmf.digitalschool/" +  R.drawable.no_profile_image);

    ImageView contactImageImgView;
    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    Button addBtn;
    Contact contact;
    ContactDbHandler dbHandler;
    Menu menu;

    int idTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_editor);

        getSupportActionBar().setDisplayShowHomeEnabled(false);

        Drawable dAll = getRepeatingBG(ContactEditor.this, R.drawable.main_bg_gauss);
        View view = this.findViewById(R.id.creatorView).getRootView();
        view.setBackgroundDrawable(dAll);

        dbHandler = new ContactDbHandler(getApplicationContext());

        nameTxt = (EditText) view.findViewById(R.id.txtName);
        phoneTxt = (EditText) view.findViewById(R.id.txtPhone);
        emailTxt = (EditText) view.findViewById(R.id.txtEmail);
        addressTxt = (EditText) view.findViewById(R.id.txtAddress);
        contactImageImgView = (ImageView) view.findViewById(R.id.imgViewContactImage);

        addBtn = (Button) view.findViewById(R.id.btnAdd);

        Intent intent = getIntent();
        idTo = intent.getIntExtra("id", -1);

        if (idTo == -1)
            getSupportActionBar().setTitle(getString(R.string.creator_header));
        else {
            getSupportActionBar().setTitle(getString(R.string.editor_header));
            addBtn.setText(getString(R.string.edit_btn));
            contact = dbHandler.getContact(idTo);
            nameTxt.setText(contact.getName());
            phoneTxt.setText(contact.getPhone());
            emailTxt.setText(contact.getEmail());
            addressTxt.setText(contact.getAddress());
            contactImageImgView.setImageURI(contact.getImageURI());
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckName();
            }
        });

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_photo_header)), 1);
            }
        });
    }

    private void CheckName() {
        if (String.valueOf(nameTxt.getText()).trim().length() > 0)
            EditContact();
        else
            Toast.makeText(getApplicationContext(), getString(R.string.contact_need_name), Toast.LENGTH_SHORT).show();
    }

    private void EditContact() {
        Contact contact;

        if (idTo == -1){
            contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), imageURI);
            if (!dbHandler.contactExist(String.valueOf(nameTxt.getText()))) {
                dbHandler.createContact(contact);

                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + getString(R.string.contact_added), Toast.LENGTH_SHORT).show();

                ContactEditor.this.finish();
            }
            else{
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + getString(R.string.contact_already_exist), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            contact = new Contact(dbHandler.getContact(idTo).getID(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), imageURI);
            int rows = dbHandler.updateContact(contact);
            if (rows > 0) {
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + getString(R.string.contact_updated), Toast.LENGTH_SHORT).show();

                ContactEditor.this.finish();
            }
            else{
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + getString(R.string.contact_not_updated), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode == Activity.RESULT_OK){
            if (reqCode == 1) {
                contactImageImgView.setImageURI(data.getData());
                imageURI = data.getData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_editor, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ready_contact) {
            CheckName();
            return true;
        }

        if (id == R.id.action_cancel_contact)
        {
            ContactEditor.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
