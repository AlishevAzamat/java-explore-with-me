package ru.practicum.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.server.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Category category;
    private Boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User initiator;
    private String description;
    private Integer participantLimit;
    @Enumerated(EnumType.STRING)
    private State state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Float lat;
    private Float lon;
    private Boolean requestModeration;
    private Integer confirmedRequests;
    private Long views;
    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Compilation> compilations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(title, event.title)
                && Objects.equals(annotation, event.annotation) && Objects.equals(category, event.category)
                && Objects.equals(paid, event.paid) && Objects.equals(eventDate, event.eventDate)
                && Objects.equals(initiator, event.initiator) && Objects.equals(description, event.description)
                && Objects.equals(participantLimit, event.participantLimit)
                && state == event.state && Objects.equals(createdOn, event.createdOn)
                && Objects.equals(lat, event.lat) && Objects.equals(lon, event.lon)
                && Objects.equals(requestModeration, event.requestModeration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, annotation, category, paid, eventDate, initiator, description,
                participantLimit, state, createdOn, lat, lon, requestModeration);
    }
}