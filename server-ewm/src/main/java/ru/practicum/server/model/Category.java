package ru.practicum.server.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}