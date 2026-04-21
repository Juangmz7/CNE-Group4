package com.cne_project.cne_project;

import com.cne_project.cne_project.config.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfig.class)
class CneProjectApplicationTests {

    @Test
    void contextLoads() {
    }

}
