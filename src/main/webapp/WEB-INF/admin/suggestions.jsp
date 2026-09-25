<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.project.elibrary.bean.suggestions.AppSuggestions"%>
<%@ page import="com.project.elibrary.bean.user.User"%>

<%
List<AppSuggestions> suggestions = (List<AppSuggestions>) request.getAttribute("suggestions");

Map<Long, User> suggestionUsers = (Map<Long, User>) request.getAttribute("suggestionUsers");

String success = (String) request.getAttribute("success");
String error = (String) request.getAttribute("error");
String contextPath = request.getContextPath();

int newCount = 0;
int acceptedCount = 0;

if (suggestions != null) {
	for (AppSuggestions item : suggestions) {
		if ("NEW".equals(item.getStatus())) {
	newCount++;
		} else if ("ACCEPTED".equals(item.getStatus())) {
	acceptedCount++;
		}
	}
}

int totalCount = suggestions == null ? 0 : suggestions.size();
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Suggestions | Stories E-Library</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/background.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/app-suggestions.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/confirm-modal.css">

<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

<!-- Page Script -->
<script
	src="${pageContext.request.contextPath}/javascript/app-suggestions.js"
	defer>
	
</script>
</head>

<body>

	<div class="dashboard-shell">

		<!-- ================= HEADER ================= -->
		<header class="header">

			<a href="<%=contextPath%>/admin/dashboard" class="brand"> <span
				class="brand-icon"> <i data-lucide="library"></i>
			</span> <span class="brand-text"> <strong>Stories</strong> <small>E-LIBRARY
						ADMIN</small>
			</span>
			</a>

			<div class="header-right">
				<span class="header-label"> <i data-lucide="shield-check"></i>
					Admin Workspace
				</span> <a href="<%=contextPath%>/admin/dashboard" class="header-btn">
					<i data-lucide="layout-dashboard"></i> <span>Dashboard</span>
				</a> <a href="<%=contextPath%>/logout" class="header-btn logout-btn">
					<i data-lucide="log-out"></i> <span>Logout</span>
				</a>
			</div>

		</header>

		<!-- ================= MAIN ================= -->
		<main class="main-content">

			<!-- Page Heading -->
			<section class="page-heading">

				<div class="heading-content">

					<div class="eyebrow">
						<span class="eyebrow-line"></span> COMMUNITY FEEDBACK
					</div>

					<h1>
						Suggestions<span>.</span>
					</h1>

					<p>Every great feature begins with an idea. Review, organize,
						and manage suggestions shared by your readers.</p>

				</div>

				<div class="heading-decoration">
					<div class="decoration-circle circle-one"></div>
					<div class="decoration-circle circle-two"></div>

					<i data-lucide="lightbulb"></i> <span>Ideas into<br>possibilities
					</span>
				</div>

			</section>

			<!-- ================= ALERTS ================= -->

			<%
			if (success != null) {
			%>
			<div class="alert alert-success" role="status">
				<i data-lucide="check-circle-2"></i> <span><%=success%></span>
			</div>
			<%
			}
			%>

			<%
			if (error != null) {
			%>
			<div class="alert alert-error" role="alert">
				<i data-lucide="alert-circle"></i> <span><%=error%></span>
			</div>
			<%
			}
			%>

			<!-- ================= STATISTICS ================= -->

			<section class="stats-grid">

				<article class="stat-card">
					<div class="stat-icon icon-total">
						<i data-lucide="messages-square"></i>
					</div>

					<div class="stat-info">
						<span class="stat-label">Total Suggestions</span>
						<h2><%=totalCount%></h2>
						<p>Currently in this list</p>
					</div>
				</article>

				<article class="stat-card">
					<div class="stat-icon icon-new">
						<i data-lucide="inbox"></i>
					</div>

					<div class="stat-info">
						<span class="stat-label">New Suggestions</span>
						<h2><%=newCount%></h2>
						<p>Waiting for review</p>
					</div>
				</article>

				<article class="stat-card">
					<div class="stat-icon icon-accepted">
						<i data-lucide="check-check"></i>
					</div>

					<div class="stat-info">
						<span class="stat-label">Accepted</span>
						<h2><%=acceptedCount%></h2>
						<p>Approved for implementation</p>
					</div>
				</article>

			</section>

			<!-- ================= SUGGESTIONS SECTION ================= -->

			<section class="suggestions-section">

				<div class="section-heading">

					<div>
						<span class="section-kicker">READER IDEAS</span>
						<h2>Suggestion Board</h2>
						<p>Explore and manage feedback from your community.</p>
					</div>

					<div class="result-count">
						<i data-lucide="layers"></i> <span id="visibleCount"><%=totalCount%></span>
						<span>suggestions</span>
					</div>

				</div>

				<!-- Search and Filters -->
				<div class="toolbar">

					<div class="search-box">
						<i data-lucide="search"></i> <input type="search"
							id="suggestionSearch"
							placeholder="Search suggestions, users, or descriptions..."
							aria-label="Search suggestions"> <span
							class="search-shortcut">⌕</span>
					</div>

					<div class="filter-group" role="group"
						aria-label="Filter suggestions">

						<button type="button" class="filter-btn active" data-filter="ALL">
							All <span><%=totalCount%></span>
						</button>

						<button type="button" class="filter-btn" data-filter="NEW">
							New <span><%=newCount%></span>
						</button>

						<button type="button" class="filter-btn" data-filter="ACCEPTED">
							Accepted <span><%=acceptedCount%></span>
						</button>

					</div>

				</div>

				<!-- ================= CARD LIST ================= -->

				<%
				if (suggestions == null || suggestions.isEmpty()) {
				%>

				<div class="empty-state">
					<div class="empty-icon">
						<i data-lucide="notebook-pen"></i>
					</div>

					<h3>No Active Suggestions</h3>

					<p>There are currently no new or accepted suggestions. When
						readers share ideas, they will appear here.</p>

					<a href="<%=contextPath%>/admin/dashboard" class="empty-btn">
						<i data-lucide="arrow-left"></i> Back to Dashboard
					</a>
				</div>

				<%
				} else {
				%>

				<div class="suggestions-container" id="suggestionsContainer">

					<%
					for (AppSuggestions suggestion : suggestions) {

						User suggestionUser = suggestionUsers == null ? null : suggestionUsers.get(suggestion.getUserId());

						String status = suggestion.getStatus() == null ? "" : suggestion.getStatus();

						String userName = suggestionUser != null ? suggestionUser.getName() : "User " + suggestion.getUserId();

						String userEmail = suggestionUser != null ? suggestionUser.getEmail() : "User information unavailable";

						String statusClass = "NEW".equals(status) ? "status-new" : "status-accepted";

						String statusLabel = "NEW".equals(status) ? "New" : "Accepted";
					%>

					<article class="suggestion-card" data-status="<%=status%>"
						data-search="<%=(userName + " " + userEmail + " " + suggestion.getDescription() + " " + suggestion.getUserId()).toLowerCase()%>">

						<!-- Card Header -->
						<div class="card-header">

							<div class="user-details">

								<div class="user-avatar">
									<i data-lucide="user-round"></i>
								</div>

								<div class="user-info">
									<h3><%=userName%></h3>

									<p class="user-email">
										<i data-lucide="mail"></i>
										<%=userEmail%>
									</p>

									<p class="user-id">
										<i data-lucide="fingerprint"></i> User ID:
										<%=suggestion.getUserId()%>
									</p>
								</div>

							</div>

							<span class="status <%=statusClass%>"> <span
								class="status-dot"></span> <%=statusLabel%>
							</span>

						</div>

						<!-- Suggestion Content -->
						<div class="suggestion-content">

							<div class="content-label">
								<i data-lucide="message-square-text"></i> SUGGESTION DETAILS
							</div>

							<div class="suggestion-description"><%=suggestion.getDescription()%></div>

						</div>

						<!-- Card Footer -->
						<div class="card-footer">

							<div class="submission-date">
								<i data-lucide="calendar-days"></i> <span> Submitted: <strong><%=suggestion.getCreatedAt()%></strong>
								</span>
							</div>

							<div class="actions">

								<%
								if ("NEW".equals(status)) {
								%>

								<!-- ACCEPT -->
								<form action="<%=contextPath%>/admin/suggestions"
									method="post" class="action-form">

									<input type="hidden" name="suggestionId"
										value="<%=suggestion.getSuggestionId()%>"> <input
										type="hidden" name="action" value="accept">

									<button type="submit" class="btn btn-accept">
										<i data-lucide="check"></i> Accept Suggestion
									</button>

								</form>

								<!-- DELETE -->
								<form action="<%=contextPath%>/admin/suggestions"
									method="post" class="action-form delete-form"
									data-confirm="Are you sure you want to delete this suggestion? This action cannot be undone.">

									<input type="hidden" name="suggestionId"
										value="<%=suggestion.getSuggestionId()%>"> <input
										type="hidden" name="action" value="delete">

									<button type="submit" class="btn btn-delete">
										<i data-lucide="trash-2"></i> Delete
									</button>

								</form>

								<%
								} else if ("ACCEPTED".equals(status)) {
								%>

								<!-- IMPLEMENTED -->
								<form action="<%=contextPath%>/admin/suggestions"
									method="post" class="action-form delete-form"
									data-confirm-title="Mark as Implemented?"
									data-confirm-label="Mark Implemented"
									data-confirm="Are you sure this suggestion has been implemented? It will be permanently removed from the active suggestions list.">

									<input type="hidden" name="suggestionId"
										value="<%=suggestion.getSuggestionId()%>"> <input
										type="hidden" name="action" value="delete">

									<button type="submit" class="btn btn-implemented">
										<i data-lucide="check-check"></i> Mark Implemented
									</button>
								</form>

								<!-- DELETE -->
								<form action="<%=contextPath%>/admin/suggestions"
									method="post" class="action-form delete-form"
									data-confirm="Are you sure you want to delete this suggestion? This action cannot be undone.">

									<input type="hidden" name="suggestionId"
										value="<%=suggestion.getSuggestionId()%>"> <input
										type="hidden" name="action" value="delete">

									<button type="submit" class="btn btn-delete">
										<i data-lucide="trash-2"></i> Delete
									</button>

								</form>

								<%
								}
								%>

							</div>

						</div>

					</article>

					<%
					}
					%>

				</div>

				<!-- No Search Results -->
				<div class="no-results" id="noResults" hidden>
					<div class="empty-icon">
						<i data-lucide="search-x"></i>
					</div>

					<h3>No matching suggestions</h3>
					<p>Try a different search term or change the status filter.</p>

					<button type="button" class="empty-btn" id="clearFilters">
						<i data-lucide="rotate-ccw"></i> Clear Filters
					</button>
				</div>

				<%
				}
				%>

			</section>

		</main>

		<!-- ================= FOOTER ================= -->
		<footer class="page-footer">
			<span> <i data-lucide="library"></i> Stories E-Library
			</span> <span>Made for readers, inspired by ideas.</span>
		</footer>

		<!-- Custom Accept Suggestion Modal -->
		<div class="accept-modal-overlay" id="acceptSuggestionModal"
			aria-hidden="true">

			<div class="accept-modal" role="dialog" aria-modal="true"
				aria-labelledby="acceptModalTitle"
				aria-describedby="acceptModalDescription">

				<div class="accept-modal-icon">
					<i data-lucide="lightbulb"></i>
				</div>

				<h2 id="acceptModalTitle">Accept This Suggestion?</h2>

				<p id="acceptModalDescription">This suggestion will be marked as
					accepted. You can review it later in the Accepted section.</p>

				<div class="accept-modal-actions">
					<button type="button" class="accept-modal-cancel"
						id="cancelAcceptSuggestion">Cancel</button>

					<button type="button" class="accept-modal-confirm"
						id="confirmAcceptSuggestion">
						<i data-lucide="check"></i> Accept Suggestion
					</button>
				</div>
			</div>
		</div>
	</div>
	<script
		src="${pageContext.request.contextPath}/javascript/confirm-modal.js"></script>
</body>
</html>