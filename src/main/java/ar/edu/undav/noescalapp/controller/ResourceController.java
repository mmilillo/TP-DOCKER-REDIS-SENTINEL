package ar.edu.undav.noescalapp.controller;

import ar.edu.undav.noescalapp.domain.Resource;
import ar.edu.undav.noescalapp.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "only-resource")
public class ResourceController {

    private ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/{name}")
    public ResponseEntity<Integer> saveResource(@PathVariable String name) {
        Resource resource = resourceService.save(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(resourceService.getResource(id));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format("Resource de id %d no encontrado", id), exception);
        } catch (IllegalStateException loadProblemsException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Mucha carga en el sistema", loadProblemsException);
        }
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(resourceService.getResources());
    }

}
