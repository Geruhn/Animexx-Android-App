package de.meisterfuu.animexx.Home;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloader;


public class HomeKontaktNewFragment extends SherlockActivity {

	ArrayList<HomeKontaktObject> Array = new ArrayList<HomeKontaktObject>();

	Context context;
	ImageDownloader Images = new ImageDownloader();
	ProgressDialog dialog;
	
	TextView msg;
	ImageView img;
	Button send;
	
	String id;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		
		setContentView(R.layout.home_kontakt_new);	

		msg = (TextView) findViewById(R.id.fragment_microblog_text);
		img = (ImageView) findViewById(R.id.fragment_microblog_img);
		img.setVisibility(View.GONE);
		send = (Button) findViewById(R.id.fragment_microblog_send);
		send.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				send();				
			}			
		});
		
		
		if (this.getIntent().hasExtra("uri")) {
			String url = this.getIntent().getStringExtra("uri");
			id = this.getIntent().getStringExtra("id");

			try {			
				// Query gallery for camera picture via
				// Android ContentResolver interface
				ContentResolver cr = getContentResolver();
				InputStream is;
				is = cr.openInputStream(Uri.parse(url));
				// Get binary bytes for encode
				byte[] data = getBytesFromFile(is);
				
				//Show Picture			
			     BitmapFactory.Options options = new BitmapFactory.Options();
			     Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length,options);
				img.setImageBitmap(bm);	
				img.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				img.setVisibility(View.GONE);
			}

		} else {
			
		}
		

	}


	
	public void send(){
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("Hochladen...");
		dialog.setCancelable(false);
		dialog.show();

		final HttpPost request = new HttpPost("https://ws.animexx.de/json/persstart5/post_microblog_message/?api=2");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("text", ""+msg.getText()));
		if(id != null)nameValuePairs.add(new BasicNameValuePair("attach_foto", id));
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		new Thread(new Runnable() {

			public void run() {
				
				try {
					String s = Request.SignSend(request);
					JSONObject ob = new JSONObject(s).getJSONObject("return");
	
					
					HomeKontaktNewFragment.this.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Toast.makeText(HomeKontaktNewFragment.this, "Gesendet!", Toast.LENGTH_LONG).show();
							HomeKontaktNewFragment.this.finish();
						}

					});
				} catch (Exception e) {
					HomeKontaktNewFragment.this.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Toast.makeText(HomeKontaktNewFragment.this, "Error!", Toast.LENGTH_LONG).show();
							HomeKontaktNewFragment.this.finish();
						}

					});
					e.printStackTrace();
				}



			}

		}).start();
		
		
	}
	
	public static byte[] getBytesFromFile(InputStream is) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();
		} catch (IOException e) {
			Log.e("Animexx", e.toString());
			return null;
		}
	}



}