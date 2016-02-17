package comp3004.ivanhoe.network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   ClientConnectivityTest.class,
   RequestBuilderTest.class,
   ResponseBuilderTest.class
})

public class NetworkTestSuite {   
} 