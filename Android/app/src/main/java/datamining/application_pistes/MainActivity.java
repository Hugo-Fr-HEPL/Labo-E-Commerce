package datamining.application_pistes;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Thread thread;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        list = findViewById(R.id.BagsList);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, arrayList);
        list.setAdapter(adapter);

        ConnectButton(null);

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                v.setChecked(!v.isChecked());

                // Check if every box are checked -> Return back (where ?)
                //if(list.isItemChecked(position)) System.out.println("OUI");
            }
        });
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
            //findViewById(R.id.ConnectButton).setClickable(false);
            findViewById(R.id.SearchButton).setClickable(true);
        }
    }

    public void SearchClick(View v) {
        if(((Connection)thread).getSocket() != null) {
            ((Connection)thread).SendMsg("//"+ RequeteSUM.CONNEXION_ANDROID +"#"+ ((EditText)findViewById(R.id.IdFlight)).getText() +"$");

            String[] msg = ((Connection)thread).GetMsg();
            if(Integer.parseInt(msg[0]) == ReponseSUM.CONNECTION_OK) {
                ArrayList<Integer> bags = new ArrayList<>();
                for(int i = 0, j = 0; i != -1; j = i+1) {
                    i = msg[1].indexOf("%", j);
                    if(i != -1)
                        bags.add(Integer.parseInt(msg[1].substring(j, i)));
                    else
                        bags.add(Integer.parseInt(msg[1].substring(j)));
                }

                adapter.clear();
                for(int i = 0; i < bags.size(); i++)
                    adapter.add(bags.get(i).toString());
            }
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
        int[] id = {R.id.ConnectButton, R.id.IdFlight, R.id.SearchButton, R.id.BagText};

        //TextView tmp = ((TextView)findViewById(id[0]));

        for (int i = 0; i < id.length; i++) {
            text.add(((TextView) findViewById(id[i])));

            //TextView tmp = ((TextView)findViewById(id[i]));
            //tmp.setText(res.getString(getResources().getIdentifier(tmp.getText().toString(), "string", getPackageName())));

            //tmp.setText(getString(getResources().getIdentifier(tmp.getText().toString(), "string", getPackageName())));
        }

        text.get(0).setText(res.getString(R.string.connected));
        text.get(1).setHint(res.getString(R.string.id_flight));
        text.get(2).setText(res.getString(R.string.search));
        text.get(3).setText(res.getString(R.string.bags_list));
    }
}