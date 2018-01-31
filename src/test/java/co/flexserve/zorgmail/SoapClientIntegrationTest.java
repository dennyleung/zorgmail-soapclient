package co.flexserve.zorgmail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoapClientIntegrationTest {

  @Autowired
  private ClientConfig clientConfig;
  @Autowired
  private SoapClient soapClient;

	@Test
	public void testListMessages() {
		List<String> listedMessages = soapClient.listMessages();
		assertThat(listedMessages.size()).isGreaterThan(1);
	}

	@Test
  public void testSendMessage() {
	  String message =
            "UNA:+ ? '\n" +
            "UNB+UNOC:1+500000000+500000000+180201:0005+00000000000009'\n" +
            "UNH+00000000000009+MEDVRI:1'\n" +
            "GGA+ABCDEF+++GHIJKL:1::XYZ:1234ZH+000-1234567'\n" +
            "DET+18:02:01+00:05'\n" +
            "PID+0000:01:01+M+TEST+BSN000000000'\n" +
            "TXT:1+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:2+'\n" +
            "TXT:3+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:4+'\n" +
            "TXT:5+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:6+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:7+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:8+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:9+'\n" +
            "TXT:10+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:11+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "TXT:12+ABCDEFGHIJKLMNOPQRSTUVWXYZ'\n" +
            "GGO+ABCDEF++GHIJ+KLMNO:11::PQRSTUV:1256 AA+000-7654321'\n" +
            "UNT+18+00000000000009'\n" +
            "UNZ+1+00000000000009'\n";
	  String recipientAccountId = clientConfig.getAccountId();
	  String senderAccountId = clientConfig.getAccountId();
	  String subject = "00000000000009";
	  soapClient.sendMessage(message, recipientAccountId, senderAccountId, subject);
  }

}
