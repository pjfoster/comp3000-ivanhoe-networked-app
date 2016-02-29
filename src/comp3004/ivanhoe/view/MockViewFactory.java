package comp3004.ivanhoe.view;

import comp3004.ivanhoe.client.AppClient;

public class MockViewFactory implements ViewFactory {
	
	public View createView(AppClient client) {
		return new MockView();
	}
}
