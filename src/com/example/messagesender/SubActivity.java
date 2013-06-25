package com.example.messagesender;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubActivity extends Activity {

	String senderName;
	String recieverAddress;
	String title;
	String message;

	EditText editTextSenderName;
	EditText editTextRecieverAddress;
	EditText editTextTitle;
	EditText editTextMessage;
	private static final int    PICK_CONTACT = 1;

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

		//登録ボタン
		Button bnImport = (Button)findViewById(R.id.bnImportAddress);
		bnImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent pickIntent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				//People.Contacts.CONTENT_URI);
				startActivityForResult(pickIntent, SubActivity.PICK_CONTACT);
			}
		});

		/* Get UserProfile */
		// Cursorの取得
		Cursor mCursor = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		mCursor.moveToFirst();

		// UserNameの取得
		String uName = "";
		int nameIndex = mCursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);
		if(nameIndex != -1){//アドレスの中身が無い時は書き換えず終了
			uName = mCursor.getString(nameIndex);
		}
		// ProfileのURLから取得したCursorを閉じる
		mCursor.close();
		 

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		senderName		= sp.getString("senderName", uName);
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

	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){

		if (data == null){//連絡先を選択しなかった場合
			return;
		}
		Uri contactData = data.getData();
		String id = getIDFromContact(contactData);
		if(id.equals("")){//idが取得できないときはそのまま終了
			return;
		}
		LinkedList<String> mailList = getMailAddresses(id);
		final String[] strAddresses = (String[])mailList.toArray(new String[0]);
		if(strAddresses.length == 0){//アドレスの中身が無い時は書き換えず終了
			Toast.makeText(SubActivity.this, "この連絡先には有効なアドレスがありません", Toast.LENGTH_LONG).show();
			return;
		}
		else if(strAddresses.length == 1){//アドレスが１つだけの時はそのアドレスに書き換える
			editTextRecieverAddress.setText(strAddresses[0]);
			return;
		}
		else{//２つ以上の時はダイアログを出す
			new AlertDialog.Builder(SubActivity.this)
			.setTitle("送りたいメールアドレスを選んでください")
			.setItems(strAddresses, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					editTextRecieverAddress.setText(strAddresses[which]);
				}
			}
					).show();
		}

	}

	/**
	 * 連絡先のUriからidを取得する（できないときは""を返す）
	 */
	private String getIDFromContact(Uri contactData){
		ContentResolver contentResolver = this.getContentResolver();
		Cursor cursor = contentResolver.query(contactData, null, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Identity._ID);
		if(columnIndex < 0){
			cursor.close();
			return "";
		}
		String id = cursor.getString(columnIndex);
		cursor.close();
		return id;
	}

	/**
	 * 連絡先のidからアドレスリストを取得する
	 * @param id
	 * @return アドレスのリスト
	 */
	private LinkedList<String> getMailAddresses(String id){
		LinkedList<String> list = new LinkedList<String>();
		ContentResolver cr = this.getContentResolver();
		Cursor emails = cr.query(Email.CONTENT_URI, null,  Email.CONTACT_ID + " = " + id, null, null);
		emails.moveToFirst();
		do{
			int index = emails.getColumnIndex(Email.DATA);
			try{
				String address = emails.getString(index);
				list.add(address);
			}catch(Exception e){
				break;
			}
		}while(emails.moveToNext());
		emails.close();
		return list;
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
			recieverAddress.replace("&", "and");
			e.putString("recieverAdress", recieverAddress).commit();
		}

		if(!title.equals("")){
			title.replace("&", "and");
			e.putString("title", title).commit();
		}

		if(!message.equals("")){
			message.replace("&", "and");
			e.putString("message", message).commit();
		}			

		//設定完了したらintentでメール画面に繊維
		Intent intent = new Intent(SubActivity.this,MainActivity.class);
		startActivity(intent);

	}

}
