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
		
		//�o�^�{�^��
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
		title			= sp.getString("title", "������A��܂�");
		message			= sp.getString("message", "����Ђ��o�܂���");
		
		editTextSenderName = (EditText)findViewById(R.id.editSenderName);
		editTextRecieverAddress = (EditText)findViewById(R.id.editRecieverAdress);
		editTextTitle = (EditText)findViewById(R.id.editTitle);
		editTextMessage = (EditText)findViewById(R.id.editMessage);
		
		editTextSenderName.setText(senderName);
		editTextRecieverAddress.setText(recieverAddress);
		editTextTitle.setText(title);
		editTextMessage.setText(message);
		
		//�e�X�g�{�^��
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
	
	
	//�o�^�{�^��
	private void saveButtonClick(){

		//�e�e�L�X�g�{�b�N�X�̒l��ǂݍ���

		senderName = editTextSenderName.getText().toString();
		recieverAddress = editTextRecieverAddress.getText().toString();
		title = editTextTitle.getText().toString();
		message = editTextMessage.getText().toString();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = sp.edit();
		
		//�󔒂łȂ���Εۑ�
		if(!senderName.equals("")){
			senderName.replace("&", "and");//&��and�ɒu��
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
		
		//�ݒ芮��������intent�Ń��[����ʂɑ@��
		Intent intent = new Intent(SubActivity.this,MainActivity.class);
		startActivity(intent);
		
	}
	
	//�e�X�g�{�^��
	/*
	private void readButtonClick(){
		TextView textView = (TextView) findViewById(R.id.textSenderName);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		textView.setText(sp.getString("senderName", null), BufferType.NORMAL);
	}
	*/
	
}
