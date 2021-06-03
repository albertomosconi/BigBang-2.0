function goHome() {
  console.log('go home');
  doRequest('view', 'GET', (req) => {
    if (req.readyState == XMLHttpRequest.DONE) {
      var responseBody = req.responseText;
      switch (req.status) {
        case 200:
          // request was successful, go to home page
          var items = JSON.parse(responseBody);

          var pageContainer = document.getElementById('pageContainer');
          // create heading
          var heading = document.createElement('h1');
          heading.textContent = 'Last viewed items';
          pageContainer.appendChild(heading);
          // create list
          var listContainer = buildLastViewed(items);
          pageContainer.appendChild(listContainer);
          break;

        default:
          // request failed, display error
          errorContainer.style.display = 'block';
          document.getElementById('errorBody').textContent = responseBody;
          break;
      }
    }
  });
}

function goCart() {
  alert('go cart');
}

function goOrders() {
  alert('go orders');
}

function doLogout() {
  alert('do logout');
}

(function () {
  window.addEventListener('load', (e) => {
    // initialize event listeners
    initializeHome();
    // display home page
    goHome();
  });
})();
