package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import comp3004.ivanhoe.game_logic.GameLogicTestSuite;
import comp3004.ivanhoe.network.NetworkTestSuite;

public class IvanhoeTestRunner {
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(NetworkTestSuite.class, GameLogicTestSuite.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}
