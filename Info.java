package edu.unitbv.ratbv;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import jxl.Sheet;
import jxl.Workbook;

public class Info extends AppCompatActivity {
    private TextView markertext, info;
    private LinearLayout LL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        getSupportActionBar().setTitle("R.A.T Brașov");getSupportActionBar().hide();
        markertext = (TextView) findViewById(R.id.marker);
        String title = getIntent().getStringExtra("title");
        markertext.setText(title);//title este numele statie luat din Arraylist-ul Nume_Statii, din MapsActivity.java
        Calendar c = Calendar.getInstance();
        LL = (LinearLayout) findViewById(R.id.LINLAY);
        if (c.get(Calendar.DAY_OF_WEEK) <= 6 && c.get(Calendar.DAY_OF_WEEK) >= 2) itinerarLV(title);
        else itinerarSD(title);
    }



    public void itinerarLV(String title) {
        try {
            int i, j, JI, fin, a;
            AssetManager am = getAssets();
            InputStream is = am.open("Itinerar(L-V).xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows(), col = s.getColumns();
            String afis = "", ore = "", sep = " - ", afis2;

            //s.getCell(j, 0).getContents() -> Stringul cu Linii
            //s.getCell(j + 1, i).getContents() -> Stringul cu ore
            for (j = 0; j < col; j = j + 2) {
                for (i = 0; i < row; i++) {
                    if (title.equals(s.getCell(j, i).getContents())) {
                        afis += s.getCell(j, 0).getContents() + ":\n";//afisare de ex Linia 1:\n
                        afis2="";
                        ore = s.getCell(j + 1, i).getContents();
                        JI = 0;
                        fin = ore.length();
                        while (JI != fin) { //are rolul sa ordoneze dupa ore
                            a = ore.substring(JI, fin).indexOf(sep)+JI;
                            if (ore.substring(JI,fin).indexOf(sep) == -1) {
                                afis2 += ore.substring(JI,fin) + "\n";
                                break;
                            }
                            afis2 += ore.substring(JI,a) + "\n";
                            JI = a + 3;
                        }afis += afis2; //afisam orele liniei respective
                    }
                  //  afis += "\n";
                }
                info = (TextView) findViewById(R.id.infor);
                info.setText(afis);
            }
        } catch (Exception e) {
            info.setText("Eroare de citire in tabel.");
        }
    }


    public void itinerarSD(String title) {
        try {
            int i, j, JI, fin, a;
            AssetManager am = getAssets();
            InputStream is = am.open("Itinerar(S-D).xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows(), col = s.getColumns();
            String afis = "", ore = "", sep = " - ", afis2;
            //s.getCell(j, 0).getContents() -> Stringul cu Linii
            //s.getCell(j + 1, i).getContents() -> Stringul cu ore
            for (j = 0; j < col; j = j + 2) {
                for (i = 0; i < row; i++) {
                    if (title.equals(s.getCell(j, i).getContents())) {
                        afis += s.getCell(j, 0).getContents() + ":\n";//afisare de ex Linia 1:\n
                        afis2="";
                        ore = s.getCell(j + 1, i).getContents();
                        JI = 0;
                        fin = ore.length();
                        while (JI != fin) { //are rolul sa ordoneze dupa ore
                            a = ore.substring(JI, fin).indexOf(sep)+JI;
                            if (ore.substring(JI,fin).indexOf(sep) == -1) {
                                afis2 += ore.substring(JI,fin) + "\n";
                                break;
                            }
                            afis2 += ore.substring(JI,a) + "\n";
                            JI = a + 3;
                        }afis += afis2; //afisam orele liniei respective
                    }
                    //  afis += "\n";
                }
                info = (TextView) findViewById(R.id.infor);
                info.setText(afis);
            }
        } catch (Exception e) {
            info.setText("Eroare de citire in tabel.");
        }
    }
}
