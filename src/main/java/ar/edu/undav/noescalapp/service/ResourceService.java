package ar.edu.undav.noescalapp.service;

import ar.edu.undav.noescalapp.domain.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
