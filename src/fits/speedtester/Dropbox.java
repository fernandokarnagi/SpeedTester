/**
 * Reference https://www.dropbox.com/developers-v1/core/start/java
 * 
 */
package fits.speedtester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

public class Dropbox {
	public static void main(String[] args) throws Exception {
		// Get your app key and secret from the Dropbox developers website.
		final String APP_KEY = "97vqw8f8tijl6rz";
		final String APP_SECRET = "0nfpcy8gobnmzmb";

		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		DbxRequestConfig config = new DbxRequestConfig("FKFileDPApp", Locale.getDefault().toString());

		String accessToken = "y4qJ-jD1WBgAAAAAAAArtx9Wp2cBCzbKZWFM9DmieiIHgQZyXgEXbyt0KrIirv2R";

		if (accessToken == null) {
			// Only when the accessToken is empty, we need to generate new one
			DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
			// Have the user sign in and authorize your app.
			String authorizeUrl = webAuth.start();
			System.out.println("1. Go to: " + authorizeUrl);
			System.out.println("2. Click \"Allow\" (you might have to log in first)");
			System.out.println("3. Copy the authorization code.");
			String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
			System.out.println(webAuth);

			DbxAuthFinish authFinish = webAuth.finish(code);
			accessToken = authFinish.accessToken;

		}

		DbxClient client = new DbxClient(config, accessToken);
		System.out.println("Linked account: " + client.getAccountInfo().displayName);

		// Uploading files
		File inputFile = new File("/Users/fernando/Work/Apps/SP/sample_file.lic");
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
			System.out.println("Uploading file...");
			DbxEntry.File uploadedFile = client.uploadFile("/sample_file.lic", DbxWriteMode.add(), inputFile.length(), inputStream);
			System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
			inputStream.close();
		}
	}
}
