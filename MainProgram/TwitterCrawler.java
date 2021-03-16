package MainProgram;

import twitter4j.*;
import twitter4j.conf.*;
import java.sql.*;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

import java.util.List;
import java.util.ArrayList;

public class TwitterCrawler extends Crawl implements Database {

	public void crawl() {
		int totalTweets = 300;
		long lastID = Long.MAX_VALUE;

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("SwEnOs3wjXJZ3C7hIVuXG2xuR")
				.setOAuthConsumerSecret("GE7S0xHjBu0V9hBw63IhmDia2g71mZpUXjg851ipMYgTzdyuWN")
				.setOAuthAccessToken("1364017893147439106-ieVSnBE3qKtGwnQxk8npxf9SC2Eloj")
				.setOAuthAccessTokenSecret("T8D7vCZtPrLiYGR5DaVh7jfjEASG2V4rZDefMdyIpFUfI");

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		Query query = new Query("GME" + "-filter:retweets -filter:links -filter:images");
		query.setLocale("en");
		query.setLang("en");
		ArrayList<Status> tweets = new ArrayList<Status>();

		while (tweets.size() < totalTweets) {
			if (totalTweets - tweets.size() > 100)
				query.setCount(100);
			else
				query.setCount(totalTweets - tweets.size());

			try {
				QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				for (Status tweet : tweets) {
					System.out.println("Time: " + tweet.getCreatedAt() + "- @ " + tweet.getUser().getScreenName()
							+ " - " + tweet.getText() + " << " + tweet.getRetweetCount() + " , "
							+ tweet.getFavoriteCount() + " >> ");

					if (tweet.getId() < lastID)
						lastID = tweet.getId();
				}
			}catch(TwitterException te){
				te.printStackTrace();
				System.out.println("Failed to search tweets: " + te.getMessage());
			}
			query.setMaxId(lastID-1);
		}
	}
	//parses tweet.getText() into NLP pipeline
	
	public void analyse(Status tweet) {
	StanfordCoreNLP stanfordCoreNLP =pipeline.getPipeline();
	CoreDocument coreDocument =new CoreDocument(tweet.getText());
	stanfordCoreNLP.annotate(coreDocument);
	List<CoreSentence> sentences =coreDocument.sentences();
	for(CoreSentence sentence : sentences) {
		String sentiment = sentence.sentiment();
		System.out.println(sentiment);

	}
}

	public void store(Status tweet, CoreDocument coreDocument) {

		// setting up JDBC connection
		Connection conn = null;
		Statement stmt = null;

		String jdbcurl = "jdbc:mysql://localhost:3306/stockdb";
		String username = "root";
		String password = "qwe123";

		try {
			// Opening a connection
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(jdbcurl, username, password);
			System.out.println(tweet);

			// EDIT THIS
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			// writes query into SQL database
			String sql = " INSERT INTO stock" + "(Date_created,Tweet,Screen_name,Sentiments)" + " VALUES" + "('"
					+ tweet.getCreatedAt() + "','" + tweet.getText() + "', '" + tweet.getUser().getScreenName() + "', '" + coreDocument.sentences() + "')";

			// String sql = "insert into stockdb(Date_created,Tweet,Sentiment,)" + " VALUES
			// ('"+status.getCreatedAt() +
			// "," +status.getText() ")";

			// updates query into MySQL database
			System.out.println(sql);
			stmt.executeUpdate(sql);
			System.exit(0);
			System.out.println("Records inserted into SQL Database");

			// close write sequence into SQL database
			System.exit(0);
			stmt.close();
			conn.close();
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // exeption errors if resources cannot be closed
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		}
	}

	@Override
	public void store(Status tweet) {
		// TODO Auto-generated method stub
		
	}
}