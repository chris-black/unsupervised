package serialized;

import java.util.List;

import com.google.common.collect.Lists;

import models.Team;

public class FlatTeam {
	public String name;
	public String[] iterations;
	
	public FlatTeam(Team team) {
		this.name = team.name;
		iterations = new String[1];
		iterations[0] = team.iterations.get(0).name;
	}
	
	public static List<FlatTeam> flatTeams(List<Team> teams) {
		List<FlatTeam> flats = Lists.newArrayList();
		for (Team team : teams) {
			flats.add(new FlatTeam(team));
		}
		return flats;
	}
}
