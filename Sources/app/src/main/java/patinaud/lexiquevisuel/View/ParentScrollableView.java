package patinaud.lexiquevisuel.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import patinaud.lexiquevisuel.ViewElement.Header;

public class ParentScrollableView extends View implements View.OnTouchListener  {

    protected Context context;
    protected Activity activity;
    protected Integer startTouchX = null;
    protected Integer startTouchY = null;
    protected Integer previousTouchX = null;
    protected Integer previousTouchY = null;
    protected boolean moved = false;

    protected Canvas canvas;
    protected int offSetY = 0; //permet d'effectuer les scroll

    protected int maxHeightContent = 0; //stock la hauteur de la colonne la plus grande (permet de stopper le scroll)


    public ParentScrollableView(Activity activity)
    {
        super((Context)activity);
        this.context = activity.getBaseContext();
        this.activity = activity;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.e("ParentScrollableView", "ACTION_DOWN");
            previousTouchX = startTouchX = (int) event.getX();
            previousTouchY = startTouchY = (int) event.getY();
            moved = false;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            Log.e("ParentScrollableView", "ACTION_MOVE");

            Log.e("ParentScrollableView", "ACTION_MOVE : X = " + Math.abs(startTouchX - (int) event.getX() ));
            Log.e("ParentScrollableView", "ACTION_MOVE : Y = " + Math.abs(startTouchY - (int) event.getY() ));


            moveAction(event);

            // l'action de déplacement n'est enregistrée comme tel que si il y  a effectivement un déplacement
            if ( Math.abs(startTouchX - (int) event.getX() ) > 20 ||  Math.abs(startTouchY - (int) event.getY() )  > 20 ) {
                moved = true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            Log.e("ParentScrollableView", "ACTION_UP");
            //test si si il s'agissait d'un click ou d'un move
            if ( moved == false)
            {
                clickAction(event);
            }
        }

        return true;
    }

    public void clickAction(MotionEvent event) {   }



    //Permet de gérer les scroll vers le bas
    public void moveAction(MotionEvent event)
    {
        offSetY = offSetY + (previousTouchY - (int)event.getY());
        if ( offSetY < 0) offSetY = 0;
        if ( offSetY > maxHeightContent - canvas.getHeight() ) offSetY = maxHeightContent - canvas.getHeight();

        //si les collonnes ne permettent pas de remplir toute la page on laisse l'offset à
        if ( maxHeightContent < canvas.getHeight()) offSetY = 0;

        previousTouchX = (int) event.getX();
        previousTouchY = (int) event.getY();
        invalidate();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
    }

}
