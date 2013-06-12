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
	
	String senderName;
	String recieverAddress;
	String title;
	String message;
	
	EditText editTextSenderName;
	EditText editTextRecieverAddress;
	EditText editTextTitle;
	EditText editTextMessage;
	
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
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		senderName		= sp.getString("senderName", "");
		recieverAddress	= sp.getString("recieverAdress", "");
		title			= sp.getString("title", "今から帰ります");
		message			= sp.getString("message", "今会社を出ました");
		
		editTextSenderName = (EditText)findViewById(R.id.editSenderName);
		editTextRecieverAddress = (EditText)findViewById(R.id.editRecieverAdress);
		editTextTitle = (EditText)findViewById(R.id.editTitle);
		editTextMessage = (EditText)findViewById(R.id.editMessage);
		
		editTextSenderName.setText(senderName);
		editTextRecieverAddress.setText(recieverAddress);
		editTextTitle.setText(title);
		editTextMessage.setText(message);
		
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

		senderName = editTextSenderName.getText().toString();
		recieverAddress = editTextRecieverAddress.getText().toString();
		title = editTextTitle.getText().toString();
		message = editTextMessage.getText().toString();
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
