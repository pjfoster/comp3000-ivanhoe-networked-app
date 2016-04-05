package comp3004.ivanhoe.view.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.view.MockViewFactory;

public class TestView {

	public static void main(String[] args) {
		//AppClient client = new AppClient()
		GUIView view;
		AppClient client = new AppClient(new MockViewFactory(), "1.2.3.4", 10001);
		view = new GUIView(client);

		JFrame frame = new JFrame();
		frame.setSize(new Dimension(800,600));
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		
		frame.add(new GameWonPanel());
		frame.setVisible(true);
		
	}

}
