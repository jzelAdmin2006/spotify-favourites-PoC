package tech.bison.trainee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class SpotifyService {
  private static final String SPOTIFY_API_URL = "https://api.spotify.com/v1/me/tracks";

  public List<String> getFavoriteTrackIds(String accessToken) {
    OkHttpClient client = new OkHttpClient();
    List<String> trackIds = new ArrayList<>();
    int limit = 50;
    int offset = 0;

    while (true) {
      String url = SPOTIFY_API_URL + "?limit=" + limit + "&offset=" + offset;

      Request request = new Request.Builder().url(url)
          .addHeader("Authorization", "Bearer " + accessToken)
          .get()
          .build();

      try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful()) {
          JSONObject jsonResponse = new JSONObject(response.body().string());
          JSONArray items = jsonResponse.getJSONArray("items");

          if (items.length() == 0) {
            break;
          }

          for (int i = 0; i < items.length(); i++) {
            JSONObject track = items.getJSONObject(i).getJSONObject("track");
            trackIds.add(track.getString("id"));
          }

          offset += limit;
        } else {
          throw new RuntimeException("Failed to fetch tracks from Spotify: " + response.message());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return trackIds;
  }
}
