package fon.mas.novica.spring.users.rest;

import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
@Produces(MediaType.TEXT_PLAIN_VALUE)
public class CheckController {

    //NE RADI S OVIM, OCIGLEDNO VALUE PAMTI U CACHE-U
    @Value("${test.value}")
    private String testValue;
    @Value("${server.port}")
    private String portNumber;
    @Autowired
    private Environment environment;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public String check(Authentication authentication){
        StringBuilder sb = new StringBuilder("\tSuccess!\n");
        if (authentication == null || !authentication.isAuthenticated()){
            sb.append("You are anonymous!\n");
        } else {
            sb.append("Logged in: ").append(authentication.getName())
                    .append("\n").append("\n");
        }
        sb.append("> > > Connected to port ").append(environment.getProperty("server.port")).append(" < < <").append("\n")
                .append("\tTest value: ")
//                .append(testValue);
                .append(environment.getProperty("test.value"));

        return sb.toString();
    }

    @GetMapping("/user")
    @ResponseStatus(code = HttpStatus.OK)
    public String user(){
        return "Successful";
    }
    @GetMapping("/admin")
    @ResponseStatus(code = HttpStatus.OK)
    public String admin(){
        return "Successful";
    }
}
