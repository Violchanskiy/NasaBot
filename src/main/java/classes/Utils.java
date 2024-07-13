package classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class Utils {
  public String getUrl() {
    String url =
        "https://api.nasa.gov/planetary/apod?api_key=f7avXNhXy6L1R7X37c1jLR710oX6waoYZpevtRYw";
    return connect(url);
  }

  public String getUrl(String date) {
    String url =
        "https://api.nasa.gov/planetary/apod?api_key=f7avXNhXy6L1R7X37c1jLR710oX6waoYZpevtRYw&date="
            + date;
    return connect(url);
  }

  private String connect(String url) {
    CloseableHttpClient client = HttpClients.createDefault();
    ObjectMapper mapper = new ObjectMapper();
    HttpGet request = new HttpGet(url);
    CloseableHttpResponse response = null;
    try {
      response = client.execute(request);
      NasaObject answer = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
      return answer.getUrl();
    } catch (IOException e) {
      return null;
    }
  }
}
