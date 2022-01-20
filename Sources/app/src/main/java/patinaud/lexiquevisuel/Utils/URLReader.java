package patinaud.lexiquevisuel.Utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;


public class URLReader {

    public static String recupererContenuPage(String URL_de_la_page)  {
        Log.e("get URL" , URL_de_la_page );
        return recupererContenuPageAvecGestionErreur( URL_de_la_page );
    }



    private static String recupererContenuPageAvecGestionErreur(String URL_de_la_page)  {

        //Gere la coherence de la connexion (validitee des donnees)
        String result = LirePage ( URL_de_la_page);

        int nmbTour = 0;
        int limite_test = 10;

        while (result.isEmpty() && nmbTour <= limite_test) {
            result = LirePage(URL_de_la_page);

            if (result.isEmpty()) {
                nmbTour ++;
                try {    Thread.sleep(1000);     } catch (Exception e) {   }
            }
        }

        return result;
    }




    public static String LirePage (String URL_de_la_page) {

        //recupere les donnees sur le site web
        String content = "";

        try {

            URL oracle = new URL( URL_de_la_page);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                content = content + inputLine;
            }
            in.close();

        }catch(Exception e)  {}

        return content;
    }





    public static ArrayList<String> lirePageArray (String URL_de_la_page) {

        //recupere les donnees sur le site web
       ArrayList<String> content = new ArrayList<String>();

        int nmbTour = 0;
        int limite_test = 10;

        while (content.isEmpty() && nmbTour <= limite_test)
        {

            try {

                URL oracle = new URL( URL_de_la_page);
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader( yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    Log.i("INTERNET" , inputLine);
                    content.add( inputLine);
                }
                in.close();

            }catch(Exception e)  {
                e.printStackTrace();
                Log.e("TAG" , "Acces internet KO");
            }
            nmbTour++;
        }

        return content;
    }

}

