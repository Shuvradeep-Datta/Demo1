package com.deep.demo.repository.Impl;

import com.deep.demo.constants.JournalEntryConstants;
import com.deep.demo.dto.JournalEntryResponse;
import com.deep.demo.dto.JournalSearchDto;
import org.apache.commons.collections.map.MultiValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalEntryRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedJdbcTemplate;

    private JournalEntryRepositoryImpl journalEntryRepositoryImplUnderTest;

    @BeforeEach
    void setUp() {
        journalEntryRepositoryImplUnderTest = new JournalEntryRepositoryImpl(mockNamedJdbcTemplate);
    }

    @Test
    void testGetJournalEntryCount() {
        assertThat(journalEntryRepositoryImplUnderTest.getJournalEntryCount()).isEqualTo(0);
    }

    @Test
    void testFindAll1() {
        assertThat(journalEntryRepositoryImplUnderTest.findAll(JournalSearchDto.builder().build(),
                PageRequest.of(0, 1))).isNull();
    }

    @Test
    void testGetJournalEntryMap() {
        // Setup
        final MultiValueMap expectedResult = new MultiValueMap();
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class))).thenReturn(new MultiValueMap());

        // Run the test
        final MultiValueMap result = journalEntryRepositoryImplUnderTest.getJournalEntryMap("sql", "value");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetJournalEntryMap_NamedParameterJdbcTemplateReturnsNull() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class))).thenReturn(null);

        // Run the test
        final MultiValueMap result = journalEntryRepositoryImplUnderTest.getJournalEntryMap("sql", "value");

        // Verify the results
        assertThat(result).isNull();
    }

    @Test
    void testGetJournalEntryMap_NamedParameterJdbcTemplateThrowsDataAccessException() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class)))
                .thenThrow(DataAccessException.class);

        // Run the test
        assertThatThrownBy(() -> journalEntryRepositoryImplUnderTest.getJournalEntryMap("sql", "value"))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void testGetJournalEntryMapForInteger() {
        // Setup
        final MultiValueMap expectedResult = new MultiValueMap();
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class))).thenReturn(new MultiValueMap());

        // Run the test
        final MultiValueMap result = journalEntryRepositoryImplUnderTest.getJournalEntryMapForInteger("sql", "value");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetJournalEntryMapForInteger_NamedParameterJdbcTemplateReturnsNull() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class))).thenReturn(null);

        // Run the test
        final MultiValueMap result = journalEntryRepositoryImplUnderTest.getJournalEntryMapForInteger("sql", "value");

        // Verify the results
        assertThat(result).isNull();
    }

    @Test
    void testGetJournalEntryMapForInteger_NamedParameterJdbcTemplateThrowsDataAccessException() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq("sql"), any(ResultSetExtractor.class)))
                .thenThrow(DataAccessException.class);

        // Run the test
        assertThatThrownBy(
                () -> journalEntryRepositoryImplUnderTest.getJournalEntryMapForInteger("sql", "value"))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void testSaveJournal() {
        journalEntryRepositoryImplUnderTest.saveJournal(null);
    }

    @Test
    void testUpdateJournal() {
        assertThat(journalEntryRepositoryImplUnderTest.updateJournal(null)).isEqualTo(0);
    }

    @Test
    void testSaveDataSourceById() {
        journalEntryRepositoryImplUnderTest.saveDataSourceById(0, List.of(0));
    }

    @Test
    void testSaveSegmentById() {
        journalEntryRepositoryImplUnderTest.saveSegmentById(0, List.of(0));
    }

    @Test
    void testSaveUpstreamDependencyById() {
        journalEntryRepositoryImplUnderTest.saveUpstreamDependencyById(0, List.of(0));
    }

    @Test
    void testSaveExpectedPeriodById() {
        journalEntryRepositoryImplUnderTest.saveExpectedPeriodById(0, List.of(0));
    }

    @Test
    void testSaveCompanyCodeById() {
        journalEntryRepositoryImplUnderTest.saveCompanyCodeById(0, List.of("value"));
    }

    @Test
    void testSaveGlAccountById() {
        journalEntryRepositoryImplUnderTest.saveGlAccountById(0, List.of("value"));
    }

    @Test
    void testSaveFunctionalAreaById() {
        journalEntryRepositoryImplUnderTest.saveFunctionalAreaById(0, List.of("value"));
    }

    @Test
    void testSaveCenterDepartmentById() {
        journalEntryRepositoryImplUnderTest.saveCenterDepartmentById(0, List.of("value"));
    }

    @Test
    void testDeleteDataSourceById() {
        journalEntryRepositoryImplUnderTest.deleteDataSourceById(0);
    }

    @Test
    void testDeleteSegmentById() {
        journalEntryRepositoryImplUnderTest.deleteSegmentById(0);
    }

    @Test
    void testDeleteDownStreamImpactById() {
        journalEntryRepositoryImplUnderTest.deleteDownStreamImpactById(0);
    }

    @Test
    void testDeleteUpstreamDependencyById() {
        journalEntryRepositoryImplUnderTest.deleteUpstreamDependencyById(0);
    }

    @Test
    void testDeleteExpectedPeriodById() {
        journalEntryRepositoryImplUnderTest.deleteExpectedPeriodById(0);
    }

    @Test
    void testDeleteCompanyCodeById() {
        journalEntryRepositoryImplUnderTest.deleteCompanyCodeById(0);
    }

    @Test
    void testDeleteGlAccountById() {
        journalEntryRepositoryImplUnderTest.deleteGlAccountById(0);
    }

    @Test
    void testDeleteFunctionalAreaById() {
        journalEntryRepositoryImplUnderTest.deleteFunctionalAreaById(0);
    }

    @Test
    void testDeleteCenterDepartmentById() {
        journalEntryRepositoryImplUnderTest.deleteCenterDepartmentById(0);
    }

    @Test
    void testGetJournalEntryById() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        final JournalEntryResponse journalEntryResponse = new JournalEntryResponse();
        journalEntryResponse.setIdentifier(0);
        journalEntryResponse.setStatus("status");
        journalEntryResponse.setTitle("title");
        journalEntryResponse.setSegment(List.of("value"));
        journalEntryResponse.setExpectedPeriod(List.of(0));
        final List<JournalEntryResponse> expectedResult = List.of(journalEntryResponse);

        // Configure NamedParameterJdbcTemplate.query(...).
        final JournalEntryResponse journalEntryResponse1 = new JournalEntryResponse();
        journalEntryResponse1.setIdentifier(0);
        journalEntryResponse1.setStatus("status");
        journalEntryResponse1.setTitle("title");
        journalEntryResponse1.setSegment(List.of("value"));
        journalEntryResponse1.setExpectedPeriod(List.of(0));
        final List<JournalEntryResponse> journalEntryResponses = List.of(journalEntryResponse1);
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(journalEntryResponses);

        // Run the test
        final List<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.getJournalEntryById(parameters);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetJournalEntryById_NamedParameterJdbcTemplateReturnsNoItems() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.getJournalEntryById(parameters);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetJournalEntryById_NamedParameterJdbcTemplateThrowsDataAccessException() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenThrow(DataAccessException.class);

        // Run the test
        assertThatThrownBy(() -> journalEntryRepositoryImplUnderTest.getJournalEntryById(parameters))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void testFindAll2() {
        // Setup
        // Configure NamedParameterJdbcTemplate.query(...).
        final JournalEntryResponse journalEntryResponse = new JournalEntryResponse();
        journalEntryResponse.setIdentifier(0);
        journalEntryResponse.setStatus("status");
        journalEntryResponse.setTitle("title");
        journalEntryResponse.setSegment(List.of("value"));
        journalEntryResponse.setExpectedPeriod(List.of(0));
        final List<JournalEntryResponse> journalEntryResponses = List.of(journalEntryResponse);
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(journalEntryResponses);

        // Run the test
        final Page<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.findAll(PageRequest.of(0, 1), 0);

        // Verify the results
    }

    @Test
    void testFindAll2_NamedParameterJdbcTemplateReturnsNoItems() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final Page<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.findAll(PageRequest.of(0, 1), 0);

        // Verify the results
    }

    @Test
    void testFindAll2_NamedParameterJdbcTemplateThrowsDataAccessException() {
        // Setup
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenThrow(DataAccessException.class);

        // Run the test
        assertThatThrownBy(() -> journalEntryRepositoryImplUnderTest.findAll(PageRequest.of(0, 1), 0))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    void testGetJournalEntryList() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        final JournalEntryResponse journalEntryResponse = new JournalEntryResponse();
        journalEntryResponse.setIdentifier(0);
        journalEntryResponse.setStatus("status");
        journalEntryResponse.setTitle("title");
        journalEntryResponse.setSegment(List.of("value"));
        journalEntryResponse.setExpectedPeriod(List.of(0));
        final List<JournalEntryResponse> expectedResult = List.of(journalEntryResponse);

        // Configure NamedParameterJdbcTemplate.query(...).
        final JournalEntryResponse journalEntryResponse1 = new JournalEntryResponse();
        journalEntryResponse1.setIdentifier(0);
        journalEntryResponse1.setStatus("status");
        journalEntryResponse1.setTitle("title");
        journalEntryResponse1.setSegment(List.of("value"));
        journalEntryResponse1.setExpectedPeriod(List.of(0));
        final List<JournalEntryResponse> journalEntryResponses = List.of(journalEntryResponse1);
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(journalEntryResponses);

        // Run the test
        final List<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.getJournalEntryList(parameters);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetJournalEntryList_NamedParameterJdbcTemplateReturnsNoItems() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<JournalEntryResponse> result = journalEntryRepositoryImplUnderTest.getJournalEntryList(parameters);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetJournalEntryList_NamedParameterJdbcTemplateThrowsDataAccessException() {
        // Setup
        final Map<String, Number> parameters = Map.ofEntries(Map.entry("value", new BigDecimal("0.00")));
        when(mockNamedJdbcTemplate.query(eq(JournalEntryConstants.JOURNAL_ENTRY_SINGLE),
                eq(Map.ofEntries(Map.entry("value", "value"))), any(BeanPropertyRowMapper.class)))
                .thenThrow(DataAccessException.class);

        // Run the test
        assertThatThrownBy(() -> journalEntryRepositoryImplUnderTest.getJournalEntryList(parameters))
                .isInstanceOf(DataAccessException.class);
    }
}
