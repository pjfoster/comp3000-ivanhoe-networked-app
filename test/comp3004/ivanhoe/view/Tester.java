package comp3004.ivanhoe.view;

import comp3004.ivanhoe.util.ClientRequestBuilder;

public class Tester {
	public static void main(String[] args){
		ViewImpl view = new ViewImpl(null,new ClientRequestBuilder(), null, null);
		new StartDisplay(view);
		new WaitingDisplay(view);
		new TwoPlayerTournament(view);
		new ThreePlayerTournament(view);
		new FourPlayerTournament(view);
		new FivePlayerTournament(view);
	}
}
