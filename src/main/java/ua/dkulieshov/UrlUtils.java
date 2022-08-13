package ua.dkulieshov;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UrlUtils<T> {

  private final Function<T, String> paramStringifier;

  public UrlUtils(Function<T, String> paramStringifier) {
    this.paramStringifier = paramStringifier;
  }

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

  public String buildTParametersSuffix(Map<T, String> paramMap) {
    Map<String, String> map = transformKeys(paramMap);
    return buildParametersSuffix(map);
  }

  private Map<String, String> transformKeys(Map<T, String> paramMap) {
    Map<String, String> map = paramMap.entrySet().stream()
        .collect(Collectors.toMap(e -> paramStringifier.apply(e.getKey()), e -> e.getValue()));
    return map;
  }
}
