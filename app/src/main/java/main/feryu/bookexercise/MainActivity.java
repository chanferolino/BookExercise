package main.feryu.bookexercise;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import main.feryu.bookexercise.fragments.ListViewFragment;


public class MainActivity extends AppCompatActivity {
    private ListViewFragment mListViewFragment;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListViewFragment = ListViewFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mListViewFragment)
                .commit();
        manager = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(MainActivity.this,BookDetails.class);
                i.putExtra("ic",100);
                startActivity(i);
                
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mListViewFragment = ListViewFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(mListViewFragment)
                    .attach(mListViewFragment)
                    .commit();

        }
        if(id == R.id.action_search){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final Spinner spinner = (Spinner) findViewById(R.id.spnChoice);
            final View dialogView = inflater.inflate(R.layout.alertdialog, null);
            dialogBuilder.setTitle(R.string.searchby)
                    .setItems(R.array.choice, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userChoice = spinner.getSelectedItem().toString();

                            if(userChoice=="Genre"){
                                //code
                            }
                            if(userChoice=="Author"){
                                //code
                            }
                        }
                    });

            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do something with edt.getText().toString();
                    final EditText edt = (EditText) findViewById(R.id.edtChoice);
                    edt.getText().toString();
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();



//            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//            // Include dialog.xml file
//            dialog.setView(R.layout.alertdialog);
//            // Set dialog title
//            dialog.setTitle(R.string.searchby);
//
//            Spinner spinner = (Spinner) findViewById(R.id.spnChoice);
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                    R.array.choice, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//            spinner.setAdapter(adapter);
//
//






            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
