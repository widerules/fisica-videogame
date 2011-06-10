/*
 * Copyright (C) 2011 The Code Bakers
 * Authors: Cleuton Sampaio e Francisco Rodrigues
 * e-mail: thecodebakers@gmail.com
 * Project: http://code.google.com/p/fisica-videogame/
 * Site: http://thecodebakers.blogspot.com
 *
 * Licensed under the GNU GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://gplv3.fsf.org/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Cleuton Sampaio e Francisco Rogrigues - thecodebakers@gmail.com
 */
package br.com.thecodebakers.gameDemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class GraVerletColisao extends Activity {
	
	private GraVerletColisao actReference = this;
	private Bola bola = new Bola();
	private Bitmap fundo; 
	private Bitmap bitmap;
	private Bitmap mancha;
	private VerletView vview;
	private Executor executor;
	
	/*
	 * Esta classe representa uma bola
	 */
	class Bola {
		double altura;
		double alturaAnterior;
		double alturaLimite;
		double limiteSuperior;
		double aceleracao;
		double velocidade;
		double dt;
		double forcaGrav;
		double massa;
		boolean parada = true;
		boolean descendo = true;
		double e = 0.35; // Coeficiente de restituição
		
		Bola() {
			reset();
		}
		
		void reset() {
			 limiteSuperior		= 370.0d;
			 altura 			= limiteSuperior;
			 alturaAnterior 	= altura;
			 alturaLimite		= 0;
			 aceleracao 		= 0.0d;
			 velocidade 		= 0.0d;
			 dt         		= 01.d;
			 forcaGrav 			= -9.8d;
			 massa            	= 1.0d;
			 descendo			= true;
		}
		
		void atualizar() {
			parada = false;
			altura = altura + velocidade * dt + 
    				  (aceleracao * Math.pow(dt, 2)) / 2.0d;
	    	double vel2 = velocidade + (aceleracao * dt) /2.0d;
	    	double aceleracao = forcaGrav / massa ;
	    	velocidade = vel2 + (aceleracao * dt) / 2.0d;
		}
	}
	
	/*
	 * Esta classe representa a view que será apresentada
	 * pela activity
	 */
	class VerletView extends View {

		public VerletView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(fundo, 0, 0, null);
			bola.atualizar();

			
			if (bola.altura <= 0) {
				// A bola bateu no chão
				bola.altura = 0;
				canvas.drawBitmap(bitmap, 130.0f, (float)(370.0d - bola.altura), null);
				double novaAltura = Math.pow(bola.e, 2) * bola.alturaAnterior;
				bola.velocidade = Math.sqrt(2 * (-bola.forcaGrav) * novaAltura);
				bola.alturaAnterior = novaAltura;
				if (novaAltura <= 0) {
					bola.parada = true;
				}
			}
			else {
				// A bola ainda está caindo
				canvas.drawBitmap(bitmap, 130.0f, (float)(370.0d - bola.altura), null);
			}
		}
		

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Options opc = new Options();
        opc.inDither = true;
		fundo  = BitmapFactory.decodeResource(getResources(), R.drawable.fundo, opc);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yingyang);
		bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
		mancha = BitmapFactory.decodeResource(getResources(), R.drawable.mancha);
		vview = new VerletView(this.getApplicationContext());
		this.setContentView(vview);
		
	}
	/*
	 * Este é o Thread que comanda a animação
	 */
	class Executor extends Thread {
		public void run() {
			bola.reset();
			bola.atualizar();
			int i = 0;
            do {
            	i++;
            	try {
					sleep(30); // É só para SIMULAR 1 segundo...
					vview.postInvalidate();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            } while (!bola.parada);
		}
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
        	executor = new Executor();
           	try {
           		bola.reset();
            	executor.start();
           	}
           	catch(IllegalThreadStateException i) {
           		// É necessário controlar para saber
           		// se o Thread já foi iniciado
           	}
        }
        
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	finish();
        }
        return false;
    }

	
	
}
