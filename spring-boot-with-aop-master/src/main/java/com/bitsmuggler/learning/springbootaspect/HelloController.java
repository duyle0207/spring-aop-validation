package com.bitsmuggler.learning.springbootaspect;

import com.bitsmuggler.learning.springbootaspect.aspects.CheckSomething;
import com.bitsmuggler.learning.springbootaspect.aspects.FileProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class HelloController {

    @RequestMapping("/get_file")
    public String index(@RequestParam @FileProperty(contentTypes = {"application/pdf"}) List<MultipartFile> files) {
        return "Greetings from Spring Boot!";
    }
}