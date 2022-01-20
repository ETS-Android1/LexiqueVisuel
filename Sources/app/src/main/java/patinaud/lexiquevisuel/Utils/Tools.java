package patinaud.lexiquevisuel.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import patinaud.lexiquevisuel.R;

public class Tools {

    // _____________________________ TELECHARGEMENT __________________________________________________________________________________

    // retourne l'emplacement ou a été telechargé le zip des ressources
    public static String telechargerRessources(Context cont)
    {
        //si le fichier de ressource existe déjà on l'efface
        File oldRessources = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Ressources.zip");
        if (oldRessources.exists())
        {
            oldRessources.delete();
        }


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Properties.getURLRessources()));
        request.setDescription("Mises à jour pour le lexique visuel");
        request.setTitle("Mises à jour pour le lexique visuel");


        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Ressources.zip");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) cont.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


        //attend d'avoir telechargé le zip (technique basée sur le hash du fichier)
        String zipRessource = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Ressources.zip";
        String oldMD5File = "";

        // Laisse le temps d'initer le chargement
        Log.e("Tools.telechargerRessources()", "Debut du telechargement");


        do {
            oldMD5File = Tools.getFileChecksum((new File(zipRessource)));
            Log.e("Tools.telechargerRessources()", "MD5 file : " + oldMD5File + "");
            try {   Thread.sleep(10 * 1000);         } catch (Exception e) {        }
        } while (!oldMD5File.equalsIgnoreCase(Tools.getFileChecksum((new File(zipRessource)))));



        Log.e("Tools.telechargerRessources()" , "Fichier zip telecharge : " + zipRessource );
        return zipRessource;
    }







    public static void unzip(String zipFile, String location)
    {
        File oldRessources = new File(location);
        if (oldRessources.exists())
        {
            deleteRecursive(oldRessources);
        }

        try
        {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin, Charset.forName("CP1252"));


            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null)
            {

                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory())
                {
                    dirChecker(location, ze.getName());
                }
                else
                {
                    File f = (new File(location + ze.getName()));
                    String canonicalPath = f.getCanonicalPath();
                    if (!canonicalPath.startsWith(location)) {

                        f.getParentFile().mkdirs();
                        FileOutputStream fout = new FileOutputStream(location + ze.getName());

                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, len);
                        }
                        fout.close();

                        zin.closeEntry();
                    }
                }

            }
            zin.close();
        }
        catch(Exception e)
        {
            Log.e("Decompress", "unzip", e);
        }

    }

    public static void dirChecker(String location,String dir)
    {
        File f = new File(location + dir);
        if(!f.isDirectory())
        {
            f.mkdirs();
        }
    }



    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }




    public static String getFileChecksum( File file)
    {
        try {
        MessageDigest digest = MessageDigest.getInstance("MD5");


        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Tools.getFileChecksum()", e.getMessage() + e.getLocalizedMessage() );
        }
        return "";
    }





    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }



