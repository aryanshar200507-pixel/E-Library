package com.project.elibrary.service.appsuggestionservice;

import java.util.List;

import com.project.elibrary.bean.suggestions.AppSuggestions;

public interface AppSuggestionService {

    boolean submitSuggestion(AppSuggestions suggestion);

    List<AppSuggestions> findAllActiveSuggestions();

    boolean acceptSuggestion(Long suggestionId);

    boolean deleteSuggestion(Long suggestionId);
}