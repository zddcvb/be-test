package org.example.ping.controller

import org.example.ping.entry.PingResponseVO
import org.example.ping.service.impl.PingServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@WebFluxTest(controllers = [PingController.class])
@AutoConfigureMockMvc
class PingControllerTest extends Specification {
    private MockMvc mockMvc
    @Autowired
    private PingServiceImpl pingService

    void setup() {
        def pingController = new PingController(pingService)
        mockMvc = MockMvcBuilders.standaloneSetup(pingController).build();
    }

    def "request ping url"() {
        given:
        def param = "hello"
        def vo = new PingResponseVO();
        vo.setCode(200)
        vo.setMsg("success")
        vo.setData("world")
        def pingResponseVO = Mono.just(vo)
        and:
        pingService.invokePong(param) >> pingResponseVO
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/ping/").param("param", param))
        then:
        result.andExpect { MockMvcResultMatchers.status().isOk() }.andDo { MockMvcResultHandlers.print() }.andReturn()
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory();

        @Bean
        PingServiceImpl pingService1() {
            return detachedMockFactory.Mock(PingServiceImpl.class)
        }
    }
}