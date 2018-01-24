package com.apextech.realmbrowser;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apextech.realmbrowser.data.EmployeeTable;
import com.dd.realmbrowser.RealmBrowser;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

public class MainActivity extends AppCompatActivity {

    public static final String REALM_FILE_NAME = "default.realm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmBrowser.showRealmFilesNotification(this);
    }

    public void onSwapDB(View view)
    {
        copyDatabaseFromDirectory();
    }

    public void reInit(View view)
    {
        try {
            RealmConfiguration config = new RealmConfiguration.Builder(this)
                    .name(REALM_FILE_NAME)
                    .build();
            Realm realm = Realm.getInstance(config);

            Set<Class<? extends RealmObject>> schemaClasses = realm.getConfiguration().getRealmObjectClasses();
            List list = new ArrayList(schemaClasses);

            RealmBrowser.getInstance().addRealmModels(list);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    public void addToDB(View view)
    {

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .build();
        Realm realm = Realm.getInstance(config);

        final EmployeeTable emp=new EmployeeTable();
        emp.setAddress("A");
        emp.setFirst_name("ASD");
        emp.setLast_name("WER");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(emp);
            }
        });

        realm.close();

       /*RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(REALM_FILE_NAME)
                .build();
        Realm realm = Realm.getInstance(config);

        final ArrayList<Book> books = new ArrayList<>();
        Book book = new Book();
        for(int i=1;i<=100;i++)
        {
            book=new Book();
            book.setId(i);
            book.setDescription("DES "+i);
            book.setAuthor("Reto Meier "+i);
            book.setTitle("Android 4 Application Development "+i);
            book.setImageUrl("http://api.androidhive.info/images/realm/1.png");
            books.add(book);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(books);
            }
        });

        Log.d("WriteInfo","Done");
        Log.d("WriteInfo","**********************************");

        RealmResults<Book> allData=realm.where(Book.class).findAll();
        for(Book b:allData)
        {
            Log.d("WriteInfo","ID: "+b.getId());
            Log.d("WriteInfo","ID: "+b.getAuthor());
            Log.d("WriteInfo","ID: "+b.getDescription());
            Log.d("WriteInfo","ID: "+b.getImageUrl());
            Log.d("WriteInfo","ID: "+b.getTitle());

            Log.d("WriteInfo","_______________________________");
        }


        Set<Class<? extends RealmObject>> schemaClasses=realm.getConfiguration().getRealmObjectClasses();
        List list = new ArrayList(schemaClasses);

        RealmBrowser.getInstance().addRealmModels(list);

        realm.close();
*/

    }

    //Data submission
    String D_Path;
    String S_Path;
    String DATABASE_NAME;

    File source_path;
    File destination_path;

    public void copyDatabaseFromDirectory()
    {
        DATABASE_NAME=REALM_FILE_NAME;

        D_Path="/data/data/com.apextech.realmbrowser/files/"+DATABASE_NAME;
        S_Path= Environment.getExternalStorageDirectory()+"/RealmDB/"+DATABASE_NAME;

        source_path=new File(S_Path);
        destination_path=new File(D_Path);

        try {
            File dbFile = new  File(S_Path);
            if(dbFile.exists())
            {
                FileUtils.copyFile(source_path, destination_path);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
            else
            {
                FileUtils.copyFile(source_path, destination_path);
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();

            Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void readFromDB(View view)
    {
        startRealmFilesActivity();
    }

    private void startRealmFilesActivity() {
        RealmBrowser.startRealmFilesActivity(this);
    }

    private void startRealmModelsActivity() {
        RealmBrowser.startRealmModelsActivity(this, REALM_FILE_NAME);
    }

}
