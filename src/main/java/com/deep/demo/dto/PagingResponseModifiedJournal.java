package com.deep.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponseModifiedJournal {
   public List<tempJournalResponse> journals;
   public Long totalItems;
   public Integer currentPage;
   public Integer pageSize;
   public Integer totalPages;


}
