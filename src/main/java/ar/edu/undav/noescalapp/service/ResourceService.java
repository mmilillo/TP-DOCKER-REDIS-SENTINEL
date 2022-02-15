package ar.edu.undav.noescalapp.service;

import ar.edu.undav.noescalapp.domain.Resource;
import ar.edu.undav.noescalapp.domain.ResourceRepository;
import com.martensigwart.fakeload.FakeLoad;
import com.martensigwart.fakeload.FakeLoadExecutor;
import com.martensigwart.fakeload.FakeLoads;
import com.martensigwart.fakeload.MemoryUnit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ResourceService {

    private static final int HARDNESS_HARD = 5;
    private static final int HARDNESS_MEDIUM = 2;
    private static final int HARDNESS_EASY = 1;

    private ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository repository) {
        this.resourceRepository = repository;
    }

    public Resource save(String name) {
        Resource resource = new Resource();
        resource.setName(name);
        this.work(HARDNESS_HARD);
        resource = this.resourceRepository.save(resource);
        return resource;
    }

    public Resource getResource(Integer id) {
        this.work(HARDNESS_EASY);
        Optional<Resource> resource = this.resourceRepository.findById(id);
        if (!resource.isPresent()) {
            throw new IllegalArgumentException("Recurso no existente");
        }
        return resource.get();
    }

    public List<Resource> getResources() {
        this.work(HARDNESS_MEDIUM);
        List<Resource> resources = new ArrayList<Resource>();
        this.resourceRepository.findAll().forEach(resources::add);
        return resources;
    }

    private void work(int hardness) {
        Long random =  Math.round(Math.random() * hardness);
        try {
            FakeLoad fakeload = FakeLoads.create()
                    .lasting(random, TimeUnit.SECONDS)
                    .withCpu(5)
                    .withMemory(1, MemoryUnit.MB);
            FakeLoadExecutor executor = NoEscalappFakeLoadExecutor.newNoEscalappFakeExecutor();
            executor.execute(fakeload);
        } catch (Exception exception) {

            try {
                System.out.println("No podemos fakear más carga. Solo dormimos.");
                Thread.sleep(random);
                throw exception;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
