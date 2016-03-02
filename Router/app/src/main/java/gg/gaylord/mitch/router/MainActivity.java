package gg.gaylord.mitch.router;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import gg.gaylord.mitch.network.LL1Daemon;
import gg.gaylord.mitch.network.LL2P;
import gg.gaylord.mitch.support.Factory;
import gg.gaylord.mitch.support.LabException;
import gg.gaylord.mitch.support.NetworkConstants;
import gg.gaylord.mitch.support.UIManager;

public class MainActivity extends AppCompatActivity {

    Factory myFactory;
    UIManager uiManager;
    LL2P myLL2P;
    LL1Daemon myll1Daemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myFactory = new Factory(this);
        uiManager = myFactory.getUiManager();
        myll1Daemon = myFactory.getLl1Daemon();

        myLL2P = new LL2P("478201", "253103", "8001", "Hello World");

        uiManager.updateLL2PDisplay(myLL2P);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id==R.id.showIPAdressMenuItem){
            uiManager.raiseToast(NetworkConstants.IP_ADDRESS);
        } else if (id==R.id.showLL2PSendFrameMenuItem){
            try{
                myll1Daemon.sendLL2PFrame();
            } catch (LabException e){

            }
        }

        return super.onOptionsItemSelected(item);
    }

}
