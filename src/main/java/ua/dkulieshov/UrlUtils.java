package ua.dkulieshov;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlUtils {

  static String buildUrl(String command, Map<String, String> parameters) {
    String encodedParameters = buildParametersSuffix(parameters);
    String url = command + encodedParameters;
    return url;
  }

  public static String buildParametersSuffix(Map<String, String> parameters) {
    String parametersPrefix = "?";
    String keyValueDelimiter = "=";
    String keyValuePairsDelimiter = "&";
    String parametersSuffix = "";

    return parameters.keySet().stream()
        .map(key -> key + keyValueDelimiter + encodeValue(parameters.get(key)))
        .collect(Collectors.joining(keyValuePairsDelimiter, parametersPrefix, parametersSuffix));
  }

  private static String encodeValue(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

}
