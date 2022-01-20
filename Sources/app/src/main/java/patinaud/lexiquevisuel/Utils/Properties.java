package patinaud.lexiquevisuel.Utils;

import android.content.Context;

import patinaud.lexiquevisuel.MenuActivity;

public class Properties {

    //************************************** RESSOURCES *********************************************************

    public static String getURLRessources()
    {
        return "https://capucindore.org/APPLIS/RESSOURCES.zip";
    }
    public static String getURL_MD5_Ressources_serveur()
    {
        return "https://capucindore.org/APPLIS/LEXIQUE_MD5.php";
    }


    //**************************************** REPERTOIRES *************************************************************

    public static String getAppsDirectory(Context context)
    {
        return context.getFilesDir().getAbsolutePath() + "/LEXIQUE";
    }

    public static String getRessourcesDirectory(Context context)
    {
        return getAppsDirectory(context) + "RESSOURCES/";
    }

    public static String getRessourcesDirectoryTemp(Context context)
    {
        return getAppsDirectory(context) + "RESSOURCES_TEMP/";
    }

    public static String getMotsDirectory(Context context)
    {
        return getRessourcesDirectory(context) + "MOTS/";
    }


    public static String getMotDirectory(String mot, Context context)
    {
        return getRessourcesDirectory(context) + "MOTS/" + mot + "/";
    }

    public static String getCategoriesDirectory(Context context)
    {
        return getRessourcesDirectory(context) + "CATEGORIES/";
    }


    public static String getCategoriesMotFile(String mot , Context context)
    {
        return getMotDirectory( mot , context) + "CATEGORIES.CSV";
    }

    public static String getDifficulteFile(String mot , Context context)
    {
        return getMotDirectory( mot , context) + "DIFFICULTE.CSV";
    }



    //recupère le reprtoire d'images du mot passé en paramètre
    public static String getImageMotDirrectory(String mot , Context context)
    {
        return getMotDirectory(mot , context) + "IMAGES/";
    }
    //recupère le reprtoire de son du mot passé en paramètre
    public static String getSonMotDirrectory(String mot , Context context)
    {
        return getMotDirectory(mot , context) + "SON/";
    }

    public static String getMotsLiesFile (String mot , Context context)
    {
        return getMotDirectory(mot , context) + "MOT_LIE.CSV";
    }


    // ******************************************************* GRAPHIQUE ***************************************************
    public static int getHautHeader()
    {
        return 120;
    }
}
