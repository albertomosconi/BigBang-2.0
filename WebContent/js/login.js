(function () {
  // hide error container from page
  var form = document.getElementById('loginForm');
  var errorContainer = form.children[form.childElementCount - 1];
  errorContainer.style.display = 'none';

  document.getElementById('loginButton').addEventListener('click', (e) => {
    e.preventDefault();
    if (form.checkValidity()) {
      doRequest('login', 'POST', (req) => {
          if (req.readyState == XMLHttpRequest.DONE) {
            var responseBody = req.responseText;
            switch (req.status) {
              case 200:
                // request was successful, go to home page
                var user = JSON.parse(responseBody);
                console.log(user);
                sessionStorage.setItem('user', responseBody);
                window.location.href = 'home.html';
                break;
              default:
                // request failed, display error
                errorContainer.style.display = 'block';
                document.getElementById('errorBody').textContent = responseBody;
                break;
            }
          }
        },
        form
      );
    } else {
      form.reportValidity();
    }
  });

  document.getElementById('register').addEventListener('click', (e) => {
    e.preventDefault();
    form.innerHTML = "";
    //form.style.display = 'none';
    var container = document.getElementById('form-box');
    var registerForm = showRegister();
    registerForm.style.display = 'block';

    container.appendChild(registerForm);
    console.log(registerForm);
    registerForm.style.display = 'block';
  })
})();
