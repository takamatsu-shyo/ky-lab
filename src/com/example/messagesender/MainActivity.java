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
	//����N�����G���[����̂��߂̃_�~�[�����l
    //�ꎞ�I�ȏ���
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
     * �Í����v���O�����̏�����
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
     * byte����16�Ŋ���؂��悤�ɋ󔒂��Ō�ɂ���
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
     * ������̈Í���
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
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE); //�^�C�g���o�[����

		setContentView(R.layout.activity_main);
		messageText = (TextView) findViewById(R.id.textView1);
        
		///ad part
		//AdView adView = (AdView)this.findViewById(R.id.adView);
		//AdRequest adRequest = new AdRequest();
		//adView.loadAd(adRequest);
		

		initEnc();
				
		//���M���s���@Gmail���M�{�^��
		//sharedPreference ���瑗�M���[���f�[�^���擾
		//�����������������M�����ɂ�����̂Ő�������\��	
		Button btnGmail = (Button)findViewById(R.id.btnGmail);
		btnGmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				readButtonClick();
				Intent intent = new Intent();
				
				//���Đ�͔z��Ŏ󂯎��݂����Ȃ̂ŁA�L���X�g
				String[] arrayRecieverAdress = {recieverAdress, "takamatsu.shyo@googlemail.com"};	
				
				intent.putExtra(Intent.EXTRA_EMAIL, arrayRecieverAdress);
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, message + "\n from " + senderName);
				intent.setType("message/ref822");
				
				intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
				try{
				startActivity(intent);
				}catch(Exception e){
					Toast.makeText(MainActivity.this, "Gmail�A�v�����J���܂���B�A�v�����C���X�g�[������Ă��Ȃ��\��������܂�", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		
		//this.moveTaskToBack(true); 
		
		
		//�����ݒ�/�ݒ�ύX�{�^��
		Button btnSetting = (Button)findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubActivity.class);
				startActivity(intent);
			}
		});
		
		
		//����
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
			// ���邭���\��  
		    dialog = new ProgressDialog(MainActivity.this);  
		    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
		    dialog.setMessage("���[����");  
		    dialog.show();  
			//���[���𑗂鏈��
			sendMail();		
		}

	}
	
	//gmail���M�{�^���@�������Ƀf�[�^����镔��
	public void readButtonClick(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		senderName		= sp.getString("senderName", null);
		recieverAdress	= sp.getString("recieverAdress", null);
		title			= sp.getString("title", null);
		message			= sp.getString("message", null);
	}
	
	/*
	 ���[���𑗂鏈��
	 */
	protected void sendMail(){
		//sharedPreference ���瑗�M���[���f�[�^���擾
		
		String url = "http://mail.doyeah.info/mail.php";
		byte[] urlBytes;

	    try {
	        // ���M�p�����[�^�̃G���R�[�h���w��
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
	 * �񓯊��X���b�h�Ń��[���𑗂�N���X
	 */
	class AsyncSendMail extends AsyncTask<byte[],Void, String> {
		
		/**
		 * �񓯊�����
		 */
		@Override
		protected String doInBackground(byte[]... params) {
			return doPost(params[0],params[1]);
		}
		
		 @Override  
        public void onPostExecute(String params) {  
            // ���邭�������  
            dialog.dismiss();  
            final Calendar calendar = Calendar.getInstance();
            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);
            final int second = calendar.get(Calendar.SECOND);
            

            
            if (params == null){
            	messageText.setText("���L�̃{�^������ݒ�����Ă�������");
            	return;
            }
            params = params.trim();//�Ȃ����ŏ��ɃX�y�[�X�������Ă��邽��
            if(params.equals("send")){
            	messageText.setText("���[�������M����܂���(" + hour + "��" + minute + "��" + second + "�b)");
            }
            else if(params.equals("not send")){
            	messageText.setText("���[��������܂���ł����B���L�̃{�^������ݒ���������Ƒ����\��������܂�");
            }
            else{
            	messageText.setText("�T�[�o�[�������ł��B���L�̃{�^�����烁�[���ő����ĉ�����");
            }
            
            
        }  

	    
	    public String doPost(byte[] urlStr, byte[] requestBytes)
	    {
	    	  HttpURLConnection http = null;  // HTTP�ʐM
	    	  OutputStream out = null;   // HTTP���N�G�X�g���M�p�X�g���[��
	    	  InputStream in = null;    // HTTP���X�|���X�擾�p�X�g���[��
	    	  BufferedReader reader = null;  // ���X�|���X�f�[�^�o�͗p�o�b�t�@
	    	  String result = "";
	    	  try {
		    	   // URL�w��
		    	   URL url = new URL(new String(urlStr, request_encoding));
		    	   // HttpURLConnection�C���X�^���X�쐬
		    	   http = (HttpURLConnection)url.openConnection();
		    	   // POST�ݒ�
		    	   http.setRequestMethod("POST");
		    	   // HTTP�w�b�_�́uContent-Type�v���uapplication/octet-stream�v�ɐݒ�
		    	   http.setRequestProperty("Content-Type","application/octet-stream");
		    	   // URL �ڑ����g�p���ē��o�͂��s��
		    	   http.setDoInput(true);
		    	   http.setDoOutput(true);
		    	   // �L���b�V���͎g�p���Ȃ�
		    	   http.setUseCaches(false);
		    	   // �ڑ�
		    	   http.connect();
		    	   // �f�[�^���o��
		    	   out = new BufferedOutputStream(http.getOutputStream());
		    	   out.write(requestBytes);
		    	   out.flush();
		    	   // ���X�|���X���擾
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
	    
	
	
	//Gmail���M�e�X�g�{�^��

	
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
