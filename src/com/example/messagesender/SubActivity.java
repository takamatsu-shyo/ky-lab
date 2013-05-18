package com.example.messagesender;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class SubActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		
		//�o�^�{�^��
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveButtonClick();
			}
		});
		
		//�e�X�g�{�^��
		Button btnRead = (Button)findViewById(R.id.btnRead);
		btnRead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readButtonClick();
			}
		});
	}
	
	
	//�o�^�{�^��
	private void saveButtonClick(){
		EditText editText = (EditText)findViewById(R.id.editSenderName);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = sp.edit();
		e.putString("senderName", editText.getText().toString()).commit();
	}
	
	//�e�X�g�{�^��
	private void readButtonClick(){
		TextView textView = (TextView) findViewById(R.id.textSenderName);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		textView.setText(sp.getString("senderName", null), BufferType.NORMAL);
	}
}
