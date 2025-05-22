package space.astralbridge.spring.moviehub.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping(value = { "/{path:[^\\.]*}", "/admin/{path:[^\\.]*}" })
    public String forward() {
        return "forward:/index.html";
    }
}