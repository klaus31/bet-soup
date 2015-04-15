package beso.services;

import java.util.List;

import beso.pojo.Match;
import beso.pojo.Quota;

public interface QuotaService {

  List<Match> getMatchesWithoutQuotaFound();

  List<Quota> getQuotasFound();
}