//________________________________________ PERMISSION __________________________________________________________________________________________

    // Gère les demandes de permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyPermissions(Activity activity) {
        for (int i = 0 ; i < PERMISSIONS_STORAGE.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_STORAGE[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }


    public static boolean checkIfPermissionAreGiven(Activity activity)
    {
        for (int i = 0 ; i < PERMISSIONS_STORAGE.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_STORAGE[i]);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    // _____________________________________________________________________ GESTION FICHIERS RESSOURCES _______________________________________________________________________

    //recupère les mots correspondant à la catégorie passée en paramètre
    public static ArrayList<String> getListeMotCategorie(String categorie , Context context)
    {
        ArrayList<String> listeMots = new ArrayList<String>();

        //parcours tous les repertoires de mot
        String[] listDirMots = new File(Properties.getMotsDirectory(context)).list();

        for (int i = 0 ; i < listDirMots.length ; i++)
        {
           // Log.e( "TOOLS" , listDirMots[i]);
            String mot = listDirMots[i];

            //Pour retrouver ceux de la catégorie
            ArrayList<String> listCategMot_Al = getCategorieMot(mot , context);

            //Si le mot appartient à la catégorie passée en paramètre
            if ( ifMotInCategorie(mot , categorie , context ) ) {
                //On ajoute le mot à la liste des mots
                listeMots.add(mot);
            }
        }

        listeMots.sort(String::compareToIgnoreCase); //trie les mots par ordre alphebetique

        return listeMots;
    }



    //Test si le mot appartient à la catégorie passée en paramètre
    public static boolean ifMotInCategorie(String mot, String categorie , Context context)
    {
        ArrayList<String> categoriesDuMot = getCategorieMot(mot , context);

        for (int i = 0 ; i < categoriesDuMot.size(); i++)
        {
            if ( categoriesDuMot.get(i).equalsIgnoreCase(categorie))
            {
                return true;
            }
        }
        return false;
    }


    //recupère dans une arraylist la liste des catégories associées au mot passé en paramètre
    public static ArrayList<String> getCategorieMot(String mot , Context context)
    {
        return readCSVFile(Properties.getCategoriesMotFile(mot , context));
    }

    // recupère le niveau de difficulte du mot
    public static int getDifficulteMot(String mot , Context context)
    {

        //si le fichier de difficulte n'existe pas ou n'est pas alimente, on met le mot au niveau difficile ( 3 ) par defaut

        int diff = 3;
        ArrayList<String> diffAL = readCSVFile(Properties.getDifficulteFile(mot , context));

        if ( diffAL.size() >= 1 )
        {
            try {
                diff = Integer.valueOf(diffAL.get(1).replaceAll( ";" , ""));
            }catch(Exception e){
                Log.e( "TOOLS" , e.getMessage());
            }
        }
        return diff;
    }

    //Lit le fichier CSV passé en paramètre
    public static ArrayList<String> readCSVFile( String path)
    {
        ArrayList<String> lignes = new ArrayList<String>();
        File categFile = new File ( path);

        if ( categFile.exists())
        {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(categFile));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replace(";" , "");
                    lignes.add(line);

                }
                br.close();
            } catch (Exception e) { }
        }

        return lignes;
    }


    //True si le mot à au moins un fichier son
    public static boolean motHasASound(String mot , Context context)
    {
        File dirSon = new File ( Properties.getSonMotDirrectory(mot , context) );
        if ( dirSon.exists() && dirSon.list().length > 0)
        {
            return true;
        }
        return false;
    }

    //True si le fichier à au moins une image
    public static boolean motHasAImage(String mot , Context context)
    {
        File dirSon = new File ( Properties.getImageMotDirrectory(mot , context) );
        if ( dirSon.list().length > 0)
        {
            return true;
        }
        return false;
    }

    // retourne True si le mot dispose de tout les prérequis pour pouvoir etre affiché
    public static boolean motPeutEtreAffiche(String mot , Context context)
    {
        boolean motPeutEtreAffiche = true;

        // Le mot doit contenir au moins le son et une image
        if ( ! (motHasASound(mot , context) && motHasAImage(mot , context)) )
        {
            motPeutEtreAffiche = false;
        }

        // Si le niveau de difficulte du mot correspond à celui de l'utilisateur
        int difficulteMot = getDifficulteMot( mot , context);
        Log.v( "TOOLS" , "difficulteMot : " + mot + " : " + difficulteMot);

        if ( difficulteMot > Utilisateur.getNiveauDifficulte(context))
        {
            motPeutEtreAffiche = false;
        }

        return motPeutEtreAffiche;
    }


    // Retourne la liste des mots liés
    public static ArrayList<String> getMotsLies(String mot , Context context)
    {
        ArrayList<String> lst = readCSVFile( Properties.getMotsLiesFile(mot , context) );
        ArrayList<String> lstRes = new ArrayList<String>();

        for (int i = 0 ; i < lst.size(); i++)
        {
            if ( lst.get(i) != null &&  (new File( Properties.getMotDirectory(lst.get(i) , context) )).exists() && Tools.motPeutEtreAffiche(lst.get(i) , context))  //si le mot n'est pas null et que le repertoire existe et que le mot est bien formatté : On ajoute le mot à la liste
            {
                lstRes.add(lst.get(i));
            }
        }
        return lstRes;
    }



    // recupère le chemin complet de la premierre image associé au mot
    // retourne null si aucune image n'est présente
    public static String getFirstImageMot(String mot, Context context)
    {
        String result = null;
        File dirImageMot = new File(Properties.getImageMotDirrectory(mot, context));

        if ( dirImageMot.exists())
        {
            String[] imgsTab = dirImageMot.list();
            if (imgsTab.length > 0) {
                Arrays.sort(imgsTab); //trie par ordre alphabétique les images des mots
                result = Properties.getImageMotDirrectory(mot, context) + imgsTab[0];
            }
        }
        return result;
    }


    public static String getRandomWord(Context context)
    {
        String motRes = "";

        do {
            String[] listDirMots = new File(Properties.getMotsDirectory(context)).list();
            int rand = (int) (Math.random() * listDirMots.length);
            motRes = listDirMots[rand];
        } while ( ! motPeutEtreAffiche(motRes, context));

        return motRes ;
    }

    //Génère un mot aléatoire à l'exeption de ceux passés en paramètre et optionnellement des mots liés à ceux passés en paramètre
    public static String getRandomWordExcept(ArrayList<String> ALmotExclut, boolean authorizeLinkedWord,Context context) {
        String res = null;
        boolean wordValid = false;


        Log.e("getRandomWordExcept" , ALmotExclut.toString() );

        while (! wordValid)
        {
            res = getRandomWord(context);
            wordValid = true;

            Log.e("getRandomWordExcept" , "" + ALmotExclut.size() );

            for (int i = 0 ; i < ALmotExclut.size(); i++)
            {
                //enlève les parenthèse pour pouvoir comparer les omonymes
                String resShort = res.split("\\(")[0].trim();
                String motExclut = ALmotExclut.get(i);
                String motExclutShort = motExclut.split("\\(")[0].trim();

                //Teste la correspondance directe du mot
                if ( motExclutShort.equalsIgnoreCase(resShort))
                {
                    wordValid = false;
                }

                //si les mots liés ne sont pas authorisés
                if ( ! authorizeLinkedWord )
                {
                    ArrayList<String> ALMotsLies = Tools.getMotsLies(motExclut, context);

                    for (int j = 0 ; j < ALMotsLies.size(); j++)
                    {
                        String motLiesShort =  ALMotsLies.get(j).split("\\(")[0].trim();

                        if (motLiesShort.equalsIgnoreCase(resShort))
                        {
                            wordValid = false;
                        }
                    }
                }

                if ( ! motPeutEtreAffiche(res, context))
                {
                    wordValid = false;
                }
            }
        }

        return res;
    }


    public static boolean valideDirectoryRessource(String ressourcesDirectoryTemp) {
        boolean correct = true;
        File ress = new File(ressourcesDirectoryTemp);

        if ( ! ress.exists()) //le repertoire doit exister
        {
            correct = false;
            Log.e("Tools.valideDirectoryRessource()", "le repertoire " + ressourcesDirectoryTemp + " doit exister");
        }
        String [] contenu = ress.list();
        if ( contenu.length < 2) //le repertoire doit contenir au moins 2 éléments (repertoire catégories et mot)
        {
            correct = false;
            Log.e("Tools.valideDirectoryRessource()", "le repertoire " + ressourcesDirectoryTemp + " doit contenir au moins 2 éléments (repertoire catégories et mots)");
        }

        return correct;
    }


