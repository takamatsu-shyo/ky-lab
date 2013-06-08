package com.example.messagesender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		
		//登録ボタン
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveButtonClick();
			}
		});
		
		//テストボタン
		/*
		Button btnRead = (Button)findViewById(R.id.btnRead);
		btnRead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readButtonClick();
			}
		});
		*/
	}
	
	
	//登録ボタン
	private void saveButtonClick(){
		//各テキストボックスの値を読み込む
		EditText editTextSenderName = (EditText)findViewById(R.id.editSenderName);
		EditText editTextRecieverAddress = (EditText)findViewById(R.id.editRecieverAdress);
		EditText editTextTitle = (EditText)findViewById(R.id.editTitle);
		EditText editTextMessage = (EditText)findViewById(R.id.editMessage);
		String senderName = editTextSenderName.getText().toString();
		String recieverAddress = editTextRecieverAddress.getText().toString();
		String title = editTextTitle.getText().toString();
		String message = editTextMessage.getText().toString();
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = sp.edit();
		
		//空白でなければ保存
		if(!senderName.equals("")){
			senderName.replace("&", "and");//&はandに置換
			e.putString("senderName", senderName).commit();
		}
		
		if(!recieverAddress.equals("")){
			senderName.replace("&", "and");
			e.putString("recieverAdress", recieverAddress).commit();
		}
				
		if(!title.equals("")){
			senderName.replace("&", "and");
			e.putString("title", title).commit();
		}
		
		if(!message.equals("")){
			senderName.replace("&", "and");
			e.putString("message", message).commit();
		}			
		
		//設定完了したらintentでメール画面に繊維
		Intent intent = new Intent(SubActivity.this,MainActivity.class);
		startActivity(intent);
		
	}
	
	//テストボタン
	/*
	private void readButtonClick(){
		TextView textView = (TextView) findViewById(R.id.textSenderName);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		textView.setText(sp.getString("senderName", null), BufferType.NORMAL);
	}
	*/
	
}
