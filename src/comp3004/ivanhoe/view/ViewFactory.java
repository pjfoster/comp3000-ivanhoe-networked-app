package comp3004.ivanhoe.view;

import comp3004.ivanhoe.client.AppClient;

public interface ViewFactory {
	public View createView(AppClient client);
}
