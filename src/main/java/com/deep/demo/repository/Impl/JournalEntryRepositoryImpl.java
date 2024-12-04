package com.deep.demo.repository.Impl;

import com.deep.demo.dto.JournalEntryResponse;
import com.deep.demo.dto.JournalSearchDto;
import com.deep.demo.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

import static com.deep.demo.constants.JournalEntryConstants.*;

@RequiredArgsConstructor
@Repository
public class JournalEntryRepositoryImpl implements JournalRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    private  static  final BeanPropertyRowMapper<JournalEntryResponse> BEAN_PROPERTY_ROW_MAPPER =BeanPropertyRowMapper.newInstance(JournalEntryResponse.class);

    @Override
    public int getJournalEntryCount() {
        return 0;
    }

    @Override
    public Page<JournalEntryResponse> findAll(JournalSearchDto journalSearchDto, PageRequest pageable) {
        return null;
    }


    @Override
    public MultiValueMap getJournalEntryMap(String sql, String value) {
        return namedJdbcTemplate.query(sql,rs -> {
            MultiValueMap journalEntryMap = new MultiValueMap() ;
            while (rs.next()){
                journalEntryMap.put(rs.getInt("id"),rs.getInt(value));
            }
            return journalEntryMap;
        });
    }

    @Override
    public MultiValueMap getJournalEntryMapForInteger(String sql, String value) {

        return namedJdbcTemplate.query(sql,rs -> {
            MultiValueMap journalEntryMap = new MultiValueMap() ;
            while (rs.next()){
                journalEntryMap.put(rs.getInt("id"),rs.getInt(value));
            }
            return journalEntryMap;
        });
    }

    @Override
    public void saveJournal(SqlParameterSource parameters) {

    }

    @Override
    public int updateJournal(SqlParameterSource parameters) {
        return 0;
    }

    @Override
    public void saveDataSourceById(Integer journalId, List<Integer> dataSource) {

    }

    @Override
    public void saveSegmentById(Integer journalId, List<Integer> segment) {

    }

    @Override
    public void saveUpstreamDependencyById(Integer journalId, List<Integer> upStreamDependency) {

    }

    @Override
    public void saveExpectedPeriodById(Integer journalId, List<Integer> expectedPeriod) {

    }

    @Override
    public void saveCompanyCodeById(Integer journalId, List<String> companyCode) {

    }

    @Override
    public void saveGlAccountById(Integer journalId, List<String> glAccount) {

    }

    @Override
    public void saveFunctionalAreaById(Integer journalId, List<String> functionalArea) {

    }

    @Override
    public void saveCenterDepartmentById(Integer journalId, List<String> department) {

    }

    @Override
    public void deleteDataSourceById(Integer journalId) {

    }

    @Override
    public void deleteSegmentById(Integer journalId) {

    }

    @Override
    public void deleteDownStreamImpactById(Integer journalId) {

    }

    @Override
    public void deleteUpstreamDependencyById(Integer journalId) {

    }

    @Override
    public void deleteExpectedPeriodById(Integer journalId) {

    }

    @Override
    public void deleteCompanyCodeById(Integer journalId) {

    }

    @Override
    public void deleteGlAccountById(Integer journalId) {

    }

    @Override
    public void deleteFunctionalAreaById(Integer journalId) {

    }

    @Override
    public void deleteCenterDepartmentById(Integer journalId) {

    }

    @Override
    public List<JournalEntryResponse> getJournalEntryById(Map<String, Number> parameters) {
        return namedJdbcTemplate.query(JOURNAL_ENTRY_SINGLE,parameters,BEAN_PROPERTY_ROW_MAPPER);

    }

    @Override
    public void saveDownstreamImpactById(Integer identifier, List<Integer> integers) {

    }

    @Override
    public void deleteJournalById(Integer journalId) {

    }

    @Override
    public boolean getJournalTempEntry(Map<String, Number> parameters) {
        return false;
    }

    @Override
    public boolean getJournalEntry(Map<String, Number> parameters) {
        return false;
    }

    @Override
    public void updateModifiedJournal(SqlParameterSource parameters) {

    }

    @Override
    public void saveJournalModificationTable(SqlParameterSource modifiedParameters) {

    }


    public Page<JournalEntryResponse> findAll(Pageable page, int count){
        Map<String,Number> parameters =Map.of(OFFSET,page.getOffset(),PAGESIZE,page.getPageSize());
        List<JournalEntryResponse> journalEntryResponseList = getJournalEntryList(parameters);
        return  new PageImpl<>(journalEntryResponseList,page,count);
    }

    public List<JournalEntryResponse> getJournalEntryList(Map<String, Number> parameters) {
        return namedJdbcTemplate.query(JOURNAL_ENTRY_SINGLE,parameters,BEAN_PROPERTY_ROW_MAPPER);
    }
}
