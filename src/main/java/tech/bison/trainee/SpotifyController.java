package tech.bison.trainee;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kotlin.Pair;
import tech.bison.trainee.SpotifyFavouritesPoCApplication.Auth;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {
  private final SpotifyService spotifyService;
  private final SpotifyAuthService spotifyAuthService;
  private final Auth auth;

  public SpotifyController(SpotifyService spotifyService, SpotifyAuthService spotifyAuthService, Auth auth) {
    this.spotifyService = spotifyService;
    this.spotifyAuthService = spotifyAuthService;
    this.auth = auth;
  }

  @GetMapping("/favorite-tracks")
  public List<String> getFavoriteTracks() {
    String accessToken = auth.getAccessToken();
    if (accessToken == null) {
      throw new RuntimeException("User is not authenticated with Spotify.");
    }
    return spotifyService.getFavoriteTrackIds(accessToken);
  }

  @GetMapping("/token")
  public ResponseEntity<Void> initiateAuthentication() {
    URI spotifyAuthURI = spotifyAuthService.getAuthenticationURI();
    return ResponseEntity.status(HttpStatus.FOUND).location(spotifyAuthURI).build();
  }

  @GetMapping("/callback")
  public ResponseEntity<String> callback(@RequestParam String code) {
    Pair<String, String> tokens = spotifyAuthService.getTokensFromCode(code);
    auth.setAccessToken(tokens.getFirst());
    auth.setRefreshToken(tokens.getSecond());
    return ResponseEntity.ok("Authentication successful!");
  }

}
