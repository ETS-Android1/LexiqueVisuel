package patinaud.lexiquevisuel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import patinaud.lexiquevisuel.View.MotsCategView;

public class MotsCategActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Affiche l'IHM
        super.onCreate(savedInstanceState);

        Intent intentIn = getIntent();
        Bundle extras = intentIn.getExtras();

        String categorie = "";
        if (extras != null) {
            if (extras.containsKey("CATEGORIE")) {
                categorie = extras.getString("CATEGORIE");
            }
        }

        Log.e("MotsCategActivity", "Categorie passée en paramètre :" + categorie + ":");

        MotsCategView IHM = new MotsCategView(this, categorie);
        setContentView(IHM);



    }
}
