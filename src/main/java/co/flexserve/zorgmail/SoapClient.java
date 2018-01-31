package co.flexserve.zorgmail;

import static co.flexserve.zorgmail.util.BasicAuthenticationUtil.generateBasicAuthenticationHeader;

import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import javafx.util.Builder;
import nl.enovation.ems.mailwebservice.AccountIdRequest;
import nl.enovation.ems.mailwebservice.ListResponse;
import nl.enovation.ems.mailwebservice.ObjectFactory;
import nl.enovation.ems.mailwebservice.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

@Component
public class SoapClient {

  @Autowired
  private ClientConfig clientConfig;

  @Autowired
  private WebServiceTemplate webServiceTemplate;



  private final Builder<WebServiceMessageCallback> callbackBuilder = () -> webServiceMessage -> {
    TransportContext context = TransportContextHolder.getTransportContext();
    HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();
    connection.getConnection().setRequestProperty("Authorization", generateBasicAuthenticationHeader(clientConfig.getUserName(), clientConfig.getUserPassword()));
  };

  private String generateMessageId() {
    return ("<" + UUID.randomUUID().toString() + "@" + "123P.NL>");
  }
  
  public void sendMessage(String message, String recipientAccountId, String senderAccountId, String subject) {
    final String messageContentType = "application/edifact";
    
    DataHandler dataHandler = new DataHandler(new String(message.getBytes(), clientConfig.getDefaultCharset()),
            messageContentType + "; charset=" + clientConfig.getDefaultCharsetName());

    SendMessage sendMessage = new ObjectFactory().createSendMessage();
    sendMessage.setContent(dataHandler);
    sendMessage.setContentType(messageContentType);
    sendMessage.setMessageId(generateMessageId());
    sendMessage.setRecipient(recipientAccountId);
    sendMessage.setSender(senderAccountId);
    sendMessage.setSubject(subject);

    JAXBElement<SendMessage> request = new ObjectFactory().createSendRequest(sendMessage);
    webServiceTemplate.marshalSendAndReceive(request, callbackBuilder.build());
  }

  List<String> listMessages() {
    AccountIdRequest accountIdRequest = new AccountIdRequest();
    accountIdRequest.setAccountId(clientConfig.getAccountId());

    JAXBElement<AccountIdRequest> request = new ObjectFactory().createListRequest(accountIdRequest);
    JAXBElement<ListResponse> response = (JAXBElement<ListResponse>) webServiceTemplate.marshalSendAndReceive(request, callbackBuilder.build());
    
    return response.getValue().getUniqueIds();

  }
}
