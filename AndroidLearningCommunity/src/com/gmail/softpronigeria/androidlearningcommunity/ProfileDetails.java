package com.gmail.softpronigeria.androidlearningcommunity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileDetails extends Activity implements OnClickListener {

	// private UserProfile userProfile;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

		setContentView(R.layout.profile_display);
		Button share = (Button) findViewById(R.id.shear_profile);
		TextView profileURL = (TextView) findViewById(R.id.text_profile_url);
		TextView username = (TextView) findViewById(R.id.text_username);
		ImageView imageView = (ImageView) findViewById(R.id.profile_image);

		bundle = getIntent().getExtras();
		if (bundle != null) {

			// To retrieve user profile details

			imageView.setImageBitmap((Bitmap) bundle.get("photoURL"));
			username.setText((String) bundle.get("username"));
			profileURL.setText((String) bundle.get("userURL"));
		}
		profileURL.setOnClickListener(this);

		share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.shear_profile) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Shear Profile!");
			intent.putExtra(
					Intent.EXTRA_TEXT,
					"Check out this awesome developer @"
							+ (String) bundle.get("username") + ","
							+ (String) bundle.get("userURL"));
			startActivity(Intent.createChooser(intent, "Share"));

		} else if (v.getId() == R.id.text_profile_url) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse((String) bundle.get("userURL")));
			startActivity(i);
		}
	}
}
