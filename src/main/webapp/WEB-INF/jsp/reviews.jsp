<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reviews</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Reviews</h1>

        <div class="row">
            <c:forEach items="${reviews}" var="review">
                <div class="col-md-6 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">
                                <a href="${pageContext.request.contextPath}/movies/view/${review.movie.id}">
                                    ${review.movie.title}
                                </a>
                            </h5>
                            <h6 class="card-subtitle mb-2 text-muted">By ${review.user.fullName}</h6>
                            <p class="card-text">${review.text}</p>
                            <div class="text-warning">
                                <c:forEach begin="1" end="${review.rating}">★</c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty reviews}">
            <div class="alert alert-info">No reviews found.</div>
        </c:if>

        <a href="${pageContext.request.contextPath}/movies" class="btn btn-secondary">Back to Movies</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
