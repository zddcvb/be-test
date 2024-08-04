package org.example.ping.controller

import org.example.ping.entry.PingResponseVO
import org.example.ping.service.PingService
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reactor.core.publisher.Mono
import spock.lang.Specification

class PingControllerTest extends Specification {
    @InjectMocks
    private PingController pingController;
    @Mock
    private PingService pingService;
    private MockMvc mockMvc;

    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pingController).build();
    }

    def "InvokePong"() {
        given:
        def param = "hello"
        def vo = new PingResponseVO();
        vo.setCode(200)
        vo.setMsg("success")
        vo.setData("world")
        def pingResponseVO = Mono.just(vo)
        when:
        Mockito.when(pingService.invokePong(param)).thenReturn(pingResponseVO)
        def result = mockMvc.perform { MockMvcRequestBuilders.get("/ping/").param("param", param) }
                .andExpect { MockMvcResultMatchers.status().isOk() }.andDo { MockMvcResultHandlers.print() }.andReturn()
        def data = result.getResponse().contentAsString
        then:
        def string = "";
        pingResponseVO.subscribe(pingResponseVo -> {
            string = vo.toString();
        })
        data == string
    }
}