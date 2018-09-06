package net.fgghjjkll.wasabinfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import be.appfoundry.nfclibrary.exceptions.InsufficientCapacityException;
import be.appfoundry.nfclibrary.exceptions.ReadOnlyTagException;
import be.appfoundry.nfclibrary.exceptions.TagNotPresentException;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncOperationCallback;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncUiCallback;
import be.appfoundry.nfclibrary.utilities.async.WriteEmailNfcAsync;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcWriteUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;

public class MainActivity extends AppCompatActivity implements AsyncUiCallback {

    private DrawerLayout mDrawerLayout;

    //NFC lib variables
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    private NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NFC setup
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View view, float v) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View view) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View view) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int i) {
                        // Respond when the drawer motion state changes
                    }
                }
        );


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        int itemId = menuItem.getItemId();
                        displayView(itemId);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }

//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        // set item as selected to persist highlight
//                        menuItem.setChecked(true);
//                        // close drawer when item is tapped
//                        mDrawerLayout.closeDrawers();
//
//                        // Add code here to update the UI based on the item selected
//                        // For example, swap UI fragments here
//
//                        return true;
//                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Fragment fragment = null;

    public void displayView(int viewId) {
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_readwrite:
                fragment = new RWFragment();
                title = "Read/Write";

                break;

            case R.id.nav_events:
                fragment = new EventsFragment();
                title = "Events";

                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mIntentFilters, mTechLists);
        }
    }

    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    //    @Override
//    protected void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
//
//        if(fragment instanceof RWFragment){
//            RWFragment my = (RWFragment) fragment;
//            my.read_nfc(intent);
//
//        }
//    }
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);

        if(fragment instanceof  RWFragment){
            final RWFragment my = (RWFragment) fragment;

            Log.d("onNewIntent",my.getRW() +  "is checked?");
            if(my.getRW() == false){ // Read is checked
                SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
                for (int i = 0; i < res.size(); i++) {
//                Toast.makeText(this, res.valueAt(i), Toast.LENGTH_SHORT).show();
                    my.setText(res.valueAt(i));
                }
            } else { // Write is checked
                Log.d("new intent","write operation");
                AsyncOperationCallback mAsyncOperationCallback = new AsyncOperationCallback() {
                    @Override
                    public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                        return writeUtility.writeTextToTagFromIntent(my.getText(),intent);
                    }
                };
                new WriteEmailNfcAsync(this,mAsyncOperationCallback).executeWriteOperation();
            }

        } else if(fragment instanceof EventsFragment){
            final EventsFragment my = (EventsFragment) fragment;

            SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
            for (int i = 0; i < res.size(); i++) {
//                Toast.makeText(this, res.valueAt(i), Toast.LENGTH_SHORT).show();
//                my.setText(res.valueAt(i));
            }
        }
//        SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
//        for (int i = 0; i < res.size(); i++) {
//            Toast.makeText(this, res.valueAt(i), Toast.LENGTH_SHORT).show();
//            res.valueAt(i).
//        }
    }

    @Override
    public void callbackWithReturnValue(Boolean result) {
        String message = result ? "Success" : "Failed!";
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(Boolean... booleans) {
        Toast.makeText(this, booleans[0] ? "We started writing" : "We could not write!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}