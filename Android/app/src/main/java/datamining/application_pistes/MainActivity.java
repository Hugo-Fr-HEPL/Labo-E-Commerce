package datamining.application_pistes;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v7.app.ActionBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        ConnectButton(null);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
*/

    public void ConnectButton(View v) {
        thread = new Connection();
        thread.start();
        int timer;
        for(timer = 0; timer < 25; timer++) {
            if(((Connection)thread).getSocket() != null)
                break;
            else {
                try {
                    thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(timer < 25) {
            Resources res = (LangManager.setLocale(MainActivity.this, "en")).getResources();
            ((TextView)findViewById(R.id.ConnectButton)).setText(res.getString(R.string.connected));
            ((TextView)findViewById(R.id.ConnectButton)).setTextColor(Color.GREEN);
            //findViewById(R.id.ConnectButton).setClickable(false);
            findViewById(R.id.SearchButton).setClickable(true);
        }
    }

    public void SearchClick(View v) {
        UpdateText("fr");

        if(((Connection)thread).getSocket() != null) {
            ((Connection)thread).SendMsg("//-"+ RequeteSUM.CONNEXION_ANDROID +"#-"+ ((EditText)findViewById(R.id.IdFlight)).getText() +"$");
            //((Connection)thread).SendMsg("--" + (new RequeteSUM(RequeteSUM.CONNEXION_RSERVE).toString()) + "$");

            String[] msg = ((Connection)thread).GetMsg();
            if(Integer.parseInt(msg[0]) == ReponseSUM.CONNECTION_OK) {
                // Display list
            }
        }
    }
    
    public void UpdateText(String lang) {
        Resources res = (LangManager.setLocale(MainActivity.this,lang)).getResources();

        ArrayList<TextView> text = new ArrayList<>();
        int[] id = {R.id.ConnectButton, R.id.IdFlight, R.id.SearchButton};

        //TextView tmp = ((TextView)findViewById(id[0]));

        for(int i = 0; i < id.length; i++) {
            text.add(((TextView)findViewById(id[i])));

            //TextView tmp = ((TextView)findViewById(id[i]));
            //tmp.setText(res.getString(getResources().getIdentifier(tmp.getText().toString(), "string", getPackageName())));

            //tmp.setText(getString(getResources().getIdentifier(tmp.getText().toString(), "string", getPackageName())));
        }

        text.get(0).setText(res.getString(R.string.connected));
        text.get(1).setText(res.getString(R.string.id_flight));
        text.get(2).setText(res.getString(R.string.search));
    }
}