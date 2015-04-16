package beso.base;

import java.util.List;

public interface BesoTable {

  void add(final List<Object> contentCols);

  void add(final Object... contentCols);

  void addHeaderCols(final List<Object> headerCols);

  void addHeaderCols(final Object... headerCols);

  void addHeadline(final Object headline);

  void clear();

  String getOutput();

  void print();

  void setNoHeaderColumns(final int withColumns);
}
