function doRequest(url, method, callback, form = null) {
  var request = new XMLHttpRequest();
  request.onreadystatechange = () => callback(request);
  request.open(method, url);
  if (form == null) request.send();
  else {
    var fd = new FormData(form);
    request.send(fd);
  }
}

function linkButton(id, cb) {
  var button = document.getElementById(id);
  button.addEventListener('click', (e) => {
    e.preventDefault();
    cb();
  });
}

function initializeHome() {
  // hide error container from page
  var errorContainer = document.getElementById('errorMessage');
  errorContainer.style.display = 'none';

  // fill username text in top right
  var userString = sessionStorage.getItem('user');
  var user = JSON.parse(userString);

  var navUsername = document.getElementById('navUsername');
  navUsername.textContent = user['name'] + ' ' + user['surname'];

  // link nav buttons
  linkButton('goHomeLink', () => goHome());
  linkButton('goCartLink', () => goCart());
  linkButton('goOrdersLink', () => goOrders());
  linkButton('doLogoutLink', () => doLogout());
}
