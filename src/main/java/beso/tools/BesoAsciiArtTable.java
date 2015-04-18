package beso.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import beso.pojo.Match;
import beso.pojo.Quota;

@Primary
@Component
public class BesoAsciiArtTable extends AsciiArtTable {

  @Autowired
  private PrintStream defaultPrintStream;

  public BesoAsciiArtTable() {
    super();
    this.setBorderCharacters("┏━┯┓┃┠─┬┨┿┣┫│┗┷┛┼");
  }

  public void addContentCols(final Match match, final boolean withResult) {
    final List<Object> contentCols = new ArrayList<>();
    contentCols.add(BesoFormatter.format(match.getStart()));
    contentCols.add(match.getTeam1().getName());
    if (withResult) {
      contentCols.add(match.getGoalsTeam1() == null ? "-" : match.getGoalsTeam1() + "");
      contentCols.add(match.getGoalsTeam2() == null ? "-" : match.getGoalsTeam2() + "");
    }
    contentCols.add(match.getTeam2().getName());
    this.add(contentCols);
  }

  public void addContentCols(final Quota quota) {
    final List<Object> contentCols = new ArrayList<>();
    contentCols.add(BesoFormatter.format(quota.getRateTeam1()));
    contentCols.add(BesoFormatter.format(quota.getRateTeam1()));
    contentCols.add(BesoFormatter.format(quota.getRateTeam1()));
    this.add(contentCols);
  }

  public void addHeaderColsForMatch(final boolean withResult) {
    final List<Object> headerCols = new ArrayList<>();
    headerCols.add("kick-off");
    headerCols.add("team 1");
    if (withResult) {
      headerCols.add("goals 1");
      headerCols.add("goals 2");
    }
    headerCols.add("team 2");
    this.addHeaderCols(headerCols);
  }

  public void addHeaderColsForQuota() {
    final List<Object> headerCols = new ArrayList<>();
    headerCols.add("quota 1");
    headerCols.add("quota X");
    headerCols.add("quota 2");
    this.addHeaderCols(headerCols);
  }

  public void print() {
    this.print(defaultPrintStream);
  }
}
