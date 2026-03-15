package com.app.Library_Management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookStatesResponse {
    private long totalActiveBooks;
    private long totalAvailableBooks;
}

