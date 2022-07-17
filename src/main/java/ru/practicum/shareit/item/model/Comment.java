package ru.practicum.shareit.item.model;

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
@Table(name = "comments")
public class Comment {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(name = "text")
    String text;

    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    User user;

    @Column(name = "created")
    LocalDateTime created;
}
