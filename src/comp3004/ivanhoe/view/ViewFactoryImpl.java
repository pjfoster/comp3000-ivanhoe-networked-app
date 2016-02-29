package comp3004.ivanhoe.view;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;

public class ViewFactoryImpl implements ViewFactory {
	public View createView(AppClient client) {
		return new TextViewImpl(client, new ClientRequestBuilder());
	}
}
