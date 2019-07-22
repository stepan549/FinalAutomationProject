import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.After;
import org.junit.runner.RunWith;
import patterns.DriverSingleton;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features/Resend.feature")
public class ResendTest {
}
