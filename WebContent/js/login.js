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
    var formBox = document.getElementById('form-box');
    //formBox.innerHTML = "";
    formBox.style.display = 'none';
    var formBox2 = document.createElement('form2-box');
    //var container = document.getElementById('form-box');
    var registerForm = showRegister();
    registerForm.style.display = 'block';

    //formBox.appendChild(registerForm);
    formBox2.appendChild(registerForm);
    console.log(registerForm);
    registerForm.style.display = 'block';
  })
})();
