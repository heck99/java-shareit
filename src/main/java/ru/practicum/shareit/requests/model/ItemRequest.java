package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "item_requests")
public class ItemRequest {

    @Column(name = "id")
    @Id
    Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User request;

    @Column(name = "created")
    private LocalDateTime created;

}
