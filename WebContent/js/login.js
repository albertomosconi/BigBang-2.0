(function () {
  // hide error container from page
  var form = document.getElementById('loginForm');
  var errorContainer = form.children[form.childElementCount - 1];
  errorContainer.style.display = 'none';

  document.getElementById('loginButton').addEventListener('click', (e) => {
    e.preventDefault();
    if (form.checkValidity()) {
      doRequest('login', 'POST', form, (req) => {
        if (req.readyState == XMLHttpRequest.DONE) {
          var responseBody = req.responseText;
          switch (req.status) {
            case 200:
              var user = JSON.parse(responseBody);
              sessionStorage.setItem('user', user);
              window.location.href = 'home.html';
              // request was successful, go to home page
              console.log(user);
              break;
            default:
              // request failed, display error
              errorContainer.style.display = 'block';
              document.getElementById('errorBody').textContent = responseBody;
              break;
          }
        }
      });
    } else {
      form.reportValidity();
    }
  });
})();
