package sqli.com.hulkchallenge.mqtt;

/**
 * Created by rcadel on 06/10/17.
 */

public interface FailureHandler {

    void failure(Throwable exception);
}
