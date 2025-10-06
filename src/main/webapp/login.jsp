<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <style>
        .container { max-width: 400px; margin: 50px auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="password"] { width: 100%; padding: 8px; }
        button { padding: 10px 20px; margin-right: 10px; }
        .error { color: red; margin-bottom: 15px; }
        .toggle { margin-top: 15px; text-align: center; }
    </style>
</head>
<body>
<div class="container">
    <h2 id="form-title">Login</h2>

    <%-- Error message display --%>
    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <%-- Login Form --%>
    <form id="login-form" action="login" method="post">
        <input type="hidden" name="action" value="login">
        <div class="form-group">
            <label>Email:</label>
            <input type="text" name="email" required>
        </div>
        <div class="form-group">
            <label>Password:</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>

    <%-- Registration Form (initially hidden) --%>
    <form id="register-form" action="login" method="post" style="display: none;">
        <input type="hidden" name="action" value="register">
        <div class="form-group">
            <label>Email:</label>
            <input type="text" name="email" required>
        </div>
        <div class="form-group">
            <label>Username:</label>
            <input type="text" name="username" required>
        </div>
        <div class="form-group">
            <label>Password:</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">Create Account</button>
    </form>

    <div class="toggle">
        <a href="#" id="toggle-link">Need to create an account?</a>
    </div>
</div>

<script>
    document.getElementById('toggle-link').addEventListener('click', function(e) {
        e.preventDefault();

        const loginForm = document.getElementById('login-form');
        const registerForm = document.getElementById('register-form');
        const formTitle = document.getElementById('form-title');
        const toggleLink = document.getElementById('toggle-link');

        if (loginForm.style.display === 'none') {
            // Show login form
            loginForm.style.display = 'block';
            registerForm.style.display = 'none';
            formTitle.textContent = 'Login';
            toggleLink.textContent = 'Need to create an account?';
        } else {
            // Show registration form
            loginForm.style.display = 'none';
            registerForm.style.display = 'block';
            formTitle.textContent = 'Create Account';
            toggleLink.textContent = 'Already have an account?';
        }
    });
</script>
</body>
</html>