package com.infosys.nolantrn.wasabinfc;

import android.content.Intent;
import android.nfc.FormatException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.exceptions.InsufficientCapacityException;
import be.appfoundry.nfclibrary.exceptions.ReadOnlyTagException;
import be.appfoundry.nfclibrary.exceptions.TagNotPresentException;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncOperationCallback;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncUiCallback;
import be.appfoundry.nfclibrary.utilities.async.WriteCallbackNfcAsync;
import be.appfoundry.nfclibrary.utilities.async.WriteEmailNfcAsync;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcWriteUtility;

public class MainActivity extends NfcActivity implements AsyncUiCallback{

    String readData = "";
    String writeData = "";
    boolean checked = false;

    Switch writeSwitch;
    TextView readText;
    EditText writeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeSwitch = (Switch) findViewById(R.id.writeSwitch);
        readText = (TextView) findViewById(R.id.readText);
        writeText = (EditText) findViewById(R.id.editText);

        writeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = b;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.switch_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(!checked) {
            for (String message : getNfcMessages()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                readText.setText(message);
            }
        } else {
//            new WriteEmailNfcAsync(this,mAsyncOperationCallback).executeWriteOperation();
            new WriteCallbackNfcAsync(this,mAsyncOperationCallback).executeWriteOperation();
        }

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

    AsyncOperationCallback mAsyncOperationCallback = new AsyncOperationCallback() {

        @Override
        public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
//            return writeUtility.writeEmailToTagFromIntent("some@email.tld","Subject","Message",getIntent());
            return writeUtility.writeTextToTagFromIntent(writeText.getText().toString(),getIntent());
        }
    };

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        new WriteEmailNfcAsync(this,mAsyncOperationCallback).executeWriteOperation();
//    }
}
