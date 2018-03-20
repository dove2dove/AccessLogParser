package com.ef;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ParserTests {
  	
    @Autowired
    private Parser parser;
	
    @Rule
    public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testMainForLogfile() throws Exception {
		//Log file with wrongly formated date
		parser.run("--accesslog=/Users/dove/logfiles/wallethub/accesswrongdate.log", "--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("Exception Error Message"));
		//log file does not exist
		parser.run("--accesslog=/Users/dove/logfiles/wallethub/access3.log", "--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : accesslog"));
		//Threshold value not provided
		Parser.main(new String[] {"--accesslog=/Users/dove/logfiles/wallethub/access2.log", "--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold= "});
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : threshold"));
		//duration value not provided
		Parser.main(new String[] {"--accesslog=/Users/dove/logfiles/wallethub/access2.log", "--startDate=2017-01-01.13:00:00","--duration= ","--threshold=100"});
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : duration"));
		//startDate value not provided
		Parser.main(new String[] {"--accesslog=/Users/dove/logfiles/wallethub/access2.log", "--startDate= ","--duration=hourly","--threshold=100"});
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : startDate"));
		//accessLog value not provided
		parser.run("--accesslog= ", "--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : accesslog"));
		//correct required 4 argument and Values for hourly
		parser.run("--accesslog=/Users/dove/logfiles/wallethub/access.log", "--startDate=2017-01-01.13:00:00","--duration=daily","--threshold=250");
		assertTrue(outputCapture.toString().contains("Request processed successfully"));
	}
    
	@Test
	public void testMainNoLogfileSuccess() throws Exception {
		//correct required 3 argument and Values for hourly
		parser.run("--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("Request processed successfully"));
		//correct required 3 argument and Values for daily
		Parser.main(new String[] {"--startDate=2017-01-01.13:00:00","--duration=daily","--threshold=250"});
		assertTrue(outputCapture.toString().contains("Request processed successfully"));
	}	

	@Test
	public void testMainNoLogfileFails() throws Exception {
		//Wrong duration Value
		parser.run("--startDate=2017-01-01.13:00:00","--duration=HOURLY","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : duration"));
		//Wrong StateDate Value
		parser.run("--startDate=2017-01-01 13:00:00","--duration=daily","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : startDate"));
		//Requested StartDate data not loaded 
		parser.run("--startDate=2017-01-04.13:00:00","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("No Data Found"));
		//Threshold value not a number
		parser.run("--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold=1pp");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : threshold"));
		//duration Value not provided
		parser.run("--startDate=2017-01-01.13:00:00","--duration= ","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : duration"));
		//Threshold value not provided
		parser.run("--startDate=2017-01-01.13:00:00","--duration=hourly","--threshold= ");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : threshold"));
		//startDate Value not provided
		parser.run("--startDate= ","--duration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : startDate"));
	}
	
	@Test
	public void testMainNoArgument() throws Exception {
		parser.run("");
		assertTrue(outputCapture.toString().contains("Command Line Argument is Empty"));
	}
	@Test
	public void testMainWrongArgument() throws Exception {
		parser.run("--stDate=2017-01-01.13:00:00","--duration=hourly","--vthreshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : threshold"));	
		parser.run("--stDate=2017-01-01.13:00:00","--vduration=hourly","--threshold=100");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : duration"));
	}
	
	@Test
	public void testMainLessNoOfArgument() throws Exception {
		parser.run("--startDate=2017-01-01.13:00:00","--duration=hourly");
		assertTrue(outputCapture.toString().contains("command Line Argument Error On : threshold"));
	}	
}
