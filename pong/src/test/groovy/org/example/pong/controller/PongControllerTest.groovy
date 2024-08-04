package org.example.pong.controller

import org.example.pong.entry.vo.PongResponseVO
import org.example.pong.service.impl.PongServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@WebFluxTest(controllers = [PongController.class])
@AutoConfigureMockMvc
class PongControllerTest extends Specification {
    private MockMvc mockMvc;
    @Autowired
    private PongServiceImpl pongService;

    def setup() {
        def pongController = new PongController(pongService)
        mockMvc = MockMvcBuilders.standaloneSetup(pongController).build();
    }

    def testHandlerRequest() {
        given:
        def param = "hello"
        def vo = new PongResponseVO();
        vo.setCode(HttpStatus.OK.value())
        vo.setMsg("success")
        vo.setData("world")
        def request = Mono.just(vo)
        and:
        pongService.handlerRequest(param) >> request
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/pong/").param("param", param))
        then:
        result.andExpect { MockMvcResultMatchers.status().isOk() }.andDo { MockMvcResultHandlers.print() }.andReturn()
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory();

        @Bean
        PongServiceImpl pingService1() {
            return detachedMockFactory.Mock(PongServiceImpl.class)
        }
    }
}
