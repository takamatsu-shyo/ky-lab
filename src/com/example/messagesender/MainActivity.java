package com.example.messagesender;

import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;



public class MainActivity extends Activity {

    private ProgressDialog dialog;  
    TextView messageText;
    
	//初回起動時エラー回避のためのダミー初期値
    //一時的な処理
	public String senderName		= "yano";
	public String recieverAdress	= "takamatsu.shyo@gmail.com";
	public String title				= "initialTitle";
	public String message			= "initialMessage";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		messageText = (TextView) findViewById(R.id.textView1);
        // くるくるを表示  
        dialog = new ProgressDialog(MainActivity.this);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setMessage("メール中");  
        dialog.show();  
		//メールを送る処理
		sendMail();		
		
		
		//送信失敗時　Gmail送信ボタン
		//sharedPreference から送信メールデータを取得
		//同じ処理が自動送信部分にもあるので整理する予定
		
		Button btnGmail = (Button)findViewById(R.id.btnGmail);
		btnGmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readButtonClick();
				
				Intent intent = new Intent();
				
				//あて先は配列で受け取るみたいなので、キャスト
				String[] arrayRecieverAdress = {recieverAdress, "takamatsu.shyo@googlemail.com"};	
				
				intent.putExtra(Intent.EXTRA_EMAIL, arrayRecieverAdress);
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, message + "\n from " + senderName);
				intent.setType("message/ref822");
				
				intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
				
				startActivity(intent);
			}
		});
		
		
	//送信成功時　動作確認ボタン
	/*
	Button btnAdd = (Button)findViewById(R.id.btnOK);
	btnAdd.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MessageBox("送信しました。　：）","message");
		}
	});
	*/
	
	//送信失敗時　動作確認テストボタン
	/*
	Button btnCancel = (Button)findViewById(R.id.btnCancel);
	btnCancel.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//メッセージボックス
			//MessageBox("失敗しました。Gmailで送信します。","message");
			
			//アラートダイアログ
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
			alertDialogBuilder.setTitle("自動送信に失敗しました");
			alertDialogBuilder.setMessage("Gmailで送信します");

			alertDialogBuilder.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							
							String[] strTo = {"shoyano.ee@ezweb.ne.jp"};
							intent.putExtra(Intent.EXTRA_EMAIL, strTo);
							intent.putExtra(Intent.EXTRA_SUBJECT, "今から帰ります[タイトル]");
							intent.putExtra(Intent.EXTRA_TEXT, "あと１時間くらい[本文]\n");
							intent.setType("message/ref822");
							
							intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
							startActivity(intent);
						}
					});
			
			alertDialogBuilder.setNegativeButton("キャンセル", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			
			//キャンセル可能か設定
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
	});
	*/
	//初期設定/設定変更ボタン
	Button btnSetting = (Button)findViewById(R.id.btnSetting);
	btnSetting.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, SubActivity.class);
			startActivity(intent);
		}
	});
	


	}
	
	//gmail送信ボタン　押下時にデータを取る部分
	public void readButtonClick(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		senderName		= sp.getString("senderName", null);
		recieverAdress	= sp.getString("recieverAdress", null);
		title			= sp.getString("title", null);
		message			= sp.getString("message", null);
	}
	
	/*
	 メールを送る処理
	 */
	protected void sendMail(){
		//sharedPreference から送信メールデータを取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		senderName		= sp.getString("senderName", null);
		recieverAdress	= sp.getString("recieverAdress", null);
		title			= sp.getString("title", null);
		message			= sp.getString("message", null);
		
		//String url = "http://mail.doyeah.info/mail.php?to=kasuya-u@yama.info.waseda.ac.jp&from=kasuya-u@yama.info.waseda.ac.jp&message=ちょりっす&subject=今帰る";
		String url = "http://mail.doyeah.info/mail.php?to="+ recieverAdress +"&from="+ senderName +"&message="+ message +"&subject="+ title;
		AsyncSendMail sync = new AsyncSendMail();
		sync.execute(url);
	}
	
	/**
	 * 
	 * 非同期スレッドでメールを送るクラス
	 */
	class AsyncSendMail extends AsyncTask<String, Void, String> {
		
		/**
		 * 非同期処理
		 */
		@Override
		protected String doInBackground(String... params) {
			return doGet(params[0]);
		}
		
		 @Override  
	        public void onPostExecute(String params) {  
	            // くるくるを消去  
	            dialog.dismiss();  
	            final Calendar calendar = Calendar.getInstance();
	            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
	            final int minute = calendar.get(Calendar.MINUTE);
	            final int second = calendar.get(Calendar.SECOND);
	            if (params == null){
	            	messageText.setText("下記のボタンから設定をしてください");
	            	return;
	            }
	            if(params.equals("send")){
	            	messageText.setText("メールが送信されました(" + hour + ":" + minute + ":" + second + ")");
	            }
	            else if(params.equals("not send")){
	            	messageText.setText("メールが送れませんでした。下記のボタンから設定を見直すと送れる可能性があります");
	            }
	            else{
	            	messageText.setText("サーバー調整中です。下記のボタンからメールで送って下さい");
	            }
	            
	            
	        }  
		/**
		 * メールを送る処理
		 * @param url
		 * @return httpgetの結果
		 */
	    public String doGet( String url ){
	    	try{
	    		HttpGet method = new HttpGet( url );
	    		DefaultHttpClient client = new DefaultHttpClient();
	    		// ヘッダを設定する
	    		method.setHeader( "Connection", "Keep-Alive" );
	        
	    		HttpResponse response = client.execute( method );
	    		int status = response.getStatusLine().getStatusCode();
	    		if ( status != HttpStatus.SC_OK )
	    			throw new Exception( "" );
	        
	    		return EntityUtils.toString( response.getEntity(), "UTF-8" );
	    	}
	    	catch ( Exception e )
	    	{
	    		return null;
	    	}
	    }
	}
	
	//Gmail送信テストボタン

	
	void MessageBox(String message, String title){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK);
				
			}
		});
		alertDialog.create();
		alertDialog.show();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
