package tech.bison.trainee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class SpotifyFavouritesPoCApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpotifyFavouritesPoCApplication.class, args);
  }

  @Component
  public class Auth {
    private String accessToken = null;
    private String refreshToken = null;

    public String getAccessToken() {
      return accessToken;
    }

    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }

    public String getRefreshToken() {
      return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
    }
  }

  @Component
  public class TokenRefresher {
    private final SpotifyAuthService spotifyAuthService;
    private final Auth auth;

    public TokenRefresher(SpotifyAuthService spotifyAuthService, Auth auth) {
      this.spotifyAuthService = spotifyAuthService;
      this.auth = auth;
    }

    @Scheduled(fixedRate = 3540 * 1000)
    public void refreshToken() {
      if (auth.getRefreshToken() != null) {
        spotifyAuthService.refreshToken();
      }
    }
  }

}
