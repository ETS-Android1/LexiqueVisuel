package patinaud.lexiquevisuel.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import patinaud.lexiquevisuel.MotsCategActivity;
import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.ThreadDownloadRessources;
import patinaud.lexiquevisuel.Utils.Tools;
import patinaud.lexiquevisuel.ViewElement.BulleChargement;
import patinaud.lexiquevisuel.ViewElement.ButtonImage;
import patinaud.lexiquevisuel.ViewElement.Header;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.ViewElement.ButtonImageFloatHeight;

public class MenuView extends ParentScrollableView {

    private String [] listeCategories;
    private ArrayList<ButtonImageFloatHeight> btnCategorie;
    protected Canvas canvas;

    protected Header header;

    // variables de l'écran de chargement
    private ArrayList <BulleChargement> lstBulles = null;
    private int bulleOff = 0;
    int nmbPoint = 15;
    int newColorBulle = Color.rgb( 255,255,255 );
    int chronoChangeColor = 0;
    long lastMAJaffichage = 0;


    public MenuView(Activity activity) {
        super(activity);
        listeCategories = null;
        btnCategorie = new ArrayList<ButtonImageFloatHeight>();

        //Telecherge les mises à jours si les ressources ont étaient actualisées
        ThreadDownloadRessources downloadRessources = new ThreadDownloadRessources(this.getContext());
        downloadRessources.start();

    }



    @Override
     public void clickAction(MotionEvent event)
     {
         if ( ! header.isClicked(event.getX() , event.getY())) { // si le clique n'est pas sur le header*
             //regarde si il s'agit d'un clique sur un boutton
             for (int iBtn = 0; iBtn < btnCategorie.size(); iBtn++) {
                 if (btnCategorie.get(iBtn).isClicked(event.getX(), event.getY())) {
                     Log.e("MenuView", "Ouverture de la catégorie : " + btnCategorie.get(iBtn).getId());

                     Intent intent = new Intent(this.activity, MotsCategActivity.class);
                     intent.putExtra("CATEGORIE", btnCategorie.get(iBtn).getId());
                     this.activity.startActivity(intent);
                 }
             }
         }
    }




    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

            //Réinitialise les variables
            listeCategories = null;

            int nmbColonnes = (this.getWidth() > this.getHeight()) ? 3 : 2;
            int largeurColonne = this.getWidth() / nmbColonnes;

