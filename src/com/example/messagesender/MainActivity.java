package com.example.messagesender;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.R.layout;
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
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.google.ads.*;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends Activity {

    private ProgressDialog dialog;  
    TextView messageText;
    private static String request_encoding = "utf-8";
	//初回起動時エラー回避のためのダミー初期値
    //一時的な処理
    public String initialSenderName = "yano-initialABCDX";
	public String senderName		= "yano-initialABCDX";
	public String recieverAdress	= "takamatsu.shyo@gmail.com";
	public String title				= "initialTitle";
	public String message			= "initialMessage";
    
	
    private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    
    private String SecretKey = "0123456789abcdef";//Dummy secretKey (CHANGE IT!)
	
    private AdView adView;
    
    /**
     * 暗号化プログラムの初期化
     */
    protected void initEnc(){
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        try {
                cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (NoSuchPaddingException e) {
                e.printStackTrace();
        }
    }
    

    /**
     * byte数が16で割り切れるように空白を最後につける
     * @param source
     * @return
     */
    private static byte[] padByte(String source)
    {
      byte paddingByte = 0x20;
      int size = 16;
      byte[] sourceBytes;
      try{
          sourceBytes = source.getBytes(request_encoding);
      }catch(Exception e){
    	  return null;
      }
      int x = sourceBytes.length % size;
      int padLength = size - x;
      
      byte[] result = new byte[sourceBytes.length + padLength];
      for (int i = 0; i < sourceBytes.length; i++)
      {
              result[i] = sourceBytes[i];
      }
      for(int i=sourceBytes.length; i< result.length;i++){
    	  result[i] = paddingByte;
      }

      return result;
    }
    
    
    
    /**
     * 文字列の暗号化
     */
    protected byte[] encString(String str) throws Exception{
        if(str == null || str.length() == 0){
            return new byte[0];
        }
	    byte[] encrypted = null;
	
	    try {
	            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
	            byte[] bytes = padByte(str);
	            encrypted = cipher.doFinal(bytes);
	    } catch (Exception e)
	    {                       
	            throw new Exception("[encrypt] " + e.getMessage());
	    }
	    
	    return encrypted;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE); //タイトルバー消去

		setContentView(R.layout.activity_main);
		messageText = (TextView) findViewById(R.id.textView1);
        
		///ad part
		//AdView adView = (AdView)this.findViewById(R.id.adView);
		//AdRequest adRequest = new AdRequest();
		//adView.loadAd(adRequest);
		

		initEnc();
				
		//送信失敗時　Gmail送信ボタン
		//sharedPreference から送信メールデータを取得
		//同じ処理が自動送信部分にもあるので整理する予定	
		Button btnGmail = (Button)findViewById(R.id.btnGmail);
		btnGmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				readButtonClick();
				Intent intent = new Intent();
				
				//あて先は配列で受け取るみたいなので、キャスト
				String[] arrayRecieverAdress = {recieverAdress, "takamatsu.shyo@googlemail.com"};	
				
				intent.putExtra(Intent.EXTRA_EMAIL, arrayRecieverAdress);
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, message + "\n from " + senderName);
				intent.setType("message/ref822");
				
				intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
				try{
				startActivity(intent);
				}catch(Exception e){
					Toast.makeText(MainActivity.this, "Gmailアプリを開けません。アプリがインストールされていない可能性があります", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		
		//this.moveTaskToBack(true); 
		
		
		//初期設定/設定変更ボタン
		Button btnSetting = (Button)findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubActivity.class);
				startActivity(intent);
			}
		});
		
		
		//閉じる
		Button btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		sendMailTask();

	}
	
	private void loadData(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		senderName		= sp.getString("senderName", null);
		recieverAdress	= sp.getString("recieverAdress", null);
		title			= sp.getString("title", null);
		message			= sp.getString("message", null);

	}
	
	public void sendMailTask(){
		
		loadData();

		if(senderName == null || senderName.equals(initialSenderName)){
			Intent intent = new Intent(MainActivity.this, SubActivity.class);
			startActivity(intent);
		}
		else{
			// くるくるを表示  
		    dialog = new ProgressDialog(MainActivity.this);  
		    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
		    dialog.setMessage("メール中");  
		    dialog.show();  
			//メールを送る処理
			sendMail();		
		}

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
		
		String url = "http://mail.doyeah.info/mail.php";
		byte[] urlBytes;

	    try {
	        // 送信パラメータのエンコードを指定
	        urlBytes = url.getBytes(request_encoding);
	    } 
	    catch (UnsupportedEncodingException e1) {
	        e1.printStackTrace();
	        return;
	      }
	    AsyncSendMail sync = new AsyncSendMail();
	    
	    String postParams =  recieverAdress  +  "&" + title + "&" + message+ "&" + senderName;
	    try{
	    	byte[] paramBytes = encString(postParams);
	    	sync.execute(urlBytes, paramBytes);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }

	}
	
	/**
	 * 
	 * 非同期スレッドでメールを送るクラス
	 */
	class AsyncSendMail extends AsyncTask<byte[],Void, String> {
		
		/**
		 * 非同期処理
		 */
		@Override
		protected String doInBackground(byte[]... params) {
			return doPost(params[0],params[1]);
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
            params = params.trim();//なぜか最初にスペースが入っているため
            if(params.equals("send")){
            	messageText.setText("メールが送信されました(" + hour + "時" + minute + "分" + second + "秒)");
            }
            else if(params.equals("not send")){
            	messageText.setText("メールが送れませんでした。下記のボタンから設定を見直すと送れる可能性があります");
            }
            else{
            	messageText.setText("サーバー調整中です。下記のボタンからメールで送って下さい");
            }
            
            
        }  

	    
	    public String doPost(byte[] urlStr, byte[] requestBytes)
	    {
	    	  HttpURLConnection http = null;  // HTTP通信
	    	  OutputStream out = null;   // HTTPリクエスト送信用ストリーム
	    	  InputStream in = null;    // HTTPレスポンス取得用ストリーム
	    	  BufferedReader reader = null;  // レスポンスデータ出力用バッファ
	    	  String result = "";
	    	  try {
		    	   // URL指定
		    	   URL url = new URL(new String(urlStr, request_encoding));
		    	   // HttpURLConnectionインスタンス作成
		    	   http = (HttpURLConnection)url.openConnection();
		    	   // POST設定
		    	   http.setRequestMethod("POST");
		    	   // HTTPヘッダの「Content-Type」を「application/octet-stream」に設定
		    	   http.setRequestProperty("Content-Type","application/octet-stream");
		    	   // URL 接続を使用して入出力を行う
		    	   http.setDoInput(true);
		    	   http.setDoOutput(true);
		    	   // キャッシュは使用しない
		    	   http.setUseCaches(false);
		    	   // 接続
		    	   http.connect();
		    	   // データを出力
		    	   out = new BufferedOutputStream(http.getOutputStream());
		    	   out.write(requestBytes);
		    	   out.flush();
		    	   // レスポンスを取得
		    	   in = new BufferedInputStream(http.getInputStream());
		    	   reader = new BufferedReader(new InputStreamReader(in));
		    	   result = reader.readLine();
	    	  } 
	    	  catch(Exception e) {
	    	   e.printStackTrace();
	    	  } 
	    	  
	    	  finally {
	    	   try {
		    	    if(reader != null) {
		    	    	reader.close();
		    	    }
		    	    if(in != null) {
		    	    	in.close();
		    	    }
		    	    if(out != null) {
		    	    	out.close();
		    	    }
		    	    if(http != null) {
		    	    	http.disconnect();
		    	    }
	    	   } catch(Exception e) {
	    	   }
	    	  }
	    	  return result;
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

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	

}
