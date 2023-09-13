package ru.practicum.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.model.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @Size(min = 3, max = 120, message = "Название не должно быть меньше 3 и превышать 120.")
    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 20, max = 2000, message = "Краткое описание не может быть меньше 20 и превышать 2000.")
    private String annotation;
    private Long category;
    @Builder.Default
    private Boolean paid = false;
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание не может быть меньше 20 и превышать 7000.")
    private String description;
    @Builder.Default
    private Integer participantLimit = 0;
    @NotNull
    private Location location;
    @Builder.Default
    private Boolean requestModeration = true;
}