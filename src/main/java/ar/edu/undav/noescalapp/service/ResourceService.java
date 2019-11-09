package ar.edu.undav.noescalapp.service;

import ar.edu.undav.noescalapp.domain.Resource;
import com.martensigwart.fakeload.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ResourceService {

    private static final int HARDNESS_HARD = 5;
    private static final int HARDNESS_MEDIUM = 2;
    private static final int HARDNESS_EASY = 1;

    private static Integer lastId = 0;
    private static final Map<Integer, Resource> PERSISTENCE_MAP = new HashMap<>();

    public Resource save(String name) {
        Integer lastIdAndIncrement = this.getLastIdAndIncrement();
        Resource resource = new Resource(lastIdAndIncrement, name);
        this.work(HARDNESS_HARD);
        PERSISTENCE_MAP.put(lastIdAndIncrement, resource);
        return resource;
    }

    public Resource getResource(Integer id) {
        this.work(HARDNESS_EASY);
        Resource resource = PERSISTENCE_MAP.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("Recurso no existente");
        }
        return resource;
    }

    public List<Resource> getResources() {
        this.work(HARDNESS_MEDIUM);
        return new ArrayList<>(PERSISTENCE_MAP.values());
    }

    private synchronized Integer getLastIdAndIncrement() {
        return lastId++;
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

    @PostConstruct
    public void initialize() {
        this.save("resource0");
        this.save("resource1");
    }

}
