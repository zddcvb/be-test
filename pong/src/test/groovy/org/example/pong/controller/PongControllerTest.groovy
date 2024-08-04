package org.example.pong.controller

import org.example.pong.entry.vo.PongResponseVO
import org.example.pong.service.PongService
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reactor.core.publisher.Mono
import spock.lang.Specification

class PongControllerTest extends Specification {
    private MockMvc mockMvc;
    @Mock
    private PongService pongService;
    @InjectMocks
    private PongController pongController;

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pongController).build();
    }

    def testHandlerRequest() {
        given:
        def param = "hello"
        def vo = new PongResponseVO();
        vo.setCode(HttpStatus.OK)
        vo.setMsg("world")
        def request = Mono.just(vo)
        when:
        Mockito.when(pongService.handlerRequest("hello")).thenReturn(request)
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/pong/").param("param", param))
                .andExpect { MockMvcResultMatchers.status().isOk()}.andDo { MockMvcResultHandlers.print() }.andReturn()
        then:
        def response = result.getResponse().getContentAsString()
        response == request
    }
}
