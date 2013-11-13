/*
 * A really hugue thanks to gravitylow for providing this example class :)
 * https://github.com/gravitylow/ServerModsAPI-Example
 */

package me.darcade.minecraftlottery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateChecker {

	// The project's unique ID
	private final int projectID;

	// An optional API key to use, will be null if not submitted
	private final String apiKey;

	// Keys for extracting file information from JSON response
	private static final String API_NAME_VALUE = "name";
	private static final String API_LINK_VALUE = "downloadUrl";
	// private static final String API_RELEASE_TYPE_VALUE = "releaseType";
	// private static final String API_FILE_NAME_VALUE = "fileName";
	// private static final String API_GAME_VERSION_VALUE = "gameVersion";

	// Static information for querying the API
	private static final String API_QUERY = "/servermods/files?projectIds=";
	private static final String API_HOST = "https://api.curseforge.com";

	/**
	 * Check for updates anonymously (keyless)
	 * 
	 * @param projectID
	 *            The BukkitDev Project ID, found in the "Facts" panel on the
	 *            right-side of your project page.
	 */
	public UpdateChecker(int projectID) {
		this(projectID, null);
	}

	/**
	 * Check for updates using your Curse account (with key)
	 * 
	 * @param projectID
	 *            The BukkitDev Project ID, found in the "Facts" panel on the
	 *            right-side of your project page.
	 * @param apiKey
	 *            Your ServerMods API key, found at
	 *            https://dev.bukkit.org/home/servermods-apikey/
	 */
	public UpdateChecker(int projectID, String apiKey) {
		this.projectID = projectID;
		this.apiKey = apiKey;

		query();
	}

	/**
	 * Query the API to find the latest approved file's details.
	 */
	public String[] query() {
		URL url = null;
		String[] output = new String[2];

		try {
			// Create the URL to query using the project's ID
			url = new URL(API_HOST + API_QUERY + projectID);
		} catch (MalformedURLException e) {
			// There was an error creating the URL

			e.printStackTrace();
			return null;
		}

		try {
			// Open a connection and query the project
			URLConnection conn = url.openConnection();

			if (apiKey != null) {
				// Add the API key to the request if present
				conn.addRequestProperty("X-API-Key", apiKey);
			}

			// Add the user-agent to identify the program
			conn.addRequestProperty("User-Agent",
					"ServerModsAPI-Example (by Gravity)");

			// Read the response of the query
			// The response will be in a JSON format, so only reading one line
			// is necessary.
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String response = reader.readLine();

			// Parse the array of files from the query's response
			JSONArray array = (JSONArray) JSONValue.parse(response);

			if (array.size() > 0) {
				// Get the newest file's details
				JSONObject latest = (JSONObject) array.get(array.size() - 1);

				// Get the version's title
				String versionName = (String) latest.get(API_NAME_VALUE);

				// Get the version's link
				String versionLink = (String) latest.get(API_LINK_VALUE);

				output[0] = versionName.replaceAll("[A-Za-z ]", "");
				output[1] = versionLink;

				return output;
			} else {
				System.out.println("There are no files for this project");
			}
		} catch (IOException e) {
			// There was an error reading the query

			e.printStackTrace();
			return null;
		}

		return null;
	}
}
