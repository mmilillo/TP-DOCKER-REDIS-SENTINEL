package ar.edu.undav.noescalapp.service;

import ar.edu.undav.noescalapp.domain.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceService {

    private static final int HARDNESS_HARD = 20;

    private static Integer lastId = 0;
    private static final Map<Integer, Resource> PERSISTENCE_MAP = new HashMap<>();

    public Resource save(String name) {
        Integer lastIdAndIncrement = this.getLastIdAndIncrement();
        Resource resource = new Resource(lastIdAndIncrement, name);
        this.workHard();
        PERSISTENCE_MAP.put(lastIdAndIncrement, resource);
        return resource;
    }

    public Resource getResource(Integer id) {
        this.work(5);
        Resource resource = PERSISTENCE_MAP.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("Recurso no existente");
        }
        return resource;
    }

    public List<Resource> getResources() {
        this.work(10);
        return new ArrayList<>(PERSISTENCE_MAP.values());
    }

    private synchronized Integer getLastIdAndIncrement() {
        return lastId++;
    }

    private void work(int hardness) {
        Long random =  Math.round(Math.random() * hardness);
        try {
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void workHard() {
        work(HARDNESS_HARD);
    }

}
