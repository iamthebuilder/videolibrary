<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${movie.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>${movie.title}</h1>

        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">Movie Details</h5>
                <p class="card-text">
                    <strong>Director:</strong> ${movie.director.fullName}<br>
                    <strong>Release Date:</strong> ${movie.releaseDate}<br>
                    <strong>Country:</strong> ${movie.country}<br>
                    <strong>Genre:</strong> ${movie.genre}
                </p>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">Cast</h5>
                <ul class="list-group">
                    <c:forEach items="${movie.actors}" var="actor">
                        <li class="list-group-item">
                            ${actor.fullName}
                            <small class="text-muted">Birth Date: ${actor.birthDate}</small>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">Reviews</h5>
                <c:forEach items="${reviews}" var="review">
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="card-subtitle mb-2 text-muted">By ${review.user.fullName}</h6>
                            <p class="card-text">${review.text}</p>
                            <div class="text-warning">
                                <c:forEach begin="1" end="${review.rating}">★</c:forEach>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${sessionScope.user != null}">
                    <form action="${pageContext.request.contextPath}/reviews" method="post" class="mt-3">
                        <input type="hidden" name="movieId" value="${movie.id}">
                        <div class="mb-3">
                            <label for="text" class="form-label">Your Review</label>
                            <textarea class="form-control" id="text" name="text" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="rating" class="form-label">Rating</label>
                            <select class="form-select" id="rating" name="rating" required>
                                <option value="1">1 Star</option>
                                <option value="2">2 Stars</option>
                                <option value="3">3 Stars</option>
                                <option value="4">4 Stars</option>
                                <option value="5">5 Stars</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Submit Review</button>
                    </form>
                </c:if>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/movies" class="btn btn-secondary">Back to Movies</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
