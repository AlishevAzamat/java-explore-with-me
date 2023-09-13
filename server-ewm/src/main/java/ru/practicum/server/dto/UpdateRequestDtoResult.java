package ru.practicum.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDtoResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}