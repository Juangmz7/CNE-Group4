package com.cne_project.cne_project;
import com.cne_project.cne_project.service.JwtService;
import com.cne_project.cne_project.utils.TokenPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;
    @Test
    public void testJwt() {
        TokenPayload tp = new TokenPayload();
        tp.setUserId("user123");
        String token = jwtService.generateToken(tp);
        System.out.println("Generated token: " + token);
        jwtService.extractAllClaims(token);
        System.out.println("Parsed successfully");
    }
}
