package com.goodapp.yumas.present_catch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

    Bitmap presentImage;
    static final long FPS =30;
    static final long FRAME_TIME = 1000 / FPS;

    SurfaceHolder surfaceHolder;
    Thread thread;
    Item item;
    Player player;
    Bitmap playerImage;
    int score = 0;
    int life = 10;
    int screenWidth,screenHeight;

    public GameView (Context context){
        super (context);

        getHolder().addCallback(this);

        Resources res = context.getResources();
        //プレゼント代入
        presentImage = BitmapFactory.decodeResource(res, R.drawable.present);
        playerImage = BitmapFactory.decodeResource(res,R.drawable.bucket);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder  = holder;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    @Override
    public void run(){
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(100);
        item = new Item();
        player = new Player();
        Random rand = new Random();
        while (thread != null){
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);//背景を白に
            canvas.drawBitmap(presentImage, item.x, item.y ,null);//プレゼント登場
            canvas.drawBitmap(playerImage, player.x, player.y ,null);//プレゼント登場
            canvas.drawText("SCORE:" + score,50,150 ,textPaint);
            canvas.drawText("LIFE:" + life,50,300,textPaint);
            if(life <= 0){
                canvas.drawText("GameOver",screenWidth/3,screenHeight/2,textPaint);
                surfaceHolder.unlockCanvasAndPost(canvas);
                break;
            }

            if (player.isEnter(item)){
                item.reset();
                score += 10;
            }else if (item.y > screenHeight) {
                item.reset();
                life --;
            }else{
                item.update();
            }
            //キャンバスの内容を反映する。
            surfaceHolder.unlockCanvasAndPost(canvas);
            //例外探知
            try {
                Thread.sleep(FRAME_TIME);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    class Item {
        private static final int WIDETH = 50;
        private static final int HEIGHT = 50;
        float x,y;
        public Item(){
            Random rand = new Random();
            x = rand.nextInt(screenWidth - WIDETH);
            y = 0;
        }
        public void update(){
            Random rand = new Random();
            y += Math.random()*300;
            x += (Math.random()*200)-100;
            Log.d("present Y",""+ y);
        }
        public void reset(){
            Random rand = new Random();
            x = rand.nextInt(screenWidth - WIDETH);
            y = 0;
        }
    }
    class Player{
        final int WIDEHT = 200;
        final int HEIGHT = 320;

        float x,y;
        public Player() {
            x = 0;
            y =screenHeight - HEIGHT;

        }
        public void move(float diffX){
            this.x += diffX;
            this.x = Math.max(0,x);
            this.x = Math.min(screenWidth - WIDEHT,x);
        }
        public boolean isEnter(Item present){
            if(present.x + Item.WIDETH > x && present.x  < x +WIDEHT &&
                    present.y + Item.HEIGHT > y && present.y < y + HEIGHT ){
                return true;
            }
            return false;
            }
        }
    }