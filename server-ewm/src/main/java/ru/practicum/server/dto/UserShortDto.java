package ru.practicum.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    @Size(min = 2, max = 250, message = "Имя не может быть меньше 2 и больше 250")
    @NotBlank(message = "Имя не должно быть пустым или null.")
    private String name;
}