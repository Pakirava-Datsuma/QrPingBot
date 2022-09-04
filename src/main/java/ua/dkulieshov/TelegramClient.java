package ua.dkulieshov;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import ua.dkulieshov.BotId.Cmd;
import ua.dkulieshov.BotId.Param;

public class TelegramClient {

  public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  final BotId botId;

  public TelegramClient(BotId botId) {
    this.botId = botId;
  }

  public Optional<String> getUpdates() {
    String url = botId.buildCmdUrl(Cmd.GET_UPDATES);
    return doGet(url);
  }

  private Optional<String> doGet(String url) {
    HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
    return execute(url, request);
  }

  private Optional<String> execute(String url, HttpRequest request) {
    System.out.println();
    System.out.printf(" < < < %s: %s\n", request.method(), botId.mask(url));
    HttpResponse<String> response = null;
    try {
      response = HTTP_CLIENT.send(request,
                                  responseInfo -> BodySubscribers.ofString(StandardCharsets.UTF_8)
      );
      System.out.println(" > > > : " + botId.mask(response.body()));
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(response).map(HttpResponse::body);
  }

  /*{
    "ok": true,
    "result": [
      {
        "update_id": 713697435,
        "message": {
          "message_id": 3,
          "from": {"id": 221287654, "is_bot": false, "first_name": "Dm\u0443troK", "last_name": "\ud83c\uddfa\ud83c\udde6", "username": "Pakirava_Datsuma", "language_code": "uk"},
          "chat": {"id": -1001650852901, "title": "dv-ftp", "type": "supergroup"},
          "date": 1658613231,
          "text": "/start@swantabot PingGroup",
          "entities": [{"offset": 0, "length": 16, "type": "bot_command"}]
        }
      }
    ]
  }
  */

  public void deleteMessage(String chatId, String messageId) {
    String url = botId.buildCmdUrl(Cmd.DELETE_MESSAGE,
                                   Map.of(Param.CHAT_ID, chatId, Param.MESSAGE_ID, messageId)
    );
    Optional<String> maybeResponse = doGet(url);
    boolean notDeleted = maybeResponse.filter(response -> response.contains("true")).isEmpty();
    if (notDeleted) {
      System.out.println("\n! ! ! ! Not deleted ! ! !\n");
    }
  }

  public Optional<String> getMessageUpdates(String offset) {
    String url = botId.buildCmdUrl(Cmd.GET_UPDATES, Map.of(Param.OFFSET, offset));
    return doGet(url);
  }

  public Optional<String> send(String chatId, String message) {
    String url =
        botId.buildCmdUrl(Cmd.SEND_MESSAGE, Map.of(Param.CHAT_ID, chatId, Param.TEXT, message));
    return doGet(url);
  }

  public void updateMessage(String id, String repeatedMessage) {
    throw new RuntimeException("NotImplementedException");
  }

  public Optional<String> sendPhoto(String chatId, InputStream inputStream, String filename) {
    String url = botId.buildCmdUrl(Cmd.SEND_PHOTO, Map.of(Param.CHAT_ID, chatId));
    return postPhoto(url, inputStream, filename);
  }

  private Optional<String> postPhoto(String url, InputStream inputStream, String filename) {
    HttpEntity entity = MultipartEntityBuilder.create()
                                              // FORM
                                              //        .addPart("name", new StringBody("<Spring Cloud>",
                                              //            ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8)))
                                              // JSON
                                              //        .addPart("info", new StringBody("{\"site\": \"https://www.springcloud.io\"}",
                                              //            ContentType.APPLICATION_JSON))
                                              // FILE
                                              .addBinaryBody(Param.PHOTO.getKey(),
                                                             inputStream,
                                                             ContentType.IMAGE_PNG,
                                                             filename
                                              ).build();
    return postEntity(url, entity);
  }

  /**
   * Use pipeline streams to write the encoded data directly to the network instead of caching it in
   * memory. Because Multipart request bodies contain files, they can cause memory overflows if
   * cached in memory.
   */
  private Optional<String> postEntity(String url, HttpEntity httpEntity) {
    try {
      Pipe pipe = Pipe.open();

      // Pipeline streams must be used in a multi-threaded environment. Using one
      // thread for simultaneous reads and writes can lead to deadlocks.
      new Thread(() -> {
        try (OutputStream outputStream = Channels.newOutputStream(pipe.sink())) {
          // Write the encoded data to the pipeline.
          httpEntity.writeTo(outputStream);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

      HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                                       // The Content-Type header is important, don't forget to set it.
                                       .header(HttpHeaders.CONTENT_TYPE,
                                               httpEntity.getContentType().getValue()
                                       )
                                       // Reads data from a pipeline stream.
                                       .POST(BodyPublishers.ofInputStream(() -> Channels.newInputStream(
                                           pipe.source()))).build();

      return execute(url, request);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public String getMe() {
    String url = botId.buildCmdUrl(Cmd.GET_ME);
    return doGet(url).orElseThrow();
  }
}
