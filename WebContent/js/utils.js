function doRequest(url, method, callback, form = null) {
  console.log(form);
  var request = new XMLHttpRequest();
  request.onreadystatechange = () => callback(request);
  request.open(method, url);
  if (form == null) {
  console.log("XXX");
  request.send();}

  else {
    var fd = new FormData(form);
    console.log(fd);
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

  //search form
  var keyword = document.getElementById('keyword');
  keyword.addEventListener("keydown", (e) =>{
    if (e.code === "Enter") {
      e.preventDefault();
      console.log(keyword);
      if (keyword.checkValidity()) {
        //call the method that handle the search
        var searchForm = document.getElementById('searchForm');
        console.log(searchForm);

        doSearch(keyword.value);
      }
      else{
        errorContainer.style.display = 'block';
        document.getElementById('errorBody').textContent("Error in Input string");
      }
    }
  })
}

blackStarGeneretor = function() {
return `<span class="fa fa-star"></span>`;
}

coloredStarGeneretor = function() {
return `<span class="fa fa-star checked"></span>`;
}
