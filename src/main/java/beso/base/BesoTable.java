package beso.base;

import java.util.List;

import beso.pojo.Match;
import beso.pojo.Quota;

public interface BesoTable {

  void add(final List<Object> contentCols);

  void add(final Object... contentCols);

  void addContentCols(final Match match, final boolean withResult);

  void addContentCols(final Quota quota);

  void addHeaderCols(final List<Object> headerCols);

  void addHeaderCols(final Object... headerCols);

  void addHeaderColsForMatch(final boolean withResult);

  void addHeaderColsForQuota();

  void addHeadline(final Object headline);

  void clear();

  String getOutput();

  void print();

  void setNoHeaderColumns(final int withColumns);
}
