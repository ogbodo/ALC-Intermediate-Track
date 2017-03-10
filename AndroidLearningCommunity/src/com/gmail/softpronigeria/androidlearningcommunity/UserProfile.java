package com.gmail.softpronigeria.androidlearningcommunity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UserProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String profileURL;
	private Bitmap imgBitmap;

	public UserProfile(String username, String profileURL, Bitmap imgBitmap) {
		super();
		this.username = username;
		this.profileURL = profileURL;
		this.imgBitmap = imgBitmap;
	}

	public UserProfile(String username, Bitmap imgBitmap) {
		super();
		this.username = username;
		this.imgBitmap = imgBitmap;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileURL() {
		return profileURL;
	}

	public void setProfileURL(String profileURL) {
		this.profileURL = profileURL;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

		byte[] byteArray = stream.toByteArray();

		out.writeInt(byteArray.length);
		out.write(byteArray);

		imgBitmap = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);

	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		int bufferLength = in.readInt();

		byte[] byteArray = new byte[bufferLength];

		int pos = 0;
		do {
			int read = in.read(byteArray, pos, bufferLength - pos);

			if (read != -1) {
				pos += read;
			} else {
				break;
			}

		} while (pos < bufferLength);

		imgBitmap = BitmapFactory.decodeByteArray(byteArray, 0, bufferLength);

	}

}
