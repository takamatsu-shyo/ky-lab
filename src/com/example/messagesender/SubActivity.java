package com.example.messagesender;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;

public class SubActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		//‚à‚Ç‚éƒ{ƒ^ƒ“
		Button btnSetting = (Button)findViewById(R.id.btnSBack);
		btnSetting.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
