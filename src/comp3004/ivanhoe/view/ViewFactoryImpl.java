package comp3004.ivanhoe.view;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.view.gui.GUIView;

public class ViewFactoryImpl implements ViewFactory {
	public View createView(AppClient client) {
		return new GUIView(client, new ClientRequestBuilder());
	}
}
