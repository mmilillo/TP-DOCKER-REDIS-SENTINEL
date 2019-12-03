package ar.edu.undav.noescalapp.config;

import ar.edu.undav.noescalapp.controller.ResourceController;
import ar.edu.undav.noescalapp.service.ResourceService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ResourceController.class,
        ResourceService.class,
        RedisConfig.class
})
public class SpringConfig {
}
