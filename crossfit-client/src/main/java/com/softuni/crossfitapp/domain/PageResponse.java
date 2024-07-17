package com.softuni.crossfitapp.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private List<T> content;
    private PagedModel.PageMetadata page;

    @NotNull
    public List<T> getContent() {
        return content != null ? content : List.of();
    }
}