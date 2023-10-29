package com.capstone.workspace.dtos.order;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Setter(AccessLevel.NONE)
@Data
public class SearchOrderCriteriaDto {
    @Valid
    private SearchOrderFilterDto filter;

    private String keyword;

    @Valid
    private Map<String, Sort.Direction> order;
}
