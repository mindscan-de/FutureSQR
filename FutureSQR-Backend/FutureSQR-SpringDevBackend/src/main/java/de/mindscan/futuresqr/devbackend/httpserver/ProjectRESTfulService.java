/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.devbackend.httpserver;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileChangeInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileContentForRevisionModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputFileHistoryModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputProjectRevisionsRevisionEntry;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputRecentReviewsModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputReviewCreatedModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputReviewModel;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSimpleProjectInformation;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputSingleCommitFullChangeSet;
import de.mindscan.futuresqr.devbackend.httpresponse.OutputStatusOkayModel;
import de.mindscan.futuresqr.devbackend.projectdb.FSqrLazyProjectDatabaseImpl;
import de.mindscan.futuresqr.domain.application.FSqrApplication;
import de.mindscan.futuresqr.domain.databases.FSqrUserToProjectRepositoryImpl;
import de.mindscan.futuresqr.domain.model.FSqrCodeReview;
import de.mindscan.futuresqr.domain.model.FSqrRevision;
import de.mindscan.futuresqr.domain.model.FSqrRevisionFileChangeList;
import de.mindscan.futuresqr.domain.model.FSqrScmHistory;
import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.domain.model.content.FSqrFileContentForRevision;
import de.mindscan.futuresqr.domain.model.history.FSqrFileHistory;
import de.mindscan.futuresqr.domain.repository.FSqrCodeReviewRepository;
import de.mindscan.futuresqr.domain.repository.FSqrScmProjectRevisionRepository;

/**
 * 
 */
@RestController
/* URL is FutureSQR/rest/project */
@RequestMapping("/rest/project")
public class ProjectRESTfulService {

	// TODO: get rid of it as soon as possible.
	private static final String HARDCODED_MINDSCAN_DE_UUID = "8ce74ee9-48ff-3dde-b678-58a632887e31";

	// -------------------------------------------------------------------------------------------
	// this should be provided by a web-application instance, instead of a new
	// instance each time.
	private static FSqrLazyProjectDatabaseImpl projectDB = new FSqrLazyProjectDatabaseImpl();

	@GetMapping(path = "/testme", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTest_JSON() {
		return "{\"hello\":\"world\"}";
	}

	@GetMapping(path = "/{projectid}/information", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputSimpleProjectInformation getSimpleProjectInformation(@PathVariable("projectid") String projectId) {
		FSqrScmProjectConfiguration projectConfiguration = projectDB.getProjectConfiguration(projectId);

		// TODO: problem is that this project project information is user specific,
		// because it may be starred by the user.
		// TODO: we might have to consider the current session context here.

		OutputSimpleProjectInformation response = transform(HARDCODED_MINDSCAN_DE_UUID, projectConfiguration);

		return response;
	}

	private OutputSimpleProjectInformation transform(String userUUID,
			FSqrScmProjectConfiguration projectConfiguration) {
		OutputSimpleProjectInformation transformed = new OutputSimpleProjectInformation();

		transformed.projectID = projectConfiguration.getProjectId();
		transformed.projectDisplayName = projectConfiguration.getProjectDisplayName();
		transformed.projectDescription = projectConfiguration.getProjectDescription();
		transformed.projectUuid = projectConfiguration.getProjectUuid();

		FSqrUserToProjectRepositoryImpl userToProjectRepository = FSqrApplication.getInstance().getServices()
				.getUserToProjectRepository();
		transformed.projectIsStarred = userToProjectRepository.isStarred(userUUID, projectConfiguration.getProjectId());

		return transformed;
	}

	@GetMapping(path = "{projectid}/recentcommits", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputProjectRevisionsModel getProjectRevisions(@PathVariable("projectid") String projectId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();

			FSqrScmProjectRevisionRepository revisionProvider = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrScmHistory scmHistory = revisionProvider.getRecentRevisionHistory(projectId);

			scmHistory.getRevisions().stream().forEach(rev -> response.revisions.add(translate(rev)));

			return response;
		}

		// return empty revision.
		OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();

		return response;
	}

	private OutputProjectRevisionsRevisionEntry translate(FSqrRevision rev) {
		return new OutputProjectRevisionsRevisionEntry(rev);
	}

	@GetMapping(path = "/{projectid}/recentcommitsfromrevid/{fromrevisionid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputProjectRevisionsModel getProjectRevisionsSinceDefinedRevision(
			@PathVariable("projectid") String projectId, @PathVariable("fromrevisionid") String fromRevision) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionProvider = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrScmHistory scmHistory = revisionProvider.getRecentRevisionHistoryStartingFrom(projectId, fromRevision);

			OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();
			scmHistory.getRevisions().stream().forEach(rev -> response.revisions.add(translate(rev)));

			return response;
		}

		// return empty revision.
		OutputProjectRevisionsModel response = new OutputProjectRevisionsModel();

		return response;
	}

