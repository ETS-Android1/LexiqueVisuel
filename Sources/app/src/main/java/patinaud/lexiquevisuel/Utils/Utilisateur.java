package patinaud.lexiquevisuel.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Utilisateur {

    public static int getNiveauDifficulte(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("USER_LEXIQUE_VISUEL" , Context.MODE_PRIVATE);
        int diff = sharedPref.getInt("NIVEAU_DIFFICULTE", 1);
        return diff;
    }

    public static void saveDifficulty(int difficulty, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("USER_LEXIQUE_VISUEL" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NIVEAU_DIFFICULTE", difficulty);
        editor.apply();
    }
}
