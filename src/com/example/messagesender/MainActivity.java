package com.example.messagesender;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//メールを送る処理
		sendMail();		
		
	//送信成功時　動作確認ボタン
	Button btnAdd = (Button)findViewById(R.id.btnOK);
	btnAdd.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MessageBox("送信しました。　：）","message");
		}
	});
	
	//送信失敗時　動作確認テストボタン
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
	
	/**
	 * メールを送る処理
	 */
	protected void sendMail(){
		String url = "http://mail.doyeah.info/mail.php?to=kasuya-u@yama.info.waseda.ac.jp&from=kasuya-u@yama.info.waseda.ac.jp&message=ちょりっす&subject=今帰る";
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
	/*
	//Gmail送信テストボタン
	Button btnGmail = (Button)findViewById(R.id.btnGmail);
	btnGmail.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			
			String[] strTo = {"shoyano.ee@ezweb.ne.jp"};
			intent.putExtra(Intent.EXTRA_EMAIL, strTo);
			intent.putExtra(Intent.EXTRA_SUBJECT, "今から帰ります[タイトル]");
			intent.putExtra(Intent.EXTRA_TEXT, "あと１時間くらい[本文]\n");
			intent.setType("message/ref822");
			
			intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
			
			
			//intent.setAction(Intent.ACTION_VIEW);
			//intent.setDataAndNormalize(Uri.parse("http://b.hatna.ne.jp"));
			
			
			//intent.setClassName("com.android.broser","com.android.browser.BrowserActivity");
			startActivity(intent);
		}
	});
	}
	*/
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
