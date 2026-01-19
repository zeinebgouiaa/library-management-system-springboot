package com.library.book.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255)
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255)
    private String author;

    @Size(max = 255)
    private String publisher;

    @Min(value = 1000)
    @Max(value = 2100)
    private Integer publishedYear;

    @NotNull(message = "Available copies is required")
    @Min(value = 0)
    private Integer availableCopies;
}