// ___________________________________________________________ INTERFACE UTILISATEUR _________________________________________________

    public static void playSound(String source)
    {

        Log.e("Tools", "Play : " + source);

        // La lecture du son doit se faire dans un Thread
        Thread lectureSon = new Thread() {
            @Override
            public void run() {
                try {
                    MediaPlayer mp = new MediaPlayer();
                    mp.setDataSource(source);
                    mp.prepare();
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();

                        };
                    });
                    Thread.sleep(20 * 1000);
                } catch (Exception e) {
                    Log.e("MotView", e.getMessage() + e.getLocalizedMessage());
                }
            }
        };
        lectureSon.start();
    }


    public static void playSoundWin(Context context)
    {
        playSound(context, R.raw.win);
    }


    public static void playSoundLose(Context context)
    {
        playSound(context, R.raw.lose);
    }


    public static void playSound(Context context, int source)
    {
        Log.e("Tools", "Play : " + source);

        // La lecture du son doit se faire dans un Thread
        Thread lectureSon = new Thread() {
            @Override
            public void run() {
                try {
                    MediaPlayer mp = MediaPlayer.create(context , source);
                    mp.start();
                    Thread.sleep(20 * 1000);
                } catch (Exception e) {
                    Log.e("MotView", e.getMessage() + e.getLocalizedMessage());
                }
            }
        };
        lectureSon.start();
    }



}
