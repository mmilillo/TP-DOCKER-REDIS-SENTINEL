package ar.edu.undav.noescalapp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.lang.IllegalArgumentException;

import ar.edu.undav.noescalapp.domain.Resource;
import ar.edu.undav.noescalapp.domain.ResourceRepository;
import ar.edu.undav.noescalapp.service.ResourceService;

@SpringBootTest
class NoescalappApplicationTests {

    @MockBean
    private ResourceRepository resourceRepository;

	@Autowired
    private ResourceService resourceService;

	@Test
	void contextLoads() {
	}


	/*public Resource save(String name) {
        Integer lastIdAndIncrement = 1;
        Resource resource = new Resource(lastIdAndIncrement, name);
        //this.work(HARDNESS_HARD);
        this.resourceRepository.save(resource);
        return resource;
    }*/

	@Test
	// Se mockea el repository para poder probar el servicio al momento de guardar una entidad nueva.
	public void saveEntity() {

        String nombreDelRecurso = "nuevoRecurso";
        int id = 123456;
        Resource recursoASerCreado = new Resource(id, nombreDelRecurso);

		when(resourceRepository.save(any(Resource.class))).thenReturn(recursoASerCreado);
	

        Resource recursoCreado = resourceService.save("nuevoRecurso");


        Assertions.assertNotNull(recursoCreado);

		Assertions.assertEquals("nuevoRecurso", recursoCreado.getName()
		, "El nombre generado es incorrecto");

		Assertions.assertEquals(123456, recursoCreado.getId(), "El id generado es incorrecto");
    }

	@Test
	// Se mockea el repository para poder probar el servicio al momento de recuperar una entidad existente
	public void getEntity() {

        String resourceName = "unRecurso";
        int id = 123456;
		int idNoExistente = 654321;
        Resource recursoExistente = new Resource(id, resourceName);
		Resource recursoNoExistente = null;

		// consulta de un recurso existente
		when(resourceRepository.findById(id)).thenReturn(Optional.of(recursoExistente));
		// consulta de un recurso no existente
		when(resourceRepository.findById(idNoExistente)).thenReturn(Optional.empty());
	

        Resource recursoRecuperado = resourceService.getResource(123456);


        Assertions.assertNotNull(recursoRecuperado);
		Assertions.assertEquals("unRecurso", recursoRecuperado.getName()
		, "El nombre recuperado es incorrecto");
		Assertions.assertEquals(123456, recursoRecuperado.getId()
		, "El id recuperado es incorrecto");


		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			resourceService.getResource(654321);
		});

		String expectedMessage = "Recurso no existente";
		String actualMessage = exception.getMessage();
	
		assertTrue(actualMessage.contains(expectedMessage));
    }

	@Test
	// Se mockea el repository para poder probar el servicio al momento de recuperar todas las entidades
	public void getEntities() {

        Resource resource1 = new Resource(123, "resource1");
		Resource resource2 = new Resource(124, "resource2");
		Resource resource3 = new Resource(125, "resource3");

		List<Resource> resources = new ArrayList<Resource>();
		resources.add(resource1);
		resources.add(resource2);
		resources.add(resource3);

		// consulta a todos los recursos
		when(resourceRepository.findAll()).thenReturn(resources);
	
		//List<Resource> recursosRecuperados = new ArrayList<Resource>();
        List<Resource> recursosRecuperados = resourceService.getResources();

        Assertions.assertNotNull(recursosRecuperados);
		assertTrue(!recursosRecuperados.isEmpty());
    }


}
