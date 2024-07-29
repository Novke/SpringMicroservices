package fon.mas.novica.spring.users.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException {

    private String message;
    private ZonedDateTime timestamp;

}
