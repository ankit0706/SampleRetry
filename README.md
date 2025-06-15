The retry pattern is quite useful for a number of scenarios in microservices architecture.

What does it do?
Simply told, it helps us retry a failed operation. For example we have to hit a web service and get some data to store in db. Our request to web service could fail because of a network issue. Now, instead of failing the operation after a single hit, we retry the action, say 3 times with an interval of 1 second in between the retry attempts. This is very helpful because most of the times these network issues are transient in nature, meaning they fail one second and are back the next second.

What are appropriate use cases for using Retry pattern?
Retry should be used for scenarios involving network connectivity or I/O operations. Spring recommends:

Errors that are susceptible to intermittent failure are often transient in nature. Examples include remote calls to a web service that fails because of a network glitch or a DeadlockLoserDataAccessException in a database update.

How to use Spring Retry?
Step 1: Add the following dependencies to your pom.xml

    <dependency>
      <groupId>org.springframework.retry</groupId>
      <artifactId>spring-retry</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aspects</artifactId>
    </dependency>
    
Step 2: Take any configuration class in your code and annotate it with Enable retry

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class CustomConfig {
}
This annotation can also be used on the main class which starts the spring boot application.

Step 3: Configure the method which we want to retry

@Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay=1000))
public String downloadAndSendData(String countryName) throws RuntimeException {
    if(countryName.equalsIgnoreCase("England")) {
        // since England has never won the ICC Test Championship, we simulate a failure
        throw new RuntimeException("Failed to download data for England");
    } else if(countryName.equalsIgnoreCase("Australia")) {
        return "Won ICC Test Championship 2023";
    } else if(countryName.equalsIgnoreCase("New Zealand")) {
        return "Won ICC Test Championship 2021";
    } else{
        return "Country yet to win ICC Test Championship";
    }

}

Here, we return some hard coded values for simplicity, but we can assume that to get the values we hit another microservice which may fail.

Now, the method has retryFor = RuntimeException.class and the same exception is being thrown by the method. This is very important as retry won’t be triggered if the exception is caught and handled by the method and not thrown.

maxAttempts tell us how many times the retry will be tried. 3 is the default value.

delay tells us the time in milliseconds between successive retry attempts. A value of 1000 means 1 second.

So, in above setup, when a RunTimeException occurs, the method will be invoked again for a total of 3 times with a gap of 1 second in between the invocations.

What happens after the max retry attempts are over and exception is thrown again?
In this scenario, a recovery method is called which will have the thrown exception in its argument. The return type of this recovery method should be the same as that of the actual method (recommended). All we need is to annotate the method with Recover.

@Recover
public String getMessageAfterMaxAttempts(RuntimeException e) {
    return "Max attempts reached. Please try again later.";
}

In which cases retry won’t be triggered?

Retry won’t be triggered if there is internal calling of method. It means if the retryable method is called from the same class in which it exists, retry mechanism won’t come into action. So, the retryable method and its calling method must exist in separate classes.

Retry mechanism doesn’t work with JUnit tests so we can skip trying the same.

More information can be found here https://github.com/spring-projects/spring-retry
