package beso.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import beso.pojo.Match;
import beso.pojo.Quota;
import beso.tools.UrlReader;

abstract class QuotaServiceAbstract implements QuotaService {

  private final List<Match> matchesWithoutQuotaFound = new ArrayList<>();
  private final List<Quota> quotasFound = new ArrayList<>();

  public QuotaServiceAbstract(final List<Match> matches) {
    final Map<String, String> urlContents = new HashMap<>();
    for (Match match : matches) {
      final String url = getUrl(match);
      Quota quota = null;
      if (url != null) {
        if (!urlContents.containsKey(url)) {
          urlContents.put(url, getContent(url));
        }
        quota = getQuota(match, urlContents.get(url));
      }
      if (quota == null) {
        matchesWithoutQuotaFound.add(match);
      } else {
        quotasFound.add(quota);
      }
    }
  }

  protected int endOf(final String regex, final String subject) {
    final Matcher matcher = Pattern.compile(regex).matcher(subject);
    return matcher.find() ? matcher.end() : -1;
  }

  private String getContent(final String url) {
    final String htmlContent = UrlReader.getStringFromUrl(url);
    final String result = Jsoup.parse(htmlContent.replaceAll("(?i)<br[^>]*>", "br2n").replaceAll("(?i)<tr[^>]*>", "br2n")).text();
    return result.replaceAll("br2n", System.lineSeparator());
  }

  @Override
  public List<Match> getMatchesWithoutQuotaFound() {
    return matchesWithoutQuotaFound;
  }

  abstract Quota getQuota(Match match, String content);

  @Override
  public List<Quota> getQuotasFound() {
    return quotasFound;
  }

  abstract String getUrl(Match match);
}
