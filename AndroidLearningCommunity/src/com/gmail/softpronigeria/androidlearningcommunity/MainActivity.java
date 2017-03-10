package com.gmail.softpronigeria.androidlearningcommunity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	private ProgressDialog pdLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		setContentView(R.layout.activity_main);

		try {
			initializeActivity();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {

			case KeyEvent.KEYCODE_BACK:
				AlertDialog.Builder build = new AlertDialog.Builder(this);
				build.setTitle("Confirm Close");
				build.setMessage("Do you realy want to close?");
				build.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						finish();
					}
				});
				build.show();

				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {

		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		super.onStart();
	}

	private void initializeActivity() throws JSONException,
			InterruptedException, ExecutionException {

		pdLoading = new ProgressDialog(this);
		pdLoading.setCancelable(false);

		final List<UserProfile> listOfDevelopers = getListOfDevelopers();

		MyCustomList adapter = new MyCustomList(MainActivity.this,
				listOfDevelopers);

		ListView list = (ListView) findViewById(R.id.my_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						MainActivity.this,
						"You Clicked at "
								+ listOfDevelopers.get(position).getUsername(),
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(MainActivity.this, ProfileDetails.class);
				// pass to another activity for profile viewing:
				i.putExtra("username", listOfDevelopers.get(position).getUsername());
				i.putExtra("photoURL", listOfDevelopers.get(position).getImgBitmap());
				i.putExtra("userURL", listOfDevelopers.get(position).getProfileURL());
				
				startActivity(i);

			}
		});

	}

	private void displayMessage(String msg) {
		AlertDialog.Builder alertInput = new AlertDialog.Builder(this);
		// alertInput.setTitle("Welcome!");
		alertInput.setInverseBackgroundForced(true);
		// alertInput.setIcon(R.drawable.wecome_image);
		alertInput.setMessage(msg);
		alertInput.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					@SuppressLint("DefaultLocale")
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						arg0.dismiss();

					}
				});
		alertInput.show();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {

		super.onResume();
	}

	/**
	 * retrieve a list of Java Developers in Lagos using the Github API
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private List<UserProfile> getListOfDevelopers() throws JSONException,
			InterruptedException, ExecutionException {

		QueryGitHubServer queryGitHubServer = new QueryGitHubServer();

		return queryGitHubServer.execute(AppConfig.URL_LIST).get();

	}

	/**
	 * retrieves a Java Developer in Lagos using the Github API
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private void getDeveloper() throws JSONException, InterruptedException,
			ExecutionException {

		QueryGitHubServer queryGitHubServer = new QueryGitHubServer();

		// return null;
		// return queryGitHubServer.execute(AppConfig.URL_LIST).get().get(0);

	}

	// Make Http call to retrieve a list of Java Developers in Lagos
	private class QueryGitHubServer extends
			AsyncTask<String, Void, List<UserProfile>> {

		// Creating JSON Parser object
		JSONParser jParser = new JSONParser();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// this method will be running on UI thread
			pdLoading
					.setMessage(" Retrieve a list of Java Developers in Lagos,please wait...");
			pdLoading.show();
		}

		@Override
		protected List<UserProfile> doInBackground(String... URL) {
			List<UserProfile> userProfileList = new ArrayList<UserProfile>();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// make HTTP call and return result
			JSONObject jsonLagosianDevelopers = jParser.makeHttpRequest(URL[0],
					"GET", params);

			if (jsonLagosianDevelopers != null) {
				try {
					if (jsonLagosianDevelopers.getInt("total_count") > 0) {

						if (jsonLagosianDevelopers != null) {

							if (jsonLagosianDevelopers.length() >= 0) {

								JSONArray jsonArray = new JSONArray();

								jsonArray = jsonLagosianDevelopers
										.getJSONArray("items");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject json = jsonArray
											.getJSONObject(i);

									userProfileList.add(new UserProfile(json
											.getString("login"), json
											.getString("html_url"),
											downloadBitmap(json
													.getString("avatar_url"))));
								}

							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return userProfileList;

		}

		@Override
		protected void onPostExecute(List<UserProfile> result) {
			super.onPostExecute(result);

			// this method will be running on UI thread

			pdLoading.dismiss();
		}

	}

	// load user profile photo
	private class LoadImageFromServer extends AsyncTask<String, Void, Bitmap> {

		// Creating JSON Parser object
		// JSONParser jParser = new JSONParser();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// this method will be running on UI thread
			// pdLoading.setMessage("Retrieving user photo,Please wait...");
			// pdLoading.show();
		}

		@Override
		protected Bitmap doInBackground(String... URL) {

			// List<NameValuePair> params = new ArrayList<NameValuePair>();
			// make HTTP call and return result
			return downloadBitmap(URL[0]);

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			// this method will be running on UI thread

			// pdLoading.dismiss();
		}

	}

	// download user profile photo
	private Bitmap downloadBitmap(String url) {
		HttpURLConnection urlConnection = null;
		try {
			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			int statusCode = urlConnection.getResponseCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (Exception e) {
			urlConnection.disconnect();
			displayMessage("Error downloading image from\n " + url
					+ e.getMessage());
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

}
