package patinaud.lexiquevisuel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.MotView;
import patinaud.lexiquevisuel.View.MotsCategView;

public class MotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Affiche l'IHM
        super.onCreate(savedInstanceState);

        Intent intentIn = getIntent();
        Bundle extras = intentIn.getExtras();

        String mot = "";
        String categorie = "";
        if (extras != null) {
            if (extras.containsKey("MOT")) {
                mot = extras.getString("MOT");
            }
            if (extras.containsKey("CATEGORIE")) {
                categorie = extras.getString("CATEGORIE");
            }
        }

        Log.e("MotActivity", "mot passé en paramètre :" + mot + ":");

        MotView IHM = new MotView(this, categorie, mot);
        setContentView(IHM);

    }
}