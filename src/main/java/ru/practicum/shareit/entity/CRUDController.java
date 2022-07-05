package ru.practicum.shareit.entity;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.entity.model.Entity;
import ru.practicum.shareit.entity.service.CRUDService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public abstract class CRUDController<T extends Entity, V extends CRUDService<T>, E extends Entity> {
    V service;

    public CRUDController(V service) {
        this.service = service;
    }

    @GetMapping()
    public Collection<E> getAll() {
        log.info("/GET");
        return toDtoCollection(service.getAll());
    }

    @GetMapping("/{id}")
    public E getElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return toDto(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public boolean deleteElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return service.delete(id);
    }

    @PostMapping
    public E createData(@Valid @RequestBody E element) {
        log.info("/POST: " + element.toString());
        return toDto(service.create(fromDto(element)));
    }

    @PatchMapping("/{id}")
    public E updateData(@RequestBody E element, @PathVariable Long id) {
        log.info("/PATCH: " + element.toString());
        element.setId(id);
        return toDto(service.update(fromDto(element)));
    }

    public abstract E toDto(T element);

    protected Collection<E> toDtoCollection(Collection<T> elements) {
        Collection<E> collection = new ArrayList<>();
        for (T element : elements) {
            collection.add(toDto(element));
        }
        return collection;
    }

    public abstract T fromDto(E element);
}
