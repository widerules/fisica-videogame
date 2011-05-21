package br.com.thecodebakers.gameDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Principal extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void graverlet (View view) {
    	Intent i = new Intent(this.getApplicationContext(), GraVerlet.class);
    	startActivity(i);
    }
}