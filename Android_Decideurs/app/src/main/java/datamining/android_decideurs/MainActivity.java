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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Thread thread;

    String lang = "en";

    private static double[] resultats = new double[] { 8, 23, 31, 17 };
    private static final String[] nomPartis = new String[] { "Ecolos naturistes","Grand ami de la N-VA", "Gauche pour la démocratie contrôlée", "Parti Papiste et Oecuménique" };

    private CategorySeries serieStat = new CategorySeries("Elections à DataMiningGulch");
    private DefaultRenderer rendererGlobal = new DefaultRenderer();
    private int[] couleurs = new int[] {Color.GREEN, Color.RED, Color.BLACK, Color.BLUE, Color.WHITE, Color.YELLOW, Color.GRAY, Color.MAGENTA, Color.CYAN, Color.DKGRAY, Color.LTGRAY};

    private GraphicalView vue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        ConnectButton(null);
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

    public void RegButton(View v) {
        if(((Connection)thread).getSocket() != null) {
            ((Connection)thread).SendMsg("//"+ RequeteSUM.REG_CORR_LUG_AND +"$");

            String[] msg = ((Connection)thread).GetMsg();
            if(Integer.parseInt(msg[0]) == ReponseSUM.STATISTIC_OK) {
                CategorySeries serieStat = new CategorySeries("Relation Poids/Zone Géographique");
                for(int i = 0; i < nomPartis.length; i++) {
                    serieStat.add(nomPartis[i] + "" + resultats[i],resultats[i]);
                    SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                    renderer.setColor(couleurs[(serieStat.getItemCount()-1)%couleurs.length]);
                    rendererGlobal.addSeriesRenderer(renderer);
                }

                vue = ChartFactory.getPieChartView(this,serieStat,rendererGlobal);
                LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

                layout.addView(vue,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            }
        }
    }

    public void AnovaButton(View v) {
        if(((Connection)thread).getSocket() != null) {
            ((Connection)thread).SendMsg("//"+ RequeteSUM.ANOVA_1_LUG_AND +"$");

            String[] names = null;
            Double[] datas = null;

            String[] msg = ((Connection)thread).GetMsg();

            if(Integer.parseInt(msg[0]) == ReponseSUM.STATISTIC_OK) {
                for(int k = 1; k < 3; k++) {
                    ArrayList<String> tmp = new ArrayList<>();
                    for (int i = 0, j = 0; i != -1; j = i + 1) {
                        i = msg[k].indexOf(",", j);
                        if (i != -1) {
                            tmp.add(msg[k].substring(j, i));
                        } else {
                            tmp.add(msg[k].substring(j));
                        }
                    }

                    if(k == 1) {
                        datas = new Double[tmp.size()];
                        for(int i = 0; i < tmp.size(); i++)
                            datas[i] = Double.parseDouble(tmp.get(i));
                    } else {
                        names = new String[tmp.size()];
                        for(int i = 0; i < tmp.size(); i++)
                            names[i] = tmp.get(i);
                    }
                }

                Resources res = (LangManager.setLocale(this, lang)).getResources();
                CategorySeries serieStat = new CategorySeries(res.getString(R.string.anova_text));
                for(int i = 0; i < names.length - 1; i++) {
                    serieStat.add(names[i] + "" + datas[i], datas[i]);
                    SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                    renderer.setColor(couleurs[(serieStat.getItemCount()-1)%couleurs.length]);
                    rendererGlobal.addSeriesRenderer(renderer);
                }

                vue = ChartFactory.getPieChartView(this,serieStat,rendererGlobal);
                LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

                layout.addView(vue,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.en :
                lang = "en";
                break;
            case R.id.fr :
                lang = "fr";
                break;
            case R.id.es :
                lang = "es";
                break;
        }
        UpdateText();
        return true;
    }
    public void UpdateText() {
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