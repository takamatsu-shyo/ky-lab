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
    
	//����N�����G���[����̂��߂̃_�~�[�����l
    //�ꎞ�I�ȏ���
	public String senderName		= "yano";
	public String recieverAdress	= "takamatsu.shyo@gmail.com";
	public String title				= "initialTitle";
	public String message			= "initialMessage";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		messageText = (TextView) findViewById(R.id.textView1);
        // ���邭���\��  
        dialog = new ProgressDialog(MainActivity.this);  
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        dialog.setMessage("���[����");  
        dialog.show();  
		//���[���𑗂鏈��
		sendMail();		
		
		
		//���M���s���@Gmail���M�{�^��
		//sharedPreference ���瑗�M���[���f�[�^���擾
		//�����������������M�����ɂ�����̂Ő�������\��
		
		Button btnGmail = (Button)findViewById(R.id.btnGmail);
		btnGmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readButtonClick();
				
				Intent intent = new Intent();
				
				//���Đ�͔z��Ŏ󂯎��݂����Ȃ̂ŁA�L���X�g
				String[] arrayRecieverAdress = {recieverAdress, "takamatsu.shyo@googlemail.com"};	
				
				intent.putExtra(Intent.EXTRA_EMAIL, arrayRecieverAdress);
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, message + "\n from " + senderName);
				intent.setType("message/ref822");
				
				intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
				
				startActivity(intent);
			}
		});
		
		
	//���M�������@����m�F�{�^��
	/*
	Button btnAdd = (Button)findViewById(R.id.btnOK);
	btnAdd.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MessageBox("���M���܂����B�@�F�j","message");
		}
	});
	*/
	
	//���M���s���@����m�F�e�X�g�{�^��
	/*
	Button btnCancel = (Button)findViewById(R.id.btnCancel);
	btnCancel.setOnClickListener(new View.OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//���b�Z�[�W�{�b�N�X
			//MessageBox("���s���܂����BGmail�ő��M���܂��B","message");
			
			//�A���[�g�_�C�A���O
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
			alertDialogBuilder.setTitle("�������M�Ɏ��s���܂���");
			alertDialogBuilder.setMessage("Gmail�ő��M���܂�");

			alertDialogBuilder.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							
							String[] strTo = {"shoyano.ee@ezweb.ne.jp"};
							intent.putExtra(Intent.EXTRA_EMAIL, strTo);
							intent.putExtra(Intent.EXTRA_SUBJECT, "������A��܂�[�^�C�g��]");
							intent.putExtra(Intent.EXTRA_TEXT, "���ƂP���Ԃ��炢[�{��]\n");
							intent.setType("message/ref822");
							
							intent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
							startActivity(intent);
						}
					});
			
			alertDialogBuilder.setNegativeButton("�L�����Z��", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
			
			//�L�����Z���\���ݒ�
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
	});
	*/
	//�����ݒ�/�ݒ�ύX�{�^��
	Button btnSetting = (Button)findViewById(R.id.btnSetting);
	btnSetting.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, SubActivity.class);
			startActivity(intent);
		}
	});
	


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
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		senderName		= sp.getString("senderName", null);
		recieverAdress	= sp.getString("recieverAdress", null);
		title			= sp.getString("title", null);
		message			= sp.getString("message", null);
		
		//String url = "http://mail.doyeah.info/mail.php?to=kasuya-u@yama.info.waseda.ac.jp&from=kasuya-u@yama.info.waseda.ac.jp&message=��������&subject=���A��";
		String url = "http://mail.doyeah.info/mail.php?to="+ recieverAdress +"&from="+ senderName +"&message="+ message +"&subject="+ title;
		AsyncSendMail sync = new AsyncSendMail();
		sync.execute(url);
	}
	
	/**
	 * 
	 * �񓯊��X���b�h�Ń��[���𑗂�N���X
	 */
	class AsyncSendMail extends AsyncTask<String, Void, String> {
		
		/**
		 * �񓯊�����
		 */
		@Override
		protected String doInBackground(String... params) {
			return doGet(params[0]);
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
	            if(params.equals("send")){
	            	messageText.setText("���[�������M����܂���(" + hour + ":" + minute + ":" + second + ")");
	            }
	            else if(params.equals("not send")){
	            	messageText.setText("���[��������܂���ł����B���L�̃{�^������ݒ���������Ƒ����\��������܂�");
	            }
	            else{
	            	messageText.setText("�T�[�o�[�������ł��B���L�̃{�^�����烁�[���ő����ĉ�����");
	            }
	            
	            
	        }  
		/**
		 * ���[���𑗂鏈��
		 * @param url
		 * @return httpget�̌���
		 */
	    public String doGet( String url ){
	    	try{
	    		HttpGet method = new HttpGet( url );
	    		DefaultHttpClient client = new DefaultHttpClient();
	    		// �w�b�_��ݒ肷��
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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
