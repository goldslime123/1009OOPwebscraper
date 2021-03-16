package MainProgram;
import twitter4j.Status;

interface Database {
	
	public void store(Status tweet);

}
