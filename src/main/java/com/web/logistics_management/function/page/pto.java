package com.web.logistics_management.function.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class pto {
    @GetMapping("/main")
    public String board(Model model) {
        return "main";
    }
}
