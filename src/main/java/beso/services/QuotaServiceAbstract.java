package beso.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import beso.pojo.Match;
import beso.tools.UrlReader;

abstract class QuotaServiceAbstract implements QuotaService {

  private final List<Match> matchesWithoutQuotaFound = new ArrayList<>();
  private final List<Match> matchesWithQuotaFound = new ArrayList<>();

  public QuotaServiceAbstract(final List<Match> matchesWithoutQuota) {
    final Map<String, String> urlContents = new HashMap<>();
    for (Match matchWithoutQuota : matchesWithoutQuota) {
      final String url = getUrl(matchWithoutQuota);
      Match matchWithQuote = null;
      if (url != null) {
        if (!urlContents.containsKey(url)) {
          urlContents.put(url, getContent(url));
        }
        matchWithQuote = getMatchWithQuota(matchWithoutQuota, urlContents.get(url));
      }
      if (matchWithQuote == null) {
        matchesWithoutQuotaFound.add(matchWithoutQuota);
      } else {
        matchesWithQuotaFound.add(matchWithQuote);
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

  abstract Match getMatchWithQuota(Match match, String content);

  @Override
  public List<Match> getMatchesWithQuotaFound() {
    return matchesWithQuotaFound;
  }

  abstract String getUrl(Match match);
}
