package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam("text") String text) {
        log.info("/GET");
        return service.search(text);
    }

    @GetMapping()
    public Collection<ItemDto> getAllByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        log.info("/GET");
        return service.getAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto element, @PathVariable Long id,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        element.setId(id);
        return service.update((element), userId);
    }

    @PostMapping
    public ItemDto createData(@Valid @RequestBody ItemDto element,
                              @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        log.info("/POST: " + element.toString());
        return service.create(element, userId);
    }
}
