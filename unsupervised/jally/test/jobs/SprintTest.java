package jobs;

import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

public class SprintTest {
	 
	 @Test
	 public void checkIteration() {
	     assertThat(new Sprint().isSprintDay("2012-05-21", "2012-05-24")).isTrue();
	     assertThat(new Sprint().isSprintDay("2012-05-14", "2012-05-24")).isFalse();
	 }
}
