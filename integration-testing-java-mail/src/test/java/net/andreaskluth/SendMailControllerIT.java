package net.andreaskluth;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class SendMailControllerIT {

  private static final InternetAddress MY_MAIL = prepareMail("andreas@kluth.co.uk");
  private static final String RESOURCE_LOCATION = "http://localhost:8080/send";
  private final GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);

  @Before
  public void before() {
    greenMail.start();
  }

  @After
  public void after() {
    greenMail.start();
  }

  @Test
  public void happyPathSuccess() throws Exception {
    // Curl the resource, which causes the external process to send a mail.
    ResponseEntity<Void> entity = new RestTemplate().getForEntity(RESOURCE_LOCATION, Void.class);

    assertThat(entity.getStatusCode(), equalTo(OK));
    assertTrue("Should have received a mail", greenMail.waitForIncomingEmail(200, 1));

    Message[] messages = greenMail.getReceivedMessages();
    assertThat(messages.length, equalTo(1));
    assertThat(messages[0].getFrom(), equalTo(new Address[]{MY_MAIL}));
  }

  private static InternetAddress prepareMail(String mail) {
    try {
      return new InternetAddress(mail);
    } catch (AddressException e) {
      throw new IllegalStateException(e);
    }
  }

}