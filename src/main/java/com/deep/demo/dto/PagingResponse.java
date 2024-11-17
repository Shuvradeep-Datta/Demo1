package com.deep.demo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponse {
    public List<JournalEntryResponse> journals;
    public Long totalItems;
    public Integer currentPage;
    public Integer pageSize;
    public Integer totalPages;



}
