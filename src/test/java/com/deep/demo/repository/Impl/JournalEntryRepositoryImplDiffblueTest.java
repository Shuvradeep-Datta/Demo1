package com.deep.demo.repository.Impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deep.demo.dto.JournalEntryResponse;
import com.deep.demo.dto.JournalSearchDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JournalEntryRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
class JournalEntryRepositoryImplDiffblueTest {
  @Autowired
  private JournalEntryRepositoryImpl journalEntryRepositoryImpl;

  @MockBean
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryMap(String, String)}
   */
  @Test
  void testGetJournalEntryMap() throws DataAccessException {
    MultiValueMap multiValueMap = new MultiValueMap();
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any()))
            .thenReturn(multiValueMap);
    MultiValueMap actualJournalEntryMap = journalEntryRepositoryImpl.getJournalEntryMap("Sql", "42");
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any());
    assertTrue(actualJournalEntryMap.isEmpty());
    assertSame(multiValueMap, actualJournalEntryMap);
  }

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryMap(String, String)}
   */
  @Test
  void testGetJournalEntryMap2() throws DataAccessException {
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any()))
            .thenThrow(new EmptyResultDataAccessException(3));
    assertThrows(EmptyResultDataAccessException.class,
            () -> journalEntryRepositoryImpl.getJournalEntryMap("Sql", "42"));
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any());
  }

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryMapForInteger(String, String)}
   */
  @Test
  void testGetJournalEntryMapForInteger() throws DataAccessException {
    MultiValueMap multiValueMap = new MultiValueMap();
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any()))
            .thenReturn(multiValueMap);
    MultiValueMap actualJournalEntryMapForInteger = journalEntryRepositoryImpl.getJournalEntryMapForInteger("Sql",
            "42");
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any());
    assertTrue(actualJournalEntryMapForInteger.isEmpty());
    assertSame(multiValueMap, actualJournalEntryMapForInteger);
  }

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryMapForInteger(String, String)}
   */
  @Test
  void testGetJournalEntryMapForInteger2() throws DataAccessException {
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any()))
            .thenThrow(new EmptyResultDataAccessException(3));
    assertThrows(EmptyResultDataAccessException.class,
            () -> journalEntryRepositoryImpl.getJournalEntryMapForInteger("Sql", "42"));
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<ResultSetExtractor<Object>>any());
  }

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryById(Map)}
   */
  @Test
  void testGetJournalEntryById() throws DataAccessException {
    ArrayList<Object> objectList = new ArrayList<>();
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
            Mockito.<RowMapper<Object>>any())).thenReturn(objectList);
    List<JournalEntryResponse> actualJournalEntryById = journalEntryRepositoryImpl.getJournalEntryById(new HashMap<>());
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
            Mockito.<RowMapper<Object>>any());
    assertTrue(actualJournalEntryById.isEmpty());
    assertSame(objectList, actualJournalEntryById);
  }

  /**
   * Method under test:
   * {@link JournalEntryRepositoryImpl#getJournalEntryById(Map)}
   */
  @Test
  void testGetJournalEntryById2() throws DataAccessException {
    when(namedParameterJdbcTemplate.query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
            Mockito.<RowMapper<Object>>any())).thenThrow(new EmptyResultDataAccessException(3));
    assertThrows(EmptyResultDataAccessException.class,
            () -> journalEntryRepositoryImpl.getJournalEntryById(new HashMap<>()));
    verify(namedParameterJdbcTemplate).query(Mockito.<String>any(), Mockito.<Map<String, Object>>any(),
            Mockito.<RowMapper<Object>>any());
  }
}
