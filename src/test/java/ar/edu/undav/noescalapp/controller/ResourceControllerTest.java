package ar.edu.undav.noescalapp.controller;

import ar.edu.undav.noescalapp.domain.Resource;
import ar.edu.undav.noescalapp.service.ResourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ResourceService resourceService;


    @Test
    public void testGetResourceWithMockito() {

        //PRO: se aisla el servicio y se testea a fondo el controlador
        //CONS: más allá de que el test pone a prueba la integración HTTP -> Controller, el test termina ahi,
        //no se testea más allá
        String resourceName = "newResource";
        int id = 0;
        Resource resourceToBeReturned = new Resource(id, resourceName);
        doReturn(resourceToBeReturned).when(resourceService).getResource(eq(id));

        ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(String.format("/only-resource/%d", id),
                Resource.class);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode()
                , "El status code debe ser 200");

        Resource resource = responseEntity.getBody();

        Assertions.assertNotNull(resource);
        Assertions.assertEquals(resourceName, resource.getName(),
                "El resource obtenido no se llama como el que se guardo");

        Assertions.assertEquals(resourceToBeReturned, resource);
    }

    @Test
    @Disabled("Para correr este test, cambiar la inyección del ResourceService de @MockBean a @Autowired")
    public void testGetResourceUsingService() {
        //
        //PRO: se testea la integración HTTP Request -> controller -> servicio -> datos
        //CONTRA: si tenemos un error en el método save del ResourceService, nuestro test rompe
        //OTRA CONTRA: acá damos por sentado que el primer id es el cero. Si cambia esa implementación, rompe el test
        String resourceName = "newResource";
        resourceService.save(resourceName);

        int id = 0;
        ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(String.format("/only-resource/%d", id),
                Resource.class);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode()
                , "El status code debe ser 200");

        Resource resource = responseEntity.getBody();

        Assertions.assertNotNull(resource);
        Assertions.assertEquals(resourceName, resource.getName(),
                "El resource obtenido no se llama como el que se guardo");

        Assertions.assertEquals(new Resource(id, resourceName), resource);
    }


}