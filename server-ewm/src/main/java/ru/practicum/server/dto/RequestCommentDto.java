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
public class RequestCommentDto {
    @NotBlank(message = "Комментарий не может быть пустым.")
    @Size(max = 200, message = "Комментарий не должен быть больше 200")
    private String text;
}