	@GetMapping(path = "/{projectid}/revision/{revisionid}/information", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputProjectRevisionsRevisionEntry getRevisionInformation(@PathVariable("projectid") String projectId,
			@PathVariable("revisionid") String revisionId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionProvider = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrRevision revisionInfo = revisionProvider.getSimpleRevisionInformation(projectId, revisionId);

			OutputProjectRevisionsRevisionEntry response = new OutputProjectRevisionsRevisionEntry(revisionInfo);

			return response;
		}

		OutputProjectRevisionsRevisionEntry response = new OutputProjectRevisionsRevisionEntry();

		return response;
	}

	@GetMapping(path = "/{projectid}/revisionfilelist/{revisionid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputFileChangeInformation getRevisionFileList(@PathVariable("projectid") String projectId,
			@PathVariable("revisionid") String revisionId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionProvider = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrRevisionFileChangeList fileChangeList = revisionProvider.getRevisionFileChangeList(projectId,
					revisionId);

			OutputFileChangeInformation response = new OutputFileChangeInformation(fileChangeList);

			return response;
		}

		OutputFileChangeInformation response = new OutputFileChangeInformation();

		return response;
	}

	@GetMapping(path = "/{projectid}/revisiondiff/{revisionid}")
	public OutputSingleCommitFullChangeSet getRevisionFullChangeset(@PathVariable("projectid") String projectId,
			@PathVariable("revisionid") String revisionId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionRepository = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrRevisionFullChangeSet fullChangeSet = revisionRepository.getRevisionFullChangeSet(projectId,
					revisionId);

			OutputSingleCommitFullChangeSet response = new OutputSingleCommitFullChangeSet(fullChangeSet);

			return response;
		}

		OutputSingleCommitFullChangeSet response = new OutputSingleCommitFullChangeSet();

		return response;
	}

	@GetMapping(path = "/{projectid}/revisionfilecontent/{revisionid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputFileContentForRevisionModel getRevisionFileContent(@PathVariable("projectid") String projectId,
			@PathVariable("revisionid") String revisionId, @RequestParam("filepath") String filePath) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionRepository = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrFileContentForRevision fileContent = revisionRepository.getFileContentForRevision(projectId, revisionId,
					filePath);

			OutputFileContentForRevisionModel response = new OutputFileContentForRevisionModel(fileContent);

			return response;
		}

		OutputFileContentForRevisionModel response = new OutputFileContentForRevisionModel();

		return response;
	}

	@GetMapping(path = "{projectid}/filehistory", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputFileHistoryModel getParticularFileHistory(@PathVariable("projectid") String projectId,
			@PathVariable("revisionid") String revisionId, @RequestParam("filepath") String filePath) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionRepository = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();
			FSqrFileHistory fileHistory = revisionRepository.getParticularFileHistory(projectId, revisionId, filePath);

			OutputFileHistoryModel response = new OutputFileHistoryModel(fileHistory);

			return response;

		}

		OutputFileHistoryModel response = new OutputFileHistoryModel();

		return response;
	}

	// TODO: @app.get("/FutureSQR/rest/project/{projectid}/reviewdiff/{reviewid}",
	// response_class=JSONResponse) <<-- refactor from this
	// TODO: @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/diff",
	// response_class=JSONResponse) <<-- refactor to this

	// TODO:
	// @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/filelist",
	// response_class=JSONResponse)

	// TODO:
	// @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/information",
	// response_class=JSONResponse)

	// TODO:
	// @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/revisiondetails",
	// response_class=JSONResponse)

	// TODO:
	// @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/suggestedreviewers",
	// response_class=JSONResponse)

	// TODO:
	// @app.get("/FutureSQR/rest/project/{projectid}/review/{reviewid}/threads",
	// response_class=JSONResponse)

	@GetMapping(path = "/{projectid}/recentreviews", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputRecentReviewsModel getRecentReviews(@PathVariable("projectid") String projectId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			// TODO: implement loading the open and recently reviews.
			FSqrCodeReviewRepository reviewRepository = FSqrApplication.getInstance().getServices()
					.getReviewRepository();

			List<FSqrCodeReview> openReviews = reviewRepository.selectOpenReviews(projectId);
			List<FSqrCodeReview> closedReviews = reviewRepository.selectRecentlyClosedReviews(projectId);

			OutputRecentReviewsModel response = new OutputRecentReviewsModel(openReviews, closedReviews);

			return response;
		}

		OutputRecentReviewsModel response = new OutputRecentReviewsModel();

		return response;
	}

	// -------------------------------------------
	// Starring and unstarring of projects by user
	// -------------------------------------------

	@PostMapping(path = "/{projectid}/star", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postProjectStarred(@PathVariable("projectid") String projectId, String requestBody,
			@RequestPart("userid") String userid) {
		// System.out.println( requestBody );

		if (projectDB.isProjectIdPresent(projectId)) {

			FSqrApplication.getInstance().getServices().getUserToProjectRepository().starProject(userid, projectId);
		}

		return "";
	}

	@PostMapping(path = "/{projectid}/unstar", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postProjectUnstarred(@PathVariable("projectid") String projectId,
			@RequestPart("userid") String userid) {
		// System.out.println( requestBody );

		if (projectDB.isProjectIdPresent(projectId)) {

			// System.out.println( userid );

			FSqrApplication.getInstance().getServices().getUserToProjectRepository().unstarProject(userid, projectId);
		}

		return "";
	}

	// TODO: @app.post * discussion related
	// @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/createthread",
	// response_class=JSONResponse)
	// @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/replythread",
	// response_class=JSONResponse)
	// @app.post("/FutureSQR/rest/project/{projectid}/review/{reviewid}/editmessage",
	// response_class=JSONResponse)

	// ------------------------------
	// Code review state related code
	// ------------------------------

	@PostMapping(path = "/{projectid}/review/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputReviewCreatedModel postCreateReviewFromRevision(@PathVariable("projectid") String projectId,
			@RequestPart("revisionid") String revisionId, @RequestPart("opening_userid") String openerUserUUID) {

		if (projectDB.isProjectIdPresent(projectId)) {

			System.out.println("[CR-CREATE]: revisionid: " + revisionId);
			System.out.println("[CR-CREATE]: openerUserUUID: " + openerUserUUID);

			FSqrCodeReview codeReview = FSqrApplication.getInstance().getServices().getReviewRepository()
					.createReviewFromRevision(projectId, revisionId);

			// TODO: create the code review itself from a revision - currently not
			// completed.
			OutputReviewModel outputReview = new OutputReviewModel(codeReview);

			// TODO: take that code review and fill the response model.
			OutputReviewCreatedModel response = new OutputReviewCreatedModel();
			response.projectId = projectId;
			response.revisionId = revisionId;
			response.reviewId = outputReview.reviewId;
			response.reviewData = outputReview;

			return response;
		}

		// TODO maybe a null is not nice here
		return null;
	}

	@PostMapping(path = "/{projectid}/review/close", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postCloseReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/reopen", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReopenReview(@PathVariable("projectid") String projectId, String requestBody) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postDeleteReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	// ------------------------------
	// code review reviewers related.
	// ------------------------------

	@PostMapping(path = "/{projectid}/review/addreviewer", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewAddReviewer(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/removereviewer", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewRemoveReviewer(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/approvereview", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewApproveReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "{projectid}/review/concernreview", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewConcernReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/resetreview", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewResetReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	// ------------------------------
	// code review revisions related.
	// ------------------------------

	@PostMapping(path = "/{projectid}/review/appendrevision", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewAppendRevisionToReview(@PathVariable("projectid") String projectId, String requestBody) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	@PostMapping(path = "/{projectid}/review/removerevision", produces = MediaType.APPLICATION_JSON_VALUE)
	public String postReviewRemoveRevisionFromReview(@PathVariable("projectid") String projectId) {

		if (projectDB.isProjectIdPresent(projectId)) {
			// TODO: implement me
			return "{}";
		}

		return "{}";
	}

	// ------------------------------------
	// update the temporary cache on demand
	// ------------------------------------

	@PostMapping(path = "/{projectid}/updatecache", produces = MediaType.APPLICATION_JSON_VALUE)
	public OutputStatusOkayModel postUpdateCache(@PathVariable("projectid") String projectId) {
		if (projectDB.hasProjectLocalPath(projectId)) {
			FSqrScmProjectRevisionRepository revisionRepository = FSqrApplication.getInstance().getServices()
					.getRevisionRepository();

			revisionRepository.updateProjectCache(projectId);

			OutputStatusOkayModel response = new OutputStatusOkayModel();

			return response;
		}

		OutputStatusOkayModel response = new OutputStatusOkayModel();

		return response;

	}

}
