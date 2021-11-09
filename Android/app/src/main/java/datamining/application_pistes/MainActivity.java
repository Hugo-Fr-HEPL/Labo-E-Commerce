package datamining.application_pistes;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectButton(null);
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
        if(timer < 5) {
            ((TextView)findViewById(R.id.ConnectButton)).setText("@string/connected");
            ((TextView)findViewById(R.id.ConnectButton)).setTextColor(Color.GREEN);
            findViewById(R.id.ConnectButton).setClickable(false);
        }

/*
        // Envoie de la requête
        try {
            ObjectOutputStream oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e) {
            System.err.println("Error network ? [" + e.getMessage() + "]");
        }

        // Lecture de la réponse
        ReponseSUM rep = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseSUM)ois.readObject();
            if(rep.getCode()==201)
                System.out.println(" *** Answer : Connection ok");
            else
                System.out.println(" *** Answer : Connection failed");
        }
        catch (ClassNotFoundException e) {
            System.out.println("--- Error with class = " + e.getMessage());
        } catch (IOException e) {
            System.out.println("--- Error IO = " + e.getMessage());
        }
 */
    }

    public void SearchClick(View v) {
        UpdateText("fr");
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