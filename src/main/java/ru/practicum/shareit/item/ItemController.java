package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

/*Я планировал что все контроллеры будут наследоваться от CRUDController, но в них разная логика что с мапперами что с
что с заголовками, поэтому этот класс я создал с нуля. Я пока оставлю логику с пакетом entity для других классов,
но если в будущем придётся много методов переопределять, то я уберу наследование из сервисов\контроллеров и хранилищ*/

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private ItemMapper im = new ItemMapper();
    private ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam("text") String text) {
        log.info("/GET");
        return toDtoCollection(service.search(text));
    }

    @GetMapping()
    public Collection<ItemDto> getAllByUserId(@RequestHeader(name = "X-Sharer-User-Id", required = true)  long userId) {
        log.info("/GET");
        return toDtoCollection(service.getAllByUserId(userId));
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return toDto(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto element, @PathVariable Long id,
                        @RequestHeader(name = "X-Sharer-User-Id", required = true) long userId) {
        element.setId(id);
        return toDto(service.update(fromDto(element), userId));
    }

    @PostMapping
    public ItemDto createData(@Valid @RequestBody ItemDto element,
                              @RequestHeader(name = "X-Sharer-User-Id", required = true) long userId) {
        log.info("/POST: " + element.toString());
        return toDto(service.create(fromDto(element), userId));
    }

    private ItemDto toDto(Item element) {
        return im.toItemDto(element);
    }

    private Item fromDto(ItemDto element) {
        return im.toItem(element);
    }

    protected Collection<ItemDto> toDtoCollection(Collection<Item> elements) {
        Collection<ItemDto> collection = new ArrayList<>();
        for (Item element : elements) {
            collection.add(toDto(element));
        }
        return collection;
    }

}
