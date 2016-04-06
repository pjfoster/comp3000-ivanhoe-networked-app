package comp3004.ivanhoe.util;

public class Strings {
	public static String connection_accepted_waiting = "<html>You are now connected! <br> Please wait for more players to join.</html>";
	public static String connection_rejected = "<html>Connection rejected!<br>Either the server has maxed out its player, or there's a problem with your connection</html>";
	
	public static String your_turn_to_play = "<html>It's your turn to play!<br>Pick one or more cards</html>";
	public static String choose_color = "<html>You are playing a supporter card as your first move - which means you can pick the colour of the tournament: </html>";
	public static String choose_token = "<html>Choose a token:</html>";
	public static String change_tournament_color = "<html>You are changing the colour of the tournament! Please chooe a new colour</html>";
	
	public static String action_card_played = "<html>%s played the action card %s</html>";
	
	public static String pick_opponent_card = "<html>Pick one of your opponent's cards:</html>";
	public static String pick_own_card = "<html>Pick one of your own cards:</html>";
	
	public static String invalid_turn = "<html>Invalid move. Please try again.</html>";
	public static String invalid_colour = "<html>Invalid colour. Please try again.</html>";
	public static String invalid_opponent = "<html>Invalid opponent - they are not participating in the current tournament. Please try again.</html>";

	public static String tournament_loss = "<html>%s won the tournament! A new tournament begins...</html>";
	public static String tournament_won = "<html>You win the tournament! You get a new token.</html>";
	public static String tournament_won_purple = "<html>You won a jousting tournament! That means you can select the color of the token you'd like to receive</html>";

	public static String game_over_loss = "<html>The winner is %s. <br><br> Better luck next time.</html>";
	public static String game_over_win = "<html>Congratulations! You won the game!<br><br>Thank you for playing.</html>";
}
