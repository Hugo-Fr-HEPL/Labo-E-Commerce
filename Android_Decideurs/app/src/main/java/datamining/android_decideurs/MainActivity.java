package datamining.android_decideurs;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

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
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.en :
                UpdateText("en");
                break;
            case R.id.fr :
                UpdateText("fr");
                break;
            case R.id.es :
                UpdateText("es");
                break;
        }
        return true;
    }
    public void UpdateText(String lang) {
        Resources res = (LangManager.setLocale(this, lang)).getResources();

        ArrayList<TextView> text = new ArrayList<>();
        int[] id = {R.id.ConnectButton, R.id.AnovaButton, R.id.RegButton, R.id.MainText};

        for (int i = 0; i < id.length; i++) {
            text.add(((TextView) findViewById(id[i])));
        }

        text.get(0).setText(res.getString(R.string.connected));
        text.get(1).setText(res.getString(R.string.anova));
        text.get(2).setText(res.getString(R.string.reg));
        text.get(3).setText(res.getString(R.string.main_text));
    }
}