            //test les permissions pour savoir si l'utilisateur a le droit d'accèder aux images téléchargées
            if (Tools.checkIfPermissionAreGiven(activity)) {

                //_____________________________________ LIT LES FICHIERS BINAIRES UNIQUEMENT A L OUVERTURE DE LA PAGE_________________________________________________
                if ( btnCategorie.isEmpty() ) {
                    File dirCateg = new File(Properties.getCategoriesDirectory(this.getContext()));

                    //verifie que le repertoire existe
                    if (dirCateg.exists()) {

                        //recupère la liste des catégories par ordre alphabétique
                        listeCategories = dirCateg.list();
                        if ( listeCategories != null) {
                            Arrays.sort(listeCategories);


                            for (int itDirCate = 0; itDirCate < listeCategories.length; itDirCate++) {
                                String categ = listeCategories[itDirCate];
                                Log.e("view", categ);

                                File fCateg = new File(Properties.getCategoriesDirectory(this.getContext()) + categ + "/IMAGES/");

                                String[] lisTriee = fCateg.list();
                                if (lisTriee.length > 0) {
                                    Arrays.sort(lisTriee); //trie la liste des fichier par ordre alphabétique, ce qui permet de choisir l'image à afficher directment dans les essources
                                    String fileCategPath = Properties.getCategoriesDirectory(this.getContext()) + categ + "/IMAGES/" + lisTriee[0];

                                    if (new File(fileCategPath).exists()) {
                                        ButtonImageFloatHeight cateBtn = new ButtonImageFloatHeight(fileCategPath, 0, 0, largeurColonne, canvas, categ);
                                        btnCategorie.add(cateBtn);
                                    }
                                }
                            }

                        }
                    }
                }

                //_________________________ SI IL NY A PAS D IMAGE, AFFICHE UN MESSAGE DE CHARGEMENT ____________________________
                if ( btnCategorie.isEmpty() )
                {

                        if (lstBulles == null) { // initie la liste des bulles pour la premierre fois
                            lstBulles = new ArrayList<BulleChargement>();

                            int xCentre = this.getWidth() / 2;
                            int yCentre = this.getHeight() / 2;

                            // Par defaut le rayon du cercle correspond à un quart de la largeur, sauf si la hauteur de l'écran est inférieur à la largeur, dans ce cas on inverse
                            int rayon = this.getWidth() / 5;
                            if (this.getWidth() > this.getHeight()) {
                                rayon = this.getHeight() / 5;
                            }

                            for (int i = 0; i < nmbPoint; i++) {
                                double ratio = (double) i / (double) nmbPoint * Math.PI * 2;
                                lstBulles.add(new BulleChargement((int) (xCentre + rayon * Math.cos(ratio)), (int) (yCentre + rayon * Math.sin(ratio)), 0));

                            }

                        }

                        for (int i = 0; i < lstBulles.size(); i++) { // affiche les bulles
                            if (i != bulleOff) {
                                Paint p = new Paint();
                                p.setColor(lstBulles.get(i).getColor());
                                canvas.drawOval(lstBulles.get(i).getX(), lstBulles.get(i).getY(), lstBulles.get(i).getX() + lstBulles.get(i).getLarg(), lstBulles.get(i).getY() + lstBulles.get(i).getLarg(), p);

                            } else {
                                lstBulles.get(i).setColor(newColorBulle);
                                lstBulles.get(i).setLargeur(90);
                            }
                        }

                        // la variable bulle off definit la blle qui ne s'affiche pas
                    if (System.currentTimeMillis() - lastMAJaffichage > 100 ) // efinit la vitesse de rafraichissement de l'écran
                    {
                        bulleOff++;
                        if (bulleOff == nmbPoint) {
                            bulleOff = 0;
                        }

                        if ( chronoChangeColor == 0 )
                        {
                            newColorBulle = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                            chronoChangeColor = (int) (Math.random() * 60) + 20;
                        }
                        chronoChangeColor --;

                        for (int i = 0; i < lstBulles.size(); i++) {
                            lstBulles.get(i).retrecir(5);
                        }

                        lastMAJaffichage = System.currentTimeMillis();
                    }


                    // rafraichit à interval regulièrre l'écran tant qu'aucune image n'est disponnible continue à animer la page de chargement
                    postDelayed(new Runnable() {
                        public void run() {
                            invalidate();
                        }
                    }, 10);
                }



                //____________________ SINON DESSINE LES BOUTONS ________________________________________________
                else {

                    //dessine le fond en blanc
                    Paint p = new Paint();
                    p.setColor(Color.WHITE);
                    canvas.drawPaint(p);


                    ArrayList<Integer> taillesColonnes = new ArrayList<Integer>();

                    //initialise les tailles des colonnes à 0
                    for (int itInitCol = 0; itInitCol < nmbColonnes; itInitCol++)
                        taillesColonnes.add(Properties.getHautHeader());


                    //parcours chaque bouton pour le dessiner
                    for (int itBtn = 0; itBtn < btnCategorie.size(); itBtn++) {
                        //trouve la colonne dans la quelle ajouter l'image (la plus petite)
                        int numColonne = 0;
                        for (int itC = 0; itC < nmbColonnes; itC++) {
                            if (taillesColonnes.get(itC) < taillesColonnes.get(numColonne)) {
                                numColonne = itC;
                            }
                        }


                        int x = numColonne * (this.getWidth() / nmbColonnes);
                        int y = taillesColonnes.get(numColonne);

                        btnCategorie.get(itBtn).drawOn(canvas, x, y - offSetY);
                        taillesColonnes.set(numColonne, taillesColonnes.get(numColonne) + btnCategorie.get(itBtn).getHaut()); // ajoute la taille de chaque image à la colonne assoiciée

                    }

                    //retrouve la taille de la colonne la plus grande
                    maxHeightContent = 0;

                    for (int itC = 0; itC < taillesColonnes.size(); itC++) {
                        if (taillesColonnes.get(itC) > maxHeightContent) {
                            maxHeightContent = taillesColonnes.get(itC);
                        }
                    }
                }
            }
        else {
                // si les droits pour lire l'image ne sont pas obtenus alors on ressaye dans 1 seconde
                postDelayed(new Runnable() {
                    public void run() {
                        invalidate();
                    }
                }, 1000);
        }


            //termine par dessiner le header
            if ( header == null)
            {
                header  = new Header(canvas, this, "Menu", false);
            }
            header.drawOn(canvas);
        }

}
