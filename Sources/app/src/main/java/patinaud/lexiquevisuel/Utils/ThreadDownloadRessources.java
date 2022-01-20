package patinaud.lexiquevisuel.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Tools;

import static patinaud.lexiquevisuel.Utils.Properties.getAppsDirectory;

public class ThreadDownloadRessources extends Thread {
    protected Context contextLauncher;

    public ThreadDownloadRessources( Context cont)
    {
        contextLauncher = cont;
    }

    public void run() {
        //gestion des préférences
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(contextLauncher);

        long currentTmps = System.currentTimeMillis();
        Log.e("ThreadDownloadRessources" , "current timestamp : " + currentTmps);


        long lastTmps = app_preferences.getLong("TIME_LAST_CHECK_DL" , 0);

        //effectue un check toutes les 5 minutes
        if ( lastTmps + 5 * 60 * 1000 > currentTmps)
        {
            Log.e("ThreadDownloadRessources" , "Exit : Le precedent telechargement a eu lieu il y a moins de 5 minutes");
            return;
        }

        //sauvegarde le timestamp du nouveau check
        SharedPreferences.Editor editorTmp = app_preferences.edit();
        editorTmp.putLong("TIME_LAST_CHECK_DL"  , currentTmps);
        editorTmp.commit();


        //si l'appareil n'est pas connecté à internet on ne poursuit pas l'opération
        if( ! Tools.isInternetAvailable()) {
            Log.e("ThreadDownloadRessources" , "internet n'est pas disponnible");
            return ;
        }

            String md5RessourcesServeur = URLReader.recupererContenuPage(Properties.getURL_MD5_Ressources_serveur());
            String md5Ressourceslocal = app_preferences.getString("MD5_PREVIOUS_RESSOURCE_FILE", "XXX");

            Log.e("ThreadDownloadRessources" , "md5RessourcesServeur :" + md5RessourcesServeur + ":");
            Log.e("ThreadDownloadRessources" , "md5Ressourceslocal :" + md5Ressourceslocal + ":");

            //si le fichiers de ressources sont différents entre le local et le serveur, engage une procédure de mise à jour OU que le repertoire des ressources n'existe pas (plus)
            if ( (md5RessourcesServeur != null && ! md5RessourcesServeur.equalsIgnoreCase("") && ! md5RessourcesServeur.equalsIgnoreCase(md5Ressourceslocal)) ) {

                Log.e("ThreadDownloadRessources" , "Lancement du téléchargement");

                //telecharge les ressources
                String zipRessource = Tools.telechargerRessources(contextLauncher);

                Log.e("ThreadDownloadRessources" , "Le telechargement est terminé");

                // Si le fichier devant etre téléchargé ne l'est pas on stop l'execution du script, et on log l'anomalie
                if (!(new File(zipRessource)).exists()) {
                    Log.e("ThreadDownloadRessources", "Le fichier : " + zipRessource + " n'a pas pu etre téléchargé");
                    return;
                }

                Log.e("ThreadDownloadRessources" , "Repertoire final : " + Properties.getAppsDirectory(contextLauncher));

                //dézzipe le fichier téléchargé dans le repertoire temporaire
                Tools.unzip(zipRessource, Properties.getRessourcesDirectoryTemp(contextLauncher));

                //Si le repertoire est valide, on le renomme avec le nom du repertoire final
                if ( Tools.valideDirectoryRessource(Properties.getRessourcesDirectoryTemp(contextLauncher))) {

                    Tools.deleteRecursive(new File(Properties.getRessourcesDirectory(contextLauncher)));
                    (new File((Properties.getRessourcesDirectoryTemp(contextLauncher)))).renameTo(new File(Properties.getRessourcesDirectory(contextLauncher)));

                    // Si toutes les étapes sont validées, enregistre le hash du nouveau fichier ressource
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("MD5_PREVIOUS_RESSOURCE_FILE", Tools.getFileChecksum(new File(zipRessource)));
                    editor.commit();
                }
            }

    }


}
