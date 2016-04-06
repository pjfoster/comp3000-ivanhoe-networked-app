package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import comp3004.ivanhoe.action_cards.ActionCardTestSuite;
import comp3004.ivanhoe.ai.AITestSuite;
import comp3004.ivanhoe.controller.ControllerTestSuite;
import comp3004.ivanhoe.game_logic.GameLogicTestSuite;
import comp3004.ivanhoe.game_scenarios.GameScenariosTestSuite;
import comp3004.ivanhoe.network.NetworkTestSuite;

public class IvanhoeTestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(NetworkTestSuite.class, 
											 GameLogicTestSuite.class,
											 ActionCardTestSuite.class,
											 ControllerTestSuite.class,
											 GameScenariosTestSuite.class,
											 AITestSuite.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}
