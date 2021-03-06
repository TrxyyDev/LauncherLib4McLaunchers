package fr.trxyy.authentication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import fr.trxyy.account.Account;
import fr.trxyy.interfaces.utils.OSUtil;
import fr.trxyy.interfaces.utils.Wrapper;

public class GameAuth {

	public String USERNAME = "";
	public String PASSWORD = "";
	public static boolean isAuthed = false;
	private Random rand;

	public GameAuth(String userName, String passWord, boolean realAuth) {
		if (realAuth) {
			USERNAME = userName;
			PASSWORD = passWord;
		} else {
			rand = new Random();
			Account.setUsername(userName);
			Account.setAccessToken("theAccessTokenForPlayer" + rand.nextInt(10000));
			Account.setClientToken("theClientTokenForPlayer" + rand.nextInt(10000));
			Account.setUuID("theUuIdForPlayer" + rand.nextInt(10000));
			Account.setUserType("legacy");
			isAuthed = true;
		}
	}

	public void tryLogin() {
		Wrapper.log("Try login...");
		connectMinecraft(USERNAME, PASSWORD);
	}

	public static String[] reConnectMinecraft() {
		InputStream is = null;

		String uuid = null;
		String username = null;
		String name = null;
		String accessToken = null;
		String clientToken = null;
		if (new File(OSUtil.getDirectory() + "/lastlogin.profil").exists()) {
			try {
				is = new FileInputStream(OSUtil.getDirectory() + "/lastlogin.profil");
			} catch (IOException e) {
				e.printStackTrace();
			}
			DataInputStream dis = new DataInputStream(is);
			try {
				uuid = dis.readLine();
				name = dis.readLine();
				accessToken = dis.readLine();
				clientToken = dis.readLine();
				username = dis.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if ((uuid == null) || (name == null) || (accessToken == null) || (clientToken == null) || (username == null)) {
				return null;
			}
		} else {
			return null;
		}
		DataInputStream dis;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost("https://authserver.mojang.com/refresh");
			StringEntity params = new StringEntity("{\"accessToken\":\"" + accessToken + "\",\"clientToken\":\""
					+ clientToken + "\",\"selectedAccount\":{\"id\":\"" + uuid + "\",\"name\":\"" + username + "\"}}");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			String[] arrayOfString1;
			if (result.toString().contains("Access token already has a Account assigned")) {
				String[] r = { name, accessToken, uuid, clientToken };

				request = new HttpPost("https://authserver.mojang.com/validate");
				params = new StringEntity("{\"accessToken\":\"" + accessToken + "\"}");
				request.addHeader("content-type", "application/json");
				request.setEntity(params);

				response = httpClient.execute(request);
				if (response.getEntity() == null) {
					return r;
				}
				rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				result = new StringBuffer();
				line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				if (result.toString().contains("Invalid token")) {
					String[] s = new String[4];
					return s;
				}
				return r;
			}
			return getResult(result.toString(), username);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String[] connectMinecraft(String username, String password) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost("https://authserver.mojang.com/authenticate");
			StringEntity params = new StringEntity("{\"agent\":{\"name\":\"Minecraft\",\"version\":1},\"username\":\""
					+ username + "\",\"password\":\"" + password + "\"}", ContentType.create("application/json"));
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			Wrapper.log("Getting result...");
			return getResult(result.toString(), username);
		} catch (Exception localException) {
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String[] getResult(String r, String username) {
		r = r.replace("\"", "");
		r = r.replace("{", "");
		r = r.replace("}", "");
		r = r.replace("[", "");
		r = r.replace("]", "");
		String accessToken = "";
		String name = "";
		String uuId = "";
		String clientToken = "";
		String[] split = r.split(",");
		for (int i = 0; i < split.length; i++) {
			if (split[i].startsWith("accessToken:")) {
				accessToken = split[i].replace("accessToken:", "");
			} else if (split[i].startsWith("id:")) {
				uuId = split[i].replace("id:", "");
			} else if (split[i].startsWith("clientToken:")) {
				clientToken = split[i].replace("clientToken:", "");
			} else if (split[i].startsWith("name:")) {
				name = split[i].replace("name:", "");
			}
		}
		String[] results = { name, accessToken, clientToken, uuId };
		String u = insertAt(clientToken, 8, "-");
		String v = insertAt(u, 12 + 1, "-");
		String k = insertAt(v, 16 + 2, "-");
		String accessTokenModif = insertAt(k, 20 + 3, "-");
		String u2 = insertAt(uuId, 8, "-");
		String v2 = insertAt(u2, 12 + 1, "-");
		String k2 = insertAt(v2, 16 + 2, "-");
		String uu2 = insertAt(k2, 20 + 3, "-");
		isAuthed = true;
		Account.setUsername(results[0]);
		Account.setAccessToken(results[1]);
		Account.setClientToken(accessTokenModif);
		Account.setUuID(results[3]);
		return results;
	}

	public static String insertAt(final String target, final int position, final String insert) {
		final int targetLen = target.length();
		if (position < 0 || position > targetLen) {
			throw new IllegalArgumentException("position=" + position);
		}
		if (insert.isEmpty()) {
			return target;
		}
		if (position == 0) {
			return insert.concat(target);
		} else if (position == targetLen) {
			return target.concat(insert);
		}
		final int insertLen = insert.length();
		final char[] buffer = new char[targetLen + insertLen];
		target.getChars(0, position, buffer, 0);
		insert.getChars(0, insertLen, buffer, position);
		target.getChars(position, targetLen, buffer, position + insertLen);
		return new String(buffer);
	}
	
	public static boolean isLogged() {
		return isAuthed;
	}
	
}
