package ru.practicum.server.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Boolean pinned;
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Event> events;
}