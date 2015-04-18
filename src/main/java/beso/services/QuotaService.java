package beso.services;

import java.util.List;

import beso.pojo.Match;

public interface QuotaService {

  List<Match> getMatchesWithoutQuotaFound();

  List<Match> getMatchesWithQuotaFound();
}
