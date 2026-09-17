package com.project.elibrary.service.appsuggestionservice;

import java.util.List;

import com.project.elibrary.bean.suggestions.AppSuggestions;
import com.project.elibrary.dao.appsuggestiondao.AppSuggestionDao;
import com.project.elibrary.dao.appsuggestiondao.AppSuggestionDaoImpl;

public class AppSuggestionServiceImpl implements AppSuggestionService {

    private final AppSuggestionDao appSuggestionDao;

    public AppSuggestionServiceImpl() {
        this.appSuggestionDao = new AppSuggestionDaoImpl();
    }

    @Override
    public boolean submitSuggestion(AppSuggestions suggestion) {

        if (suggestion == null) {
            return false;
        }

        if (suggestion.getDescription() == null
                || suggestion.getDescription().trim().isEmpty()) {

            return false;
        }

        String description = suggestion.getDescription().trim();

        if (description.length() < 10
                || description.length() > 1000) {

            return false;
        }

        suggestion.setDescription(description);

        // Every newly submitted suggestion starts as NEW.
        suggestion.setStatus("NEW");

        return appSuggestionDao.save(suggestion);
    }

    @Override
    public List<AppSuggestions> findAllActiveSuggestions() {

        return appSuggestionDao.findAllActive();
    }

    @Override
    public boolean acceptSuggestion(Long suggestionId) {

        if (suggestionId == null || suggestionId <= 0) {
            return false;
        }

        return appSuggestionDao.updateStatus(
                suggestionId,
                "ACCEPTED"
        );
    }

    @Override
    public boolean deleteSuggestion(Long suggestionId) {

        if (suggestionId == null || suggestionId <= 0) {
            return false;
        }

        return appSuggestionDao.delete(suggestionId);
    }
}