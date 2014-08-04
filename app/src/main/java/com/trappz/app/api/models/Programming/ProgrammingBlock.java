package com.trappz.app.api.models.Programming;

import java.util.List;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class ProgrammingBlock {

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    private String blockId;

    private String dateTime;

    private String tickets;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public List<String> getMatches() {
        return matches;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public String getTbdTime() {
        return tbdTime;
    }

    public void setTbdTime(String tbdTime) {
        this.tbdTime = tbdTime;
    }

    public String getLeagueColor() {
        return leagueColor;
    }

    public void setLeagueColor(String leagueColor) {
        this.leagueColor = leagueColor;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private List<String> matches;

    private String leagueId;

    private String tournamentId;

    private String tournamentName;

    private String significance;

    private String tbdTime;

    private String leagueColor;

    private String week;

    private String label;

}
