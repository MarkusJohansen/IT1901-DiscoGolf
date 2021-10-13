package todolist.ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoteTodoModelAccessTest {

  private WireMockConfiguration config;
  private WireMockServer wireMockServer;

  private RemoteTodoModelAccess todoModelAccess;
  
  @BeforeEach
  public void startWireMockServerAndSetup() throws URISyntaxException {
    config = WireMockConfiguration.wireMockConfig().port(8089);
    wireMockServer = new WireMockServer(config.portNumber());
    wireMockServer.start();
    WireMock.configureFor("localhost", config.portNumber());
    todoModelAccess = new RemoteTodoModelAccess(new URI("http://localhost:" + wireMockServer.port() + "/todo"));
  }

  @Test
  public void testGetTodoListNames() {
    stubFor(get(urlEqualTo("/todo"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"lists\": [ {\"name\": \"todo1\"}, {\"name\": \"todo3\"} ]}")
        )
    );
    Collection<String> names = todoModelAccess.getTodoListNames();
    assertEquals(2, names.size());
    assertTrue(names.containsAll(List.of("todo1", "todo3")));
  }

  @AfterEach
  public void stopWireMockServer() {
    wireMockServer.stop();
  }
}