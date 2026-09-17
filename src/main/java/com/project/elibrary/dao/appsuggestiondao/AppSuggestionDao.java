package com.project.elibrary.dao.appsuggestiondao;

import java.util.List;

import com.project.elibrary.bean.suggestions.AppSuggestions;

public interface AppSuggestionDao {

    boolean save(AppSuggestions suggestion);

    List<AppSuggestions> findAllActive();

    boolean updateStatus(Long suggestionId, String status);

    boolean delete(Long suggestionId);
}