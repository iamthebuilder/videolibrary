<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Movies</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Movies</h1>
        
        <form action="${pageContext.request.contextPath}/movies" method="get" class="mb-4">
            <div class="row">
                <div class="col-md-4">
                    <div class="input-group">
                        <input type="number" name="year" class="form-control" placeholder="Enter year">
                        <button type="submit" class="btn btn-primary">Filter</button>
                    </div>
                </div>
            </div>
        </form>

        <div class="row">
            <c:forEach items="${movies}" var="movie">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${movie.title}</h5>
                            <p class="card-text">
                                <strong>Director:</strong> ${movie.director.fullName}<br>
                                <strong>Release Date:</strong> ${movie.releaseDate}<br>
                                <strong>Country:</strong> ${movie.country}<br>
                                <strong>Genre:</strong> ${movie.genre}
                            </p>
                            <a href="${pageContext.request.contextPath}/movies/view/${movie.id}" 
                               class="btn btn-primary">View Details</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty movies}">
            <div class="alert alert-info">No movies found.</div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
