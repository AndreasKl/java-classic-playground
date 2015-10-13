package net.andreaskluth;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMailController {

  @RequestMapping("/send")
  public HttpEntity<Void> send() throws EmailException {

    // An unlucky fool hardcoded some smtp code here.
    Email email = new SimpleEmail();
    email.setHostName("localhost");
    email.setSmtpPort(3025);
    email.setAuthenticator(new DefaultAuthenticator("username", "password"));
    email.setFrom("andreas@kluth.co.uk");
    email.setSubject("TestMail");
    email.setMsg("This is a test mail ... :-)");
    email.addTo("foo@bar.com");
    email.send();

    return ResponseEntity.ok().build();
  }

}
