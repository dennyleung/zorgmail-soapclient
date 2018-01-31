package co.flexserve.zorgmail;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
public class ClientConfig {

  @Value("${zorgmail.soapclient.uri}")
  private String defaultUri;

  @Value("${zorgmail.soapclient.user.name}")
  private String userName;

  @Value("${zorgmail.soapclient.user.password}")
  private String userPassword;

  @Value("${zorgmail.edifact.accountId}")
  private String accountId;

  @Value("${zorgmail.edifact.charSet}")
  private String charSet;

  @Bean
  Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setContextPath("nl.enovation.ems.mailwebservice");
    jaxb2Marshaller.setMtomEnabled(true);

    return jaxb2Marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() {
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageFactory(soapMessageFactory());
    webServiceTemplate.setMarshaller(jaxb2Marshaller());
    webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
    webServiceTemplate.setDefaultUri(defaultUri);
    return webServiceTemplate;
  }

  @Bean
  public SoapMessageFactory soapMessageFactory() {
    SoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory();
    soapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
    return soapMessageFactory;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getDefaultUri() {
    return defaultUri;
  }

  public Charset getDefaultCharset() {
    return Charset.forName(charSet);
  }

  public String getDefaultCharsetName() {
    return charSet;
  }
}
