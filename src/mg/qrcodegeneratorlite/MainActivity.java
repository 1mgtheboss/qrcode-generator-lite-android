package mg.qrcodegeneratorlite;



import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import mg.qrcodegeneratorlite.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends Activity {
	
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create the interstitial.
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("a1535a31432d72b");
	    
	    
	    interstitial.setAdListener(new AdListener() {
	        @Override
	        public void onAdLoaded() {
	          
	        	displayInterstitial();

	          
	        }
	        @Override
	        public void onAdFailedToLoad(int errorCode) {
	          
	        }
	    });
		
	    // Create ad request.
	    
	    AdRequest adRequest = new AdRequest.Builder().build();
	    
	   
	    
	    
	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequest);
		
		
	}
	
	public void generateButtonClick(View view) {
		ImageView  mIV   = (ImageView)findViewById(R.id.imageView1);
		
		try
		{
			EditText mET=(EditText)findViewById(R.id.editText1);
			generateQRCode(mET.getText().toString(),mIV);
			Bitmap mBM = ((BitmapDrawable)mIV.getDrawable()).getBitmap();
			
			String SD_CARD = Environment.getExternalStorageDirectory().toString();
			
			File QRCD = new File(SD_CARD + "/QR_CODES");
			QRCD.mkdirs();
			SimpleDateFormat sDF = new SimpleDateFormat("ddMMyyyyhhmmss",Locale.US);
			
			String FILE_NAME=sDF.format(new Date()).toString()+".png";
			File file = new File (QRCD, FILE_NAME);
			if (file.exists()) file.delete (); 
			FileOutputStream out = new FileOutputStream(file);
			mBM.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
		    out.close();
		    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}
		catch(Exception e)
		{
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void generateQRCode(String data, ImageView img)throws WriterException {
	    com.google.zxing.Writer writer = new QRCodeWriter();
	    String finaldata = Uri.encode(data, "utf-8");

	    BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE,150, 150);
	    Bitmap ImageBitmap = Bitmap.createBitmap(150, 150,Config.ARGB_8888);

	    for (int i = 0; i < 150; i++) {//width
	        for (int j = 0; j < 150; j++) {//height
	            ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
	        }
	    }

	    if (ImageBitmap != null) {
	        img.setImageBitmap(ImageBitmap);
	    } else {
	        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userInputError),
	                Toast.LENGTH_SHORT).show(); 
	    }
	}
	
	// Invoke displayInterstitial() when you are ready to display an interstitial.
	  public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    }
	  }

}
