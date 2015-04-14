package beso.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import beso.laboratory.tools.UrlReader;
import beso.model.Match;
import beso.model.Odds;

abstract class OddsServiceAbstract implements OddsService {

  private final List<Match> matchesWithoutOddsFound = new ArrayList<>();
  private final List<Odds> oddsFound = new ArrayList<>();

  public OddsServiceAbstract(final List<Match> matches) {
    final Map<String, String> urlContents = new HashMap<>();
    for (Match match : matches) {
      final String url = getUrl(match);
      Odds odds = null;
      if (url != null) {
        if (!urlContents.containsKey(url)) {
          urlContents.put(url, getContent(url));
        }
        odds = getOdds(match, urlContents.get(url));
      }
      if (odds == null) {
        matchesWithoutOddsFound.add(match);
      } else {
        oddsFound.add(odds);
      }
    }
  }

  private String getContent(final String url) {
    final String htmlContent = UrlReader.getStringFromUrl(url);
    final String result = Jsoup.parse(htmlContent.replaceAll("(?i)<br[^>]*>", "br2n").replaceAll("(?i)<tr[^>]*>", "br2n")).text();
    return result.replaceAll("br2n", System.lineSeparator());
  }

  @Override
  public List<Match> getMatchesWithoutOddsFound() {
    return matchesWithoutOddsFound;
  }

  abstract Odds getOdds(Match match, String content);

  @Override
  public List<Odds> getOddsFound() {
    return oddsFound;
  }

  abstract String getUrl(Match match);

  protected int endOf(final String regex, final String subject) {
    final Matcher matcher = Pattern.compile(regex).matcher(subject);
    return matcher.find() ? matcher.end() : -1;
  }
}
