package ru.practicum.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Size(min = 2, max = 250, message = "Имя не может быть меньше 2 и больше 250")
    @NotBlank(message = "Имя не должно быть пустым или null.")
    private String name;
    @Email
    @Size(min = 6, max = 254, message = "Email не может быть меньше 6 и больше 254")
    @NotBlank(message = "Email не должен быть пустым или null.")
    private String email;
}