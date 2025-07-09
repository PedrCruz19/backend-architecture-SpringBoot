package org.pedrcruz.backendarch.core.categorymanagement.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryRequest {
    private String name;
    private String description;
}
