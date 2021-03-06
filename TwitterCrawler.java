import twitter4j.*;
import twitter4j.conf.*;
import java.util.List;
import java.util.ArrayList;

//https://www.byteplusone.com/searching-twitter-with-java-twitter4j
public class TwitterCrawler {

	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("SwEnOs3wjXJZ3C7hIVuXG2xuR")
				.setOAuthConsumerSecret("GE7S0xHjBu0V9hBw63IhmDia2g71mZpUXjg851ipMYgTzdyuWN")
				.setOAuthAccessToken("1364017893147439106-ieVSnBE3qKtGwnQxk8npxf9SC2Eloj")
				.setOAuthAccessTokenSecret("T8D7vCZtPrLiYGR5DaVh7jfjEASG2V4rZDefMdyIpFUfI");

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		try {
			Query query = new Query("#GME" + "-filter:retweets -filter:links");
			query.setCount(100);
			QueryResult result;
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			
			for (Status tweet : tweets) {
				System.out.println("Time: "+ tweet.getCreatedAt() + "- @ " + tweet.getUser().getScreenName() + " - " + tweet.getText());
			}

			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
	}
}